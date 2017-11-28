package com.wk.engine.event;

import com.wk.server.ibatis.select.User;

/**
 * 事件抽象
 * 
 */
public abstract class EventAbs {

	private final EventType type;// 事件类型

	public EventAbs(EventType type) {
		super();
		this.type = type;
	}

	public EventType getType() {
		return type;
	}

	/**
	 * 事件通知
	 * 
	 * @param username
	 * @param params
	 * @param 抛出异常
	 */
	public abstract void eventNotify(User user, Object... params)
			throws Exception;
}
