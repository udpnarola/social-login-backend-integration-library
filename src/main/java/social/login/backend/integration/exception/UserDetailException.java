package social.login.backend.integration.exception;

public class UserDetailException extends RuntimeException {

    public UserDetailException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
