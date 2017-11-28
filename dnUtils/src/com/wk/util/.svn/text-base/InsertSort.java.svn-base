package com.wk.util;

import java.util.Comparator;
import java.util.List;

/**
 * 插入排序器
 * 
 * @author lixing
 **/
public class InsertSort<T> {
	/**
	 * 排序器 默认为null
	 * 
	 */
	private Comparator<? super T> comparator = null;

	public InsertSort() {
	}

	public InsertSort(Comparator<? super T> comparator) {
		this.comparator = comparator;
	}

	/**
	 * 向list中插入数据
	 * 
	 * @param ls
	 * @param target
	 */
	public void insert(List<T> ls, T target) {
		// split comparator and comparable paths
		Comparator<? super T> cpr = comparator;
		if (cpr != null) {
			boolean isInsert = false;
			for (int index = 0; index < ls.size(); index++) {
				if (cpr.compare(ls.get(index), target) > 0) {
					ls.add(index, target);
					isInsert = true;
					break;
				}
			}
			if (!isInsert) {
				ls.add(target);
			}
		} else {
			boolean isInsert = false;
			for (int index = 0; index < ls.size(); index++) {
				Comparable<T> co = (Comparable<T>) ls.get(index);
				if (co.compareTo(target) > 0) {
					ls.add(index, target);
					isInsert = true;
					break;
				}
			}
			if (!isInsert) {
				ls.add(target);
			}
		}
	}

	public Comparator<? super T> getComparator() {
		return comparator;
	}
}
