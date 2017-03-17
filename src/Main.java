import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    /** socket端口号 */
    private final static int SOCKET_PORT = 1314;

    public static void main(String[] args) {
        dealText();
        // dealFile();
    }

    private static void dealText() {
        try {
            final ServerSocket server = new ServerSocket(SOCKET_PORT);
            Thread th = new Thread(new Runnable() {
                public void run() {
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
            });
            th.run(); // 启动线程运行
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void receiveText(Socket socket) {
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            // 读取头部长度值
            byte[] headerLengthBytes = new byte[4];
            dataInputStream.read(headerLengthBytes, 0, 4);
            int headerLength = bytesToInt(headerLengthBytes, 0);
            // 读取头部
            byte[] headerBytes = new byte[headerLength];
            dataInputStream.read(headerBytes, 0, headerLength);
            String header = new String(headerBytes);
            System.out.println("header：" + header);

            String body = "";
            byte[] bodyBytes = new byte[1024];
            int length = 0;
            System.out.println("start receiving...");
            while ((length = dataInputStream.read(bodyBytes, 0, bodyBytes.length)) > 0) {
                body += new String(bodyBytes);
            }
            System.out.println("body length：" + body.length());
            System.out.println("body content：" + body);
            System.out.println("receive complete");

            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            String answer = "received message!";
            int answerLength = answer.length();
            dataOutputStream.write(answerLength);
            dataOutputStream.writeChars(answer);
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

    private static void dealFile() {
        try {
            final ServerSocket server = new ServerSocket(SOCKET_PORT);
            Thread th = new Thread(new Runnable() {
                public void run() {
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
            });
            th.run(); // 启动线程运行
        } catch (Exception e) {
            e.printStackTrace();
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
                int fileNameLength = bytesToInt(fileNameLengthBytes, 0);
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

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24) | ((src[offset + 1] & 0xFF) << 16) | ((src[offset + 2] & 0xFF) << 8) | (src[offset + 3] & 0xFF));
        return value;
    }
}
