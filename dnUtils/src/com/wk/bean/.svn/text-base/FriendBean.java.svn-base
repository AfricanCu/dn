package com.wk.bean;

import com.wk.enun.FriendState;
import com.wk.enun.UserState;

/**
 * 好友
 * 
 * @author ems
 *
 */
public class FriendBean implements Comparable<FriendBean> {
	private long uid;
	private String nickname;
	private UserState userState;
	private int roomId;
	private int serverId;

	public FriendBean() {
	}

	public FriendBean(long uid, String nickname, UserState userState,
			int roomId, int serverId) {
		this.uid = uid;
		this.nickname = nickname;
		this.serverId = serverId;
	}

	public long getUid() {
		return uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setUserState(boolean userState) {
		if (userState)
			this.userState = UserState.online;
		else
			this.userState = UserState.offline;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public FriendState getState() {
		if (this.userState == UserState.offline) {
			return FriendState.off;
		}
		if (roomId == SystemConstantsAbs.NoRoomId) {
			return FriendState.on;
		} else
			return FriendState.game;
	}

	public boolean isOnline() {
		return this.userState == UserState.online;
	}

	@Override
	public int compareTo(FriendBean o) {
		return this.serverId > o.serverId ? 1 : -1;
	}

	public void setState(UserState state) {
		this.userState = state;
	}

	public void setRoomId(int roomId2) {
		this.roomId = roomId2;
	}

}
