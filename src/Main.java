
public class Main {


    public static void main(String[] args) {
        //dealText();
        dealFile();
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
