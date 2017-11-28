package com.wk.enun;

/**
 * 玩家状态
 * 
 * @author lixing
 *
 */
public enum UserState {
	/**
	 * 在线
	 */
	online(1, "在线"),
	/**
	 * 离线
	 */
	offline(2, "离线"), ;

	private int type;
	private String name;

	private UserState(int type, String name) {
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
public static UserState getEnum(int type){
switch(type) {
case 1:
  return online;
case 2:
  return offline;
default:
  return null;
}
}// 自动生成结束
}