package social.login.backend.integration.provider;

import org.springframework.web.servlet.view.RedirectView;
import social.login.backend.integration.exception.SocialProviderException;
import social.login.backend.integration.exception.UserDetailException;
import social.login.backend.integration.user.SocialUser;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.servlet.http.HttpServletRequest;

import static social.login.backend.integration.constant.ErrorMessage.ERR_GET_TWITTER_REQUEST_TOKEN;
import static social.login.backend.integration.constant.ErrorMessage.ERR_GET_TWITTER_USER_DETAIL;

public class TwitterProvider extends SocialLoginProvider {

    private static final String REQUEST_TOKEN_KEY = "requestToken";
    private static final String TWITTER_KEY = "twitter";

    private Twitter twitter;

    public TwitterProvider(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setIncludeEmailEnabled(true);
        builder.setOAuthConsumerKey(clientId);
        builder.setOAuthConsumerSecret(clientSecret);
        Configuration configuration = builder.build();
        this.twitter = new TwitterFactory(configuration).getInstance();
    }

    public String getAuthenticationUrl(HttpServletRequest request) {
        RequestToken requestToken;
        try {
            requestToken = twitter.getOAuthRequestToken(redirectUri);
        } catch (TwitterException e) {
            throw new SocialProviderException(ERR_GET_TWITTER_REQUEST_TOKEN, e);
        }
        request.getSession().setAttribute(REQUEST_TOKEN_KEY, requestToken);
        request.getSession().setAttribute(TWITTER_KEY, twitter);
        return requestToken.getAuthorizationURL();
    }

    public RedirectView twitterLogin(HttpServletRequest request) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(getAuthenticationUrl(request));
        return redirectView;
    }

    public SocialUser getUser(HttpServletRequest request, String oauthVerifier) {
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
