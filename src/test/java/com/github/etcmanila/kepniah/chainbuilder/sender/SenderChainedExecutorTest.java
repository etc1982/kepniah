package com.github.etcmanila.kepniah.chainbuilder.sender;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;
import com.github.etcmanila.kepniah.chainbuilder.ChainedExecutor;
import com.github.etcmanila.kepniah.chainbuilder.sender.SenderChainedExecutor;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class SenderChainedExecutorTest {

    private boolean nextIsExecuted;
    private boolean errorExecuted;

    @Test
    public void succssfulAcknowledgement() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        TestConsumer consumer = new TestConsumer(vertx);
        consumer.replyText = "foo";
        ChainContext context = new ChainContext();
        SenderChainedExecutor<String> unitUnderTest = new SenderChainedExecutor<>(vertx, ctx -> {
            ChainMessage<String> chainMessage = new ChainMessage<>();
            chainMessage.setAddress("test");
            chainMessage.setBody("Hello");
            return chainMessage;
        });
        unitUnderTest.setNext(new ChainedExecutor() {
            @Override
            public void execute(ChainContext context) {
                nextIsExecuted = true;
            }
        });
        unitUnderTest.execute(context);
        TimeUnit.SECONDS.sleep(1L);
        Assert.assertEquals("Message should be sent", "Hello", consumer.receivedText);
        Assert.assertTrue("Next Executor should be executed", nextIsExecuted);
        Assert.assertEquals("chain's payload should set with the body of the reply message",
                context.getPayload(String.class), consumer.replyText);
    }
    
    @Test
    public void handlingOfError() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        TestConsumer consumer = new TestConsumer(vertx);
        consumer.replyText = "foo";
        consumer.success = false;
        ChainContext context = new ChainContext();
        SenderChainedExecutor<String> unitUnderTest = new SenderChainedExecutor<>(vertx, ctx -> {
            ChainMessage<String> chainMessage = new ChainMessage<>();
            chainMessage.setAddress("test");
            chainMessage.setBody("Hello");
            return chainMessage;
        });
        unitUnderTest.setNext(new ChainedExecutor() {
            @Override
            public void execute(ChainContext context) {
                nextIsExecuted = true;
            }
        });
        unitUnderTest.setError(new ChainedExecutor() {
            
            

            @Override
            public void execute(ChainContext context) {
                errorExecuted = true;
            }
        });
        unitUnderTest.execute(context);
        TimeUnit.SECONDS.sleep(1L);
        Assert.assertEquals("Message should be sent", "Hello", consumer.receivedText);
        Assert.assertFalse("Next Executor should be executed", nextIsExecuted);
        Assert.assertTrue("Error executor must be executed", errorExecuted);
        Assert.assertTrue("Expecting Message Ack Failed Exception", context.getLastException().getClass().equals(MessageAckFailedException.class));

    }

    public static class TestConsumer implements Handler<io.vertx.core.eventbus.Message<Object>> {

        public String replyText;
        private String receivedText;
        private boolean success = true;

        public TestConsumer(Vertx vertx) {
            vertx.eventBus().consumer("test").handler(this);
        }

        @Override
        public void handle(io.vertx.core.eventbus.Message<Object> message) {
            this.receivedText = (String) message.body();
            if (success) {
                message.reply(replyText);
            } else {
                message.fail(0, "Failed Message");
            }
            
        }

    }

}
