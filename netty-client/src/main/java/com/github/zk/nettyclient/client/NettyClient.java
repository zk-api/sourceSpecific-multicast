package com.github.zk.nettyclient.client;

import com.github.zk.nettyclient.handler.MessageChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Netty 客户端连接
 *
 * @author zk
 * @date 2020/5/18 17:08
 */
@Component
public class NettyClient {
    public AtomicLong sum = new AtomicLong();

    final NioEventLoopGroup group = new NioEventLoopGroup();
    private ChannelFuture f;
    public void startNettyClient() {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
//                            channel.joinGroup(InetAddress.getByName("224.0.0.2"), NetworkInterface.getByInetAddress(InetAddress.getByName("172.20.160.158")), InetAddress.getByName("172.20.160.158"));
                            pipeline.addLast(new MessageChannelHandler());
                        }
                    });
            f = b.bind(0).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("客户端断开连接");
            group.shutdownGracefully();
        }
    }

    public void doSend(String multicastAddr, int port, int contentSize) {
        byte[] bytes = new byte[contentSize];
        f.channel().writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(bytes), new InetSocketAddress(multicastAddr, port)));
        f.addListener(future -> {
            if (future.isSuccess()) {
                sum.incrementAndGet();
            }
        });
    }
}
