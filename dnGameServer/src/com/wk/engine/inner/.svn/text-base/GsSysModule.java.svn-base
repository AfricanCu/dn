package com.wk.engine.inner;

import io.netty.channel.Channel;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import msg.InnerMessage.GuildGsToGs;
import msg.InnerMessage.GuildGsToGsbk;
import msg.InnerMessage.Server;
import msg.InnerMessage.ServerListCastBusToGs;
import msg.InnerMessage.UserGsToGs;
import msg.InnerMessage.UserGsToGsbk;
import msg.RoomMessage.SwServer;

import org.json.JSONObject;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.SystemModuleAbs;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.inner.gstogs.GuildGTGHandler;
import com.wk.engine.inner.gstogs.UserGTGHandler;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.engine.secur.SecureUtil;
import com.wk.logic.enm.SwType;
import com.wk.user.bean.UserBean;

/**
 * 处理其他gs发来的信息
 * 
 * @author ems
 *
 */
public class GsSysModule extends SystemModuleAbs {

	private static GsSysModule instance;

	public static GsSysModule getInstance() {
		return instance;
	}

	/**
	 * 其他游戏服 Map
	 * 
	 * <gsid，gs>
	 */
	private final HashMap<Integer, GsConnect> gsMap = new HashMap<>();

	@Override
	public void processMsg(Channel channel, InnerIoMessage msg,
			ChannelAttachmentAbs channelAttachment) throws Exception {
		GsConnect gs = (GsConnect) channelAttachment.getCsConnectI();
		if (gs == null) {
			InetSocketAddress address = (InetSocketAddress) channel
					.remoteAddress();
			String hostAddress = address.getAddress().getHostAddress();
			for (GsConnect connect : this.gsMap.values()) {
				if (connect.getServer().getHost().equals(hostAddress)) {
					channelAttachment.setCsConnectI(connect);
					break;
				}
			}
			gs = (GsConnect) channelAttachment.getCsConnectI();
		}
		if (gs == null) {
			LoggerService.getLogicLog().error("找不到gs服务器!");
			return;
		}
		processMsg(msg, gs);
	}

	/**
	 * 处理来自另一gs的消息
	 * 
	 * @param msg
	 * @param gs
	 * @throws InvalidProtocolBufferException
	 * @throws UnsupportedEncodingException
	 */
	public void processMsg(InnerIoMessage msg, GsConnect gs)
			throws InvalidProtocolBufferException, UnsupportedEncodingException {
		MessageLite liteorBuilder = null;
		InnerMsgId msgId = msg.getMsgId().getResMsgId();
		MessageLite messageLite = msg.genMessageLite();
		switch (msg.getMsgId()) {
		case GuildGsToGs:
			liteorBuilder = GuildGTGHandler.getInstance()
					.requestSwServerProcess((GuildGsToGs) messageLite, gs);
			break;
		case GuildGsToGsbk:
			GuildGTGHandler.getInstance().responseSwServerProcess(
					(GuildGsToGsbk) messageLite, gs);
			break;
		case UserGsToGs:
			liteorBuilder = UserGTGHandler.getInstance()
					.requestSwServerProcess((UserGsToGs) messageLite, gs);
			break;
		case UserGsToGsbk:
			UserGTGHandler.getInstance().responseSwServerProcess(
					(UserGsToGsbk) messageLite, gs);
			break;
		default:
			break;
		}
		if (liteorBuilder != null && gs != null)
			gs.sendMessage(msgId, liteorBuilder);
	}

	public GsConnect getGs(int id) {
		return gsMap.get(id);
	}

	/**
	 * 服务器列表推送
	 * 
	 * @param genMessageLite
	 */
	public void setServerListCast(ServerListCastBusToGs genMessageLite) {
		List<Server> serverList = genMessageLite.getServerList();
		gsMap.clear();
		for (Server server : serverList) {
			if (server.getServerId() != ServerConfig.serverId) {
				gsMap.put(server.getServerId(), new GsConnect(server));
			}
		}
	}

	/**
	 * 获取切服信息
	 * 
	 * @param serverId
	 *            目标GS
	 * @param type
	 *            切服类型
	 * @param user
	 *            切服玩家
	 * @return
	 * @throws SQLException
	 */
	public SwServer.Builder getSw(int serverId, SwType type, UserBean user) {
		GsConnect gs = gsMap.get(serverId);
		if (gs == null) {
			return null;
		}
		user.setSessionCode(SecureUtil.genSessionCode(user));
		user.setServerId(serverId);
		try {
			user.save();
		} catch (SQLException e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
			user.setServerId(ServerConfig.serverId);
			return null;
		}
		JSONObject json = new JSONObject();
		json.put("loginTime", SecureUtil.genLoginTime());
		json.put("sessionCode", user.getSessionCode());
		SwServer.Builder sw = SwServer.newBuilder();
		sw.setSId(ServerConfig.serverId);
		sw.setHost(gs.getServer().getHost());
		sw.setPort(gs.getServer().getPort());
		sw.setMyCode(json.toString());
		sw.setType(type.getType());
		return sw;
	}

	public void shutdown() {
		for (GsConnect conn : this.gsMap.values()) {
			try {
				conn.shutdown();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
