import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xiajun on 2017/3/17.
 */
public class FileServerSocket extends Thread {
    /**
     * socket端口号
     */
    private final static int SOCKET_PORT = 1314;

    ServerSocket server;

    public FileServerSocket(String name) {
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
                            /*
							 * 如果没有访问它会自动等待
							 */
                Socket socket = server.accept();
                System.out.println("有链接");
                receiveFile(socket);
            } catch (Exception e) {
                System.out.println("文件服务器异常");
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收文件方法
     *
     * @param socket
     * @throws IOException
     */
    public static void receiveFile(Socket socket) throws IOException {
        DataInputStream dataInputStream = null;
        FileOutputStream fileOutputStream = null;
        String dirFilePath = "E:/temp";
        DataOutputStream dataOutputStream = null;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            File dirFile = new File(dirFilePath);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            // 读取文件名长度值
            byte[] fileNameLengthBytes = new byte[4];
            dataInputStream.read(fileNameLengthBytes, 0, 1);
            int fileNameLength = fileNameLengthBytes[0];//DataTypeUtil.bytesToInt(fileNameLengthBytes, 0);
            // 读取文件名
            byte[] fileNameBytes = new byte[fileNameLength];
            dataInputStream.read(fileNameBytes, 0, fileNameLength);
            String fileName = new String(fileNameBytes);
            System.out.println("接收文件名：" + fileName);

            fileOutputStream = new FileOutputStream(new File(dirFile, fileName));
            byte[] inputByte = new byte[1024];
            int length;
            //double sumL = 0;
            System.out.println("开始接收数据...");
            while ((length = dataInputStream.read(inputByte, 0, inputByte.length)) > 0) {
                //System.out.println("已传输：" + ((sumL / l) * 100) + "%");
                fileOutputStream.write(inputByte, 0, length);
                fileOutputStream.flush();
            }
            System.out.println("完成接收");

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
            if (fileOutputStream != null)
                fileOutputStream.close();
            if (dataInputStream != null)
                dataInputStream.close();
            if (dataOutputStream != null)
                dataOutputStream.close();
            if (socket != null)
                socket.close();
        }
    }
}
