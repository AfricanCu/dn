package com.wk.engine.inner.gstogs;

import msg.InnerMessage.GuildGsToGs;
import msg.InnerMessage.GuildGsToGs.Builder;
import msg.InnerMessage.GuildGsToGsbk;

import com.wk.engine.inner.GsConnect;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.GuildGTGType;
import com.wk.server.logic.friend.HandlerResultI;
import com.wk.server.logic.guild.FindGuildHandlerI;
import com.wk.server.logic.guild.FindMemberHandlerI;

public class GuildGTGHandler extends GsToGsHandler<GuildGsToGs, GuildGsToGsbk> {

	private static final GuildGTGHandler instance = new GuildGTGHandler();

	public static GuildGTGHandler getInstance() {
		return instance;
	}

	/**
	 * 查找公会处理
	 * 
	 * @param uid
	 * @param id
	 * @param userNickname
	 * @param serverId
	 * @param handlerI
	 * @param type
	 */
	public void processGuild(long uid, int id, String userNickname,
			String data, int serverId, FindGuildHandlerI handlerI,
			GuildGTGType type) {
		GuildGsToGs.Builder guildGsToGs = GuildGsToGs.newBuilder()
				.setReqId(genReqId()).setUid(uid).setId(id)
				.setType(type.getType());
		if (userNickname != null) {
			guildGsToGs.setNickname(userNickname);
		}
		if (data != null) {
			guildGsToGs.setData(data);
		}
		GuildGsToGs messageLite = guildGsToGs.build();
		super.swHandle(messageLite, messageLite.getReqId(), serverId, handlerI);
	}

	/**
	 * 查找公会成员处理
	 * 
	 * @param uid
	 * @param id
	 * @param userNickname
	 * @param serverId
	 * @param handlerI
	 * @param type
	 */
	public void processMember(long uid, int id, String userNickname,
			String data, int serverId, FindMemberHandlerI handlerI,
			GuildGTGType type) {
		GuildGsToGs.Builder guildGsToGs = GuildGsToGs.newBuilder()
				.setReqId(genReqId()).setUid(uid).setId(id)
				.setType(type.getType());
		if (userNickname != null) {
			guildGsToGs.setNickname(userNickname);
		}
		if (data != null) {
			guildGsToGs.setData(data);
		}
		GuildGsToGs messageLite = guildGsToGs.build();
		super.swHandle(messageLite, messageLite.getReqId(), serverId, handlerI);
	}

	@Override
	public GuildGsToGsbk requestSwServerProcess(GuildGsToGs genMessageLite,
			GsConnect gs) {
		int code = swServerProcess(genMessageLite, gs);
		return GuildGsToGsbk.newBuilder().setReqId(genMessageLite.getReqId())
				.setCode(code).build();
	}

	public int swServerProcess(GuildGsToGs guildGsToGs, GsConnect gs) {
		GuildGTGType guildGTGType = GuildGTGType.getEnum(guildGsToGs.getType());
		if (guildGTGType == null) {
			return NTxt.GUILDGTGTYPE_ERROR;
		}
		return guildGTGType.process(guildGsToGs);
	}

	@Override
	public void responseSwServerProcess(GuildGsToGsbk messageLitebk,
			GsConnect gs) {
		responseResolve(messageLitebk.getUid(), messageLitebk);
	}

	public DeferredHandleI<GuildGsToGsbk> getDefaultDeferredHandler(
			final HandlerResultI handlerI) {
		return new DeferredHandleI<GuildGsToGsbk>() {
			@Override
			public void onDone(GuildGsToGsbk result) {
				handlerI.setCode(result.getCode());
			}

			@Override
			public void onFail(Object result) {
				handlerI.setCode(NTxt.OVER_TIME);
			}

			@Override
			public void notFoundGs() {
				handlerI.setCode(NTxt.GS_CONNECT_EMPTY);
			}
		};
	}

}
