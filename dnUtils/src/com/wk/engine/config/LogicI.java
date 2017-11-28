package com.wk.engine.config;

import msg.RoomMessage.PlayType;

/**
 * 游戏逻辑层实现接口
 * 
 * @author ems
 *
 */
public abstract class LogicI {
	public static LogicI instance;

	public static LogicI getInstance() {
		return instance;
	}

	/**
	 * 获取玩法的描述
	 * 
	 * @param playType
	 * @return
	 */
	public abstract String getPlayTypeDesc(PlayType playType);

	/**
	 * 游戏记录最多多少个
	 * 
	 * @return
	 */
	public abstract int getGameRecordMax();
}
