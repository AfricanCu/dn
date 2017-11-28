package com.wk.play.enun;

/**
 * 番数限制
 * 
 * @author ems
 *
 */
public enum TimesLimitType {
	/**  **/
	_24Times(1, "24倍", 24),
	/**  **/
	_noLimitTimes(2, "无上限", Integer.MAX_VALUE),
	/**  **/
	_16Times(3, "16倍", 16),
	/**  **/
	_32Times(4, "32倍", 32),
	/**  **/
	_64Times(5, "64倍", 64), ;

	private final int type;
	private final String name;
	private final int timesLimit;

	private TimesLimitType(int type, String name, int timesLimit) {
		this.type = type;
		this.name = name;
		this.timesLimit = timesLimit;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getTimesLimit() {
		return timesLimit;
	}

	// 自动生成开始
public static TimesLimitType getEnum(int type){
switch(type) {
case 1:
  return _24Times;
case 2:
  return _noLimitTimes;
case 3:
  return _16Times;
case 4:
  return _32Times;
case 5:
  return _64Times;
default:
  return null;
}
}// 自动生成结束
}