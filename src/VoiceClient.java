import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by xiajun on 2017/4/12.
 */
public class VoiceClient extends Thread {
    private static int PORT_LISTEN = 3568;

    @Override
    public void run() {
        super.run();
        startListen();
    }

    public void startListen() {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(PORT_LISTEN);
            while (true) {
                byte data[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);//阻塞
                System.out.println("新udp数据包");
                //String result = new String(packet.getData(), packet.getOffset(), packet.getLength());
                String hostAddress = packet.getAddress().getHostAddress();
                int port = packet.getPort();
                System.out.println(hostAddress + ":" + port + "--数据长度-->" + packet.getLength());
                send(hostAddress, port, packet.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String hostAddress, int port, byte[] data) {
        DatagramSocket sender = null;
        try {
            sender = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("192.168.0.100");
            String msg = hostAddress + ":" + port;
            byte[] bytes = msg.getBytes();
            // 创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 42409);
            // 调用socket对象的send方法，发送数据
            sender.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sender != null)
                sender.close();
        }

    }
}
