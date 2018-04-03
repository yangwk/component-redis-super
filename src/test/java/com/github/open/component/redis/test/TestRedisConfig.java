package com.github.open.component.redis.test;

import org.junit.Before;
import org.junit.Test;

import com.github.open.component.redis.RedisConfig;

public class TestRedisConfig {
	
	private RedisConfig redisConfig;
	
	@Before
	public void init() {
		redisConfig = new RedisConfig("redis-server.properties");
		redisConfig.select("basicGroop");
	}
	
	@Test
	public void testContent() {
		System.out.println(redisConfig.toString());
	}
	
}

