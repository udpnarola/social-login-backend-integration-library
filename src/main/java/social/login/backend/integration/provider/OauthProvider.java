package social.login.backend.integration.provider;

import social.login.backend.integration.dto.SocialLoginDetail;
import social.login.backend.integration.user.SocialUser;

public interface OauthProvider {

    SocialUser getUser(SocialLoginDetail socialLoginDetail);
}
