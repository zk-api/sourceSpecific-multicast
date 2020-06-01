package com.github.zk.nettyserver.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Netty配置
 *
 * @author zk
 * @date 2020/5/21 16:05
 */
@Configuration
@ConfigurationProperties(prefix = "netty.zk")
public class NettyProperties {
    //监听的组播地址
    private List<Map<String, String>> hosts;

    public List<Map<String, String>> getHosts() {
        return hosts;
    }

    public void setHosts(List<Map<String, String>> hosts) {
        this.hosts = hosts;
    }

}
