package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    //创建线程池，newFixedThreadPool里的参数为线程池大小
    private static ExecutorService service = Executors.newFixedThreadPool(50);

    public Server() throws IOException {
        System.out.println("服务器在端口8888监听...");
        ServerSocket serverSocket = new ServerSocket(8888);
        while (true) {
            //获取连接socket,如果没有客户端连接，就会阻塞在这里
            Socket socket = serverSocket.accept();
            service.execute(new ServerConnectClientThread(socket));
            System.out.println(socket + "连接了服务器");
        }
    }
}
