package com.github.zk.nettyclient.client;

import com.github.zk.nettyclient.handler.MessageChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Netty 客户端连接
 *
 * @author zk
 * @date 2020/5/18 17:08
 */
public class NettyClient {
    final NioEventLoopGroup group = new NioEventLoopGroup();
    public void startNettyClient(String multicastAddr, int port) {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
//                            channel.joinGroup(InetAddress.getByName("224.0.0.2"), NetworkInterface.getByInetAddress(InetAddress.getByName("172.20.160.158")), InetAddress.getByName("172.20.160.158"));
                            pipeline.addLast(new MessageChannelHandler());
                        }
                    });
            ChannelFuture f = b.bind(0).sync();
            f.channel().writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("你好".getBytes()), new InetSocketAddress(multicastAddr, port)));
            System.out.println("客户端已发送");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("客户端断开连接");
            group.shutdownGracefully();
        }

    }
}
