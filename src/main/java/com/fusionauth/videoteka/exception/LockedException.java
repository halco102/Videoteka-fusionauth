package com.fusionauth.videoteka.exception;

public class LockedException extends RuntimeException {

    private static final long serialVersionUID = 6008678986917497181L;

    public LockedException(final String message) {
        super(message);
    }
}
