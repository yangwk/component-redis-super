package com.github.open.component.redis.test.example;

import com.github.open.component.redis.RedisClient;
import com.github.open.component.redis.RedisConfig;

public class Main {

	public static void main(String[] args) {
		//多个对象可以使用同一个RedisClient
		
		//同一个配置
		RedisConfig redisConfig = new RedisConfig("redis-server.properties");
		
		//集群模式
		redisConfig.select("sendmsgGroop");
		RedisClient clusterRedisClient = new RedisClient(redisConfig);
		
		SendMsgQueRedisDeque sendMsgQueRedisDeque = new SendMsgQueRedisDeque(clusterRedisClient);
		sendMsgQueRedisDeque.offerFirst("Hello,World!");
		
		//单机模式
		redisConfig.select("recmsgGroop");
		RedisClient singleRedisClient = new RedisClient(redisConfig);
		
		RecMsgQueRedisDeque recMsgQueRedisDeque = new RecMsgQueRedisDeque(singleRedisClient);
		recMsgQueRedisDeque.removeLast("Yes");
	}

}
