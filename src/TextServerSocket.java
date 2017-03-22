import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xiajun on 2017/3/17.
 */
public class TextServerSocket extends Thread {
    /**
     * socket端口号
     */
    private final static int SOCKET_PORT = 1314;

    ServerSocket server;

    public TextServerSocket(String name) {
        super(name);
        System.out.println(this.getName() + " created");
        try {
            server = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        System.out.println(this.getName() + " run");

        while (true) {
            try {
                System.out.println("开始监听...");
                //如果没有访问它会自动等待
                Socket socket = server.accept();
                System.out.println("有链接");
                receiveText(socket);
            } catch (Exception e) {
                System.out.println("文本服务器异常");
                e.printStackTrace();
            }
        }
    }

    public void receiveText(Socket socket) {
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            // 读取头部长度值
            byte[] headerLengthBytes = new byte[4];
            dataInputStream.read(headerLengthBytes, 0, 1);
            int headerLength = headerLengthBytes[0];//DataTypeUtil.bytesToInt(headerLengthBytes, 0);
            // 读取头部
            byte[] headerBytes = new byte[headerLength];
            dataInputStream.read(headerBytes, 0, headerLength);
            String header = new String(headerBytes);
            System.out.println("header：" + header);

            // 读取消息体长度值
            byte[] bodyLengthBytes = new byte[4];
            dataInputStream.read(bodyLengthBytes, 0, 1);
            int bodyLength = bodyLengthBytes[0];//DataTypeUtil.bytesToInt(headerLengthBytes, 0);
            // 读取消息体
            byte[] bodyBytes = new byte[bodyLength];

            System.out.println("start receiving...");
            dataInputStream.read(bodyBytes, 0, bodyLength);
            String body = new String(bodyBytes);

            System.out.println("body length：" + body.length());
            System.out.println("body content：" + body);
            System.out.println("receive complete");

            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            String result = "received message!";
            byte[] resultBytes = result.getBytes();
            int resultLength = resultBytes.length;
            System.out.println("result length：" + resultLength);
            dataOutputStream.write(resultLength);
            dataOutputStream.write(resultBytes);
            dataOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataInputStream != null)
                    dataInputStream.close();
                if (dataOutputStream != null)
                    dataOutputStream.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
