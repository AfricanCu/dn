package com.wk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 数组乱序类
 * 
 * @author noam
 */
public class NRandom {

	/**
	 * 对给定数目的自0开始步长为1的数字序列进行乱序
	 * 
	 * @param no
	 *            给定数目
	 * 
	 * @return 乱序后的数组
	 */
	public static int[] genSequence(int no, boolean beginZero) {
		int[] sequence = new int[no];
		for (int i = 0; i < no; i++) {
			if (!beginZero) {
				sequence[i] = i + 1;
			} else {
				sequence[i] = i;
			}
		}
		for (int i = 0; i < no; i++) {
			int p = ThreadLocalRandom.current().nextInt(no);
			int tmp = sequence[i];
			sequence[i] = sequence[p];
			sequence[p] = tmp;
		}
		return sequence;
	}

	/**
	 * 获取随机数堆栈
	 * 
	 * @param no
	 * @param beginZero
	 *            是否从0开始，否则从1开始
	 * @return
	 */
	public static Stack<Integer> genStackSequence(int no, boolean beginZero) {
		int[] genSequence = genSequence(no, beginZero);
		Stack<Integer> stack = new Stack<>();
		for (int i : genSequence) {
			stack.push(i);
		}
		return stack;
	}

	/**
	 * 对一个列表随机排列
	 * 
	 * @param list
	 * @return ArrayList
	 */
	public static <T extends Object> List<T> genArrayListSequence(List<T> list) {
		int[] genSequence = genSequence(list.size(), true);
		ArrayList<T> stack = new ArrayList<>();
		for (int index : genSequence) {
			stack.add(list.get(index));
		}
		return stack;
	}

	/**
	 * 对一个列表随机排列
	 * 
	 * @param list
	 * @return 堆栈
	 */
	public static <T extends Object> Stack<T> genStackSequence(List<T> list) {
		int[] genSequence = genSequence(list.size(), true);
		Stack<T> stack = new Stack<>();
		for (int index : genSequence) {
			stack.push(list.get(index));
		}
		return stack;
	}

	public static void main(String[] args) {

	}
}