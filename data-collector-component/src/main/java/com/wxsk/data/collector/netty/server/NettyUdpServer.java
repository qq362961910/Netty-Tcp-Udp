package com.wxsk.data.collector.netty.server;

import com.wxsk.data.collector.launcher.Launcher;
import com.wxsk.data.collector.server.AbstractDaemonServer;
import com.wxsk.data.collector.server.listener.DemonServerListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class NettyUdpServer extends AbstractDaemonServer {

    private static final Logger logger = LogManager.getLogger(NettyUdpServer.class);
    private ChannelInitializer channelInitializer;
    private List<DemonServerListener> demonServerListenerList;
    private Channel serverChannel;

    public void doStart(Launcher launcher) {
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_RCVBUF, 43690)
                    .handler(channelInitializer);
            ChannelFuture channelFuture = b.bind(getPort()).sync();
            logger.info(String.format("UDP Server: %s has been started successfully, port: %d", getName(), getPort()));
            if (demonServerListenerList != null && !demonServerListenerList.isEmpty()) {
                for (DemonServerListener listener: demonServerListenerList) {
                    listener.startup(this);
                }
            }
            if(launcher != null) {
                launcher.serverStartSuccess(this);
            }
            serverChannel = channelFuture.channel();
            serverChannel.closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.info(String.format("UDP Server: %s closed, port: %d ",getName(), getPort()));
                    if (demonServerListenerList != null && !demonServerListenerList.isEmpty()) {
                        for (DemonServerListener listener: demonServerListenerList) {
                            listener.close(NettyUdpServer.this);
                        }
                    }
                    launcher.serverShutdownSuccess(NettyUdpServer.this);
                }
            });
        } catch (InterruptedException e) {
            logger.error(String.format("UDP Server: %s Down, port: %d ",getName(), getPort()), e);
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void doClose(Launcher launcher) {
        serverChannel.close();
    }

    public NettyUdpServer(int port, ChannelInitializer channelInitializer) {
        super(port);
        this.channelInitializer = channelInitializer;
    }

    public NettyUdpServer(String name, int port, ChannelInitializer channelInitializer) {
        super(name, port);
        this.channelInitializer = channelInitializer;
    }

    public ChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    public NettyUdpServer setChannelInitializer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
        return this;
    }

    public List<DemonServerListener> getDemonServerListenerList() {
        return demonServerListenerList;
    }

    public NettyUdpServer setDemonServerListenerList(List<DemonServerListener> demonServerListenerList) {
        this.demonServerListenerList = demonServerListenerList;
        return this;
    }
}
