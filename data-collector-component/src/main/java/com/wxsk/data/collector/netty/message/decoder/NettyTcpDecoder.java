package com.wxsk.data.collector.netty.message.decoder;

import com.wxsk.data.base.analyser.message.MessageAnalyser;
import com.wxsk.data.base.analyser.message.MessageAnalyserManager;
import com.wxsk.data.base.analyser.protocol.ProtocolAnalyser;
import com.wxsk.data.base.analyser.protocol.ProtocolAnalyserManager;
import com.wxsk.data.base.enums.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * netty根解码器
 */
public class NettyTcpDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LogManager.getLogger(NettyTcpDecoder.class);

    private ProtocolAnalyserManager protocolAnalyserManager;
    private MessageAnalyserManager messageAnalyserManager;
    private MessageProtocol currentMessageProtocol;
    private MessageAnalyser currentMessageAnalyser;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {

        if (in.isReadable()) {
            if (currentMessageProtocol == null) {
                //mark reader index
                markReaderIndex(in);
                ProtocolAnalyser protocolAnalyser = protocolAnalyserManager.selectMessageAnalyser(in);
                if (protocolAnalyser == null) {
                    close(channelHandlerContext, "no ProtocolAnalyser found");
                }
                resetReaderIndex(in);
                markReaderIndex(in);
                currentMessageProtocol = protocolAnalyser.analyser(in);
                if (currentMessageProtocol == null) {
                    close(channelHandlerContext, "no MessageProtocol found");
                }
                resetReaderIndex(in);
                currentMessageAnalyser = messageAnalyserManager.selectMessageAnalyser(currentMessageProtocol);
                if (currentMessageAnalyser == null) {
                    close(channelHandlerContext, "no MessageAnalyser found");
                }
            }
            //mark reader index
            markReaderIndex(in);
            Object message = currentMessageAnalyser.analyse(in);
            if (message != null) {
                out.add(message);
                reset();
            }
            else {
                resetReaderIndex(in);
                return;
            }
        }
    }

    public MessageAnalyserManager getMessageAnalyserManager() {
        return messageAnalyserManager;
    }

    public NettyTcpDecoder setMessageAnalyserManager(MessageAnalyserManager messageAnalyserManager) {
        this.messageAnalyserManager = messageAnalyserManager;
        return this;
    }

    public ProtocolAnalyserManager getProtocolAnalyserManager() {
        return protocolAnalyserManager;
    }

    public NettyTcpDecoder setProtocolAnalyserManager(ProtocolAnalyserManager protocolAnalyserManager) {
        this.protocolAnalyserManager = protocolAnalyserManager;
        return this;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("app analyse message from: " + ctx.channel().localAddress() + "exception, connection is closing!", cause);
    }

    public void markReaderIndex(ByteBuf in) {
        in.markReaderIndex();
    }
    public void resetReaderIndex(ByteBuf in) {
        in.resetReaderIndex();
    }


    public void reset() {
        currentMessageAnalyser = null;
        currentMessageProtocol = null;
    }

    public void close(ChannelHandlerContext channelHandlerContext, String message) {
        logger.error(message);
        channelHandlerContext.close();
    }
}
