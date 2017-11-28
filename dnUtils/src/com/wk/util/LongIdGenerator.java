package com.wk.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * id生成器
 * 
 * @author lixing 2015年4月9日
 */
public class LongIdGenerator {

	private AtomicLong idCounter;

	private LongIdGenerator(AtomicLong atomicInteger) {
		super();
		this.idCounter = atomicInteger;
	}

	public static LongIdGenerator createIdGenerator(long initialValue) {
		return new LongIdGenerator(new AtomicLong(initialValue));
	}

	public long generate() {
		return this.idCounter.addAndGet(1);
	}

	// 当前最大id
	public long getNowId() {
		return idCounter.get();
	}
}
