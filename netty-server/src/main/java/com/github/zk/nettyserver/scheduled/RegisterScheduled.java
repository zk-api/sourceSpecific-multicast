package com.github.zk.nettyserver.scheduled;

import com.github.zk.nettyserver.handler.MessageChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 计数任务
 *
 * @author zk
 * @date 2020/6/4 14:52
 */
@Component
public class RegisterScheduled {

    private Logger logger = LoggerFactory.getLogger(RegisterScheduled.class);
    //上次接收的数据量
    private long beforeCount = 0L;

    @Scheduled(cron = "0/1 * * * * ?")
    public void receiveCount() {
        //当前接收总量
        long count = MessageChannelHandler.allReceive.get();
        //增长量
        long increaseCount = count - beforeCount;
        logger.debug("每秒接收量：" + increaseCount);
        beforeCount = count;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void receiveCount2() {
        logger.debug("接收量总量：" + MessageChannelHandler.allReceive.get());
    }
}
