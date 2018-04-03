package com.github.open.component.redis.api;

import java.util.List;
import java.util.Map;

/**
 * redis hash
 * @author yangwk
 *
 */
public interface RedisMap<K, V> {

	/**
	 * 是否包含某个键
	 * @author yangwk
	 */
	boolean containsKey(Object key);

	/**
	 * 获取某个键的值
	 * @author yangwk
	 */
	V get(Object key);

	/**
	 * @return true如果key是一个新的字段；false如果key原来在map里面已经存在
	 * @author yangwk
	 */
	boolean put(K key, V value);

	/**
	 * @return true如果删除成功；false如果没有删除任何字段
	 * @author yangwk
	 */
	boolean remove(Object ... keys);
	
	/**
	 * 读取所有关联的K/V Map
	 * @return
	 */
	Map<K, V> readAllMap();
	
	/**
	 * 批量获取多个键的值
	 * @param keys
	 * @return
	 */
	List<V> getAll(List<K> keys);
}
