package com.wk.server.logic.friend;

import java.sql.SQLException;

import msg.InnerMessage.GmBusToGsbk;

import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.engine.inner.BusConnect;
import com.wk.engine.net.InnerMsgId;
import com.wk.enun.GmType;
import com.wk.logic.config.NTxt;
import com.wk.server.ibatis.select.User;
import com.wk.server.logic.item.ItemTemplate;
import com.wk.server.logic.login.LoginModule;
import com.wk.user.bean.UserBean;
import com.wk.user.enm.ExpiresInType;

public class GmFFHandler extends FindUserHandlerI {
	private final JSONObject json;
	private final GmType gmType;
	private final String reqId;

	public GmFFHandler(long uid, GmType gmType, JSONObject json, String reqId) {
		super(null, uid, 0, NEED_LOAD, NOT_NEED_SW);
		this.gmType = gmType;
		this.json = json;
		this.reqId = reqId;
		handle();
	}

	@Override
	public int currentServerUnloadProcess(UserBean userBean) {
		BusConnect.getInstance().sendMessage(
				InnerMsgId.GmBusToGsbk,
				GmBusToGsbk.newBuilder().setCode(NTxt.UN_LOAD_USER)
						.setReqId(this.reqId));
		return NTxt.SUCCESS;
	}

	@Override
	public int currentServerProcess(User user) {
		switch (gmType) {
		case proxyAddPlayerDiamond:
			return proxyAddPlayerDiamond(user);
		case fenghao:
			return fenghao(user);
		case playerSetProxyId:
			return playerSetProxyId(user);
		case chargeAddDiamond:
			return chargeAddDiamond(user);
		default:
			return NTxt.NOT_IMPLEMENT;
		}
	}

	private int chargeAddDiamond(User user) {
		int diamond = Integer.parseInt(json.optString("diamond"));
		try {
			user.addItem(ItemTemplate.Diamond_ID, diamond, true,
					NTxt.CHARGE_ADD_DIAMOND);
			user.save();
			return NTxtAbs.SUCCESS;
		} catch (SQLException e) {
			LoggerService.getChargeLog().error(
					String.format("充值给玩家加钻，钻:%s,当前：%s,error:%s", diamond,
							e.getMessage()), e);
			return NTxtAbs.SQL_EXCEPTION;
		}
	}

	private int playerSetProxyId(User user) {
		int proxyUid = json.optInt("proxyUid");
		user.setMyAgency(proxyUid);
		try {
			user.save();
			return NTxt.SUCCESS;
		} catch (SQLException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return NTxt.SQL_EXCEPTION;
		}
	}

	private int fenghao(User user) {
		boolean feng = json.optBoolean("feng");
		if (feng) {
			user.setExpires_in(ExpiresInType.feng.getType());
			if (user.isOnline()) {
				LoginModule.getInstance().kick(user.getUid());
			}
		} else {
			user.setExpires_in(ExpiresInType.nofeng.getType());
		}
		try {
			user.save();
			return NTxtAbs.SUCCESS;
		} catch (SQLException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return NTxtAbs.SQL_EXCEPTION;
		}
	}

	private int proxyAddPlayerDiamond(User user) {
		int diamond = Integer.parseInt(json.optString("diamond"));
		String proxyId = json.optString("proxyUid");
		try {
			user.addItem(ItemTemplate.Diamond_ID, diamond, true,
					NTxt.PROXY_ADD_PLAYER_DIAMOND);
			user.save();
			return NTxtAbs.SUCCESS;
		} catch (SQLException e) {
			LoggerService.getChargeLog().error(
					String.format("代理给玩家加钻，代理id:%s,钻:%s,当前：%s,error:%s",
							proxyId, diamond, e.getMessage()), e);
			return NTxtAbs.SQL_EXCEPTION;
		}
	}

	@Override
	public void handleCode(int code) {
		BusConnect.getInstance().sendMessage(InnerMsgId.GmBusToGsbk,
				GmBusToGsbk.newBuilder().setCode(code).setReqId(this.reqId));
	}
}
