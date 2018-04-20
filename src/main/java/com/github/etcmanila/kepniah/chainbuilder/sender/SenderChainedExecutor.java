package com.github.etcmanila.kepniah.chainbuilder.sender;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;
import com.github.etcmanila.kepniah.chainbuilder.ChainedExecutor;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;

public class SenderChainedExecutor<T> extends ChainedExecutor {

    private ChainMessageCreator<T> messageCreator;
    private Vertx vertx;
    private ChainMessage<T> message;

    public SenderChainedExecutor(Vertx vertx, ChainMessageCreator<T> messageCreator) {
        this.messageCreator = messageCreator;
        this.vertx = vertx;
    }

    @Override
    public void execute(final ChainContext ctx) {
        try {
            message = messageCreator.create(ctx);
            vertx.eventBus().send(message.getAddress(), message.getBody(), reply -> this.handleReply(reply, ctx));
        } catch (Exception e) {
            safelyExecuteErrorHandler(e, ctx);
        }
    }

    private void handleReply(AsyncResult<Message<Object>> reply, ChainContext ctx) {
        if (reply.succeeded()) {
            ctx.setPayload(reply.result().body());
            safelyExecuteNext(ctx);
        } else {
            ReplyException replyException = (ReplyException) reply.cause();
            MessageAckFailedException exception = new MessageAckFailedException(replyException, message.getBody());
            ctx.setLastException(exception);
            getError().execute(ctx);
        }

    }

}
