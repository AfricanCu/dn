package com.wk.logic.enm;

/**
 * 聊天类型
 * 
 * @author ems
 *
 */
public enum ChatType {
	common(0), system(1);
	private final int type;

	private ChatType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

// 自动生成开始
public static ChatType getEnum(int type){
switch(type) {
case 0:
  return common;
case 1:
  return system;
default:
  return null;
}
}// 自动生成结束
}