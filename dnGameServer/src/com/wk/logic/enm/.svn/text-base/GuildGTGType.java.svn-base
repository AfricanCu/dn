package com.wk.logic.enm;

import msg.InnerMessage.GuildGsToGs;

import com.wk.server.logic.guild.FindGuildHandlerI;
import com.wk.server.logic.guild.FindMemberHandlerI;

/**
 * 
 * @author ems
 *
 */
public enum GuildGTGType {
	/**
	 * 申请加入
	 */
	ApplyJulebu(1) {
		@Override
		public int process(GuildGsToGs guildGsToGs) {
			return new FindGuildHandlerI.ApplyJulebuHandler(guildGsToGs)
					.getCode();
		}
	},
	/**
	 * 解散公会
	 */
	DissolveJulebu(2) {
		@Override
		public int process(GuildGsToGs guildGsToGs) {
			return new FindMemberHandlerI.DissolveJulebu(guildGsToGs).getCode();
		}
	},
	/**
	 * 剔除俱乐部成员
	 */
	KickJulebuMember(3) {
		@Override
		public int process(GuildGsToGs guildGsToGs) {
			return new FindMemberHandlerI.KickJulebuMember(guildGsToGs)
					.getCode();
		}
	},
	DisagreeApply(4) {
		@Override
		public int process(GuildGsToGs guildGsToGs) {
			return new FindMemberHandlerI.DisagreeApply(guildGsToGs).getCode();
		}
	},
	QuitJulebu(5) {
		@Override
		public int process(GuildGsToGs guildGsToGs) {
			return new FindGuildHandlerI.QuitJulebuHandler(guildGsToGs)
					.getCode();
		}
	},
	AgreeApply(6) {
		@Override
		public int process(GuildGsToGs guildGsToGs) {
			return new FindMemberHandlerI.AgreeApply(guildGsToGs).getCode();
		}
	},
	UpdateJulebu(7) {
		@Override
		public int process(GuildGsToGs guildGsToGs) {
			return new FindMemberHandlerI.UpdateJulebu(guildGsToGs).getCode();
		}
	};
	private final int type;

	private GuildGTGType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	// 自动生成开始
public static GuildGTGType getEnum(int type){
switch(type) {
case 1:
  return ApplyJulebu;
case 2:
  return DissolveJulebu;
case 3:
  return KickJulebuMember;
case 4:
  return DisagreeApply;
case 5:
  return QuitJulebu;
case 6:
  return AgreeApply;
case 7:
  return UpdateJulebu;
default:
  return null;
}
}// 自动生成结束

	public abstract int process(GuildGsToGs guildGsToGs);
}