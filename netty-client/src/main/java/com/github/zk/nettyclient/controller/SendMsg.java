package com.github.zk.nettyclient.controller;

import com.github.zk.nettyclient.client.NettyClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试发送
 *
 * @author zk
 * @date 2020/5/18 17:24
 */
@RestController
public class SendMsg {
    boolean flag = false;
    @RequestMapping("/send")
    public String send(@RequestParam("multicastAddr") String multicastAddr, @RequestParam("port") int port, @RequestParam("flag") boolean flag) {
        NettyClient nettyClient = new NettyClient();
        this.flag = flag;
        /*while (this.flag) {
            nettyClient.startNettyClient(multicastAddr, port);
        }*/
        nettyClient.startNettyClient(multicastAddr, port);
        return "已发送至【"+ multicastAddr + ":"+ port +"】";
    }
}
