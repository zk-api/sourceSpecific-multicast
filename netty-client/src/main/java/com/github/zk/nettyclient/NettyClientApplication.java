package com.github.zk.nettyclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class NettyClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyClientApplication.class, args);
	}

}
