package udp;

import com.wxsk.data.collector.netty.message.decoder.UdpTransferToTcpDecoder;
import com.wxsk.data.collector.netty.server.NettyUdpServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class TestUdpServerLauncher {

    public static void main(String[] args) {
        String name = "test-udp";
        int port = 9999;

        TestUdpChannelInitializer initializer = new TestUdpChannelInitializer();
        NettyUdpServer udpServer = new NettyUdpServer(name, port, initializer);
//        udpServer.setBindCompleteChannelFutureListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                final HashedWheelTimer timer = new HashedWheelTimer();
//                String remoteIp = "127.0.0.1";
//                int remotePort = 5000;
//                int period = 5;
//                timer.newTimeout(new TimerTask() {
//                    public void run(Timeout timeout) throws Exception {
//                        byte[] hearBeat = "$$$@netstate@netok@%%%".getBytes();
//                        DatagramPacket sendPacket = new DatagramPacket(hearBeat, hearBeat.length, InetAddress.getByName(remoteIp), remotePort);
//                        DatagramSocket sendSocket = new DatagramSocket();
//                        sendSocket.send(sendPacket);
//                        sendSocket.close();
//                        System.out.println(String.format("send a heart beat to cfs: ip: %s, port: %d", remoteIp, remotePort));
//                        timer.newTimeout(this, period, TimeUnit.SECONDS);
//                    }
//                }, period, TimeUnit.SECONDS);
//                timer.start();
//            }
//        });
        udpServer.start(null);
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, io.netty.channel.socket.DatagramPacket msg) throws Exception {
        System.out.println(msg.content().toString(Charset.defaultCharset()));
    }
}
