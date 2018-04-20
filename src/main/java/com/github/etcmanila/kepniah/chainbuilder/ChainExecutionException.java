package com.github.etcmanila.kepniah.chainbuilder;

public class ChainExecutionException extends RuntimeException {

    public ChainExecutionException() {
        super();
    }

    public ChainExecutionException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ChainExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChainExecutionException(String message) {
        super(message);
    }

    public ChainExecutionException(Throwable cause) {
        super(cause);
    }

}
