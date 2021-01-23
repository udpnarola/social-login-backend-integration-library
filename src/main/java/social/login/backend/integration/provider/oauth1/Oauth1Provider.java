package social.login.backend.integration.provider.oauth1;

import org.springframework.web.servlet.view.RedirectView;
import social.login.backend.integration.provider.OauthProvider;

import javax.servlet.http.HttpServletRequest;

public interface Oauth1Provider extends OauthProvider {

    String getLoginUrl(HttpServletRequest request);

    RedirectView login(HttpServletRequest request);
}
