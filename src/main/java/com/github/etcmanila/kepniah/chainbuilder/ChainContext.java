package com.github.etcmanila.kepniah.chainbuilder;

public class ChainContext {

    private Object payload;
    private ChainExecutionException lastException;

    public <T> T getPayload(Class<T> clazz) {
        return (T) payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public ChainExecutionException getLastException() {
        return lastException;
    }

    public void setLastException(ChainExecutionException exception) {
        this.lastException = exception;
    }

}
