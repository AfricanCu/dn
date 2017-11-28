package com.wk.engine.inner;

import io.netty.channel.Channel;
import msg.InnerMessage.ChargeBusToGs;
import msg.InnerMessage.ChargeBusToGsbk;
import msg.InnerMessage.GmBusToGs;
import msg.InnerMessage.GmBusToGsbk;
import msg.InnerMessage.KickBusToGs;
import msg.InnerMessage.ServerListCastBusToGs;
import msg.InnerMessage.ServerStatusBusToGs;
import msg.LoginMessage.ChargeCast;
import msg.RoomMessage.SwServer;

import org.json.JSONObject;

import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.bean.SystemConstantsAbs;
import com.wk.engine.SystemModuleAbs;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.enun.DistrictType;
import com.wk.enun.GmType;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.MsgId;
import com.wk.logic.enm.SwType;
import com.wk.server.DnServer;
import com.wk.server.ibatis.select.User;
import com.wk.server.logic.friend.GmFFHandler;
import com.wk.server.logic.item.ItemTemplate;
import com.wk.server.logic.login.LoginModule;
import com.wk.server.logic.login.UserManager;

public class BusSysModule extends SystemModuleAbs {

	private static BusSysModule instance;

	public static BusSysModule getInstance() {
		return instance;
	}

	/**
	 * 可以切换 到哪个服务器
	 */
	private int oserverId;
	private String othost;
	private int otport;
	private String otswCode;
	/** 是否关闭创房 **/
	private boolean closeCreateRoom;

	@Override
	public void processMsg(Channel channel, InnerIoMessage msg,
			ChannelAttachmentAbs channelAttachment) throws Exception {
		MessageLite genMessageLite = msg.genMessageLite();
		switch (msg.getMsgId()) {
		case ServerListCastBusToGs:
			BusConnect.getInstance().registerClient(channel);
			GsSysModule.getInstance().setServerListCast(
					(ServerListCastBusToGs) genMessageLite);
			break;
		case ServerStatusBusToGs:
			serverStatus((ServerStatusBusToGs) genMessageLite);
			break;
		case KickBusToGs:
			kick((KickBusToGs) genMessageLite);
			break;
		case ChargeBusToGs:
			charge((ChargeBusToGs) genMessageLite);
			break;
		case GmBusToGs:
			gm((GmBusToGs) genMessageLite);
			break;
		default:
			LoggerService.getLogicLog().error("error!!! 未实现{}",
					msg.getMsgId().name());
			break;
		}
	}

	private void gm(GmBusToGs genMessageLite) {
		String reqId = genMessageLite.getReqId();
		GmType gmType = GmType.getEnum(genMessageLite.getGmType());
		long uid = genMessageLite.getUid();
		JSONObject json = new JSONObject(genMessageLite.getJson());
		if (gmType == null) {
			BusConnect.getInstance().sendMessage(
					InnerMsgId.GmBusToGsbk,
					GmBusToGsbk.newBuilder().setReqId(reqId)
							.setCode(NTxtAbs.INFO_ERROR));
			return;
		}
		switch (gmType) {
		case pubChatCast:
			// LoginModule.getInstance()
			// .chatPub(ChatType.system, SystemConstants.sysUserUid + "",
			// json.optString("nickname", "系统"),
			// json.optString("content"));
			// BusConnect.getInstance().sendMessage(
			// InnerMsgId.GmBusToGsbk,
			// GmBusToGsbk.newBuilder().setReqId(reqId)
			// .setCode(NoticeTextAbs.SUCCESS));
			return;
		case closeCreateRoom:
			this.closeCreateRoom = json.optBoolean("close");
			LoggerService.getPlatformLog().error("关服关闭创房！！！！！{}",
					this.closeCreateRoom);
			closeCreateRoom();
			BusConnect.getInstance().sendMessage(
					InnerMsgId.GmBusToGsbk,
					GmBusToGsbk.newBuilder().setReqId(reqId)
							.setCode(NTxtAbs.SUCCESS));
			return;
		default:
			new GmFFHandler(uid, gmType, json, reqId);
			break;
		}
	}

	private void closeCreateRoom() {
		// TODO Auto-generated method stub

	}

	private void charge(ChargeBusToGs genMessageLite) {
		String orderId = genMessageLite.getOrderId();
		long uid = genMessageLite.getUid();
		int diamond = genMessageLite.getDiamond();
		User friend = UserManager.getInstance().getUser(uid);
		if (friend == null) {
			BusConnect.getInstance().sendMessage(
					InnerMsgId.ChargeBusToGsbk,
					ChargeBusToGsbk.newBuilder().setOrderId(orderId)
							.setCode(NTxt.CHARGE_NOT_FOUND_USER));
			LoggerService.getChargeLog().error("充值加钻石失败，找不到玩家!uid{},diamond{}",
					uid, diamond);
			return;
		}
		friend.addItem(ItemTemplate.Diamond_ID, diamond, true,
				NTxt.CHARGE_ADD_DIAMOND);
		friend.sendMessage(MsgId.ChargeCast, ChargeCast.newBuilder()
				.setDiamond(diamond));
		BusConnect.getInstance().sendMessage(
				InnerMsgId.ChargeBusToGsbk,
				ChargeBusToGsbk.newBuilder().setOrderId(orderId)
						.setCode(NTxtAbs.SUCCESS));
		LoggerService.getChargeLog().error("充值加钻石成功!uid{},diamond{}", uid,
				diamond);
	}

	private void kick(KickBusToGs genMessageLite) {
		long uid = genMessageLite.getUid();
		LoginModule.getInstance().kick(uid);
	}

	public void serverStatus(ServerStatusBusToGs proto) {
		if (proto.getOserverId() != SystemConstantsAbs.NoServerId
				&& proto.getOthost() != null) {
			oserverId = proto.getOserverId();
			othost = proto.getOthost();
			otport = proto.getOtport();
			otswCode = proto.getOtswCode();
		} else {
			oserverId = SystemConstantsAbs.NoServerId;
		}
		LoginModule.getInstance().serverStatusbk();
	}

	public SwServer.Builder getSw(SwType type) {
		if (oserverId == SystemConstantsAbs.NoServerId) {
			return null;
		}
		SwServer.Builder sw = SwServer.newBuilder();
		sw.setSId(ServerConfig.serverId);
		sw.setHost(othost);
		sw.setPort(otport);
		sw.setMyCode(otswCode);
		sw.setType(type.getType());
		return sw;
	}

	public int getOserverId() {
		return oserverId;
	}

	public String getOthost() {
		return othost;
	}

	public int getOtport() {
		return otport;
	}

	public String getOtswCode() {
		return otswCode;
	}

	public void shutdown() {
		try {
			BusConnect.getInstance().shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isCloseCreateRoom() {
		return closeCreateRoom;
	}

}
