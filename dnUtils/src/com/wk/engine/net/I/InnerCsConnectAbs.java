package com.wk.engine.net.I;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMessageManager;
import com.wk.engine.net.InnerMsgId;

/**
 * 内部 c/s 客户端抽象
 * 
 * @author ems
 *
 */
public abstract class InnerCsConnectAbs extends
		SimpleChannelInboundHandler<InnerIoMessage> implements
		CsConnectI<InnerMsgId> {
	/**
	 * 客户端
	 */
	protected Channel client;

	public void sendMessage(InnerMsgId msgId, MessageLite.Builder builder) {
		this.sendMessage(msgId, builder.build().toByteArray());
	}

	public void sendMessage(InnerMsgId msgId, MessageLite messageLite) {
		this.sendMessage(msgId, messageLite.toByteArray());
	}

	public void sendMessage(InnerMsgId msgId, byte[] bytes) {
		if (client == null) {
			checkClient();
		}
		InnerMessageManager.sendInnerMessage(client, msgId, bytes);
	}
	
	/**
	 * Channel注册到EventLoop中，并且能够处理IO流时被调用
	 */
	@Override
	public final void channelRegistered(ChannelHandlerContext ctx)
			throws Exception {
		try {
			this.registerClient(ctx.channel());
		} catch (Exception e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
		} finally {
			super.channelRegistered(ctx);
		}
	}
	
	/**
	 * Channel处于活动状态时被调用；Channel已经连接和绑定并且就绪。
	 */
	// @Override
	// public void channelActive(ChannelHandlerContext ctx) throws Exception {
	// super.channelActive(ctx);
	// }
	
	/**
	 * 当Channel从它的EventLoop注销并且无法处理任何IO时被调用
	 */
	@Override
	public final void channelUnregistered(ChannelHandlerContext ctx)
			throws Exception {
		try {
			this.unregisterClient();
		} catch (Exception e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
		} finally {
			super.channelUnregistered(ctx);
		}
	}
	
	/**
	 * 当Channel离开活动状态并且不再连接它的远程节点时被调用
	 */
	// @Override
	// public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	// super.channelInactive(ctx);
	// }
}
