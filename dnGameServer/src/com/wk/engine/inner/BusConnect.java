package com.wk.engine.inner;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

import msg.InnerMessage.GsLoginBusHttp;
import msg.InnerMessage.GsLoginBusHttpbk;
import msg.InnerMessage.ServerListCastBusToGsbk;

import com.googlecode.protobuf.format.JsonFormat;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.net.I.InnerCsConnectAbs;
import com.wk.logic.config.NTxt;
import com.wk.util.ToolsUtil;

/**
 * 将bus作为一个user
 * 
 * @author ems
 *
 */
public class BusConnect extends InnerCsConnectAbs {

	private static final BusConnect instance = new BusConnect();

	public static BusConnect getInstance() {
		return instance;
	}

	public Channel checkClient() {
		if (client != null && !(client.isOpen() && client.isActive())) {
			client = null;
		}
		if (client == null) {
			GsLoginBusHttp.Builder newBuilder = GsLoginBusHttp.newBuilder();
			newBuilder.setServerId(ServerConfig.serverId);
			newBuilder.setCode(ServerConfig.gsLoginBusCode);
			try {
				InnerIoMessage innerIoMessage = new InnerIoMessage(
						InnerMsgId.GsLoginBusHttp, newBuilder);
				GsLoginBusHttpbk gsLoginBusHttpbk = (GsLoginBusHttpbk) ToolsUtil
						.gsToBusHttp(innerIoMessage);
				LoggerService.getLogicLog().warn("gs登陆bus http请求返回:{}",
						JsonFormat.printToString(gsLoginBusHttpbk));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	public void registerClient(Channel channel) throws Exception {
		ServerListCastBusToGsbk.Builder serverListCastBusToGsbk = ServerListCastBusToGsbk
				.newBuilder();
		InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
		String hostAddress = address.getAddress().getHostAddress();
		if (!ServerConfig.loginserverAddress.equals(hostAddress)) {
			throw new Exception(String.format("非法的bus连接！%s != %s",
					ServerConfig.loginserverAddress, hostAddress));
		}
		if (client == channel) {
			LoggerService.getLogicLog().error("bus<=>gs 重复注册连接!");
		} else {
			client = channel;
		}
		if (client != null && !(client.isOpen() && client.isActive())) {
			client = null;
		}
		if (client != null) {
			serverListCastBusToGsbk.setCode(NTxt.SUCCESS);
			LoggerService.getLogicLog().error("bus<=>gs 注册连接!");
			client.attr(SystemConstants.CHANNEL_ATTR_KEY).get()
					.setCsConnectI(this);
		} else {
			serverListCastBusToGsbk.setCode(NTxt.COMMON_ERROR);
			LoggerService.getLogicLog().error("bus<=>gs 注册连接失败!");
		}
		sendMessage(InnerMsgId.ServerListCastBusToGsbk, serverListCastBusToGsbk);
	}

	public void unregisterClient() {
		client = null;
		LoggerService.getLogicLog().error("bus<=>gs 注销连接!");
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, InnerIoMessage msg)
			throws Exception {
	}

	public void shutdown() throws InterruptedException {
		if (client != null) {
			ChannelFuture syncUninterruptibly = client.close()
					.syncUninterruptibly();
			LoggerService.getLogicLog().error("bus<=>gs 关闭连接！{} {}",
					syncUninterruptibly.isSuccess(),
					syncUninterruptibly.isDone());
		}
	}

}
