package com.github.open.component.redis.client.redisson;

import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;

import com.github.open.component.redis.api.RedisDeque;

public class RedissonRedisDeque<E> extends RedissonBase implements RedisDeque<E>{
	
	private RBlockingDeque<E> blockingDeque;
	
	public RedissonRedisDeque(RedissonClient redissonClient, String name) {
		this.blockingDeque = redissonClient.getBlockingDeque(name, codec);
	}

	@Override
	public E takeLastAndOfferFirstTo(String destination) throws InterruptedException {
		return blockingDeque.takeLastAndOfferFirstTo(destination);
	}

	@Override
	public boolean offerFirst(E e) {
		return blockingDeque.offerFirst(e);
	}

	@Override
	public boolean removeLast(E e) {
		return blockingDeque.removeLastOccurrence(e);
	}

	@Override
	public E takeLast() throws InterruptedException {
		return blockingDeque.takeLast();
	}

}
