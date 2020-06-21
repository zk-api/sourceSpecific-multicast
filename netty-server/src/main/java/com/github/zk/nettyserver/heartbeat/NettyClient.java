package com.github.zk.nettyserver.heartbeat;

import com.github.zk.nettyserver.heartbeat.handler.SendHeartBeatHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端连接服务端（心跳）
 *
 * @author zk
 * @date 2020/6/16 14:15
 */
public class NettyClient {
    private Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void startClient() {
        logger.info("启动心跳发送......");
        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.TCP_NODELAY, false)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new SendHeartBeatHandler(3));
                    }
                });
        while(true) {
            try {
                ChannelFuture f = b.connect(host, port).sync();
                f.channel().closeFuture().sync();
            } catch (Exception e) {
                logger.info("尝试寻找backup。。。");
                System.out.println(e.getMessage());
            }
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
