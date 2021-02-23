package com.github.udpnarola.provider.oauth1;

import org.springframework.web.servlet.view.RedirectView;
import com.github.udpnarola.provider.OauthProvider;

import javax.servlet.http.HttpServletRequest;

public interface Oauth1Provider extends OauthProvider {

    String getLoginUrl(HttpServletRequest request);

    RedirectView login(HttpServletRequest request);
}
