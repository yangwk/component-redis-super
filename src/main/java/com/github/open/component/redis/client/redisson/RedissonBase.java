package com.github.open.component.redis.client.redisson;

import java.nio.charset.Charset;

import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;

public abstract class RedissonBase {

	protected final Codec codec = new StringCodec(Charset.forName("UTF-8"));
	
}
