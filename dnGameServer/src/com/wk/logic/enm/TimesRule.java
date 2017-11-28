package com.wk.logic.enm;

import com.jery.ngsp.server.log.LoggerService;

/**
 * 加倍规则
 * 
 * @author Administrator
 *
 */
public enum TimesRule {

	/**
	 * 七八九2倍/牛牛3倍/金牛银牛4倍
	 */
	LOW(1, 4) {
		public int getTimes(int niu) {
			if (niu < 7) {
				return 1;
			}
			switch (niu) {
			case 7:
				return 2;
			case 8:
				return 2;
			case 9:
				return 2;
			case 10:
				return 3;
			case 11:
				return 4;
			case 12:
				return 4;
			default:
				LoggerService.getLogicLog().error("严重错误！牛{}", niu);
				return 1;
			}
		}
	},

	/**
	 * 七八2倍/牛九3倍/牛牛4倍/金牛银牛5倍
	 */
	MEDIUM(2, 5) {
		public int getTimes(int niu) {
			if (niu < 7) {
				return 1;
			}
			switch (niu) {
			case 7:
				return 2;
			case 8:
				return 2;
			case 9:
				return 3;
			case 10:
				return 4;
			case 11:
				return 5;
			case 12:
				return 5;
			default:
				LoggerService.getLogicLog().error("严重错误！牛{}", niu);
				return 1;
			}
		}
	},
	/**
	 * 见翻
	 */
	HIGH(3, 12) {

		@Override
		public int getTimes(int niu) {
			if (niu == 0) {
				return 1;
			}
			return niu;
		}
	}

	;
	private final int type;
	/** 最高倍数 */
	private final int maxTimes;

	private TimesRule(int type, int maxTimes) {
		this.type = type;
		this.maxTimes = maxTimes;
	}

	public abstract int getTimes(int niu);

	public int getType() {
		return type;
	}

	// 自动生成开始
public static TimesRule getEnum(int type){
switch(type) {
case 1:
  return LOW;
case 2:
  return MEDIUM;
case 3:
  return HIGH;
default:
  return null;
}
}// 自动生成结束

	public int getMaxTimes() {
		return maxTimes;
	}
}