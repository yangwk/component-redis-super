package com.github.open.component.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.redisson.config.Config;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置
 * @author yangwk
 *
 */
public class RedisConfig {
	public static final String MODE_SINGLE = "single";
	public static final String MODE_CLUSTER = "cluster";
	
	public static final String PROVIDER_JEDIS = "jedis";
	public static final String PROVIDER_REDISSON = "redisson";
	
	
	//特定key规范定义
	private final String Mode_Prefix = "redis.mode.";
	private final String Address_Prefix = "redis.address.";
	private final String Provider_Key = "redis.provider";
	
	private Properties properties;
	private List<String> suffixList = new ArrayList<>();
	
	private String suffix;
	
	public RedisConfig(String resourceFile) {
		initProperties(resourceFile);
		init();
	}
	
	private void initProperties(String resourceFile) {
		properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = RedisConfig.class.getClassLoader().getResourceAsStream(resourceFile);
			properties.load(inputStream);
		} catch (IOException e) {
			throw new IllegalArgumentException("can not load properties : "+resourceFile, e);
		}finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	
	/**
	 * 选择后缀作为构建
	 */
	public RedisConfig select(String suffix) {
		if(! suffixList.contains(suffix)) {
			throw new IllegalArgumentException("unknown redis suffix : " + suffix);
		}
		this.suffix = suffix;
		return this;
	}
	
	
	private void init() {
		List<String> modeSuffixs = new ArrayList<>();
		List<String> addressSuffixs = new ArrayList<>();
		
		//提取特定值
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		for(Entry<Object, Object> entry : entrySet) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			
			if(key.startsWith(Mode_Prefix)) {
				modeSuffixs.add( key.substring(Mode_Prefix.length()) );
				
				if(! MODE_SINGLE.equals(value) && ! MODE_CLUSTER.equals(value)) {
					throw new IllegalArgumentException("unknown redis mode : " + value);
				}
			}else if(key.startsWith(Address_Prefix)) {
				addressSuffixs.add( key.substring(Address_Prefix.length()) );
			}
		}
		
		if(modeSuffixs.isEmpty() || addressSuffixs.isEmpty()) {
			throw new IllegalArgumentException("no redis mode and address");
		}
		
		Collections.sort(modeSuffixs);
		Collections.sort(addressSuffixs);
		if(! modeSuffixs.equals(addressSuffixs)) {
			throw new IllegalArgumentException("redis mode suffix and address suffix do not equal");
		}
		
		suffixList.addAll(modeSuffixs);
		
		String provider = properties.getProperty(Provider_Key);
		
		if(! PROVIDER_JEDIS.equals(provider) && ! PROVIDER_REDISSON.equals(provider)) {
			throw new IllegalArgumentException("unknown redis provider : " + provider);
		}
	}
	
	

	@Override
	public String toString() {
		return properties.toString() + " , suffix : " + suffix;
	}
	
	protected String[] getAddress() {
		return properties.getProperty(Address_Prefix+suffix).split(",");
	}
	
	protected String getMode() {
		return properties.getProperty(Mode_Prefix+suffix);
	}

	protected String getProvider() {
		return properties.getProperty(Provider_Key);
	}
	
	protected HostAndPort parse(String url) {
		String[] arr = url.split(":");
		return new HostAndPort(arr[0], Integer.parseInt(arr[1]));
	}
	
	
	protected JedisPoolConfig buildJedisPoolConfig() {
		Properties p = properties;
		
		JedisPoolConfig config = new JedisPoolConfig();

		// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		config.setBlockWhenExhausted(Boolean.valueOf(p.getProperty("jedis.blockWhenExhausted")));

		// 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
		config.setEvictionPolicyClassName(p.getProperty("jedis.evictionPolicyClassName"));

		// 是否启用pool的jmx管理功能, 默认true
		config.setJmxEnabled(Boolean.valueOf(p.getProperty("jedis.jmxEnabled")));

		config.setJmxNamePrefix(p.getProperty("jedis.jmxNamePrefix"));

		// 是否启用后进先出, 默认true
		config.setLifo(Boolean.valueOf(p.getProperty("jedis.lifo")));

		// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		config.setMinEvictableIdleTimeMillis(Long.parseLong(p.getProperty("jedis.minEvictableIdleTimeMillis")));

		// 最小空闲连接数, 默认0
		config.setMinIdle(Integer.parseInt(p.getProperty("jedis.minIdle")));

		// 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		config.setNumTestsPerEvictionRun(Integer.parseInt(p.getProperty("jedis.numTestsPerEvictionRun")));

		// 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数
		// 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
		config.setSoftMinEvictableIdleTimeMillis(Long.parseLong(p.getProperty("jedis.softMinEvictableIdleTimeMillis")));

		// 在获取连接的时候检查有效性, 默认false
		config.setTestOnBorrow(Boolean.valueOf(p.getProperty("jedis.testOnBorrow")));
		
		config.setTestOnReturn(Boolean.valueOf(p.getProperty("jedis.testOnReturn")));

		// 在空闲时检查有效性, 默认false
		config.setTestWhileIdle(Boolean.valueOf(p.getProperty("jedis.testWhileIdle")));

		// 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		config.setTimeBetweenEvictionRunsMillis(Long.parseLong(p.getProperty("jedis.timeBetweenEvictionRunsMillis")));

		// 最大空闲连接数, 默认8个
		config.setMaxIdle(Integer.parseInt(p.getProperty("jedis.maxIdle")));

		// 最大连接数, 默认8个
		config.setMaxTotal(Integer.parseInt(p.getProperty("jedis.maxTotal")));

		// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,默认-1
		config.setMaxWaitMillis(Long.parseLong(p.getProperty("jedis.maxWaitMillis")));

		return config;
	}
	
	
	protected Config buildRedissonConfig() {
		Properties p = properties;

		final int connPoolSize = Integer.parseInt(p.getProperty("redisson.connectionPoolSize"));
		
		if (MODE_SINGLE.equals( getMode() )) {
			Config config = new Config();
			
			config.useSingleServer()
					.setConnectionPoolSize(connPoolSize)
					.setAddress("redis://"+getAddress()[0]);	//转成标准URI
			
			return config;
		} else if (MODE_CLUSTER.equals( getMode() )) {
			Config config = new Config();
			
			String[] urls = getAddress();
			for(int r=0; r<urls.length; r++) {
				urls[r] = "redis://"+urls[r];	//转成标准URI
			}
			
			config.useClusterServers()
				.setMasterConnectionPoolSize(connPoolSize)
				.setSlaveConnectionPoolSize(connPoolSize)
				.addNodeAddress( urls );
			
			return config;
		}

		return null;
	}
	
	
}
