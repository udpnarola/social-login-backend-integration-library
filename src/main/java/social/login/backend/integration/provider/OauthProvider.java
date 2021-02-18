package social.login.backend.integration.provider;

import org.springframework.web.servlet.view.RedirectView;
import social.login.backend.integration.dto.SocialLoginDetail;
import social.login.backend.integration.user.SocialUser;

public interface OauthProvider {

    String getLoginUrl();

    RedirectView login();

    SocialUser getUser(SocialLoginDetail socialLoginDetail);

}
