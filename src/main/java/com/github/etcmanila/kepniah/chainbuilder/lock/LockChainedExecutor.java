package com.github.etcmanila.kepniah.chainbuilder.lock;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;
import com.github.etcmanila.kepniah.chainbuilder.ChainedExecutor;

import io.vertx.core.Vertx;

public class LockChainedExecutor extends ChainedExecutor {

    private Vertx vertx;
    private LockInfoGenerator lockInfoGenerator;

    public LockChainedExecutor(Vertx vertx, LockInfoGenerator lockInfoGenerator) {
        this.vertx = vertx;
        this.lockInfoGenerator = lockInfoGenerator;
    }

    @Override
    public void execute(ChainContext ctx) {
        LockInfo info = lockInfoGenerator.getLockInfo(ctx);
        try {
            vertx.sharedData().getLockWithTimeout(info.getLockKey(), info.getTimeout(), res -> {
                if (res.succeeded()) {
                    safelyExecuteNext(ctx);
                } else {
                    getError().execute(ctx);
                }
            });
        } catch (Exception e) {
            safelyExecuteErrorHandler(e, ctx);
        }
    }

}
