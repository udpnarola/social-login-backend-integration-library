package com.github.udpnarola.exception;

public class SocialProviderException extends RuntimeException {

    public SocialProviderException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
