package com.github.zk.nettyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class NettyServerApplication {

	//服务是否为Master
	public static boolean isMaster = false;

	public static void main(String[] args) {
		SpringApplication.run(NettyServerApplication.class, args);
	}

}
