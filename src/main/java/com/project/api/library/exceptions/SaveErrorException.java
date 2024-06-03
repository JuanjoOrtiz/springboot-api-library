package com.project.api.library.exceptions;

public class SaveErrorException extends  RuntimeException{
    public SaveErrorException() {
    }

    public SaveErrorException(String message) {
        super(message);
    }

    public SaveErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveErrorException(Throwable cause) {
        super(cause);
    }

    public SaveErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
