package com.github.open.component.redis.test;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.open.component.redis.RedisClient;
import com.github.open.component.redis.RedisConfig;
import com.github.open.component.redis.api.RedisPubsub;
import com.github.open.component.redis.api.listener.MessageListener;

public class TestRedisPubsub {

	private RedisClient redisClient;
	
	@Before
	public void init() {
		redisClient = new RedisClient(new RedisConfig("redis-server.properties").select("basicGroop"));
	}
	
	@Test
	public void testChannels() {
		RedisPubsub pubsub = redisClient.getRedisPubsub();
		List<String> channels = pubsub.channels();
		
		System.out.println(channels);
	}
	
	
	@Test
	public void testChannelsPattern() {
		RedisPubsub pubsub = redisClient.getRedisPubsub();
		List<String> channels = pubsub.channels("ckmsg_*");
		
		System.out.println(channels);
	}
	
	
	@Test
	public void testSubscribe() {
		RedisPubsub pubsub = redisClient.getRedisPubsub();
		pubsub.subscribe("abc", new MessageListener() {
			
			@Override
			public void onMessage(String channel, Object message) {
				System.out.println("channel : " + channel + " , message : " + String.valueOf(message));
			}
		});
		
		
		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@After
	public void end() {
		try {
			redisClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
