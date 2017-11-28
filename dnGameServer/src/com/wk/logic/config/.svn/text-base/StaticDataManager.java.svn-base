package com.wk.logic.config;

import test.client.util.NoticeTextTemplate;

import com.wk.util.ReadUtil;

/**
 * 静态数据管理器
 * 
 */
public class StaticDataManager {
	/** 竖线 **/
	public static final String SEP_COMMON = "\\|";
	/** 逗号 **/
	public static final String SEP_DOT = ",";
	/** 分号 **/
	public static final String SEP_SEMICOLON = ";";
	private static final String dir = "./resource/csv/";
	public static final String SEP_SHARP = "#";

	/**
	 * 解析静态数据
	 * 
	 * @throws Exception
	 */
	public static void loadData() throws Exception {
		TimeConfig.init();
		ConfigTemplate.explain(dir + "Config.csv");
		NoticeTextTemplate.explain(dir + "NoticeText.csv");
		ReadUtil.check();
	}
}
