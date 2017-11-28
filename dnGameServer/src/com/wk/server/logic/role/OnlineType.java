package com.wk.server.logic.role;

/**
 * 在线时长类型
 * 
 * @author ems
 *
 */
public enum OnlineType {

	/**
	 * 正常
	 */
	Normal(0),
	/**
	 * 超过3个小时
	 */
	ThreeHour(3 * 60 * 60),

	/**
	 * 超过5个小时
	 */
	FiveHour(5 * 60 * 60);

	private int seconds;

	private OnlineType(int seconds) {
		this.seconds = seconds;
	}

	public static OnlineType checkType(int seconds) {
		OnlineType type = Normal;
		for (OnlineType iterable_element : OnlineType.values()) {
			if (seconds < iterable_element.seconds) {
				break;
			} else {
				type = iterable_element;
			}
		}
		return type;
	}
}
