package com.github.udpnarola.provider.oauth1;

import org.springframework.web.servlet.view.RedirectView;
import com.github.udpnarola.dto.SocialLoginDetail;
import com.github.udpnarola.exception.SocialProviderException;
import com.github.udpnarola.exception.UserDetailException;
import com.github.udpnarola.provider.SocialLoginProvider;
import com.github.udpnarola.user.SocialUser;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.servlet.http.HttpServletRequest;

import static com.github.udpnarola.constant.ErrorMessage.ERR_GET_TWITTER_REQUEST_TOKEN;
import static com.github.udpnarola.constant.ErrorMessage.ERR_GET_TWITTER_USER_DETAIL;

public class TwitterProvider extends SocialLoginProvider implements Oauth1Provider {

    private static final String REQUEST_TOKEN_KEY = "requestToken";
    private static final String TWITTER_KEY = "twitter";

    public TwitterProvider(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    private Twitter initializeTwitter() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setIncludeEmailEnabled(true);
        builder.setOAuthConsumerKey(clientId);
        builder.setOAuthConsumerSecret(clientSecret);
        Configuration configuration = builder.build();
        return new TwitterFactory(configuration).getInstance();
    }

    @Override
    public String getLoginUrl(HttpServletRequest request) {
        RequestToken requestToken;
        Twitter twitter;
        try {
            twitter = initializeTwitter();
            requestToken = twitter.getOAuthRequestToken(redirectUri);
        } catch (TwitterException e) {
            throw new SocialProviderException(ERR_GET_TWITTER_REQUEST_TOKEN, e);
        }
        request.getSession().setAttribute(REQUEST_TOKEN_KEY, requestToken);
        request.getSession().setAttribute(TWITTER_KEY, twitter);
        return requestToken.getAuthorizationURL();
    }

    @Override
    public RedirectView login(HttpServletRequest request) {
        return new RedirectView(getLoginUrl(request));
    }

    @Override
    public SocialUser getUser(SocialLoginDetail socialLoginDetail) {
        HttpServletRequest request = socialLoginDetail.getHttpServletRequest();
        String oauthVerifier = socialLoginDetail.getOauthVerifier();
        Twitter twitterManager = (Twitter) request.getSession().getAttribute(TWITTER_KEY);
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute(REQUEST_TOKEN_KEY);
        User twitterUser;

        try {
            twitterManager.getOAuthAccessToken(requestToken, oauthVerifier);
            request.getSession().removeAttribute(REQUEST_TOKEN_KEY);
            twitterUser = twitterManager.verifyCredentials();
        } catch (TwitterException e) {
            throw new UserDetailException(ERR_GET_TWITTER_USER_DETAIL, e);
        }

        SocialUser socialUser = new SocialUser();
        socialUser.setFirstName(twitterUser.getName());
        socialUser.setEmail(twitterUser.getEmail());
        socialUser.setImageUrl(twitterUser.getOriginalProfileImageURL());
        return socialUser;
    }

}
