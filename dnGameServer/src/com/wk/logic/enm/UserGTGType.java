package com.wk.logic.enm;

import msg.InnerMessage.GuildGsToGs;
import msg.InnerMessage.UserGsToGs;

import com.wk.server.logic.friend.FindUserHandlerI;
import com.wk.server.logic.guild.FindGuildHandlerI;
import com.wk.server.logic.guild.FindMemberHandlerI;

/**
 * 玩家服务器与服务器之间交互处理类型
 * 
 * @author ems
 *
 */
public enum UserGTGType {
	/**
	 * 
	 */
	RetDiamondToGuildMaster(1, "还钻石给会长") {
		@Override
		public int process(UserGsToGs userGsToGs) {
			return new FindUserHandlerI.ChangeDiamondHandler(this, userGsToGs)
					.getCode();
		}
	},
	/**
	 * 
	 */
	CreateJulebuRoomConsumeDiamond(2, "创建俱乐部房间消耗钻石") {
		@Override
		public int process(UserGsToGs userGsToGs) {
			return new FindUserHandlerI.ChangeDiamondHandler(this, userGsToGs)
					.getCode();
		}
	},
	/**
	 * 
	 */
	RetDiamondToProxy(3, "还钻石给代理") {
		@Override
		public int process(UserGsToGs userGsToGs) {
			return new FindUserHandlerI.ChangeDiamondHandler(this, userGsToGs)
					.getCode();
		}
	},

	proxyRecord(4, "代开房游戏记录") {

		@Override
		public int process(UserGsToGs userGsToGs) {
			return new FindUserHandlerI.ProxyRecordHandler(this, userGsToGs)
					.getCode();
		}

	},
	;
	private final int type;
	private final String name;

	private UserGTGType(int type, String name) {
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
	public static UserGTGType getEnum(int type) {
		switch (type) {
		case 1:
			return RetDiamondToGuildMaster;
		case 2:
			return CreateJulebuRoomConsumeDiamond;
		case 3:
			return RetDiamondToProxy;
		default:
			return null;
		}
	}// 自动生成结束

	/**
	 * 
	 * @param userGsToGs
	 * @return
	 */
	public abstract int process(UserGsToGs userGsToGs);
}