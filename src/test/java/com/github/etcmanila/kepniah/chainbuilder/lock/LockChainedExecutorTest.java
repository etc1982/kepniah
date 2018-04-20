package com.github.etcmanila.kepniah.chainbuilder.lock;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;
import com.github.etcmanila.kepniah.chainbuilder.ChainedExecutor;

import io.vertx.core.Vertx;

public class LockChainedExecutorTest {
    boolean locked = false;
    boolean executedNext = false;
    private boolean executedErrorHandler = false;

    @Test
    public void obtainsLocksAndExecuteNextIfSuccessful() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        final String lockKey = "goodlock";
        LockInfoGenerator lockKeyGenerator = new LockInfoGenerator() {

            @Override
            public LockInfo getLockInfo(ChainContext ctx) {
                LockInfo info = new LockInfo();
                info.setLockKey(lockKey);
                info.setTimeout(2L);
                return info;
            }
        };
        ChainContext ctx = new ChainContext();
        LockChainedExecutor uut = new LockChainedExecutor(vertx, lockKeyGenerator);
        uut.setNext(new ChainedExecutor() {

            @Override
            public void execute(ChainContext context) {
                executedNext = true;
            }
        });

        uut.execute(ctx);

        vertx.sharedData().getLockWithTimeout(lockKey, 2L, res -> {
            if (res.succeeded()) {

            } else {
                System.out.println(res.cause().getClass() + ": " + res.cause().getMessage());
                locked = true;
            }
        });

        TimeUnit.SECONDS.sleep(3L);

        Assert.assertTrue(locked);
        Assert.assertTrue(executedNext);
    }
    
    @Test
    public void executesErrorHandlerIfObtainingLockIsNotSuccessful() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        final String lockKey = "goodlock";
        vertx.sharedData().getLock(lockKey, r->{});
        LockInfoGenerator lockKeyGenerator = new LockInfoGenerator() {

            @Override
            public LockInfo getLockInfo(ChainContext ctx) {
                LockInfo info = new LockInfo();
                info.setLockKey(lockKey);
                info.setTimeout(2L);
                return info;
            }
        };
        ChainContext ctx = new ChainContext();
        LockChainedExecutor uut = new LockChainedExecutor(vertx, lockKeyGenerator);
        uut.setNext(new ChainedExecutor() {

            @Override
            public void execute(ChainContext context) {
                executedNext = true;
            }
        });
        
        uut.setError(new ChainedExecutor() {
            
            @Override
            public void execute(ChainContext context) {
                executedErrorHandler = true;
            }
        });

        uut.execute(ctx);

        

        TimeUnit.SECONDS.sleep(3L);

        Assert.assertTrue(executedErrorHandler);
    }
}
