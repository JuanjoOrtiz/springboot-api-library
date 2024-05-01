package com.project.api.library.exceptions;

public class GeneralServiceException extends RuntimeException {
    public GeneralServiceException() {
    }

    public GeneralServiceException(String message) {
        super(message);
    }

    public GeneralServiceException(Throwable cause) {
        super(cause);
    }

    public GeneralServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GeneralServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
