package com.github.zk.nettyserver.heartbeat;

import com.github.zk.nettyserver.heartbeat.handler.HeartBeatChannelHandler;
import com.github.zk.nettyserver.server.NettyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 心跳接收服务
 *
 * @author zk
 * @date 2020/6/16 14:38
 */
public class HeartBeatServer {
    private Logger logger = LoggerFactory.getLogger(HeartBeatServer.class);

    private NettyServer nettyServer;
    private NettyClient nettyClient;
    private int port;

    public HeartBeatServer(NettyServer nettyServer, NettyClient nettyClient, int port) {
        this.nettyServer = nettyServer;
        this.nettyClient = nettyClient;
        this.port = port;
    }
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void startHeartBeatServer() {
        logger.info("开启心跳检测，端口【" + port + "】");
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, false)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new HeartBeatChannelHandler(5, TimeUnit.SECONDS, nettyServer, nettyClient));
                    }
                });
        try {
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
