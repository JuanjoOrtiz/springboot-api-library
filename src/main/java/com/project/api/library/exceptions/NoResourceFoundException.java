package com.project.api.library.exceptions;

public class NoResourceFoundException extends RuntimeException {
    public NoResourceFoundException() {
    }

    public NoResourceFoundException(String message) {
        super(message);
    }

    public NoResourceFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResourceFoundException(Throwable cause) {
        super(cause);
    }

    public NoResourceFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
