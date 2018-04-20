package com.github.etcmanila.kepniah.chainbuilder.lock;import com.github.etcmanila.kepniah.chainbuilder.ChainContext;

public interface LockInfoGenerator {
    LockInfo getLockInfo(ChainContext ctx);
}
