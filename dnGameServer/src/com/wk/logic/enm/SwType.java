package com.wk.logic.enm;

/**
 * 切服类型 1创房间 2加入房间 3匹配
 * 
 * @author ems
 *
 */
public enum SwType {
	/***/
	joinRoom(1, "加入房间"),
	/***/
	createRoom(2, "创房间"),
	/***/
	proxyCreateRoom(3, "代理创房间"),
	/***/
	inJulebu(4, "进入俱乐部大厅"),
	/***/
	createJulebu(5, "创俱乐部"), ;
	private int type;
	private String name;

	private SwType(int type, String name) {
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
public static SwType getEnum(int type){
switch(type) {
case 1:
  return joinRoom;
case 2:
  return createRoom;
case 3:
  return proxyCreateRoom;
case 4:
  return inJulebu;
case 5:
  return createJulebu;
default:
  return null;
}
}// 自动生成结束
}