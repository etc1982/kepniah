package com.github.etcmanila.kepniah.chainbuilder;

import com.github.etcmanila.kepniah.chainbuilder.sender.ChainMessageCreator;

public interface ChainBuilderFilter {

    public <T> ChainBuilderFilter createMessage(Class<T> bodyClass, ChainMessageCreator<T> messageCreator);

    public void filter(ChainFilter filter);

}
