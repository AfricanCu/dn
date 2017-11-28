package com.wk.engine.net.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.ModuleManager;
import com.wk.engine.net.IoMessage;
import com.wk.engine.net.MessageManager;
import com.wk.logic.config.NTxt;
import com.wk.server.DnServer;

@Sharable
public class MsgInboundHandler extends SimpleChannelInboundHandler<IoMessage> {

	@Override
	public void channelRead0(ChannelHandlerContext ctx, IoMessage msg)
			throws Exception {
		if (msg != null && ctx.channel().isActive()) {
			if (DnServer.isOk())
				ModuleManager.processMsg(ctx.channel(), msg);
			else {
				MessageManager.sendMessage(
						ctx.channel(),
						msg.getMsgId().getResMsgId(),
						msg.getMsgId().gRErrMsg(
								NTxt.SERVER_BUSY));
				LoggerService.getPlatformLog().error("服务器未准备，无法处理消息！msgId:{}",
						msg.getMsgId());
			}
		}
	}
}
