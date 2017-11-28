package com.wk.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * id生成器
 * 
 * @author lixing 2015年4月9日
 */
public class IntegerIdGenerator {

	private AtomicInteger idCounter;

	private IntegerIdGenerator(AtomicInteger atomicInteger) {
		super();
		this.idCounter = atomicInteger;
	}

	public static IntegerIdGenerator createIdGenerator(int initialValue) {
		return new IntegerIdGenerator(new AtomicInteger(initialValue));
	}

	public int generate() {
		return this.idCounter.addAndGet(1);
	}

	// 当前最大id
	public int getNowId() {
		return idCounter.get();
	}
}
