package com.github.open.component.redis.client.jedis.cluster;

import java.util.List;
import java.util.Map;

import com.github.open.component.redis.api.RedisMap;

import redis.clients.jedis.JedisCluster;

public class JedisClusterRedisMap<K, V> implements RedisMap<K, V> {

	private JedisCluster jedisCluster;
	private String name;

	public JedisClusterRedisMap(JedisCluster jedisCluster, String name) {
		this.jedisCluster = jedisCluster;
		this.name = name;
	}

	@Override
	public boolean containsKey(Object key) {
		return jedisCluster.hexists(name, key.toString());
	}

	@Override
	public V get(Object key) {
		String result = jedisCluster.hget(name, key.toString());
		@SuppressWarnings("unchecked")
		V v = (V) result;
		return v;
	}

	@Override
	public boolean put(K key, V value) {
		Long lg = jedisCluster.hset(name, key.toString(), value.toString());
		return lg > 0;
	}

	@Override
	public boolean remove(Object... keys) {
		String[] params = new String[keys.length];
		System.arraycopy(keys, 0, params, 0, keys.length);
		Long ct = jedisCluster.hdel(name, params);
		return ct > 0;
	}

	@Override
	public Map<K, V> readAllMap() {
		Map<String,String> map = jedisCluster.hgetAll(name);
		@SuppressWarnings("unchecked")
		Map<K, V> resultMap = (Map<K, V>)map;
		return resultMap;
	}

	@Override
	public List<V> getAll(List<K> keys) {
		if(keys == null || keys.isEmpty()) {
			return null;
		}
		String[] keyArr = new String[0];
		List<String> list = jedisCluster.hmget(name, keys.toArray(keyArr));
		@SuppressWarnings("unchecked")
		List<V> resultList = (List<V>)list;
		return resultList;
	}

}
