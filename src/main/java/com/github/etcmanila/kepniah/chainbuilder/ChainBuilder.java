package com.github.etcmanila.kepniah.chainbuilder;

import com.github.etcmanila.kepniah.chainbuilder.map.ClusterWideMapChainedExecutor;
import com.github.etcmanila.kepniah.chainbuilder.map.MapElementRetrieverChainExecutor;
import com.github.etcmanila.kepniah.chainbuilder.sender.ChainMessageCreator;
import com.github.etcmanila.kepniah.chainbuilder.sender.SenderChainedExecutor;
import com.github.etcmanila.kepniah.chainbuilder.verticle.VerticleCreator;
import com.github.etcmanila.kepniah.chainbuilder.verticle.VerticleCreatorChainedExecutor;

import io.vertx.core.Vertx;

public class ChainBuilder {

    private Vertx vertx;
    private ChainedExecutor firstExecutor;
    private ChainedExecutor lastExecutor;

    public static ChainBuilder chain(Vertx vertx) {
        ChainBuilder chainBuilder = new ChainBuilder();
        chainBuilder.vertx = vertx;
        return chainBuilder;
    }

    private ChainBuilder() {
    }

    public <T> ChainBuilder send(Class<T> bodyClazz, ChainMessageCreator<T> message) {
        SenderChainedExecutor<T> executor = new SenderChainedExecutor<>(vertx, message);
        addToChain(executor);
        return this;
    }

    public ChainBuilder filter(ChainFilter filter) {
        ChainFilterChainedExecutor executor = new ChainFilterChainedExecutor(filter);
        addToChain(executor);
        return this;
    }
    
    public ChainBuilder deployVerticle(VerticleCreator verticleCreator) {
        VerticleCreatorChainedExecutor executor = new VerticleCreatorChainedExecutor(vertx, verticleCreator);
        addToChain(executor);
        return this;
    }
    
    public ChainBuilder clusterWideMap(String mapName) {
        ClusterWideMapChainedExecutor executor = new ClusterWideMapChainedExecutor(vertx, mapName);
        addToChain(executor);
        return this;
    }
    
    public ChainBuilder getMapElement(String mapElementName) {
        MapElementRetrieverChainExecutor executor = new MapElementRetrieverChainExecutor();
        addToChain(executor);
        return this;
    }

    public void execute() {
        firstExecutor.execute(new ChainContext());
    }

    public void addToChain(ChainedExecutor executor) {
        if (null == firstExecutor) {
            firstExecutor = executor;
            lastExecutor = executor;
        } else {
            lastExecutor.setNext(executor);
            lastExecutor = executor;
        }

    }

}
