import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutionException;

public class NIO1 {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
       socketChannel.connect(new InetSocketAddress("localhost",1000)).get();

        int size = 1024 * 1024 ;
        ByteBuffer wrap = ByteBuffer.allocateDirect(size);

        wrap.put(new byte[size]);
        while(true){

            socketChannel.write(wrap).get();

            wrap.clear();


        }
    }
}
