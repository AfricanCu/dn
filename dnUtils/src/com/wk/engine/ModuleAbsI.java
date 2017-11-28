package com.wk.engine;

import com.wk.engine.util.KeyValueDbCache;

/**
 * 模块抽象类
 * 
 * @author Administrator
 *
 */
public abstract class ModuleAbsI<K, V> extends KeyValueDbCache<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String name;// 模块名
	protected short downerMsgId;// 消息id下限（包含）
	protected short upperMsgId;// 消息id上限（包含）
	protected int backDbTimeInSecond = -1;// 回写数据时间（秒）-1表示无数据回写
	protected String configPath;// 配置文件路径

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	/**
	 * 是否回写数据
	 * 
	 * @return
	 */
	public boolean isBackDb() {
		return this.backDbTimeInSecond != -1 && this.backDbTimeInSecond > 0;
	}

	public int getBackDbTimeInSecond() {
		return backDbTimeInSecond;
	}

	public void setBackDbTimeInSecond(int backDbTimeInSecond) {
		this.backDbTimeInSecond = backDbTimeInSecond;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getUpperMsgId() {
		return upperMsgId;
	}

	public void setUpperMsgId(short upperMsgId) {
		this.upperMsgId = upperMsgId;
	}

	public short getDownerMsgId() {
		return downerMsgId;
	}

	public void setDownerMsgId(short downerMsgId) {
		this.downerMsgId = downerMsgId;
	}

	/**
	 * 初始化模块
	 */
	public abstract void init() throws Exception;

	/**
	 * 回写数据
	 * 
	 * @throws Exception
	 */
	public abstract void backDb() throws Exception;
}
