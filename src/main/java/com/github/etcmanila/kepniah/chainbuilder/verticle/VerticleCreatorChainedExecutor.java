package com.github.etcmanila.kepniah.chainbuilder.verticle;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;
import com.github.etcmanila.kepniah.chainbuilder.ChainedExecutor;

import io.vertx.core.Vertx;

public class VerticleCreatorChainedExecutor extends ChainedExecutor  {

    private Vertx vertx;
    private VerticleCreator verticleCreator;

    public VerticleCreatorChainedExecutor(Vertx vertx, VerticleCreator verticleCreator) {
        this.vertx = vertx;
        this.verticleCreator = verticleCreator;
    }

    @Override
    public void execute(ChainContext ctx) {
       vertx.deployVerticle(verticleCreator.createVerticle(ctx));
       safelyExecuteNext(ctx);
    }

}
