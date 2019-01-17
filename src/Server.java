import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

public class Server {

    ServerSocketChannel serverSocketChannel;
    Selector selector;

    public Server() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        selector = Selector.open();
        SocketAddress  socketAddress = new InetSocketAddress(2020);
        serverSocketChannel.bind(socketAddress);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        run();

    }

    public void runS() throws IOException {
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
    public void run() throws IOException{
        while (true){
            selector.select();
            for(SelectionKey selectionKey: selector.selectedKeys() ){
                if (selectionKey.isAcceptable()){
                    SelectableChannel selectableChannel = selectionKey.channel();
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectableChannel;
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }else{
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    Charset charset = Charset.forName("UTF-8");
                    CharBuffer charBuffer = charset.decode(byteBuffer);
                    System.out.println(charBuffer);
                    byteBuffer.clear();
                }

            }
            //Clear selector
            selector.selectedKeys().clear();

        }
    }


}
