package com.wxsk.data.collector.netty.server;


import com.wxsk.data.collector.launcher.Launcher;
import com.wxsk.data.collector.server.AbstractDaemonServer;
import com.wxsk.data.collector.server.listener.DemonServerListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class NettyTcpServer extends AbstractDaemonServer {

    private static final Logger logger = LogManager.getLogger(NettyTcpServer.class);

    private ChannelInitializer channelInitializer;
    private List<DemonServerListener> demonServerListenerList;
    private Channel serverChannel;

    public void doStart(Launcher launcher) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture future = bootstrap.bind(getPort()).sync();
            logger.info(String.format("TCP Server: %s has been started successfully, port: %d", getName(), getPort()));
            if (demonServerListenerList != null && !demonServerListenerList.isEmpty()) {
                for (DemonServerListener listener: demonServerListenerList) {
                    listener.startup(this);
                }
            }
            if(launcher != null) {
                launcher.serverStartSuccess(this);
            }
            serverChannel = future.channel();
            serverChannel.closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.info(String.format("TCP Server: %s closed, port: %d ",getName(), getPort()));
                    if (demonServerListenerList != null && !demonServerListenerList.isEmpty()) {
                        for (DemonServerListener listener: demonServerListenerList) {
                            listener.close(NettyTcpServer.this);
                        }
                    }
                    launcher.serverShutdownSuccess(NettyTcpServer.this);
                }
            });
        } catch (Exception e) {
            logger.error(String.format("TCP Server: %s Down, port: %d ",getName(), getPort()), e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void doClose(Launcher launcher) {
        serverChannel.close();
    }

    public NettyTcpServer(int port, ChannelInitializer channelInitializer) {
        super(port);
        this.channelInitializer = channelInitializer;
    }

    public NettyTcpServer(String name, int port, ChannelInitializer channelInitializer) {
        super(name, port);
        this.channelInitializer = channelInitializer;
    }

    public ChannelInitializer getChannelInitializer() {
        return channelInitializer;
    }

    public NettyTcpServer setChannelInitializer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
        return this;
    }

    public List<DemonServerListener> getDemonServerListenerList() {
        return demonServerListenerList;
    }

    public NettyTcpServer setDemonServerListenerList(List<DemonServerListener> demonServerListenerList) {
        this.demonServerListenerList = demonServerListenerList;
        return this;
    }
}
