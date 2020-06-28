package com.github.zk.nettyserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息处理 {@link io.netty.channel.ChannelInboundHandlerAdapter} 实现
 *
 * @author zk
 * @date 2020/5/18 14:11
 */
public class MessageChannelHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(MessageChannelHandler.class);

    private Map<String, String> forward;

    public MessageChannelHandler(Map<String, String> forward) {
        this.forward = forward;
    }

    //服务启动后接收的数据量
    public static AtomicLong allReceive = new AtomicLong(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket packet = (DatagramPacket) msg;
        ByteBuf content = packet.content();
        //接收量计数 +1
        allReceive.incrementAndGet();

        if (forward != null) {
            // io/netty/channel/ChannelOutboundBuffer.java#remove()方法清除计数
            ctx.writeAndFlush(new DatagramPacket(content,
                            new InetSocketAddress(forward.get("host"), Integer.parseInt(forward.get("port")))));
        } else {
            //清除直接缓冲区计数，防止内存泄漏。ctx.writeAndFlush会清除缓冲区计数，因此只需在不转发时清除计数
            ReferenceCountUtil.release(packet);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
