package com.jery.ngsp.server.log;

import java.net.URL;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.LoggerFactory;

public class LoggerService {
	private static org.slf4j.Logger platformLog = null;
	private static org.slf4j.Logger logicLog = null;
	private static org.slf4j.Logger chargeLog = null;
	private static org.slf4j.Logger roomlogs = null;
	private static org.slf4j.Logger itemlogs = null;
	private static org.slf4j.Logger guildlogs = null;

	private static void init() {
		platformLog = LoggerFactory.getLogger("platformlogs");// LogManager.getLogger("platformlogs");
		logicLog = LoggerFactory.getLogger("logiclogs");// LogManager.getLogger("logiclogs");
		chargeLog = LoggerFactory.getLogger("chargelogs");
		roomlogs = LoggerFactory.getLogger("roomlogs");
		itemlogs = LoggerFactory.getLogger("itemlogs");
		guildlogs = LoggerFactory.getLogger("guildlogs");
	}

	public static org.slf4j.Logger getGuildlogs() {
		return guildlogs;
	}

	public static org.slf4j.Logger getItemlogs() {
		return itemlogs;
	}

	public static org.slf4j.Logger getRoomlogs() {
		return roomlogs;
	}

	public static org.slf4j.Logger getPlatformLog() {
		return platformLog;
	}

	public static org.slf4j.Logger getLogicLog() {
		return logicLog;
	}

	public static org.slf4j.Logger getChargeLog() {
		return chargeLog;
	}

	public static void openLog(Logger log) {
		log.setAdditivity(true);
	}

	public static void closeLog(Logger log) {
		log.setAdditivity(false);
	}

	public static void setDebugStatus(Logger log) {
		log.setLevel(Level.DEBUG);
	}

	public static void setOperateStatus(Logger log) {
		log.setLevel(Level.ERROR);
	}

	public static void init(String logpath) {
		PropertyConfigurator.configure(logpath);
		init();
	}

	public static void init(URL logpath) {
		PropertyConfigurator.configure(logpath);
		init();
	}

	/**
	 * 默认初始化文件 ./gameServerLog4j.properties
	 */
	public static void initDef() {
		URL resource = LoggerService.class.getClassLoader().getResource(
				"gameServerLog4j.properties");
		System.err.println(String.format("%s - %s",
				new Exception().getStackTrace()[0], resource));
		LoggerService.init(resource);
		LoggerService.getLogicLog().error("{}", resource);
	}

}