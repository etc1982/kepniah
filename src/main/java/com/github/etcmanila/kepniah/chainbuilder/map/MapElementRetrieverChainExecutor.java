package com.github.etcmanila.kepniah.chainbuilder.map;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;
import com.github.etcmanila.kepniah.chainbuilder.ChainExecutionException;
import com.github.etcmanila.kepniah.chainbuilder.ChainedExecutor;

import io.vertx.core.shareddata.AsyncMap;

public class MapElementRetrieverChainExecutor extends ChainedExecutor {

    private Object key;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(ChainContext ctx) {
        AsyncMap map = ctx.getPayload(AsyncMap.class);
        try {
            map.get(key, r -> {
              ctx.setPayload(r);
              safelyExecuteNext(ctx);
            });
        } catch (Exception e) {
            safelyExecuteErrorHandler(e, ctx);
        }
    }

}
