import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by xiajun on 2017/4/12.
 */
public class UDPVoiceServer extends Thread {
    private static int PORT_LISTEN = 4567;
    private static int PORT_TARGET = 7654;
    private static String HOST_TARGET = "192.168.1.107";
    private DatagramSocket sender;

    @Override
    public void run() {
        super.run();
        startListen();
    }

    public void startListen() {
        DatagramSocket socket = null;

        try {
            sender = new DatagramSocket();
            socket = new DatagramSocket(PORT_LISTEN);
            while (true) {
                byte data[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);//阻塞
                System.out.println("new data");
                //String result = new String(packet.getData(), packet.getOffset(), packet.getLength());
                String hostAddress = packet.getAddress().getHostAddress();
                byte[] bytes = packet.getData();
                InetAddress serverAddress = InetAddress.getByName(hostAddress);
                //byte data[] = msg.getBytes();
                // 创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
                DatagramPacket packet2 = new DatagramPacket(data, data.length, serverAddress, PORT_TARGET);
                // 调用socket对象的send方法，发送数据
                sender.send(packet2);
                int port = packet.getPort();
                //System.out.println(hostAddress + ":" + port + "--->" + result);
                //send(hostAddress, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            socket.close();
        }
    }

    public void send(String hostAddress, String msg) {
        try {
            InetAddress serverAddress = InetAddress.getByName(hostAddress);
            byte data[] = msg.getBytes();
            // 创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, PORT_TARGET);
            // 调用socket对象的send方法，发送数据
            sender.send(packet);
            //sender.close();
            //socket.disconnect();
        } catch (Exception e) {
            sender.close();
            e.printStackTrace();
        }

    }
}
