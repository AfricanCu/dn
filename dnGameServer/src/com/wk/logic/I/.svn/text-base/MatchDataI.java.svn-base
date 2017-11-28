package com.wk.logic.I;

import com.google.protobuf.MessageLite;
import com.wk.logic.enm.MsgId;
import com.wk.server.ibatis.select.User;

/**
 * 玩家匹配抽象
 * 
 * @author ems
 *
 */
public abstract class MatchDataI {
	protected final User user;
	/** 是否已经匹配 */
	protected boolean isMatch = false;

	public MatchDataI(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	/** 已经匹配 ? */
	public boolean isMatch() {
		return isMatch;
	}

	public void sendMessage(MsgId msgId, MessageLite.Builder liteorBuilder) {
		this.user.sendMessage(msgId, liteorBuilder);
	}

}
