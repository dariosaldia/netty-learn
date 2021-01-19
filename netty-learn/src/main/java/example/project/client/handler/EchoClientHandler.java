package example.project.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;

// Marks this class as one whose instances can be shared among channels
@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    // When notified that the channel is active, sends a message
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    //Logs a dump of the received message
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
    }

    // Logs a dump of the received message
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
