package com.github.open.component.redis.client.redisson;

import java.util.List;

import org.redisson.Redisson;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.RedisCommand;
import org.redisson.client.protocol.decoder.ObjectListReplayDecoder;
import org.redisson.command.CommandAsyncExecutor;
import org.redisson.connection.ConnectionManager;

public class RedissonPubsub extends RedissonBase{
	
	private final RedisCommand<List<Object>> PUBSUB = new RedisCommand<List<Object>>("PUBSUB", new ObjectListReplayDecoder<Object>());
	
	private CommandAsyncExecutor commandExecutor;
	
	public RedissonPubsub(RedissonClient redissonClient) {
		ConnectionManager connectionManager = null;
		if(redissonClient instanceof Redisson) {
			Redisson redisson = (Redisson)redissonClient;
			connectionManager = redisson.getConnectionManager();
		}
		
		if(connectionManager == null) {
			throw new IllegalArgumentException("ConnectionManager is null");
		}
		
		this.commandExecutor = connectionManager.getCommandExecutor();
	}
	
	public List<String> channels(String pattern){
		RFuture<List<String>> future = commandExecutor.readAsync(PUBSUB.getName(), codec, PUBSUB, "CHANNELS", pattern);
		
		List<String> channels = commandExecutor.get(future);
		return channels;
	}
	
}
