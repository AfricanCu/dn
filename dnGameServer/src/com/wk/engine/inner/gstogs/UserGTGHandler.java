package com.wk.engine.inner.gstogs;

import msg.InnerMessage.UserGsToGs;
import msg.InnerMessage.UserGsToGsbk;
import msg.LoginMessage.GameRecord;

import com.wk.engine.inner.GsConnect;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.UserGTGType;
import com.wk.server.logic.friend.FindUserHandlerI;
import com.wk.server.logic.friend.HandlerResultI;

public class UserGTGHandler extends GsToGsHandler<UserGsToGs, UserGsToGsbk> {

	private static final UserGTGHandler instance = new UserGTGHandler();

	public static UserGTGHandler getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param handlerI
	 * @param uid
	 * @param serverId
	 * @param type
	 * @param diamond
	 */
	public void process(FindUserHandlerI handlerI, long uid, int serverId,
			UserGTGType type, int diamond, GameRecord gameRecord) {
		UserGsToGs.Builder messageLiteBuilder = UserGsToGs.newBuilder()
				.setReqId(genReqId()).setUid(uid).setType(type.getType())
				.setDiamond(diamond);
		if (gameRecord == null) {
			messageLiteBuilder.setGameRecord(gameRecord);
		}
		UserGsToGs messageLite = messageLiteBuilder.build();
		super.swHandle(messageLite, messageLite.getReqId(), serverId, handlerI);
	}

	@Override
	public UserGsToGsbk requestSwServerProcess(UserGsToGs messageLite,
			GsConnect gs) {
		int code = swServerProcess(messageLite, gs);
		return UserGsToGsbk.newBuilder().setReqId(messageLite.getReqId())
				.setCode(code).build();
	}

	public int swServerProcess(UserGsToGs guildGsToGs, GsConnect gs) {
		UserGTGType guildGTGType = UserGTGType.getEnum(guildGsToGs.getType());
		if (guildGTGType == null) {
			return NTxt.GUILDGTGTYPE_ERROR;
		}
		return guildGTGType.process(guildGsToGs);
	}

	@Override
	public void responseSwServerProcess(UserGsToGsbk messageLitebk, GsConnect gs) {
		responseResolve(messageLitebk.getUid(), messageLitebk);
	}

	@Override
	public DeferredHandleI<UserGsToGsbk> getDefaultDeferredHandler(
			final HandlerResultI handlerI) {
		return new DeferredHandleI<UserGsToGsbk>() {
			@Override
			public void onDone(UserGsToGsbk result) {
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
