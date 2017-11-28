package com.wk.engine.config;

import java.net.URL;
import java.util.Properties;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.util.ReadUtil;

/**
 * 服务器配置抽象
 * 
 * @author ems
 *
 */
public class ServerConfigAbs {
	private static final Properties systemProperties;
	/** 房间号模板 */
	private static final String roomIdTemplate;
	private static final int roomIdTopLimitation;
	/** uid 开始计数 */
	private static final long startUid;
	private static final int startProxyUid;
	/** 是否默认微信昵称 **/
	private static final boolean isNicknameDefault;
	/** 微信头像目录位置 */
	private static final String wechatHeaderDir;
	/*** 按照玩家id获取房间id ***/
	private static final boolean getRoomIdAccordUid;

	/** 是否调试 */
	protected static boolean debug;
	/** deferred异步处理超时时间 */
	protected static int handleOverTimeInSecond;
	/** 心跳时间 */
	protected static int heartBeatTimeInSecond;
	/** 是否心跳 */
	protected static boolean heartBeat;
	/** 是否监测消息发送 */
	protected static boolean monitorMessage;
	/** 是否监测打印 */
	protected static boolean monitorPrint;
	/** 是否监测麻将打印 */
	protected static boolean monitorMj;
	/** 是否关闭扣钻 */
	protected static boolean closeDiamondConsume;

	static {
		URL configURL = LoggerService.class.getClassLoader().getResource(
				"system.properties");
		systemProperties = ReadUtil.loadFromURL(configURL);
		roomIdTemplate = systemProperties.getProperty("roomIdTemplate",
				"000000");
		roomIdTopLimitation = Integer.parseInt("1" + roomIdTemplate);
		startUid = Long.parseLong(systemProperties.getProperty("startUid"));
		startProxyUid = Integer.parseInt(systemProperties
				.getProperty("startProxyUid"));
		isNicknameDefault = Boolean.parseBoolean(systemProperties
				.getProperty("isNicknameDefault"));
		wechatHeaderDir = systemProperties.getProperty("wechatHeaderDir",
				"wechatHeaders");
		getRoomIdAccordUid = Boolean.parseBoolean(systemProperties
				.getProperty("getRoomIdAccordUid"));
	}

	public static void setUp(Properties properties) {
		debug = Boolean.parseBoolean(properties.getProperty("debug"));
		handleOverTimeInSecond = Integer.parseInt(properties.getProperty(
				"handleOverTimeInSecond", "10"));
		heartBeatTimeInSecond = Integer.parseInt(properties.getProperty(
				"heartBeatTimeInSecond", "30"));
		heartBeat = Boolean.parseBoolean(properties.getProperty("heartBeat",
				"false"));
		monitorMessage = Boolean.parseBoolean(properties.getProperty(
				"monitorMessage", "false"));
		monitorPrint = Boolean.parseBoolean(properties.getProperty(
				"monitorPrint", "true"));
		monitorMj = Boolean.parseBoolean(properties.getProperty("monitorMj",
				"true"));
		closeDiamondConsume = Boolean.parseBoolean(properties.getProperty(
				"closeDiamondConsume", "false"));
	}

	public static String getRoomIdTemplate() {
		return roomIdTemplate;
	}

	/**
	 * 获取房间号的字符串表示
	 * 
	 * @param roomId
	 * @return
	 */
	public static String getRoomStr(int roomId) {
		String valueOf = String.valueOf(roomId);
		return roomIdTemplate.substring(0,
				roomIdTemplate.length() - valueOf.length())
				+ valueOf;
	}

	public static long getStartuid() {
		return startUid;
	}

	public static int getStartproxyuid() {
		return startProxyUid;
	}

	/**
	 * 是否默认启用微信昵称
	 * 
	 * @return
	 */
	public static boolean isNicknameDefault() {
		return isNicknameDefault;
	}

	/**
	 * 微信头像目录地址
	 * 
	 * @param uid
	 * @return
	 */
	public static String getWechatheaderdir(long uid) {
		return wechatHeaderDir + (uid % 1000);
	}

	public static int getRoomidtoplimitation() {
		return roomIdTopLimitation;
	}

	public static boolean isGetroomidaccorduid() {
		return getRoomIdAccordUid;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static int getHandleOverTimeInsecond() {
		return handleOverTimeInSecond;
	}

	public static int getHeartBeatTimeInSecond() {
		return heartBeatTimeInSecond;
	}

	public static boolean isHeartBeat() {
		return heartBeat;
	}

	public static boolean isMonitorMessage() {
		return monitorMessage;
	}

	public static boolean isMonitorPrint() {
		return monitorPrint;
	}

	public static String getSystemProperty(String key) {
		return systemProperties.getProperty(key);
	}

	public static boolean isMonitorMj() {
		return monitorMj;
	}

	public static boolean isCloseDiamondConsume() {
		return closeDiamondConsume;
	}
}
