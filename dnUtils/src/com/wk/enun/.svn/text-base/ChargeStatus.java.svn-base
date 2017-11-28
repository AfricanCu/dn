package com.wk.enun;

public enum ChargeStatus {
	/**
	 * 
	 */
	unHandle(0, "未处理"),
	/**
	 * 处理完成
	 */
	handled(1, "处理完成"),
	/**
	 * 一算返佣
	 */
	rebate(2,"已算返佣");
	private final int type;
	private final String desc;

	private ChargeStatus(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	// 自动生成开始
public static ChargeStatus getEnum(int type){
switch(type) {
case 0:
  return unHandle;
case 1:
  return handled;
case 2:
  return rebate;
default:
  return null;
}
}// 自动生成结束
}