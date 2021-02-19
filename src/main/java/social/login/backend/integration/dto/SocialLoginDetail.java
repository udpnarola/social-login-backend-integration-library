package social.login.backend.integration.dto;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class SocialLoginDetail {

    private String authCode;
    private String oauthVerifier;
    private HttpServletRequest httpServletRequest;
}
