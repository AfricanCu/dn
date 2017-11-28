package com.wk.engine.inner;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import msg.InnerMessage.Server;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.engine.net.I.InnerCsConnectAbs;
import com.wk.util.SocketUtil;

/**
 * 与另一gs 的连接
 * 
 * @author ems
 *
 */
@Sharable
public class GsConnect extends InnerCsConnectAbs {

	/**
	 * 配置
	 */
	private final Server server;

	public GsConnect(Server server) {
		this.server = server;
	}

	public Channel checkClient() {
		if (client == null) {
			SocketUtil.createInnerSocketClientSync(server.getInnerHost(),
					server.getPort(), this);
		}
		if (client != null && !(client.isOpen() && client.isActive())) {
			unregisterClient();
		}
		return client;
	}

	@Override
	public void registerClient(Channel channel) throws Exception {
		if (client != null) {
			throw new Exception("严重错误！重复注册channel serverId:"
					+ ServerConfig.serverId + "->gs:"
					+ this.server.getServerId() + "连接成功！");
		}
		client = channel;
		LoggerService.getLogicLog().error("serverId:{}<=>gs:{} 连接成功！",
				ServerConfig.serverId, this.server.getServerId());
		ChannelAttachmentAbs channelAttachmentAbs = client.attr(
				SystemConstants.CHANNEL_ATTR_KEY).get();
		if (channelAttachmentAbs == null) {
			channelAttachmentAbs = new ChannelAttachmentAbs(client) {
			};
			client.attr(SystemConstants.CHANNEL_ATTR_KEY).set(
					channelAttachmentAbs);
		}
		channelAttachmentAbs.setCsConnectI(this);
	}

	public void unregisterClient() {
		this.client = null;
		LoggerService.getLogicLog().error("serverId:{}<=>gs:{} 断线！",
				ServerConfig.serverId, this.server.getServerId());
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, InnerIoMessage msg)
			throws Exception {
		GsSysModule.getInstance().processMsg(msg, this);
	}

	public int getServerId() {
		return this.server.getServerId();
	}

	public Server getServer() {
		return server;
	}

	public void shutdown() throws InterruptedException {
		if (this.client != null) {
			ChannelFuture syncUninterruptibly = client.close()
					.syncUninterruptibly();
			LoggerService.getLogicLog().error(
					"gs{}<=>gs{} shutdown:{} {}",
					new Object[] { this.server.getServerId(),
							ServerConfig.serverId,
							syncUninterruptibly.isSuccess(),
							syncUninterruptibly.isDone() });
		}

	}

}
