package com.github.open.component.redis.client.jedis.single;

import java.util.List;
import java.util.Map;

import com.github.open.component.redis.api.RedisMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisSingleRedisMap<K, V> implements RedisMap<K, V> {

	private JedisPool jedisPool;
	private String name;

	public JedisSingleRedisMap(JedisPool jedisPool, String name) {
		this.jedisPool = jedisPool;
		this.name = name;
	}

	@Override
	public boolean containsKey(Object key) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hexists(name, key.toString());
		}
	}

	@Override
	public V get(Object key) {
		try (Jedis jedis = jedisPool.getResource()) {
			String result = jedis.hget(name, key.toString());
			@SuppressWarnings("unchecked")
			V v = (V) result;
			return v;
		}
	}

	@Override
	public boolean put(K key, V value) {
		try (Jedis jedis = jedisPool.getResource()) {
			Long lg = jedis.hset(name, key.toString(), value.toString());
			return lg > 0;
		}
	}

	@Override
	public boolean remove(Object... keys) {
		try (Jedis jedis = jedisPool.getResource()) {
			String[] params = new String[keys.length];
			System.arraycopy(keys, 0, params, 0, keys.length);
			Long ct = jedis.hdel(name, params);
			return ct > 0;
		}
	}

	@Override
	public Map<K, V> readAllMap() {
		try (Jedis jedis = jedisPool.getResource()) {
			Map<String,String> map = jedis.hgetAll(name);
			@SuppressWarnings("unchecked")
			Map<K, V> resultMap = (Map<K, V>)map;
			return resultMap;
		}
	}

	@Override
	public List<V> getAll(List<K> keys) {
		if(keys == null || keys.isEmpty()) {
			return null;
		}
		try (Jedis jedis = jedisPool.getResource()) {
			String[] keyArr = new String[0];
			List<String> list = jedis.hmget(name, keys.toArray(keyArr));
			@SuppressWarnings("unchecked")
			List<V> resultList = (List<V>)list;
			return resultList;
		}
	}

}
