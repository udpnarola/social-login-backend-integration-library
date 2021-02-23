package com.github.udpnarola.dto;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class SocialLoginDetail {

    private String authCode;
    private String oauthVerifier;
    private HttpServletRequest httpServletRequest;
}
