package com.github.udpnarola.provider.oauth2;

import com.github.udpnarola.provider.OauthProvider;
import org.springframework.web.servlet.view.RedirectView;

public interface Oauth2Provider extends OauthProvider {

    String getLoginUrl();

    RedirectView login();
}
