package com.wk.enun;

public enum ServerStatus {
	/**
	 * 流畅
	 */
	smooth(0, "流畅"),
	/**
	 * 繁忙
	 */
	busy(1, "繁忙"),
	/**
	 * 爆满
	 */
	full(2, "爆满"),
	/**
	 * 爆满
	 */
	very_full(3, "爆满"), ;

	private int type;
	private String name;

	private ServerStatus(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	/**
	 * 服务器是否爆满
	 * 
	 * @return
	 */
	public boolean isFull() {
		return this == very_full || this == full;
	}

	// 自动生成开始
public static ServerStatus getEnum(int type){
switch(type) {
case 0:
  return smooth;
case 1:
  return busy;
case 2:
  return full;
case 3:
  return very_full;
default:
  return null;
}
}// 自动生成结束
}