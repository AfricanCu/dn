package com.wk.server.logic.login;

import io.netty.channel.Channel;
import msg.LoginMessage.LoginCm;
import msg.LoginMessage.SwLoginCm;
import msg.LoginMessage.SwLoginSm;

import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.inner.GsConnect;
import com.wk.engine.inner.GsSysModule;
import com.wk.engine.net.MessageManager;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.MsgId;
import com.wk.logic.enm.SwType;

public class SwLoginManager {
	private static final SwLoginManager instance = new SwLoginManager();

	public static SwLoginManager getInstance() {
		return instance;
	}

	public byte[] swLogin(Channel channel, SwLoginCm genMessageLite) {
		int sId = genMessageLite.getSId();
		String swCode = genMessageLite.getSwCode();
		SwType enm = SwType.getEnum(genMessageLite.getType());
		long uid = genMessageLite.getUid();
		return SwLoginManager.getInstance().swLogin(channel, sId, uid, swCode,
				enm);
	}

	/**
	 * 切服登陆
	 * 
	 * @param channel
	 *            玩家连接
	 * @param sId
	 *            从哪里来
	 * @param uid
	 * @param enm
	 * @param swCode
	 * @return
	 */
	private byte[] swLogin(Channel channel, int sId, long uid, String swCode,
			SwType enm) {
		JSONObject json = new JSONObject(swCode);
		GsConnect gs = GsSysModule.getInstance().getGs(sId);
		if (gs == null) {
			return MsgId.SwLoginCm
					.gRErrMsg(NTxt.SW_LOGIN__GS_CONNECT_EMPTY);
		}
		try {
			LoginCm.Builder loginCm = LoginCm.newBuilder();
			loginCm.setLoginTime(json.optString("loginTime"));
			loginCm.setSessionCode(json.optString("sessionCode"));
			loginCm.setUid(uid);
			byte[] login = LoginModule.getInstance().login(channel,
					loginCm.build(), SwLoginSm.newBuilder(), sId);
			if (login != null) {
				MessageManager.sendMessage(channel, MsgId.SwLoginSm, login);
			}
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			MessageManager.sendMessage(channel, MsgId.SwLoginSm,
					MsgId.SwLoginCm
							.gRErrMsg(NTxt.SW_LOGIN_EXCEPTION));
		}
		return null;
	}

}
