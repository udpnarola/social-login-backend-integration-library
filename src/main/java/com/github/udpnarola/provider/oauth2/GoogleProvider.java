package com.github.udpnarola.provider.oauth2;

import com.github.udpnarola.constant.ErrorMessage;
import com.github.udpnarola.dto.SocialLoginDetail;
import com.github.udpnarola.exception.UserDetailException;
import com.github.udpnarola.provider.SocialLoginProvider;
import com.github.udpnarola.user.SocialUser;
import com.github.udpnarola.util.SocialLoginUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

public class GoogleProvider extends SocialLoginProvider implements Oauth2Provider {

    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private String loginUrl = "https://accounts.google.com/o/oauth2/v2/auth?scope=profile%20email&response_type=code";

    public GoogleProvider(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.loginUrl = SocialLoginUtil.prepareLoginUrl(loginUrl, clientId, redirectUri);
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public RedirectView login() {
        return new RedirectView(loginUrl);
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
            throw new UserDetailException(ErrorMessage.ERR_GET_GOOGLE_USER_DETAIL, e);
        }
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
