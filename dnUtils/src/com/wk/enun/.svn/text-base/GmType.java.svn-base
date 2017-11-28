package com.wk.enun;

import org.json.JSONObject;

public enum GmType {
	/**
	 * <pre>
	 * 公告聊天广播 
	 * 
	 * serverId=1&script=
	 * {
	 *  content:内容
	 * }
	 * </pre>
	 */
	pubChatCast(1) {
	},
	/**
	 * <pre>
	 * 发送邮件
	 * serverId=服务器id&nickname=昵称&script=
	 * {
	 * 	title:邮件标题,
	 * 	content:邮件内容,
	 * 	annex:[
	 * 		{
	 * 			itemTemplateId:道具id,
	 * 			num:道具数目
	 * 		},
	 * 		...
	 * 	]
	 * }
	 * </pre>
	 */
	mail(2) {
	},
	/**
	 * 关闭房间
	 * 
	 */
	closeCreateRoom(3) {
	},
	/**
	 * <pre>
	 * 代理给玩家加钻石
	 * serverId=服务器id&uid=唯一用户id&script=
	 * {
	 * 	diamond:钻石数目
	 * 	proxyUid:PROXY ID
	 * }
	 * </pre>
	 */
	proxyAddPlayerDiamond(4) {
	},
	/**
	 * <pre>
	 * 封号
	 * serverId=-1&uid=唯一用户id&script=
	 * {
	 * 	feng:true false
	 * }
	 * </pre>
	 */
	fenghao(5) {
	},
	/**
	 * <pre>
	 * 玩家设置代理ID
	 * serverId=-1&uid=唯一用户id&script=
	 * {
	 * 	proxyUid:代理id
	 * }
	 * </pre>
	 */
	playerSetProxyId(6) {
	},
	/**
	 * <pre>
	 * 充值给玩家加钻石
	 * serverId=服务器id&uid=唯一用户id&script=
	 * {
	 * 	diamond:钻石数目
	 * }
	 * </pre>
	 */
	chargeAddDiamond(7) {
	};

	private final int type;

	private GmType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	

	// 自动生成开始
	public static GmType getEnum(int type) {
		switch (type) {
		case 1:
			return pubChatCast;
		case 2:
			return mail;
		case 3:
			return closeCreateRoom;
		case 4:
			return proxyAddPlayerDiamond;
		case 5:
			return fenghao;
		case 6:
			return playerSetProxyId;
		case 7:
			return chargeAddDiamond;
		default:
			return null;
		}
	}// 自动生成结束
}