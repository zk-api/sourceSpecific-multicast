package com.github.zk.nettyserver;

import com.github.zk.nettyserver.property.NettyProperties;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class NettyServerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private NettyProperties nettyProperties;
	@Test
	public void test1() {
		List<Map<String, String>> list = nettyProperties.getHosts();
		System.out.println(list.toString());
	}

}
