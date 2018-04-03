package com.github.open.component.redis.client.redisson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import com.github.open.component.redis.api.RedisMap;

public class RedissonRedisMap<K, V> extends RedissonBase implements RedisMap<K, V>{

	private RMap<K, V> map;
	
	public RedissonRedisMap(RedissonClient redissonClient, String name) {
		this.map = redissonClient.getMap(name, codec);
	}
	
	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public V get(Object key) {
		Object obj = map.get(key);
		@SuppressWarnings("unchecked")
		V v = (V)obj;
		return v;
	}

	@Override
	public boolean put(K key, V value) {
		return map.fastPut(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object... keys) {
		return map.fastRemove((K[])keys) > 0;
	}

	@Override
	public Map<K, V> readAllMap() {
		return map.readAllMap();
	}

	@Override
	public List<V> getAll(List<K> keys) {
		java.util.Map<K, V> retmap = map.getAll(new HashSet<K>(keys));
		
		List<V> results = new ArrayList<V>();
		if(retmap != null) {
			for(K k : keys) {
				results.add( retmap.get(k) );
			}
		}
		return results;
	}

}
