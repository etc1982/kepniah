package com.github.etcmanila.kepniah.chainbuilder.verticle;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;

import io.vertx.core.Verticle;

public interface VerticleCreator {
    Verticle createVerticle(ChainContext ctx);
}
