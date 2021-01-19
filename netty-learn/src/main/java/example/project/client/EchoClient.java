package example.project.client;

import java.net.InetSocketAddress;

import example.project.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            // Specifies EventLoopGroup to handle client events; NIO implementation is
            // needed
            b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                    // Adds an EchoClientHandler to the pipeline when a Channel is created
                    .handler(new ChannelInitializer<SocketChannel>() {
                        private SocketChannel ch;

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            this.ch = ch;
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            // Connects to the remote peer; waits until the connect completes
            ChannelFuture f = b.connect().sync();
            // Blocks until the Channel closes
            f.channel().closeFuture().sync();
        } finally {
            // Shuts down the thread pools and the release of all resources
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }
}
