package com.wk.server.ibatis.select;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import msg.InnerMessage.KickBusToGsbk;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat.ParseException;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.event.EventManager;
import com.wk.engine.event.EventType;
import com.wk.engine.inner.BusConnect;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.net.MessageManager;
import com.wk.engine.net.I.CsConnectI;
import com.wk.engine.util.KeyValueDbCache;
import com.wk.enun.UserState;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.MsgId;
import com.wk.server.logic.login.CloseType;
import com.wk.server.logic.login.LoginModule;
import com.wk.server.logic.login.UserManager;
import com.wk.user.bean.UserBean;

/**
 * 玩家
 *
 * @author ems
 *
 */
public class User extends IncomeUserI implements CsConnectI<MsgId> {

	// tmp
	/** 发送消息通道 */
	private Channel channel = null;
	/** channel IP */
	private String ip = "";
	/** 为什么关闭连接 **/
	private CloseType causeClose = CloseType.normal;

	/**
	 * 
	 * @param bean
	 *            用户数据用来重写当前User对象
	 * @throws Exception
	 */
	public User(UserBean bean) throws Exception {
		super();
		this.reset();
		this.overWrite(bean);
	}

	public void reset() {
		super.reset();
		this.channel = null;
		this.ip = "";
		this.causeClose = CloseType.normal;
	}

	public Channel getChannel() {
		return channel;
	}

	public void unregisterClient() throws Exception {
		if (this.causeClose == CloseType.kick) {
			LoggerService.getLogicLog().warn("被踢！uid:{}", this.uid);
		}
		if (this.channel == null) {
			throw new Exception(
					String.format("严重错误！ 重复释放channel！uid:%s,nick:%s", this.uid,
							this.nickname));
		}
		LoginModule.getInstance().removeOnline(this.channel);
		this.channel.attr(SystemConstants.CHANNEL_ATTR_KEY).get()
				.setCsConnectI(null);
		this.channel = null;
		this.setState(UserState.offline);
		this.save();
		EventManager.getInstance().processEvent(EventType.User_LogOut, this);
		NTxt.println(String.format("登出！uid:%s,nick:%s", this.uid,
				this.getNickname()));
		if (!this.cannotChangeServerId()) {
			UserManager.getInstance().removeUser(this.getUid());
		}
		if (this.causeClose == CloseType.kick) {
			BusConnect.getInstance().sendMessage(
					InnerMsgId.KickBusToGsbk,
					KickBusToGsbk.newBuilder().setCode(NTxtAbs.SUCCESS)
							.setUid(this.uid).build().toByteArray());
			NTxt.println(String.format("uid:%s,Nname:%s 重登，踢人下线！",
					this.getUid(), this.getNickname()));
		}
	}

	@Override
	public Channel checkClient() {
		return null;
	}

	@Override
	public void registerClient(Channel channel) throws Exception {
		if (this.channel != null) {
			throw new Exception(String.format("严重错误！未释放channel！uid：%s,nick:%s",
					this.uid, this.nickname));
		}
		this.channel = channel;
		this.ip = ((InetSocketAddress) this.channel.remoteAddress())
				.getAddress().getHostAddress();
		this.channel.attr(SystemConstants.CHANNEL_ATTR_KEY).get()
				.setCsConnectI(this);
		this.setState(UserState.online);
		NTxt.println(String.format("uid:%s,nick:%s 登陆！", this.uid,
				this.nickname));
	}

	public void sendMessage(MsgId msgId, byte[] bytes) {
		if (this.isOnline() && this.channel != null && msgId != null) {
			MessageManager.sendMessage(this.channel, msgId, bytes);
		}
	}

	public void setCauseClose(CloseType type) {
		this.causeClose = type;
	}

	public String getIp() {
		return ip;
	}

}