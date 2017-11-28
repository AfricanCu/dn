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

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.atomic.AtomicInteger;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.engine.net.I.CsConnectI;
import com.wk.engine.net.util.ChannelAttachment;
import com.wk.server.ibatis.select.User;

@Sharable
public class SimpleChannelDuplexHandler extends ChannelDuplexHandler {

	private static final SimpleChannelDuplexHandler instance = new SimpleChannelDuplexHandler();

	public static SimpleChannelDuplexHandler getInstance() {
		return instance;
	}

	/**
	 * 会话统计
	 */
	private final AtomicInteger sessionCount = new AtomicInteger(0);

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				CsConnectI<?> csConnectI = ctx.channel()
						.attr(SystemConstants.CHANNEL_ATTR_KEY).get()
						.getCsConnectI();
				if (csConnectI != null) {
					if (csConnectI instanceof User) {// 玩家有心跳的处理
						User user = (User) csConnectI;
						if (user.getHeartPause() != 0
								&& System.currentTimeMillis()
										- user.getHeartPause() < ServerConfig.heartPauseTimeInMillis) {
						} else {
							ctx.channel().close();
							LoggerService.getLogicLog().warn(
									"未收到user心跳!uid:{},nickname:{}",
									user.getUid(), user.getNickname());
						}
					}
				} else {
					LoggerService.getLogicLog().warn(
							"未注册channel，未收到心跳!!!!!!!!!!!!!!!!!!!!!!!");
					ctx.channel().close();
				}
			} else if (e.state() == IdleState.WRITER_IDLE) {
				LoggerService.getLogicLog().warn(
						"没有写操作,断线处理!!!!!!!!!!!!!!!!!!!!!!!");
			}
		}
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		sessionCount.incrementAndGet();
		ChannelAttachmentAbs channelAttachment = ctx.channel()
				.attr(SystemConstants.CHANNEL_ATTR_KEY).get();
		if (channelAttachment == null) {
			channelAttachment = new ChannelAttachment(ctx.channel());
			ctx.channel().attr(SystemConstants.CHANNEL_ATTR_KEY)
					.set(channelAttachment);
		}
		super.channelActive(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		sessionCount.decrementAndGet();
		CsConnectI<?> csConnectI = ctx.channel()
				.attr(SystemConstants.CHANNEL_ATTR_KEY).get().getCsConnectI();
		if (csConnectI != null) {
			csConnectI.unregisterClient();
		} else {
			LoggerService.getLogicLog().warn("未注册！！！！断开通道！");
		}
		ctx.channel().attr(SystemConstants.CHANNEL_ATTR_KEY).set(null);
		super.channelInactive(ctx);
	}

	public int getSessionCount() {
		return sessionCount.get();
	}

}
