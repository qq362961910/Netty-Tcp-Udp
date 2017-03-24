package tcp;

import com.wxsk.data.collector.netty.server.NettyTcpServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class TestTcpServerLauncher {
    public static void main(String[] args) {
        String name = "test-tcp";
        int port = 9999;
        TestTcpChannelInitializer initializer = new TestTcpChannelInitializer();
        NettyTcpServer tcpServer = new NettyTcpServer(name, port, initializer);
        tcpServer.start(null);
    }
}


class TestTcpChannelInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new TcpEchoHandler());
    }
}

class TcpEchoHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println(msg.toString(Charset.defaultCharset()));
    }
}