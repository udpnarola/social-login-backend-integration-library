package social.login.backend.integration.provider;

import lombok.Getter;
import lombok.Setter;
import social.login.backend.integration.dto.SocialLoginDetail;
import social.login.backend.integration.user.SocialUser;

@Getter
@Setter
public abstract class SocialLoginProvider {

    protected String clientId;
    protected String clientSecret;
    protected String redirectUri;

    public abstract SocialUser getUser(SocialLoginDetail socialLoginDetail);
}
