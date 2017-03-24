package launcher;


import com.wxsk.data.collector.launcher.AbstractLauncher;
import com.wxsk.data.collector.launcher.DefaultLauncher;
import com.wxsk.data.collector.launcher.listener.LauncherListener;
import com.wxsk.data.collector.netty.message.decoder.UdpTransferToTcpDecoder;
import com.wxsk.data.collector.netty.server.NettyTcpServer;
import com.wxsk.data.collector.netty.server.NettyUdpServer;
import com.wxsk.data.collector.server.DaemonServer;
import com.wxsk.data.collector.server.listener.DemonServerListener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TestDefaultLauncher {

    private static final Logger logger = LogManager.getLogger(TestDefaultLauncher.class);

    public static void main(String[] args) {

        //启动器
        DefaultLauncher launcher = new DefaultLauncher();
        //启动器监听器1
        LauncherListener launcherListener1 = new LauncherListener() {
            @Override
            public void startup(AbstractLauncher launcher) {
                logger.info("i am launcherListener1#startup");
            }

            @Override
            public void close(AbstractLauncher launcher) {
                logger.info("i am launcherListener1#close");
            }
        };
        //启动器监听器2
        LauncherListener launcherListener2 = new LauncherListener() {
            @Override
            public void startup(AbstractLauncher launcher) {
                logger.info("i am launcherListener2#startup");
            }

            @Override
            public void close(AbstractLauncher launcher) {
                logger.info("i am launcherListener2#close");
            }
        };
        List<LauncherListener> launcherListenerList = new ArrayList<>();
        launcherListenerList.add(launcherListener1);
        launcherListenerList.add(launcherListener2);
        launcher.setLauncherListenerList(launcherListenerList);

        //netty Tcp Server
        String tcpServerName = "tcp-server-0";
        int tcpServerPort = 50001;
        TestTcpChannelInitializer tcpServerInitializer = new TestTcpChannelInitializer();
        NettyTcpServer tcpServer = new NettyTcpServer(tcpServerName, tcpServerPort, tcpServerInitializer);
        DemonServerListener tcpServerListener1 = new DemonServerListener() {
            @Override
            public void startup(DaemonServer server) {
                logger.info("i am tcpServerListener1#startup");
            }

            @Override
            public void close(DaemonServer server) {
                logger.info("i am tcpServerListener1#close");
                System.out.println("tcp listener1: closing");
            }
        };
        DemonServerListener tcpServerListener2 = new DemonServerListener() {
            @Override
            public void startup(DaemonServer server) {
                logger.info("i am tcpServerListener2#startup");
            }

            @Override
            public void close(DaemonServer server) {
                logger.info("i am tcpServerListener2#close");
                System.out.println("tcp listener2: closing");
            }
        };
        List<DemonServerListener> tcpServerListenerList = new ArrayList<>();
        tcpServerListenerList.add(tcpServerListener1);
        tcpServerListenerList.add(tcpServerListener2);
        tcpServer.setDemonServerListenerList(tcpServerListenerList);

        //netty udp server
        String udpServerName = "udp-server-0";
        int udpServerPort = 60001;
        TestUdpChannelInitializer udpServerInitializer = new TestUdpChannelInitializer();
        NettyUdpServer udpServer = new NettyUdpServer(udpServerName, udpServerPort, udpServerInitializer);
        DemonServerListener udpServerListener1 = new DemonServerListener() {
            @Override
            public void startup(DaemonServer server) {
                logger.info("i am udpServerListener1#startup");
            }

            @Override
            public void close(DaemonServer server) {
                logger.info("i am udpServerListener1#close");
                System.out.println("udp listener1: closing");
            }
        };
        DemonServerListener udpServerListener2 = new DemonServerListener() {
            @Override
            public void startup(DaemonServer server) {
                logger.info("i am udpServerListener2#startup");
            }

            @Override
            public void close(DaemonServer server) {
                logger.info("i am udpServerListener2#close");
                System.out.println("udp listener2: closing");
            }
        };
        List<DemonServerListener> udpServerListenerList = new ArrayList<>();
        udpServerListenerList.add(udpServerListener1);
        udpServerListenerList.add(udpServerListener2);
        udpServer.setDemonServerListenerList(udpServerListenerList);

        List<DaemonServer> daemonServerList = new ArrayList<>();
        daemonServerList.add(tcpServer);
        daemonServerList.add(udpServer);

        launcher.setDaemonServerList(daemonServerList);

        launcher.startup();

    }
}





class TestTcpChannelInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new TcpEchoHandler());
    }
}

class TcpEchoHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger logger = LogManager.getLogger(TcpEchoHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        logger.info(msg.toString(Charset.defaultCharset()));
    }
}

class TestUdpChannelInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new UdpTransferToTcpDecoder());
        ch.pipeline().addLast(new UdpEchoHandler());
    }
}

class UdpEchoHandler extends SimpleChannelInboundHandler<io.netty.channel.socket.DatagramPacket> {

    private static final Logger logger = LogManager.getLogger(UdpEchoHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, io.netty.channel.socket.DatagramPacket msg) throws Exception {
        logger.info(msg.content().toString(Charset.defaultCharset()));
    }
}
