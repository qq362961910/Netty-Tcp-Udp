package com.wxsk.data.collector.netty.protocol.analyser;

import com.wxsk.data.base.analyser.protocol.ProtocolAnalyser;
import com.wxsk.data.base.enums.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.TypeParameterMatcher;

public class NettyProtocolAnalyser implements ProtocolAnalyser<ByteBuf> {

    private final TypeParameterMatcher matcher;

    @Override
    public boolean support(ByteBuf byteBuf) {
        return matcher.match(byteBuf);
    }

    @Override
    public MessageProtocol analyser(ByteBuf byteBuf) {
        return null;
    }

    public NettyProtocolAnalyser() {
        matcher = TypeParameterMatcher.find(this, ProtocolAnalyser.class, "In");
    }
}
