package com.fusionauth.videoteka.exception;

public class UnprocessableEntityException extends RuntimeException {

    private static final long serialVersionUID = 6008678986917497181L;

    public UnprocessableEntityException(final String message) {
        super(message);
    }
}
