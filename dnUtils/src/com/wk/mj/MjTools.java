package com.wk.mj;

import java.net.URL;
import java.util.Properties;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.util.ReadUtil;

public class MjTools {

	private static int guildCreateRoomMax;
	private static boolean testHuPai;

	static {
		URL configURL = LoggerService.class.getClassLoader().getResource(
				"mj.properties");
		Properties systemProperties = ReadUtil.loadFromURL(configURL);
		guildCreateRoomMax = Integer.parseInt(systemProperties
				.getProperty("guildCreateRoomMax"));
		testHuPai = Boolean.parseBoolean(systemProperties
				.getProperty("testHuPai"));
	}

	public static int getGuildCreateRoomMax() {
		return guildCreateRoomMax;
	}

	public static boolean isTestHuPai() {
		return testHuPai;
	}

}
