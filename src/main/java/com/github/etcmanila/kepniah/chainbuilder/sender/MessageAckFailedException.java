package com.github.etcmanila.kepniah.chainbuilder.sender;

import com.github.etcmanila.kepniah.chainbuilder.ChainExecutionException;

import io.vertx.core.eventbus.ReplyException;

public class MessageAckFailedException extends ChainExecutionException {

    private Object payload;

    public MessageAckFailedException(ReplyException cause, Object payload) {
        super(cause);
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }

    public int getCode() {
        return ((ReplyException) getCause()).failureCode();
    }

}
