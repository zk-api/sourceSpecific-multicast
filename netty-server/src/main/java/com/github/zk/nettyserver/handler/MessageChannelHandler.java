package com.github.zk.nettyserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息处理 {@link io.netty.channel.ChannelInboundHandlerAdapter} 实现
 *
 * @author zk
 * @date 2020/5/18 14:11
 */
public class MessageChannelHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(MessageChannelHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket packet = (DatagramPacket) msg;
        ByteBuf content = packet.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);
        logger.info("接收到消息：" + new String(bytes));

        //清除直接缓冲区计数，防止内存泄漏
        ReferenceCountUtil.release(content);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
