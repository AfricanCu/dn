package com.wk.guild.enm;

/**
 * 大赢家类型
 * 
 * @author ems
 *
 */
public enum WinnerNumType {
	/**
	 * 
	 */
	oneWinner(1, 12),
	/**
	 * 
	 */
	twoWinner(2, 6),
	/**
	 * 
	 */
	threeWinner(3, 4),
	/**
	 * 
	 */
	fourWinner(4, 3), ;
	private final int type;
	private final int add;

	private WinnerNumType(int type, int add) {
		this.type = type;
		this.add = add;
	}

	public int getType() {
		return type;
	}

	public int getAdd() {
		return add;
	}

	// 自动生成开始
public static WinnerNumType getEnum(int type){
switch(type) {
case 1:
  return oneWinner;
case 2:
  return twoWinner;
case 3:
  return threeWinner;
case 4:
  return fourWinner;
default:
  return null;
}
}// 自动生成结束
}