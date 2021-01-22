package social.login.backend.integration.exception;

public class BadDataException extends RuntimeException {

    public BadDataException(String errorMessage) {
        super(errorMessage);
    }
}
