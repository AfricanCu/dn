package com.wk.enun;

/**
 * 操作
 * 
 * @author ems
 *
 */
public enum OperationType {
	/***/
	zhichong(1, "店铺直充"),
	/***/
	daichong(2, "代充"),
	/***/
	jinhuo(3, "进货"),
	/***/
	kaifendian(4,"开分店"),
	/***/
	fendianjinhuo(5,"分店进货"),
	/***/
	fendianzhichong(6,"分店直充"),
	;
	private final int type;
	private final String operation;

	private OperationType(int type, String operation) {
		this.type = type;
		this.operation = operation;
	}

	public int getType() {
		return type;
	}

	public String getOperation() {
		return operation;
	}

	// 自动生成开始
	public static OperationType getEnum(int type) {
		switch (type) {
		case 1:
			return zhichong;
		case 2:
			return daichong;
		case 3:
			return jinhuo;
		case 4:
			return kaifendian;
		case 5:
			return fendianjinhuo;	
		case 6:
			return fendianzhichong;
		default:
			return null;
		}
	}// 自动生成结束
}