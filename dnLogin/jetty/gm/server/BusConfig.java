package gm.server;

import gm.server.logic.config.StaticDataManager;

import java.util.Properties;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.db.IbatisDbServer;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.util.ReadUtil;

public class BusConfig extends ServerConfigAbs {

	public static final String server_config_dir = "./resource/engine/server.properties";
	private static final Properties properties = ReadUtil
			.loadFromFile(server_config_dir);
	static {
		setUp(properties);
	}

	private static final String ibatisConfig = properties
			.getProperty("ibatisConfig");
	public static final String jettyXml = properties.getProperty("jettyXml");
	public static final String csvDir = properties.getProperty("csvDir");
	/** 应用唯一标识，在微信开放平台提交应用审核通过后获得 */
	public static final String AppID = properties.getProperty("AppID", null);
	/** 应用密钥AppSecret，在微信开放平台提交应用审核通过后获得 */
	public static final String AppSecret = properties.getProperty("AppSecret",
			null);
	/** 登录Servlet缓存玩家数据LRU最大容量 */
	public static final int userLruMaxSize = Integer.parseInt(properties
			.getProperty("userLruMaxSize", "1000"));
	/** Servlet异步等待时间（重登踢人） */
	public static final long asyncWaitTimeoutInMs = Integer.parseInt(properties
			.getProperty("asyncWaitTimeoutInMs", "5000"));
	private static final boolean iosSandboxChargeNotify = Boolean
			.parseBoolean(properties.getProperty("iosSandboxChargeNotify",
					"false"));
	private static final int initDiamond = Integer.parseInt(properties
			.getProperty("initDiamond", "5"));

	public static void initLog4J() {
		LoggerService.init(properties.getProperty("log4jPath"));
	}

	public static void init() throws Exception {
//		csvDir=./resource/csv/
		StaticDataManager.init(csvDir);
//		ibatisConfig=./resource/db/SqlMapConfig.xml
		IbatisDbServer.initDB(ibatisConfig);
		ReadUtil.check();
	}

	public static boolean isIosSandboxChargeNotify() {
		return iosSandboxChargeNotify;
	}

	public static int getInitDiamond() {
		return initDiamond;
	}
}
