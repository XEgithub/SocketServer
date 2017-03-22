import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xiajun on 2017/3/17.
 */
public class ChatServerSocket extends Thread {

    private Socket socket;

    public ChatServerSocket(Socket socket, String name) {
        super(name);
        this.socket = socket;
        System.out.println(this.getName() + " created");
    }

    @Override
    public void run() {
        super.run();
        System.out.println(this.getName() + " run");

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream dataInputStream = null;
                try {

                    while (!socket.isClosed()) {
                       /* BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        reader.readLine();*/
                        InputStream inputStream = socket.getInputStream();
                        if (inputStream.available()>0){
                            System.out.println("有新消息");
                            dataInputStream = new DataInputStream(inputStream);
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

                        }
                        //sendMsg("");

                    }
                } catch (Exception e) {
                    System.out.println("新消息异常");
                    e.printStackTrace();
                } finally {
                    try {
                        if (dataInputStream != null)
                            dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();

      /*  for (int i = 0; i < 5; i++) {
            try {
                sleep(3000);
                sendMsg("消息来自服务器==" + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void sendMsg(String msg) {
        DataOutputStream dataOutputStream = null;

        try {
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
                if (dataOutputStream != null)
                    dataOutputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
