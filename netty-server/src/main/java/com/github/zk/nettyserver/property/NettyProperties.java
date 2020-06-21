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
    //发送心跳地址
    private Map<String, String> sendHeartBeat;
    //接收心跳地址
    private Map<String, Integer> checkHeartBeat;

    public List<Map<String, String>> getHosts() {
        return hosts;
    }

    public void setHosts(List<Map<String, String>> hosts) {
        this.hosts = hosts;
    }

    public Map<String, String> getSendHeartBeat() {
        return sendHeartBeat;
    }

    public void setSendHeartBeat(Map<String, String> sendHeartBeat) {
        this.sendHeartBeat = sendHeartBeat;
    }

    public Map<String, Integer> getCheckHeartBeat() {
        return checkHeartBeat;
    }

    public void setCheckHeartBeat(Map<String, Integer> checkHeartBeat) {
        this.checkHeartBeat = checkHeartBeat;
    }
}
