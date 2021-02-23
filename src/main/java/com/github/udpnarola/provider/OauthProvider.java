package com.github.udpnarola.provider;

import com.github.udpnarola.user.SocialUser;
import com.github.udpnarola.dto.SocialLoginDetail;

public interface OauthProvider {

    SocialUser getUser(SocialLoginDetail socialLoginDetail);

}
