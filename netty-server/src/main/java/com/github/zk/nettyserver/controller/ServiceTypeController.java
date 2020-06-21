package com.github.zk.nettyserver.controller;

import com.github.zk.nettyserver.NettyServerApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务类型
 *
 * @author zk
 * @date 2020/6/20 18:43
 */
@RestController
@RequestMapping("/serviceType")
public class ServiceTypeController {

    @RequestMapping("/getServiceType")
    public String getServiceType() {
        return NettyServerApplication.isMaster ? "Master" : "Backup";
    }
}
