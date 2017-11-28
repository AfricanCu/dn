package com.wk.logic.enm;

/**
 * 游戏模式
 * 
 * 
 * @author ems
 *
 */
public enum PType {
	/**
	 */
	PUB_DULL(1, "经典玩法", 0) {
	},
	/**
	 * 
	 */
	SEE_NIU_TIMES(2, "疯狂加倍", 0) {
	},
	;
	private final int type;
	private final String name;
	/** 初始积分 **/
	private final int initCoin;

	/**
	 * 
	 * @param type
	 * @param name
	 * @param initCoin
	 */
	private PType(int type, String name, int initCoin) {
		this.type = type;
		this.name = name;
		this.initCoin = initCoin;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getInitCoin() {
		return initCoin;
	}

	/** 最少作几局庄 */
	public static int getRoundDown() {
		return 3;
	}

	// 自动生成开始
	public static PType getEnum(int type) {
		switch (type) {
		case 1:
			return PUB_DULL;
		case 2:
			return SEE_NIU_TIMES;
		default:
			return null;
		}
	}// 自动生成结束
}