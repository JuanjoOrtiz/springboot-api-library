package com.project.api.library.exceptions;

public class ValidateServiceException extends RuntimeException{
    public ValidateServiceException() {
    }

    public ValidateServiceException(String message) {
        super(message);
    }

    public ValidateServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateServiceException(Throwable cause) {
        super(cause);
    }

    public ValidateServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
