package com.wk.engine.config;

import java.util.Arrays;
import java.util.List;

import msg.RoomMessage.DissolveRoomCast;

import com.wk.bean.SystemConstantsAbs;

/**
 * 系统静态变量
 * 
 * @author Administrator
 *
 */
public class SystemConstants extends SystemConstantsAbs {

	/** 登录超时时间 */
	public static final long Login_Over_Time = 60 * 1000;
	/** 1M */
	public static final long One_M = 1024 * 1024;
	/** 任务纳秒 检测 */
	public static final long taskNanosMax = 500000000l;
	/** 解散房间广播 */
	public static final byte[] dissolveRoomCast = DissolveRoomCast.newBuilder()
			.build().toByteArray();
	/** 对多同时处理的消息个数 */
	public static final int maxMessageNum = 100;
	public static final long oneSecondInMillis = 1000;
	/** 系统玩家id */
	public static final long sysUserUid = -1;
	/** 这些消息名没有setCode方法 */
	public static final List<String> exceptNameList = Arrays
			.asList("CompTimeSm");
}
