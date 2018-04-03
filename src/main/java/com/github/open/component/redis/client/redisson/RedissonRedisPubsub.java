package com.github.open.component.redis.client.redisson;

import java.util.List;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import com.github.open.component.redis.api.RedisPubsub;
import com.github.open.component.redis.api.listener.MessageListener;

public class RedissonRedisPubsub extends RedissonBase implements RedisPubsub{

	private RedissonClient redissonClient;
	
	public RedissonRedisPubsub(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public List<String> channels() {
		return this.channels("*");
	}

	@Override
	public List<String> channels(String pattern) {
		RedissonPubsub pubsub = new RedissonPubsub(redissonClient);
		return pubsub.channels(pattern);
	}

	@Override
	public void subscribe(String channel, MessageListener listener) {
		RTopic<Object> topic = redissonClient.getTopic(channel, codec);
		
		topic.addListener(new org.redisson.api.listener.MessageListener<Object>() {
			
			@Override
			public void onMessage(String channel, Object msg) {
				listener.onMessage(channel, msg);
			}
		});
	}
	

}
