package com.wk.logic.enm;

/**
 * 玩家解散类型
 * 
 * @author ems
 *
 */
public enum MemberDisType {
	/**
	 * 
	 */
	empty(0),
	/**
	 * 
	 */
	agree(1),
	/**
	 * 
	 */
	disagree(2),
	/**
	 * 
	 */
	waiting(3), ;
	private final int type;

	private MemberDisType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

// 自动生成开始
public static MemberDisType getEnum(int type){
switch(type) {
case 0:
  return empty;
case 1:
  return agree;
case 2:
  return disagree;
case 3:
  return waiting;
default:
  return null;
}
}// 自动生成结束
}