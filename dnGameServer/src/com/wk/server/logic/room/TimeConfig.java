package com.wk.server.logic.room;

import java.util.Properties;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.util.ReadUtil;

public class TimeConfig {
	private static final String _config = "./resource/config/timeConfig.properties";
	/** 解散时间 */
	private static long MemberDissolveRoomTimeInMillis;
	private static int seeOverTimeInSeconds;
	/** 游戏记录最大数目 */
	private static int GameRecordMax;
	private static int ProxyRecordMax;
	private static long NobodyDissolveTimeInSecond;
	private static long JulebuHaveOneNotStartDissolveTimeInSecond;
	private static long CommonHaveOneNotStartDissolveTimeInSecond;

	public static void init() {
		Properties properties = ReadUtil.loadFromFile(_config);
		MemberDissolveRoomTimeInMillis = Long.parseLong(properties
				.getProperty("MemberDissolveRoomTimeInSecond")) * 1000l;
		seeOverTimeInSeconds = Integer.parseInt(properties.getProperty(
				"seeOverTimeInSeconds", "15"));
		GameRecordMax = Integer.parseInt(properties.getProperty(
				"GameRecordMax", "20"));
		ProxyRecordMax = Integer.parseInt(properties.getProperty(
				"ProxyRecordMax", "50"));
		NobodyDissolveTimeInSecond = Integer.parseInt(properties.getProperty(
				"NobodyDissolveTimeInSecond", "30"));
		JulebuHaveOneNotStartDissolveTimeInSecond = Integer
				.parseInt(properties.getProperty(
						"JulebuHaveOneNotStartDissolveTimeInSecond", "30"));
		CommonHaveOneNotStartDissolveTimeInSecond = Integer
				.parseInt(properties.getProperty(
						"CommonHaveOneNotStartDissolveTimeInSecond", "30"));
		LoggerService.getPlatformLog().error("加载配置成功！{}", _config);
	}

	public static String getConfig() {
		return _config;
	}

	public static long getMemberDissolveRoomTimeInMillis() {
		return MemberDissolveRoomTimeInMillis;
	}

	public static int getSeeovertimeinseconds() {
		return seeOverTimeInSeconds;
	}

	public static int getGamerecordmax() {
		return GameRecordMax;
	}

	public static int getProxyrecordmax() {
		return ProxyRecordMax;
	}

	public static long getNobodyDissolveTimeInSecond() {
		return NobodyDissolveTimeInSecond;
	}

	public static long getJulebuHaveOneNotStartDissolveTimeInSecond() {
		return JulebuHaveOneNotStartDissolveTimeInSecond;
	}

	public static long getCommonHaveOneNotStartDissolveTimeInSecond() {
		return CommonHaveOneNotStartDissolveTimeInSecond;
	}

}