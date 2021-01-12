package social.login.backend.integration.exception;

public class SocialProviderException extends RuntimeException {

    public SocialProviderException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
