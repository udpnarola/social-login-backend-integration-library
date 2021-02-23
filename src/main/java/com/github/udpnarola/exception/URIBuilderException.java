package com.github.udpnarola.exception;

public class URIBuilderException extends RuntimeException {

    public URIBuilderException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
