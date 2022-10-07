import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

public class NIOServer {
    public static  AtomicLong atomicLong = new AtomicLong();;
    static class CompletionHandlerImpl implements CompletionHandler<Integer,ByteBuffer>{

        AsynchronousSocketChannel channel;

        public CompletionHandlerImpl(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(Integer result, ByteBuffer buffer) {
            buffer.clear();
            atomicLong.addAndGet(result);
            channel.read(buffer, buffer,this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {

        }
    }
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousServerSocketChannel socketChannel = AsynchronousServerSocketChannel.open();
        socketChannel.bind(new InetSocketAddress(1000));



        Thread thread = new Thread(()->{
            long current = 0;
            while(true){
                try {
                    Thread.sleep(1000);
                    System.out.println((atomicLong.get()-current)/1024/1024+"mbps");
                    current = atomicLong.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();

            socketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                ByteBuffer buffer = ByteBuffer.allocateDirect(1024*1024);
                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {

                        result.read(buffer,buffer,new CompletionHandlerImpl(result));


                }

                @Override
                public void failed(Throwable exc, Object attachment) {

                }
            });



    }

}
