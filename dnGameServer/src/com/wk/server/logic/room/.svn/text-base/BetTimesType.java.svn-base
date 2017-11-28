package com.wk.server.logic.room;

/**
 * 押注倍数
 * 
 * @author ems
 *
 */
public enum BetTimesType {
	oneTimes(1, "1倍"), twoTimes(2, "2倍"), threeTimes(3, "3倍"), ;
	private final int type;
	private final String name;

	private BetTimesType(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}
// 自动生成开始
public static BetTimesType getEnum(int type){
switch(type) {
case 1:
  return oneTimes;
case 2:
  return twoTimes;
case 3:
  return threeTimes;
default:
  return null;
}
}// 自动生成结束
}