package com.github.zk.nettyclient.listener;

import com.github.zk.nettyclient.client.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * servlet容器启动监听
 *
 * @author zk
 * @date 2020/7/13 11:30
 */
@WebListener
public class StartListener implements ServletContextListener {

    @Autowired
    private NettyClient nettyClient;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        new Thread(nettyClient::startNettyClient).start();
    }
}
