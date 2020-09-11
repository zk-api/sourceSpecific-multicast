package com.github.zk.nettyclient.controller;

import com.github.zk.nettyclient.client.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 测试发送
 *
 * @author zk
 * @date 2020/5/18 17:24
 */
@RestController
public class SendMsg {

    @Autowired
    private NettyClient nettyClient;

    boolean flag = false;

    @RequestMapping("/send")
    public String send(@RequestParam("multicastAddr") String multicastAddr, @RequestParam("port") int port, @RequestParam("flag") boolean flag) throws InterruptedException {

        TimeUnit.SECONDS.sleep(3);
        this.flag = flag;
        while (this.flag) {
            nettyClient.doSend(multicastAddr, port, 10);
        }
        nettyClient.doSend(multicastAddr, port, 10);
        return "已发送至【" + multicastAddr + ":" + port + "】";
    }

    @RequestMapping("/sendFixed")
    public String sendFixed(@RequestParam("multicastAddr") String multicastAddr, @RequestParam("port") int port, @RequestParam long count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            nettyClient.doSend(multicastAddr, port, 10);
        }
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println(nettyClient.sum.get());
        return "success";
    }

    @RequestMapping("/sendPeriod")
    public String sendPeriod(@RequestParam int time, @RequestParam int count, @RequestParam int contentSize) throws InterruptedException {
        for (int i = 0; i < time; i++) {
            for (int j = 0; j < count; j++) {
                nettyClient.doSend("224.0.0.2", 9001, contentSize);
            }
            TimeUnit.SECONDS.sleep(1);
        }
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println(nettyClient.sum.get());
        return "success";
    }
}
