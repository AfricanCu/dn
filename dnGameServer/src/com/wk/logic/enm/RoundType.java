package com.wk.logic.enm;

import com.wk.engine.config.ServerConfigAbs;
import com.wk.mj.MjTools;

public enum RoundType {
	/***/
	_10round(1, "10局", 10, 1),
	/***/
	_20round(2, "20局", 20, 2),

	;
	private final int type;
	private final String name;
	private final int num;
	private final int needDiamond;

	private RoundType(int type, String name, int num, int needDiamond) {
		this.type = type;
		this.name = name;
		this.num = num;
		this.needDiamond = needDiamond;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getNum() {
		if (MjTools.isTestHuPai())
			return 1;
		else
			return num;
	}

	public int getNeedDiamond() {
		if (ServerConfigAbs.isCloseDiamondConsume()) {
			return 0;
		}
		return this.needDiamond;
	}

	// 自动生成开始
	public static RoundType getEnum(int type) {
		switch (type) {
		case 1:
			return _10round;
		case 2:
			return _20round;
		default:
			return null;
		}
	}// 自动生成结束

}