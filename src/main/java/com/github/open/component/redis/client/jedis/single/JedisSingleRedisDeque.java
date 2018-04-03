package com.github.open.component.redis.client.jedis.single;

import java.util.List;

import com.github.open.component.redis.api.RedisDeque;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisSingleRedisDeque<E> implements RedisDeque<E> {

	private JedisPool jedisPool;
	private String name;

	public JedisSingleRedisDeque(JedisPool jedisPool, String name) {
		this.jedisPool = jedisPool;
		this.name = name;
	}

	@Override
	public E takeLastAndOfferFirstTo(String destination) throws InterruptedException {
		try (Jedis jedis = jedisPool.getResource()) {
			String result = jedis.brpoplpush(name, destination, 0);
			@SuppressWarnings("unchecked")
			E e = (E) result;
			return e;
		}
	}

	@Override
	public boolean offerFirst(E e) {
		try (Jedis jedis = jedisPool.getResource()) {
			Long lg = jedis.lpush(name, e.toString());
			return lg > 0;
		}
	}

	@Override
	public boolean removeLast(E e) {
		try (Jedis jedis = jedisPool.getResource()) {
			Long lg = jedis.lrem(name, -1, e.toString());	//从尾往头移除1个
			return lg > 0;
		}
	}

	@Override
	public E takeLast() throws InterruptedException {
		try (Jedis jedis = jedisPool.getResource()) {
			List<String> reply = jedis.brpop(0,name);
			String result = null;
			if(reply != null && ! reply.isEmpty()) {
				result = reply.get(1);
			}
			@SuppressWarnings("unchecked")
			E e = (E) result;
			return e;
		}
	}

}
