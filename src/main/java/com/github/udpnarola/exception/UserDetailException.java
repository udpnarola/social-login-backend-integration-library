package com.github.udpnarola.exception;

public class UserDetailException extends RuntimeException {

    public UserDetailException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
