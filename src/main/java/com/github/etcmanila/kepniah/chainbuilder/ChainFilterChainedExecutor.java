package com.github.etcmanila.kepniah.chainbuilder;

public class ChainFilterChainedExecutor extends ChainedExecutor {

    private ChainFilter fitler;

    public ChainFilterChainedExecutor(ChainFilter fitler) {
        super();
        this.fitler = fitler;
    }

    @Override
    public void execute(ChainContext ctx) {
        fitler.filter(ctx);
        safelyExecuteNext(ctx);
    }

}
