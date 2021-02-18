package social.login.backend.integration.provider.oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.view.RedirectView;
import social.login.backend.integration.dto.SocialLoginDetail;
import social.login.backend.integration.provider.SocialLoginProvider;
import social.login.backend.integration.user.SocialUser;
import social.login.backend.integration.util.SocialLoginUtil;

import java.util.Objects;

import static social.login.backend.integration.constant.ErrorMessage.ERR_GET_LINKEDIN_USER_DETAIL;
import static social.login.backend.integration.constant.ErrorMessage.ERR_GET_LINKEDIN_USER_EMAIL;

public class LinkedinProvider extends SocialLoginProvider implements Oauth2Provider {

    private static final String GET_USER_PROFILE_URL = "https://api.linkedin.com/v2/me?projection=(firstName,lastName,profilePicture(displayImage~:playableStreams))";
    private static final String GET_USER_EMAIL_URL = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";
    private String loginUrl = "https://www.linkedin.com/oauth/v2/authorization?response_type=code&scope=r_liteprofile%20r_emailaddress";

    private LinkedInConnectionFactory connectionFactory;

    public LinkedinProvider(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.loginUrl = SocialLoginUtil.prepareLoginUrl(loginUrl, clientId, redirectUri);
        this.connectionFactory = new LinkedInConnectionFactory(clientId, clientSecret);
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public RedirectView login() {
        return new RedirectView(loginUrl);
    }

    public SocialUser getUser(SocialLoginDetail socialLoginDetail) {
        String accessToken = SocialLoginUtil.getAccessToken(socialLoginDetail, connectionFactory, redirectUri);
        LinkedIn linkedIn = new LinkedInTemplate(accessToken);
        RestOperations restOperations = linkedIn.restOperations();

        ResponseEntity<JsonNode> linkedInProfileResponseEntity = restOperations.getForEntity(GET_USER_PROFILE_URL, JsonNode.class);
        ResponseEntity<JsonNode> emailResponseEntity = restOperations.getForEntity(GET_USER_EMAIL_URL, JsonNode.class);

        return prepareUser(linkedInProfileResponseEntity, emailResponseEntity);
    }

    private SocialUser prepareUser(ResponseEntity<JsonNode> linkedInProfileResponseEntity, ResponseEntity<JsonNode> emailResponseEntity) {
        JsonNode linkedinProfile = linkedInProfileResponseEntity.getBody();

        SocialUser socialUser = new SocialUser();

        socialUser.setFirstName(Objects.requireNonNull(linkedinProfile, ERR_GET_LINKEDIN_USER_DETAIL)
                .get("firstName")
                .get("localized")
                .get("en_US").asText());
        socialUser.setLastName(linkedinProfile
                .get("lastName")
                .get("localized")
                .get("en_US").asText());
        socialUser.setImageUrl(linkedinProfile
                .get("profilePicture")
                .get("displayImage~")
                .get("elements").get(2)
                .get("identifiers").get(0)
                .get("identifier").asText());
        socialUser.setEmail(Objects.requireNonNull(emailResponseEntity.getBody(), ERR_GET_LINKEDIN_USER_EMAIL)
                .get("elements").get(0)
                .get("handle~")
                .get("emailAddress").asText());

        return socialUser;
    }
}
