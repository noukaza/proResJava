import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Server {

    ServerSocketChannel serverSocketChannel;
    Selector selector;

    public Server() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        selector = Selector.open();
        SocketAddress  socketAddress = new InetSocketAddress(2020);
        serverSocketChannel.bind(socketAddress);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        run();

    }

    public void run() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);
        while (socketChannel.isConnected()){
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            Charset charset = Charset.forName("UTF-8");
            CharBuffer charBuffer = charset.decode(byteBuffer);
            System.out.println(charBuffer);
            byteBuffer.clear();

        }
    }


}
