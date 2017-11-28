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
package com.wk.engine.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import java.util.List;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.net.IoMessage;
import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.logic.enm.MsgId;
import com.wk.server.ibatis.select.User;

/**
 * <pre>
 * 将{@link ByteBuf}数据组装成{@link IoMessage}
 * </pre>
 */
public class ByteBufToIoMessageDecoder extends ByteToMessageDecoder {

	/**
	 * Creates a new instance.
	 */
	public ByteBufToIoMessageDecoder() {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		in.markReaderIndex();
		int readableBytes = in.readableBytes();
		if (readableBytes > ServerConfig.MAX_PACKAGE_LENGTH) {
			LoggerService.getPlatformLog().error("消息太密集，处理不过来，攻击!ip:{},len:{}",
					ctx.channel().remoteAddress(), in.readableBytes());
		}
		if (readableBytes < 2) {
			in.resetReaderIndex();
			if (readableBytes != 0)
				LoggerService.getPlatformLog().error("消息头太短！len:{}",
						in.readableBytes());
			return;
		}
		// header 长度
		short length = in.readShort();
		if (length < 0 || length > ServerConfig.maxChatContentLength) {
			ctx.channel().close();
			LoggerService.getPlatformLog().error(
					"ip:{}，消息长度有误！{},len:{}",
					new Object[] { ctx.channel().remoteAddress(),
							readableBytes, in.readableBytes() });
			return;
		}
		if (in.readableBytes() < length) {
			in.resetReaderIndex();
			return;
		}
		short msgId = in.readShort();
		byte[] readBytes = new byte[length - 2];
		in.readBytes(readBytes);
		if (msgId > 0) {
			MsgId enm = MsgId.getEnum(msgId);
			if (enm == null) {
				LoggerService.getPlatformLog().error("空消息id!{}", msgId);
				throw new DecoderException(String.format("空消息id:%s", msgId));
			}
			IoMessage message = new IoMessage(enm, readBytes);
			out.add(message);
			if (ServerConfig.isMonitorMessage())
				LoggerService.getPlatformLog().debug("get msgId:{} {}",
						message.getMsgId().name(), message.getMsgIdValue());
			ctx.channel().attr(SystemConstants.CHANNEL_ATTR_KEY).get()
					.incrementAndGetMessageNum();
		} else {
			if (msgId < 0 && msgId > -100) {
				if (msgId == InnerMsgId.HeartBeat.getType()) {
					ctx.channel().writeAndFlush(InnerIoMessage.heart);
					return;
				} else {
					switch (MsgId.getEnum(msgId)) {
					case HeartPause:
						heartPause(ctx);
						break;
					case HeartResume:
						heartResume(ctx);
						break;
					default:
						throw new DecoderException(String.format(
								"未解析inner消息id:%s", msgId));
					}
					return;
				}
			} else {
				InnerMsgId enm = InnerMsgId.getEnum(msgId);
				if (enm == null) {
					LoggerService.getPlatformLog().error("空inner消息 id!{}",
							msgId);
					throw new DecoderException(String.format("空inner消息id:%s",
							msgId));
				}
				InnerIoMessage message = new InnerIoMessage(enm, readBytes);
				out.add(message);
				LoggerService.getPlatformLog().debug("get innerMsgId:{}",
						message.getMsgId().name());
			}
		}
	}

	private void heartPause(ChannelHandlerContext ctx) {
		ChannelAttachmentAbs channelAttachment = ctx.channel()
				.attr(SystemConstants.CHANNEL_ATTR_KEY).get();
		User user = (User) channelAttachment.getCsConnectI();
		if (user != null) {
			user.setHeartPause(System.currentTimeMillis());
			if (ServerConfig.isMonitorMessage())
				LoggerService.getLogicLog().debug("-----------------pause!");
		}
	}

	private void heartResume(ChannelHandlerContext ctx) {
		ChannelAttachmentAbs channelAttachment = ctx.channel()
				.attr(SystemConstants.CHANNEL_ATTR_KEY).get();
		User user = (User) channelAttachment.getCsConnectI();
		if (user != null) {
			user.setHeartPause(0);
			if (ServerConfig.isMonitorMessage())
				LoggerService.getLogicLog().debug("-----------------resume!");
		}
	}
}
