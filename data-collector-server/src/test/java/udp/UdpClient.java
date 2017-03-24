package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient {
    public static void main(String[] args) throws Exception{
        // 创建发送方的套接字，IP默认为本地，端口号随机
        DatagramSocket sendSocket = new DatagramSocket();

        // 确定要发送的消息：
        String mes = "$$$@alarminfo@H201011010830100101&华联商厦&文化路南段100号&00009&华联商厦-四楼仓库11111111感烟探测器-自动火警&11111111&2010-11-01 08:30:10&2010-11-01 08:30:13@%%%";
//        String mes = "$$$@netstate@netok@%%%";

        // 由于数据报的数据是以字符数组传的形式存储的，所以传转数据
        byte[] buf = mes.getBytes("UTF-8");

        // 确定发送方的IP地址及端口号，地址为本地机器地址
        int port = 60001;
        InetAddress ip = InetAddress.getLocalHost();
//        InetAddress ip = InetAddress.getByName("114.113.120.227");
//        InetAddress ip = InetAddress.getByName("192.168.14.61");

        // 创建发送类型的数据报：
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip,port);

        // 通过套接字发送数据：

        for (int i=0; i<3; i++) {
            sendSocket.send(sendPacket);
        }
        Thread.sleep(1000);
        sendSocket.close();
    }
}
