package com.wk.engine.event;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件枚举
 * 
 * @author Administrator
 */
public enum EventType {

	/**
	 * 关闭服务器
	 */
	ShutDown(-1, "关闭服务器"),
	// 用户1-100
	/**
	 * 事件参数 ：无
	 */
	User_LogIn(1, "用户登录"),
	/**
	 * 事件参数 ：无
	 */
	User_LogOut(2, "用户登出"),
	/**
	 * 事件参数 ：游戏模式{@link com.wk.logic.enm.GameMode}，身份
	 * {@link com.wk.server.role.RoleType}，输赢{@link com.wk.logic.enm.Bunko}
	 */
	Game_End(100, "游戏结束"), ;

	/**
	 * 
	 * @param type
	 * @param desc
	 */
	EventType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type = 0;// 类型ID
	private String desc = "";// 类型描述
	private static final Map<Integer, EventType> typeMap = new HashMap<Integer, EventType>();

	static {
		EventType missionMonitorEventArr[] = EventType.values();
		for (int i = 0; i < missionMonitorEventArr.length; i++) {
			EventType event = missionMonitorEventArr[i];
			typeMap.put(event.getType(), event);
		}
	}

	/**
	 *
	 * @param type
	 * @return
	 */
	public static EventType getEnumValue(int type) {
		return typeMap.get(type);
	}

	/**
	 *
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 *
	 * @return
	 */
	public int getType() {
		return type;
	}
}