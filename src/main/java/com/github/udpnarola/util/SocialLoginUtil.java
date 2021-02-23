package com.github.udpnarola.util;

import com.github.udpnarola.constant.ErrorMessage;
import com.github.udpnarola.exception.URIBuilderException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import com.github.udpnarola.dto.SocialLoginDetail;
import com.github.udpnarola.exception.BadDataException;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class SocialLoginUtil {

    private SocialLoginUtil() {

    }

    public static String prepareLoginUrl(String loginUrl, String clientId, String redirectUri) {
        NameValuePair clientIdParam = new BasicNameValuePair("client_id", clientId);
        NameValuePair redirectUriParam = new BasicNameValuePair("redirect_uri", redirectUri);
        return prepareUrl(loginUrl, Arrays.asList(clientIdParam, redirectUriParam));
    }

    private static String prepareUrl(String url, List<NameValuePair> queryParams) {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new URIBuilderException(ErrorMessage.ERR_BUILD_URI, e);
        }
        uriBuilder.addParameters(queryParams);
        return uriBuilder.toString();
    }

    public static String getAccessToken(SocialLoginDetail socialLoginDetail, OAuth2ConnectionFactory<?> connectionFactory, String redirectUri) {
        String authCode = socialLoginDetail.getAuthCode();
        validateAuthCode(authCode);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(authCode, redirectUri, null);
        return accessGrant.getAccessToken();
    }

    public static void validateAuthCode(String authCode) {
        if (StringUtils.isBlank(authCode))
            throw new BadDataException(ErrorMessage.ERR_EMPTY_OR_NULL_AUTH_CODE);
    }
}
