package com.wk.util;

import java.util.LinkedList;
import java.util.Random;

/**
 * 随机添加链表
 *
 * @param <T>
 */
public class RandomAddLinkedList<T> extends LinkedList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Random ran = new Random();

	public void ranAdd(T e) {
		if (ran.nextBoolean()) {
			this.addFirst(e);
		} else {
			this.addLast(e);
		}
	}
}