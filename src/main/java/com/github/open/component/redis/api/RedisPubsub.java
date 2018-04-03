package com.github.open.component.redis.api;

import java.util.List;

import com.github.open.component.redis.api.listener.MessageListener;

/**
 * redis pub/sub
 * @author yangwk
 *
 */
public interface RedisPubsub {
	
	/**
	 * 查询所有订阅通道
	 * @author yangwk
	 */
	List<String> channels();
	
	/**
	 * 查询所匹配正则表达式的订阅通道
	 * @param pattern 匹配的正则表达式
	 * @author yangwk
	 */
	List<String> channels(String pattern);
	
	/**
	 * 订阅某个通道
	 * @author yangwk
	 * @param channel 通道名称
	 * @param listener 监听接口
	 */
	void subscribe(String channel, MessageListener listener);
	
}
