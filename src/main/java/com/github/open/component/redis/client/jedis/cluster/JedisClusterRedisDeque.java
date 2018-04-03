package com.github.open.component.redis.client.jedis.cluster;

import java.util.List;

import com.github.open.component.redis.api.RedisDeque;

import redis.clients.jedis.JedisCluster;

public class JedisClusterRedisDeque<E> implements RedisDeque<E> {

	private JedisCluster jedisCluster;
	private String name;

	public JedisClusterRedisDeque(JedisCluster jedisCluster, String name) {
		this.jedisCluster = jedisCluster;
		this.name = name;
	}

	@Override
	public E takeLastAndOfferFirstTo(String destination) throws InterruptedException {
		String result = jedisCluster.brpoplpush(name, destination, 0);
		@SuppressWarnings("unchecked")
		E e = (E) result;
		return e;
	}

	@Override
	public boolean offerFirst(E e) {
		Long lg = jedisCluster.lpush(name, e.toString());
		return lg > 0;
	}

	@Override
	public boolean removeLast(E e) {
		Long lg = jedisCluster.lrem(name, -1, e.toString());	//从尾往头移除1个
		return lg > 0;
	}

	@Override
	public E takeLast() throws InterruptedException {
		List<String> reply = jedisCluster.brpop(0,name);
		String result = null;
		if(reply != null && ! reply.isEmpty()) {
			result = reply.get(1);
		}
		@SuppressWarnings("unchecked")
		E e = (E) result;
		return e;
	}

}
