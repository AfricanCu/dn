package com.wk.logic.enm;

/**
 * 切牌方式
 * 
 * @author Administrator
 *
 */
public enum CutPukeType {
	/**
	 * 左手边切牌
	 */
	left(1),
	/**
	 * 轮流切牌
	 */
	order(2),
	/**
	 * 
	 */
	;
	private final int type;

	/**
	 * 
	 * @param type
	 */
	private CutPukeType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	// 自动生成开始
public static CutPukeType getEnum(int type){
switch(type) {
case 1:
  return left;
case 2:
  return order;
default:
  return null;
}
}// 自动生成结束
}