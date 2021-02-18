package social.login.backend.integration.provider.oauth2;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.web.servlet.view.RedirectView;
import social.login.backend.integration.dto.SocialLoginDetail;
import social.login.backend.integration.provider.SocialLoginProvider;
import social.login.backend.integration.user.SocialUser;
import social.login.backend.integration.util.SocialLoginUtil;

import java.util.Map;

public class FacebookProvider extends SocialLoginProvider implements Oauth2Provider {

    private FacebookConnectionFactory connectionFactory;
    private String loginUrl = "https://www.facebook.com/v9.0/dialog/oauth?response_type=code";

    public FacebookProvider(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.loginUrl = SocialLoginUtil.prepareLoginUrl(loginUrl, clientId, redirectUri);
        this.connectionFactory = new FacebookConnectionFactory(clientId, clientSecret);
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public RedirectView login() {
        return new RedirectView(loginUrl);
    }

    public SocialUser getUser(SocialLoginDetail socialLoginDetail) {
        String accessToken = SocialLoginUtil.getAccessToken(socialLoginDetail, connectionFactory, redirectUri);
        Facebook facebook = new FacebookTemplate(accessToken);
        String[] fields = {"email", "first_name", "last_name", "picture.width(400).height(400)"};
        User user = facebook.fetchObject("me", User.class, fields);
        return prepareSocialUser(user);
    }

    private SocialUser prepareSocialUser(User user) {
        SocialUser socialUser = new SocialUser();
        socialUser.setFirstName(user.getFirstName());
        socialUser.setLastName(user.getLastName());
        socialUser.setEmail(user.getEmail());

        Map<String, Map<String, String>> picture = (Map<String, Map<String, String>>) user.getExtraData().get("picture");
        Map<String, String> pictureDetails = picture.get("data");

        socialUser.setImageUrl(pictureDetails.get("url"));

        return socialUser;
    }
}
