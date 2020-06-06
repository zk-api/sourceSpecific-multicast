package com.github.zk.nettyserver.server;

import com.github.zk.nettyserver.handler.MessageChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * netty服务端
 *
 * @author zk
 * @date 2020/5/18 14:21
 */
public class NettyServer {
    private Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private EventLoopGroup group = new NioEventLoopGroup();
    private List<Map<String, String>> hosts;

    public NettyServer(List<Map<String, String>> hosts) {
        this.hosts = hosts;
    }

    public void startNettyServer() {
        try {
            //UDP不能使用ServerBootStrap
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    //支持广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    //复用端口
                    .option(ChannelOption.SO_REUSEADDR, true)
                    //设置读缓冲区为1MB
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                    //设置写缓冲区为1MB
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            //添加处理器
                            pipeline.addLast(new MessageChannelHandler());
                        }
                    });
            //监听端口
            List<ChannelFuture> f = new ArrayList<>();
            //绑定所有组播地址、网卡、源地址
            for (int i = 0; i < hosts.size(); i++) {
                Map<String, String> host = hosts.get(i);
                try {
                    String ports = host.get("ports");
                    String[] portArray = ports.split(",");
                    for (int j = 0; j < portArray.length; j++) {
                        f.add(b.bind(Integer.parseInt(portArray[j])).sync());
                        InetAddress multicast = InetAddress.getByName(host.get("multicast"));
                        NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getByName(host.get("network")));
                        String sources = host.get("sources");
                        String[] sourcesArray = sources.split(",");
                        for (String source : sourcesArray) {
                            ((NioDatagramChannel) f.get(f.size() - 1).channel()).joinGroup(multicast, network, InetAddress.getByName(source));
                        }
                    }
                } catch (UnknownHostException | SocketException e) {
                    e.printStackTrace();
                }
            }
            logger.info("服务端已启动");
            hosts.forEach(host -> {
                logger.info("监听组播地址【{}】,源地址【{}】,端口【{}】", host.get("multicast"), host.get("sources"), host.get("ports"));
            });
            //监听连接关闭事件
            f.get(0).channel().closeFuture().sync();
            logger.info("连接已断开");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭连接
            group.shutdownGracefully();
        }
    }
}
