package com.github.open.component.redis.test;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.open.component.redis.RedisClient;
import com.github.open.component.redis.RedisConfig;
import com.github.open.component.redis.api.RedisDeque;

public class TestRedisDeque {

	private final String name = "testdeque";
	private RedisClient redisClient;
	
	@Before
	public void init() {
		redisClient = new RedisClient(new RedisConfig("redis-server.properties").select("basicGroop"));
	}
	
	@Test
	public void testTakeLastAndOfferFirstTo() {
		RedisDeque<String> deque = redisClient.getRedisDeque(name);
		
		for(int r=0; r<5; r++) {
			try {
				String content = deque.takeLastAndOfferFirstTo(name + "_copy");
				System.out.println(r + " , " + content);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testOfferFirst() {
		RedisDeque<String> deque = redisClient.getRedisDeque(name);
		
		for(int r=0; r<5; r++) {
			boolean res = deque.offerFirst(r + "test");
			System.out.println(r + " , " + res);
		}
	}
	
	@Test
	public void testRemoveLast() {
		RedisDeque<String> deque = redisClient.getRedisDeque(name);
		
		for(int r=0; r<2; r++) {
			boolean res = deque.removeLast(r + "test");
			System.out.println(r + " , " + res);
		}
	}
	
	
	@Test
	public void testTakeLast() {
		RedisDeque<String> deque = redisClient.getRedisDeque(name);
		
		for(int r=0; r<2; r++) {
			try {
				String content = deque.takeLast();
				System.out.println(r + " , " + content);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
