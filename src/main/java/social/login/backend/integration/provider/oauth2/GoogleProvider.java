package social.login.backend.integration.provider.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.servlet.view.RedirectView;
import social.login.backend.integration.dto.SocialLoginDetail;
import social.login.backend.integration.exception.UserDetailException;
import social.login.backend.integration.provider.SocialLoginProvider;
import social.login.backend.integration.user.SocialUser;
import social.login.backend.integration.util.SocialLoginUtil;

import java.io.IOException;
import java.util.Arrays;

import static social.login.backend.integration.constant.ErrorMessage.ERR_GET_GOOGLE_USER_DETAIL;

public class GoogleProvider extends SocialLoginProvider implements Oauth2Provider {

    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private String loginUrl = "https://accounts.google.com/o/oauth2/auth/identifier?response_type=code%20permission%20id_token&scope=profile%20email";

    public GoogleProvider(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        prepareLoginUrl(clientId, redirectUri);
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public RedirectView login() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(loginUrl);
        return redirectView;
    }

    public SocialUser getUser(SocialLoginDetail socialLoginDetail) {
        String authCode = socialLoginDetail.getAuthCode();
        SocialLoginUtil.validateAuthCode(authCode);
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(HTTP_TRANSPORT,
                    JSON_FACTORY, TOKEN_URL, clientId, clientSecret, authCode, redirectUri)
                    .execute();

            GoogleIdToken.Payload payload = tokenResponse.parseIdToken().getPayload();
            return prepareUser(payload);
        } catch (IOException e) {
            throw new UserDetailException(ERR_GET_GOOGLE_USER_DETAIL, e);
        }
    }

    private void prepareLoginUrl(String clientId, String redirectUri){
        NameValuePair redirectUriParam = new BasicNameValuePair("redirect_uri", redirectUri);
        NameValuePair clientIdParam = new BasicNameValuePair("client_id", clientId);
        loginUrl = SocialLoginUtil.prepareUrl(loginUrl, Arrays.asList(redirectUriParam, clientIdParam));
    }

    private SocialUser prepareUser(GoogleIdToken.Payload payload) {
        SocialUser socialUser = new SocialUser();
        socialUser.setFirstName((String) payload.get("given_name"));
        socialUser.setLastName((String) payload.get("family_name"));
        socialUser.setEmail((String) payload.get("email"));
        socialUser.setImageUrl(((String) payload.get("picture"))
                .replaceFirst("(=).*$", "=s400-c"));
        return socialUser;
    }

}
