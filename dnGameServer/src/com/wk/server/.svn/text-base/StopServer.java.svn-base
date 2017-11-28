package com.wk.server;

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

import java.net.URL;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.engine.net.util.ChannelAttachment;
import com.wk.util.NettyClient;

/**
 * 服务器
 */
public class StopServer {

	public static void main(String[] args) throws Throwable {
		URL configURL = LoggerService.class.getClassLoader().getResource(
				"test/client/gameServerLog4j.properties");
		LoggerService.init(configURL);
		NettyClient.createOuterSocketClientSync(args[0],
				ServerConfig.serverPort, new InnerMsgInboundHandler());
	}

	/**
	 * 内部入境消息处理器
	 * 
	 * @author ems
	 *
	 */
	@Sharable
	static class InnerMsgInboundHandler extends
			SimpleChannelInboundHandler<InnerIoMessage> {

		@Override
		public void channelRegistered(ChannelHandlerContext ctx)
				throws Exception {
			super.channelRegistered(ctx);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			Channel channel = ctx.channel();
			ChannelAttachmentAbs channelAttachment = channel.attr(
					SystemConstants.CHANNEL_ATTR_KEY).get();
			if (channelAttachment == null) {
				channelAttachment = new ChannelAttachment(channel);
				channel.attr(SystemConstants.CHANNEL_ATTR_KEY).set(
						channelAttachment);
			}
			if (channel != null && channel.isActive()) {
				InnerIoMessage innerIoMessage = new InnerIoMessage(
						InnerMsgId.ShutDown, new byte[] {});
				channel.write(innerIoMessage).addListener(
						new GenericFutureListener<Future<? super Void>>() {
							public void operationComplete(
									Future<? super Void> future)
									throws Exception {
								LoggerService.getLogicLog().debug(
										"发送:{} {}",
										new Object[] { future.isSuccess(),
												future.isDone() });
							};
						});
				channel.flush();
			}
			super.channelActive(ctx);
		}

		@Override
		public void channelRead0(ChannelHandlerContext ctx, InnerIoMessage msg)
				throws Exception {
			if (msg != null && ctx.channel().isActive()) {
				switch (msg.getMsgId()) {
				case ShutDownbk:
					shutDownbk(msg.getMsg());
					ctx.channel().eventLoop().shutdownGracefully();
					System.exit(0);
					break;
				default:
					break;
				}
			}
		}

		private void shutDownbk(byte[] msg) {
			LoggerService.getLogicLog().error("返回：{}", new String(msg));
		}

	}

}
