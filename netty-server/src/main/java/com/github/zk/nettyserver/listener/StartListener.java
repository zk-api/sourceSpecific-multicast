package com.github.zk.nettyserver.listener;

import com.github.zk.nettyserver.NettyServerApplication;
import com.github.zk.nettyserver.heartbeat.HeartBeatServer;
import com.github.zk.nettyserver.heartbeat.NettyClient;
import com.github.zk.nettyserver.server.NettyServer;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Servlet启动实现 {@link ServletContextListener}
 *
 * @author zk
 * @date 2020/5/18 16:37
 */
@WebListener
public class StartListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(StartListener.class);

    @Autowired
    private NettyServer nettyServer;
    @Autowired
    private NettyClient nettyClient;
    @Autowired
    private HeartBeatServer heartBeatServer;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ExecutorService executorService = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory("zk"));
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(nettyClient.getHost(), nettyClient.getPort()), 1000);
        } catch (IOException e) {
            NettyServerApplication.isMaster = true;
            System.out.println(e.getMessage());
        }
        logger.info("本机为：{}", (NettyServerApplication.isMaster ? "Master" : "Backup"));
        if (NettyServerApplication.isMaster) {
            //启动接收
            executorService.execute(nettyServer::startNettyServer);
            //启动心跳发送
            executorService.execute(nettyClient::startClient);
        }
        //启动心跳检测
        executorService.execute(heartBeatServer::startHeartBeatServer);
    }
}
