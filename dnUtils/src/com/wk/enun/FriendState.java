package com.wk.enun;

public enum FriendState {
	/** */
	on(1, "在线"),
	/***/
	off(2, "离线"),
	/***/
	game(3, "游戏中"), ;

	private int type;
	private String name;

	private FriendState(int type, String name) {
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
public static FriendState getEnum(int type){
switch(type) {
case 1:
  return on;
case 2:
  return off;
case 3:
  return game;
default:
  return null;
}
}// 自动生成结束
}