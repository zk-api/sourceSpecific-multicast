package com.github.zk.nettyserver.listener;

import com.github.zk.nettyserver.server.NettyServer;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.*;

/**
 * Servlet启动实现 {@link ServletContextListener}
 *
 * @author zk
 * @date 2020/5/18 16:37
 */
@WebListener
public class StartListener implements ServletContextListener {

    @Autowired
    private NettyServer nettyServer;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ExecutorService executorService = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory("zk"));
        executorService.execute(nettyServer::startNettyServer);
    }
}
