package com.wxsk.data.base.analyser.protocol;

import com.wxsk.data.base.enums.MessageProtocol;

public interface ProtocolAnalyser<In> {

    boolean support(In in);

    MessageProtocol analyser(In in);
}
