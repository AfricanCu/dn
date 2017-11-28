package com.wk.engine.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 一个lru的实现，最少访问移除
 * 
 * 为了避免死循环，，这里要确定cache足够大
 * 
 * @author Administrator
 *
 * @param <K>
 * @param <V>
 */
public class LRUMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = 1L;
	/** 最多容纳多少元素 **/
	private final int maxElements;
	/*** 如果LRU移除错误，，再次把他放入map中， 为了避免死循环，，这里要确定cache足够大 **/
	private java.util.Map.Entry<K, V> eldest;

	public LRUMap(int maxSize) {
		super(maxSize, 0.75F, true);
		this.maxElements = maxSize;
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > this.maxElements;
	}

	@Override
	public V put(K key, V value) {
		V put = super.put(key, value);
		if (eldest != null) {// 为了避免死循环，，这里要确定cache足够大
			java.util.Map.Entry<K, V> tmp = eldest;
			eldest = null;
			this.put(tmp.getKey(), tmp.getValue());
		}
		return put;
	}

	/**
	 * 谨慎使用！
	 * 
	 * 用于应对特殊的元素的移除
	 * 
	 * 如果LRU移除错误，，再次把他放入map中
	 * 
	 * !为了避免死循环，，这里要确定cache足够大!
	 * 
	 * @param eldest
	 */
	public void setEldest(java.util.Map.Entry<K, V> eldest) {
		this.eldest = eldest;
	}
}