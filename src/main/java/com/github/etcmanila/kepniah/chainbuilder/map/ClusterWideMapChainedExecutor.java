package com.github.etcmanila.kepniah.chainbuilder.map;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;
import com.github.etcmanila.kepniah.chainbuilder.ChainExecutionException;
import com.github.etcmanila.kepniah.chainbuilder.ChainedExecutor;

import io.vertx.core.Vertx;

public class ClusterWideMapChainedExecutor extends ChainedExecutor {

    private Vertx vertx;
    private String mapName;

    public ClusterWideMapChainedExecutor(Vertx vertx, String mapName) {
        this.vertx = vertx;
        this.mapName = mapName;
    }

    @Override
    public void execute(ChainContext ctx) {
        try {
            vertx.sharedData().getClusterWideMap("map", res -> {
                if (res.succeeded()) {
                    ctx.setPayload(res.result());
                    safelyExecuteNext(ctx);
                } else {
                    ctx.setLastException(new ChainExecutionException(res.cause()));
                    getError().execute(ctx);
                }
            });
        } catch (Exception e) {
            safelyExecuteErrorHandler(e, ctx);
        }
    }

}
