package com.github.zk.nettyclient.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * 消息处理器 {@link SimpleChannelInboundHandler}
 *
 * @author zk
 * @date 2020/5/18 17:05
 */
public class MessageChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        String s = datagramPacket.content().toString();
        System.out.println("客户端接收到消息：" + s);
    }
}
