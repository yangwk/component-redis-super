package com.github.open.component.redis.test.example;

import com.github.open.component.redis.RedisClient;
import com.github.open.component.redis.api.RedisDeque;

public class RecMsgQueRedisDeque implements RedisDeque<String>{

	private final String name = "RecMsgQue";	//接收队列
	private RedisDeque<String> deque;
	
	public RecMsgQueRedisDeque(RedisClient redisClient) {
		deque = redisClient.getRedisDeque(name);
	}
	
	@Override
	public String takeLastAndOfferFirstTo(String destination) throws InterruptedException {
		return deque.takeLastAndOfferFirstTo(destination);
	}

	@Override
	public boolean offerFirst(String e) {
		return deque.offerFirst(e);
	}

	@Override
	public boolean removeLast(String e) {
		return deque.removeLast(e);
	}

	@Override
	public String takeLast() throws InterruptedException {
		return deque.takeLast();
	}
	
}
