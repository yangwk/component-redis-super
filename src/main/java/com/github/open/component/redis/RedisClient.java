package com.github.open.component.redis;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

import com.github.open.component.redis.api.RedisDeque;
import com.github.open.component.redis.api.RedisMap;
import com.github.open.component.redis.api.RedisPubsub;
import com.github.open.component.redis.client.jedis.cluster.JedisClusterPro;
import com.github.open.component.redis.client.jedis.cluster.JedisClusterRedisDeque;
import com.github.open.component.redis.client.jedis.cluster.JedisClusterRedisMap;
import com.github.open.component.redis.client.jedis.cluster.JedisClusterRedisPubsub;
import com.github.open.component.redis.client.jedis.single.JedisSingleRedisDeque;
import com.github.open.component.redis.client.jedis.single.JedisSingleRedisMap;
import com.github.open.component.redis.client.jedis.single.JedisSingleRedisPubsub;
import com.github.open.component.redis.client.redisson.RedissonRedisDeque;
import com.github.open.component.redis.client.redisson.RedissonRedisMap;
import com.github.open.component.redis.client.redisson.RedissonRedisPubsub;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * redis客户端
 * @author yangwk
 *
 */
public class RedisClient implements Closeable{
	
	private ClientWrapper clientWrapper;
	
	public RedisClient(RedisConfig redisConfig) {
		final String provider = redisConfig.getProvider();
		
		if(provider.equals(RedisConfig.PROVIDER_REDISSON)) {
			RedissonClient redissonClient = Redisson.create( redisConfig.buildRedissonConfig() );
			
			this.clientWrapper = new ClientWrapper(redissonClient, null, null);
		}else if(provider.equals(RedisConfig.PROVIDER_JEDIS)) {
			final String mode = redisConfig.getMode();
			
			if(mode.equals(RedisConfig.MODE_SINGLE)) {
				HostAndPort node = redisConfig.parse(redisConfig.getAddress()[0]);
				JedisPool jedisPool = new JedisPool(redisConfig.buildJedisPoolConfig(), node.getHost(), node.getPort() );
				
				this.clientWrapper = new ClientWrapper(null, jedisPool, null);
			}else if(mode.equals(RedisConfig.MODE_CLUSTER)) {
				String[] urls = redisConfig.getAddress();
				Set<HostAndPort> nodes = new HashSet<>();
				for(String url : urls) {
					nodes.add( redisConfig.parse(url) );
				}
				//使用扩展的
				JedisCluster jedisCluster = new JedisClusterPro(nodes, redisConfig.buildJedisPoolConfig());
				
				this.clientWrapper = new ClientWrapper(null, null, jedisCluster);
			}
		}
	}
	
	
	public <E> RedisDeque<E> getRedisDeque(String name){
		if(clientWrapper.redissonClient != null) {
			return new RedissonRedisDeque<E>(clientWrapper.redissonClient, name);
		}
		if(clientWrapper.jedisPool != null) {
			return new JedisSingleRedisDeque<E>(clientWrapper.jedisPool, name);
		}
		if(clientWrapper.jedisCluster != null) {
			return new JedisClusterRedisDeque<E>(clientWrapper.jedisCluster, name);
		}
		return null;
	}
	
	public <K,V> RedisMap<K,V> getRedisMap(String name){
		if(clientWrapper.redissonClient != null) {
			return new RedissonRedisMap<K,V>(clientWrapper.redissonClient, name);
		}
		if(clientWrapper.jedisPool != null) {
			return new JedisSingleRedisMap<K,V>(clientWrapper.jedisPool, name);
		}
		if(clientWrapper.jedisCluster != null) {
			return new JedisClusterRedisMap<K,V>(clientWrapper.jedisCluster, name);
		}
		return null;
	}
	
	public RedisPubsub getRedisPubsub() {
		if(clientWrapper.redissonClient != null) {
			return new RedissonRedisPubsub(clientWrapper.redissonClient);
		}
		if(clientWrapper.jedisPool != null) {
			return new JedisSingleRedisPubsub(clientWrapper.jedisPool);
		}
		if(clientWrapper.jedisCluster != null) {
			return new JedisClusterRedisPubsub(clientWrapper.jedisCluster);
		}
		return null;
	}
	
	
	private class ClientWrapper{
		private final RedissonClient redissonClient;	//single and cluster
		private final JedisPool jedisPool;	//single
		private final JedisCluster jedisCluster;	//cluster
		
		private ClientWrapper(RedissonClient redissonClient, JedisPool jedisPool, JedisCluster jedisCluster) {
			if(redissonClient == null && jedisPool == null && jedisCluster == null) {
				throw new IllegalArgumentException("ClientWrapper are all null");
			}
			if(redissonClient != null && jedisPool != null && jedisCluster != null) {
				throw new IllegalArgumentException("ClientWrapper are all not null");
			}
			this.redissonClient = redissonClient;
			this.jedisPool = jedisPool;
			this.jedisCluster = jedisCluster;
		}
	}


	@Override
	public void close() throws IOException {
		if(clientWrapper.redissonClient != null) {
			try {
				clientWrapper.redissonClient.shutdown();
			}catch(Exception e) {
				// ignore
			}
		}
		
		if(clientWrapper.jedisPool != null) {
			try {
				clientWrapper.jedisPool.close();
			}catch(Exception e) {
				// ignore
			}
		}
		
		if(clientWrapper.jedisCluster != null) {
			try {
				clientWrapper.jedisCluster.close();
			}catch(Exception e) {
				// ignore
			}
		}
	}
	
	
}
