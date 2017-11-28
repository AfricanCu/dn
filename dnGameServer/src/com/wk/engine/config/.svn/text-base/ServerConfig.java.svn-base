/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wk.engine.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import msg.RoomMessage.PlayType;

import org.eclipse.jetty.xml.XmlConfiguration;
import org.xml.sax.SAXException;

import com.jery.ngsp.server.InterfaceFactoryManager;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.db.IbatisDbServer;
import com.wk.logic.config.StaticDataManager;
import com.wk.logic.config.TimeConfig;
import com.wk.logic.enm.BankerMode;
import com.wk.logic.enm.PType;
import com.wk.logic.enm.RoundType;
import com.wk.util.IbatisUtil;
import com.wk.util.ReadUtil;

/**
 * 服务器配置
 *
 *
 * @author lixing
 */
public class ServerConfig extends ServerConfigAbs {
	private static final String _config = "./resource/engine/server.properties";
	private static final Properties properties = ReadUtil.loadFromFile(_config);
	static {
		setUp(properties);
	}
	public static final boolean ios = Boolean.parseBoolean(properties
			.getProperty("ios"));
	public static final int serverId = Integer.parseInt(properties
			.getProperty("serverId"));
	public static final String serverIp = properties.getProperty("serverIp");
	public static final int serverPort = Integer.parseInt(properties
			.getProperty("serverPort"));
	public static final String outLoginserverAddress = properties
			.getProperty("outLoginserverAddress");
	public static final String loginserverAddress = properties
			.getProperty("loginserverAddress");
	public static final String loginserverPort = properties
			.getProperty("loginserverPort");
	public static final int onlineMaxNum = Integer.parseInt(properties
			.getProperty("onlineMaxNum"));
	private static final String ibatisConfig = properties
			.getProperty("ibatisConfig");
	private static final String moduleConfig = properties
			.getProperty("moduleConfig");
	public static final String ADDR = properties.getProperty("ADDR");
	/** gs登陆bus code */
	public static final String gsLoginBusCode = "12345";
	public static final int bossGroupNThreads = Integer.parseInt(properties
			.getProperty("bossGroupNThreads", ""
					+ Runtime.getRuntime().availableProcessors() * 2));
	public static final int workerGroupNThreads = Integer.parseInt(properties
			.getProperty("workerGroupNThreads", ""
					+ Runtime.getRuntime().availableProcessors() * 2));
	public static final int gameCheckLRUMapSize = 1000;
	/** 能暂停心跳多少毫秒 **/
	public static final long heartPauseTimeInMillis = TimeUnit.SECONDS
			.toMillis(Integer.parseInt(properties.getProperty(
					"heartPauseTimeInSecond", "600")));
	/** 当断线时是否踢人 */
	public static final boolean isNoMasterKickWhenOffLine = Boolean
			.parseBoolean(properties.getProperty("isNoMasterKickWhenOffLine",
					"false"));
	public static final short maxChatContentLength = Short
			.parseShort(properties.getProperty("maxChatContentLength", "256"));
	public static final short MAX_PACKAGE_LENGTH = Short.parseShort(properties
			.getProperty("MAX_PACKAGE_LENGTH", "512"));
	private static final LogicI logicI = new LogicI() {
		@Override
		public String getPlayTypeDesc(PlayType playType) {
			return String.format("%s %s %s",
					RoundType.getEnum(playType.getRound()).getName(),

					PType.getEnum(playType.getPType()).getName(),

					BankerMode.getEnum(playType.getBankerMode()).getName());
		}

		@Override
		public int getGameRecordMax() {
			return TimeConfig.getGameRecordMax();
		}
	};

	public static void initLog4J() {
		LoggerService.init(properties.getProperty("log4jPath"));
	}

	/**
	 * 初始化配置
	 * 
	 * @throws Exception
	 * @throws IOException
	 * @throws SAXException
	 * @throws FileNotFoundException
	 *
	 */
	public static void init() throws Exception {
		initLog4J();
		LoggerService.getPlatformLog().error("是否关闭消耗钻石:{}",
				isCloseDiamondConsume());
		IbatisUtil.invoke(LogicI.class, "instance", logicI);
		StaticDataManager.loadData();
		InterfaceFactoryManager.getInterfaceFactory().getDirtyWordsManager()
				.loadData(null);
		IbatisDbServer.initDB(ibatisConfig);
		new XmlConfiguration(new FileInputStream(moduleConfig)).configure();
		LoggerService.getLogicLog().warn("-----------加载游戏服务器配置-------------");
	}

	public static boolean isOff() {
		return true;
	}

	/**
	 * 头像 url 地址
	 * 
	 * @param uid
	 * @param headImgTag
	 * @return
	 */
	public static String getHeadUrl(long uid) {
		return String.format("http://%s:%s/Login/%s/%s",
				ServerConfig.outLoginserverAddress,
				ServerConfig.loginserverPort,
				ServerConfig.getWechatheaderdir(uid), uid);
	}
}
