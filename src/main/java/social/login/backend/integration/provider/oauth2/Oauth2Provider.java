package social.login.backend.integration.provider.oauth2;

import org.springframework.web.servlet.view.RedirectView;

public interface Oauth2Provider {

    String getLoginUrl();

    RedirectView login();
}
