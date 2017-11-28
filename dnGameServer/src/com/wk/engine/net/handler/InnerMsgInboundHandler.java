/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.wk.engine.net.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.ModuleManager;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;
import com.wk.logic.config.NTxt;
import com.wk.server.DnServer;

/**
 * 内部入境消息处理器
 * 
 * @author ems
 *
 */
@Sharable
public class InnerMsgInboundHandler extends
		SimpleChannelInboundHandler<InnerIoMessage> {

	@Override
	public void channelRead0(ChannelHandlerContext ctx, InnerIoMessage msg)
			throws Exception {
		if (msg != null && ctx.channel().isActive()) {
			switch (msg.getMsgId()) {
			case ShutDown:
				ctx.channel().writeAndFlush(
						new InnerIoMessage(InnerMsgId.ShutDownbk, "关服成功！"
								.getBytes()));
				NTxt.println("收到ShutDown消息！");
				new Thread("关服") {
					public void run() {
						DnServer.hook.run();
					};
				}.start();
				break;
			default:
				if (DnServer.isOk())
					ModuleManager.processInnerMsg(ctx.channel(), msg);
				else {
					LoggerService.getPlatformLog().error(
							"服务器未准备，无法处理消息！msgId:{}", msg.getMsgId());
				}
				break;
			}
		}
	}
}
