package com.wk.guild.enm;

/**
 * 成员职位
 * 
 * @author ems
 *
 */
public enum MemberJobType {
	/**
	 * 
	 */
	chengYuan(1, "成员"),
	/**
	 * 
	 */
	fuhuiZhang(2, "副会长"),
	/**
	 * 
	 */
	huiZhang(3, "会长");

	private final int type;
	private final String name;

	private MemberJobType(int type, String name) {
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
public static MemberJobType getEnum(int type){
switch(type) {
case 1:
  return chengYuan;
case 2:
  return fuhuiZhang;
case 3:
  return huiZhang;
default:
  return null;
}
}// 自动生成结束
}