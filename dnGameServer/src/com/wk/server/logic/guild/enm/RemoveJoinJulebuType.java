package com.wk.server.logic.guild.enm;

public enum RemoveJoinJulebuType {
	/** 拒绝加入 */
	disagreeJoin(1),
	/** 踢出 */
	kick(2),
	/** 解散 */
	dissolve(3),
	/** 退出 */
	quit(4);
	private final int type;

	private RemoveJoinJulebuType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

}
