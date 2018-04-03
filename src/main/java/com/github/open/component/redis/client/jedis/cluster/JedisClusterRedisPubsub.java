package com.github.open.component.redis.client.jedis.cluster;

import java.util.List;

import com.github.open.component.redis.api.RedisPubsub;
import com.github.open.component.redis.api.listener.MessageListener;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

public class JedisClusterRedisPubsub implements RedisPubsub {

	private JedisCluster jedisCluster;

	public JedisClusterRedisPubsub(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	@Override
	public List<String> channels() {
		return this.channels("*");
	}

	@Override
	public List<String> channels(String pattern) {
		if(jedisCluster instanceof JedisClusterPro) {
			JedisClusterPro jedisClusterPro = (JedisClusterPro)jedisCluster;
			return jedisClusterPro.pubsubChannels(pattern);
		}
		
		throw new UnsupportedOperationException("JedisCluster do not support pubsubChannels");
	}

	@Override
	public void subscribe(String channel, MessageListener listener) {
		JedisPubSub pubSub = new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				listener.onMessage(channel, message);
			}
		};
		jedisCluster.subscribe(pubSub, channel);
	}

}
