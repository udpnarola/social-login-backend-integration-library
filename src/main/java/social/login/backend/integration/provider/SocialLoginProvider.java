package social.login.backend.integration.provider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SocialLoginProvider {

    protected String clientId;
    protected String clientSecret;
    protected String redirectUri;
}
