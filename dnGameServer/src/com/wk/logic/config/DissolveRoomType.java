package com.wk.logic.config;

/**
 * 游戏常量枚举
 * 
 * @author Administrator
 *
 */
public enum DissolveRoomType {
	/**  */
	GAME_OVER_NO_NEXT_BANKER(true, "没有下个庄了"),
	/**  */
	GAME_OVER_ROUND_OVER(true, "没有下一局了"),
	/**  */
	DISSOLVE_OPERATION(true, "房主解散操作"),
	/**  */
	MEMBER_DISSOLVE_ALL_AGREE(true, "大家都同意解散"),
	/** */
	SERVER_STOP(false, "服务器停服"),
	/**  **/
	NOBODY_DISSOLVE(false, "房间没人，多久解散房间"),
	/**  **/
	JULEBU_HAVEONE_NOT_START_DISSOLVE(false, "俱乐部房间有人，一直不开始，多久解散房间"),
	/**  **/
	COMMON_HAVEONE_NOT_START_DISSOLVE(false, "普通房间有人，一直不开始，多久解散房间"),
	/****/
	PROXY_OPERA(true, "代理操作"), ;
	/*** 是否正常解散 **/
	private final boolean isNormal;
	private final String desc;

	private DissolveRoomType(boolean isNormal, String desc) {
		this.isNormal = isNormal;
		this.desc = desc;
	}

	/**
	 * 是否正常解散
	 * 
	 * 非正常解散要归还钻石
	 * 
	 * @return
	 */
	public boolean isNormal() {
		return isNormal;
	}

	public String getDesc() {
		return desc;
	}

}
