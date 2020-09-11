package com.github.zk.nettyclient.scheduled;

import com.github.zk.nettyclient.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 计数任务
 *
 * @author zk
 * @date 2020/6/4 14:52
 */
@Component
public class RegisterScheduled {

    private Logger logger = LoggerFactory.getLogger(RegisterScheduled.class);
    @Autowired
    private NettyClient nettyClient;

    //上次接收的数据量
    private long beforeCount = 0L;

//    @Scheduled(cron = "0/1 58 * * * ?")
    public void receiveCount() throws InterruptedException {
        for (int i = 0; i < 10000 ; i++) {
            nettyClient.doSend("224.0.0.2", 9001, 10);
        }
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println(nettyClient.sum.get());
    }
}
