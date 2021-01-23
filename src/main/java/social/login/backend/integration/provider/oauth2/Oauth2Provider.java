package social.login.backend.integration.provider.oauth2;

import org.springframework.web.servlet.view.RedirectView;
import social.login.backend.integration.provider.OauthProvider;

public interface Oauth2Provider extends OauthProvider {

    String getLoginUrl();

    RedirectView login();
}
