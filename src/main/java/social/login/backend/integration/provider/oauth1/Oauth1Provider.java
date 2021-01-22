package social.login.backend.integration.provider.oauth1;

import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

public interface Oauth1Provider {

    String getLoginUrl(HttpServletRequest request);

    RedirectView login(HttpServletRequest request);
}
