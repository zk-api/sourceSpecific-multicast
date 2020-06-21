package com.github.zk.nettyserver.config;

import com.github.zk.nettyserver.heartbeat.HeartBeatServer;
import com.github.zk.nettyserver.heartbeat.NettyClient;
import com.github.zk.nettyserver.property.NettyProperties;
import com.github.zk.nettyserver.server.NettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Netty 自动装配
 *
 * @author zk
 * @date 2020/5/21 18:30
 */
@Configuration
public class NettyAutoConfiguration {

    @Bean
    public NettyServer server(NettyProperties properties) {
        List<Map<String, String>> hosts = properties.getHosts();
        return new NettyServer(hosts);
    }

    @Bean
    public NettyClient client(NettyProperties properties) {
        Map<String, String> sendHeartBeat = properties.getSendHeartBeat();
        return new NettyClient(sendHeartBeat);
    }

    @Bean
    public HeartBeatServer heartBeat(NettyProperties properties) {
        Map<String, Integer> checkHeartBeat = properties.getCheckHeartBeat();
        int port = checkHeartBeat.get("port");
        return new HeartBeatServer(server(properties), client(properties), port);
    }
}
