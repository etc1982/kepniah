package com.github.etcmanila.kepniah.chainbuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.github.etcmanila.kepniah.chainbuilder.sender.ChainMessage;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class ChainBuilderTest {

    Vertx vertx;
    private TestConsumer consumer;
    private String replyText;

    @Before
    public void setup() {
        vertx = Vertx.vertx();
        consumer = new TestConsumer(vertx);
    }

    @Test
    public void testSending() throws InterruptedException {
        ChainBuilder chainBuilder = ChainBuilder.chain(vertx);
        chainBuilder.send(String.class, ctx -> {
            ChainMessage<String> message = new ChainMessage<>();
            message.setAddress("test");
            message.setBody("Hello");
            return message;
        });
      
        chainBuilder.execute();
        TimeUnit.SECONDS.sleep(1L);
        assertEquals("Hello", consumer.receivedText);
    }
    
    @Test
    public void testReceiving() throws InterruptedException {
        
        ChainBuilder chainBuilder = ChainBuilder.chain(vertx);
        chainBuilder.send(String.class, ctx -> {
            ChainMessage<String> message = new ChainMessage<>();
            message.setAddress("test");
            message.setBody("Hello");
            return message;
        })
        .filter(ctx -> {
            replyText = ctx.getPayload(String.class);
        });
      
        chainBuilder.execute();
        TimeUnit.SECONDS.sleep(1L);
        assertEquals("Hello", replyText);
    }
    
    @Test
    public void testDeployVerticle() throws InterruptedException {
        TestVerticle verticle = new TestVerticle();
        ChainBuilder chainBuilder = ChainBuilder.chain(vertx);
        chainBuilder.deployVerticle(ctx -> {
            return verticle;
        });
      
        chainBuilder.execute();
        TimeUnit.SECONDS.sleep(1L);
        assertTrue(verticle.deployed);
    }
    
    

    public static class TestConsumer implements Handler<io.vertx.core.eventbus.Message<Object>> {

        private String receivedText;

        public TestConsumer(Vertx vertx) {
            vertx.eventBus().consumer("test").handler(this);
        }

        @Override
        public void handle(io.vertx.core.eventbus.Message<Object> message) {
            this.receivedText = (String) message.body();
            message.reply(receivedText);
        }

    }
    
    public static class TestVerticle extends AbstractVerticle {
        private boolean deployed = false;
        @Override
        public void start() throws Exception {
            this.deployed = true;
        }
    }
}
