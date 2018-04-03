package com.github.open.component.redis.api;

/**
 * redis list 双端队列
 * @author yangwk
 *
 */
public interface RedisDeque<E> {
	
	/**
	 * 阻塞式地获取并移除此队列的最后一个元素，并同时添加到另一个队列的头。如果此队列没有任何元素，会一直阻塞
	 * @author yangwk
	 * @param destination 另一个队列名称
	 */
	E takeLastAndOfferFirstTo(String destination) throws InterruptedException;
	
	/**
	 * 添加元素到此队列的头
	 * @author yangwk
	 * @return 是否添加成功
	 */
	boolean offerFirst(E e);
	
	/**
	 * 移除此队列的最后一个元素
	 * @author yangwk
	 * @return 是否删除成功
	 */
	boolean removeLast(E e);
	
	/**
	 * 阻塞式地获取并移除此队列的最后一个元素。如果此队列没有任何元素，会一直阻塞
	 * @author yangwk
	 */
	E takeLast() throws InterruptedException;
}
