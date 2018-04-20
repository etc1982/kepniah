package com.github.etcmanila.kepniah.chainbuilder.sender;

import com.github.etcmanila.kepniah.chainbuilder.ChainContext;

public interface ChainMessageCreator<T> {
    ChainMessage<T> create(ChainContext ctx);
}
