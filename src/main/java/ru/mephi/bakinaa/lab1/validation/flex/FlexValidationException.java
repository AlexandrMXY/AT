package ru.mephi.bakinaa.lab1.validation.flex;

public class FlexValidationException extends RuntimeException {
    public FlexValidationException() {
    }

    public FlexValidationException(String message) {
        super(message);
    }

    public FlexValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlexValidationException(Throwable cause) {
        super(cause);
    }

    public FlexValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
