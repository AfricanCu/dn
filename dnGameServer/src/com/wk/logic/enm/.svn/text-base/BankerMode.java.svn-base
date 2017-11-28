package com.wk.logic.enm;

/**
 * 庄家模式
 * 
 * 
 * @author ems
 *
 */
public enum BankerMode {
	/**
	 */
	qiangzhaung(1, "抢庄") {
	},
	/**
	 */
	lunzhuang(2, "轮庄") {
	},
	/**
	 */
	gudingzhuang(3, "固定庄") {
	};

	private final int type;
	private final String name;

	/**
	 * 
	 * @param type
	 * @param name
	 */
	private BankerMode(int type, String name) {
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
	public static BankerMode getEnum(int type) {
		switch (type) {
		case 1:
			return qiangzhaung;
		case 2:
			return lunzhuang;
		case 3:
			return gudingzhuang;
		default:
			return null;
		}
	}// 自动生成结束

}