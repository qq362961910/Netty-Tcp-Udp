package com.wxsk.data.collector.netty.message.decoder;

import io.netty.handler.codec.DatagramPacketDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyUdpDecoder extends DatagramPacketDecoder {

    private static final Logger logger = LogManager.getLogger(NettyUdpDecoder.class);

    public NettyUdpDecoder(UdpTransferToTcpDecoder decoder) {
        super(decoder);
    }
}


