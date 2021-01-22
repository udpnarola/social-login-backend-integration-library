package social.login.backend.integration.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import social.login.backend.integration.exception.BadDataException;
import social.login.backend.integration.exception.URIBuilderException;

import java.net.URISyntaxException;
import java.util.List;

import static social.login.backend.integration.constant.ErrorMessage.ERR_BUILD_URI;
import static social.login.backend.integration.constant.ErrorMessage.ERR_EMPTY_OR_NULL_AUTH_CODE;

public class SocialLoginUtil {

    private SocialLoginUtil() {

    }

    public static String prepareUrl(String url, List<NameValuePair> queryParams) {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new URIBuilderException(ERR_BUILD_URI, e);
        }
        uriBuilder.addParameters(queryParams);
        return uriBuilder.toString();
    }

    public static void validateAuthCode(String authCode) {
        if (StringUtils.isBlank(authCode))
            throw new BadDataException(ERR_EMPTY_OR_NULL_AUTH_CODE);
    }
}
