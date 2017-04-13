import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    /**
     * socket端口号
     */
    private final static int SOCKET_PORT = 1314;

    static ServerSocket server;

    public static void main(String[] args) {
        //dealText();
        //dealFile();
        //dealChat();
        listenUdp();
    }

    private static void listenUdp() {
        new UDPVoiceServer().start();
    }

    private static void dealChat() {
        try {
            server = new ServerSocket(SOCKET_PORT);
            while (true) {

                System.out.println("开始监听...");
                //如果没有访问它会自动等待
                Socket socket = server.accept();
                System.out.println("有新链接");
                ChatServerSocket chatServerSocket = new ChatServerSocket(socket, "chat");
                chatServerSocket.start();

            }
        } catch (Exception e) {
            System.out.println("服务器异常");
            e.printStackTrace();
        }
    }

    private static void dealText() {
        TextServerSocket server = new TextServerSocket("text thread");
        server.start(); // 启动线程运行
    }

    private static void dealFile() {
        FileServerSocket server = new FileServerSocket("file thread");
        server.start(); // 启动线程运行
    }

}
