package com.github.open.component.redis.client.jedis.cluster;

import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterCommand;

/**
 * 增强的JedisCluster，扩展一些指令
 * @author yangwk
 *
 */
public class JedisClusterPro extends JedisCluster{

	public JedisClusterPro(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig) {
		super(nodes, poolConfig);
	}
	
	/**
	 * 原来的JedisCluster不支持pubsubChannels，需要扩展
	 */
	public List<String> pubsubChannels(String pattern){
		return new JedisClusterCommand<List<String>>(connectionHandler, maxAttempts) {
		      @Override
		      public List<String> execute(Jedis connection) {
		    	  return connection.pubsubChannels(pattern);
		      }
		    }.runWithAnyNode();
	}
	
}
