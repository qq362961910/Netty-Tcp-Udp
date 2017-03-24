package com.wxsk.data.collector.netty.message.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class UdpTransferToTcpDecoder extends MessageToMessageDecoder<ByteBuf> {

    private NettyTcpDecoder nettyTcpDecoder;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List out) throws Exception {
        nettyTcpDecoder.decode(ctx, buf, out);
    }

    public NettyTcpDecoder getNettyTcpDecoder() {
        return nettyTcpDecoder;
    }

    public UdpTransferToTcpDecoder setNettyTcpDecoder(NettyTcpDecoder nettyTcpDecoder) {
        this.nettyTcpDecoder = nettyTcpDecoder;
        return this;
    }
}
