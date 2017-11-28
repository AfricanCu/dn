package com.wk.engine.event;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.server.ibatis.select.User;

/**
 * 事件管理
 * 
 * @author Administrator
 */
public class EventManager {

	private static final EventManager instance = new EventManager();

	public EventManager() {
		for (EventType type : EventType.values()) {
			this.eventMap.put(type, new ArrayList<EventAbs>());
		}
	}

	/**
	 *
	 * @return
	 */
	public static EventManager getInstance() {
		return instance;
	}

	private final EnumMap<EventType, List<EventAbs>> eventMap = new EnumMap<EventType, List<EventAbs>>(
			EventType.class);

	/**
	 * 注册事件集合
	 * 
	 * @param monitor
	 */
	public void registerGameEventMonitor(List<EventAbs> list) {
		if (list != null && !list.isEmpty())
			for (EventAbs event : list) {
				List<EventAbs> list2 = this.eventMap.get(event.getType());
				if (!list2.contains(event)) {
					list2.add(event);
				} else {
					LoggerService.getLogicLog().error("重复注册！{}",
							event.getType());
				}
			}
	}

	/**
	 * 处理事件
	 * 
	 * 按注册先后顺序执行
	 * 
	 * @param type
	 * @param user
	 * @param params
	 */
	public void processEvent(EventType type, User user, Object... params) {
		List<EventAbs> list = eventMap.get(type);
		for (EventAbs event : list) {
			try {
				event.eventNotify(user, params);
			} catch (Exception e) {
				LoggerService.getLogicLog().error(e.getMessage(), e);
			}
		}
	}
}
