package com.wk.logic.config;

import java.util.Properties;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.util.ReadUtil;

public class TimeConfig {
	private static final String _config = "./resource/config/timeConfig.properties";
	/** 解散时间 */
	private static long MemberDissolveRoomTimeInMillis;
	/** 游戏记录最大数目 */
	private static int GameRecordMax;
	private static int ProxyRecordMax;
	private static long PauseGameDissolveTimeInSecond;
	private static boolean ShutDownDissolveRoom;
	private static int bankerZhuaPaiWaitInSeconds;
	private static int onePageNum;
	/** 公会申请成员最大值 **/
	private static int guildApplyMemberMax;
	private static long updateProxyRoomTimeInSecond;

	public static void init() {
		Properties properties = ReadUtil.loadFromFile(_config);
		MemberDissolveRoomTimeInMillis = Long.parseLong(properties
				.getProperty("MemberDissolveRoomTimeInSecond")) * 1000l;
		GameRecordMax = Integer.parseInt(properties.getProperty(
				"GameRecordMax", "20"));
		ProxyRecordMax = Integer.parseInt(properties.getProperty(
				"ProxyRecordMax", "50"));
		PauseGameDissolveTimeInSecond = Integer.parseInt(properties
				.getProperty("PauseGameDissolveTimeInSecond", "50"));
		ShutDownDissolveRoom = Boolean.parseBoolean(properties.getProperty(
				"ShutDownDissolveRoom", "false"));
		bankerZhuaPaiWaitInSeconds = Integer.parseInt(properties.getProperty(
				"bankerZhuaPaiWaitInSeconds", "5"));
		onePageNum = Integer.parseInt(properties
				.getProperty("onePageNum", "20"));
		guildApplyMemberMax = Integer.parseInt(properties.getProperty(
				"guildApplyMemberMax", "20"));
		updateProxyRoomTimeInSecond = Integer.parseInt(properties.getProperty(
				"updateProxyRoomTimeInSecond", "5"));
		LoggerService.getPlatformLog().error("加载配置成功！{}", _config);
	}

	public static String getConfig() {
		return _config;
	}

	public static long getMemberDissolveRoomTimeInMillis() {
		return MemberDissolveRoomTimeInMillis;
	}

	public static int getGameRecordMax() {
		return GameRecordMax;
	}

	public static int getProxyRecordMax() {
		return ProxyRecordMax;
	}

	public static long getPauseGameDissolveTimeInSecond() {
		return PauseGameDissolveTimeInSecond;
	}

	public static long getBankerZhuaPaiWaitInSeconds() {
		return bankerZhuaPaiWaitInSeconds;
	}

	public static boolean isShutDownDissolveRoom() {
		return ShutDownDissolveRoom;
	}

	public static int getOnePageNum() {
		return onePageNum;
	}

	public static int getGuildApplyMemberMax() {
		return guildApplyMemberMax;
	}

	public static long getUpdateProxyRoomTimeInSecond() {
		return updateProxyRoomTimeInSecond;
	}
}