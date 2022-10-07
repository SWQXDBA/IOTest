import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

public class Main2 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(1000));
        OutputStream outputStream = socket.getOutputStream();
        AtomicLong total = new AtomicLong();

        Thread thread = new Thread(()->{
            long now = 0;
           while(true){
               try {
                   Thread.sleep(1000);
                   System.out.println("send speed:: "+(total.get()-now)/1024/1024+"MBPS");
                   now = total.get();
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }

        });
        thread.start();
        byte[] bytes = new byte[1024*1024];
        while(true){

            outputStream.write(bytes);
            total.addAndGet(bytes.length);
        }
    }
}
