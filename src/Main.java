import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) throws IOException {
        AtomicLong acceptCount = new AtomicLong();
        System.out.println("Hello world!");
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(1000));
        Thread thread = new Thread(() -> {

            long current = 0;
                while(true){
                    try {
                        Thread.sleep(1000);
                        long now = acceptCount.get() -current;
                        current = acceptCount.get();
                        System.out.println(now/1024/1024+"MBPS");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

        });
        thread.start();
        while(true){
            Socket accept = serverSocket.accept();
            Thread thread2 = new Thread(() -> {
                try {
                    InputStream inputStream = accept.getInputStream();
                    byte[] bytes = new byte[1024*1024];

                    while(true){
                        int read = inputStream.read(bytes);
                        acceptCount.addAndGet(read);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            thread2.start();
        }


    }
}