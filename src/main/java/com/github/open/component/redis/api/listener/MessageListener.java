package com.github.open.component.redis.api.listener;

public interface MessageListener {

	void onMessage(String channel, Object message);

}
