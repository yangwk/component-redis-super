package com.github.open.component.redis.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.open.component.redis.RedisClient;
import com.github.open.component.redis.RedisConfig;
import com.github.open.component.redis.api.RedisMap;

public class TestRedisMap {

	private final String name = "testmap";
	private RedisClient redisClient;
	
	@Before
	public void init() {
		redisClient = new RedisClient(new RedisConfig("redis-server.properties").select("basicGroop"));
	}
	
	@Test
	public void testContainsKey() {
		RedisMap<String, String> map = redisClient.getRedisMap(name);
		
		boolean res = map.containsKey("test");
		System.out.println(res);
	}
	
	@Test
	public void testGet() {
		RedisMap<String, String> map = redisClient.getRedisMap(name);
		
		String content = map.get("test");
		System.out.println(content);
	}
	
	@Test
	public void testPut() {
		RedisMap<String, String> map = redisClient.getRedisMap(name);
		
		for(int r=0; r<5; r++) {
			boolean res = map.put(r+"test", "测试"+r);
			System.out.println(r + " , "+res);
		}
	}
	
	@Test
	public void testReadAllMap() {
		RedisMap<String, String> map = redisClient.getRedisMap(name);
		
		for(int r=0; r<2; r++) {
			Map<String,String> resMap = map.readAllMap();
			System.out.println(resMap);
		}
	}
	
	
	@Test
	public void testRemove() {
		RedisMap<String, String> map = redisClient.getRedisMap(name);
		
		for(int r=0; r<2; r++) {
			boolean res = map.remove(r+"test", r+"testtest");
			System.out.println(r + " , "+res);
		}
	}
	
	
	@Test
	public void testGetAll() {
		RedisMap<String, String> map = redisClient.getRedisMap(name);
		
		List<String> result = map.getAll(Arrays.asList(new String[] {"0test","1test"}));
		System.out.println(result);
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
