package com.github.etcmanila.kepniah.chainbuilder;

public abstract class ChainedExecutor {

    private ChainedExecutor next;
    private ChainedExecutor error;

    public abstract void execute(ChainContext context);

    public ChainedExecutor getNext() {
        return next;
    }

    public void setNext(ChainedExecutor next) {
        this.next = next;
    }
    
    public ChainedExecutor getError() {
        return error;
    }

    public void setError(ChainedExecutor error) {
        this.error = error;
    }

    protected void safelyExecuteNext(ChainContext ctx) {
        if (null != next) {
            next.execute(ctx);
        }
    }
    
    protected void safelyExecuteErrorHandler(Exception e, ChainContext ctx) {
        if (error != null) {
            ChainExecutionException chainExecutionException = new ChainExecutionException(e);
            ctx.setLastException(chainExecutionException);
            error.execute(ctx);
        }
    }

    
}
