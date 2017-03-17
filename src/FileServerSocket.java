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
        DataInputStream dis = null;
        FileOutputStream fos = null;
        String dirFilePath = "E:/temp";
        try {
            try {
                dis = new DataInputStream(socket.getInputStream());
                File dirFile = new File(dirFilePath);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                // 读取文件名长度值
                byte[] fileNameLengthBytes = new byte[4];
                dis.read(fileNameLengthBytes, 0, 4);
                int fileNameLength = DataTypeUtil.bytesToInt(fileNameLengthBytes, 0);
                // 读取文件名
                byte[] fileNameBytes = new byte[fileNameLength];
                dis.read(fileNameBytes, 0, fileNameLength);
                String fileName = new String(fileNameBytes);
                System.out.println("接收文件名：" + fileName);
				/*
				 * 文件存储位置
				 */
                fos = new FileOutputStream(new File(dirFile, fileName));
                byte[] inputByte = new byte[1024];
                int length = 0;
                System.out.println("开始接收数据...");
                while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
                    fos.write(inputByte, 0, length);
                    fos.flush();
                }
                System.out.println("完成接收");
            } finally {
                if (fos != null)
                    fos.close();
                if (dis != null)
                    dis.close();
                if (socket != null)
                    socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
