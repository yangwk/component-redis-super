package com.github.open.component.redis.client.jedis.single;

import java.util.List;

import com.github.open.component.redis.api.RedisPubsub;
import com.github.open.component.redis.api.listener.MessageListener;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class JedisSingleRedisPubsub implements RedisPubsub {

	private JedisPool jedisPool;

	public JedisSingleRedisPubsub(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public List<String> channels() {
		return this.channels("*");
	}

	@Override
	public List<String> channels(String pattern) {
		try (Jedis jedis = jedisPool.getResource()) {
			List<String> channels = jedis.pubsubChannels(pattern);
			return channels;
		}
	}

	@Override
	public void subscribe(String channel, MessageListener listener) {
		try (Jedis jedis = jedisPool.getResource()) {
			JedisPubSub pubSub = new JedisPubSub() {
				@Override
				public void onMessage(String channel, String message) {
					listener.onMessage(channel, message);
				}
			};
			jedis.subscribe(pubSub, channel);
		}
	}

}
