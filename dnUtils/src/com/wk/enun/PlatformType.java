package com.wk.enun;

/**
 * 平台类型
 * 
 * @author ems
 *
 */
public enum PlatformType {
	android(1), ios(2), pc(3), ios_visitor(4), wx(5);
	private int type;

	private PlatformType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	// 自动生成开始
public static PlatformType getEnum(int type){
switch(type) {
case 1:
  return android;
case 2:
  return ios;
case 3:
  return pc;
case 4:
  return ios_visitor;
case 5:
  return wx;
default:
  return null;
}
}// 自动生成结束
}