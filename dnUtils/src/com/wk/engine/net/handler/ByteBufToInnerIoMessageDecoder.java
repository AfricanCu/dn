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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.SystemConstantsAbs;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;

/**
 * <pre>
 * 将{@link ByteBuf}数据组装成{@link IoMessage}
 * </pre>
 */
public class ByteBufToInnerIoMessageDecoder extends ByteToMessageDecoder {

	/**
	 * Creates a new instance.
	 */
	public ByteBufToInnerIoMessageDecoder() {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		in.markReaderIndex();
		int readableBytes = in.readableBytes();
		if (readableBytes < 2) {
			in.resetReaderIndex();
			if (ServerConfigAbs.isDebug() && readableBytes != 0)
				LoggerService.getPlatformLog().error("消息头太短！{}",
						in.readableBytes());
			return;
		}
		// header 长度
		short length = in.readShort();
		if (length < 0 || length > SystemConstantsAbs.MAX_PACKAGE_LENGTH) {
			LoggerService.getPlatformLog().error(
					"{}，消息长度有误！{},{}",
					new Object[] { ctx.channel().remoteAddress(), length,
							in.readableBytes() });
			return;
		}
		if (in.readableBytes() < length) {
			in.resetReaderIndex();
			return;
		}
		short msgId = in.readShort();
		byte[] readBytes = new byte[length - 2];
		in.readBytes(readBytes);
		InnerMsgId enm = InnerMsgId.getEnum(msgId);
		if (enm == null) {
			throw new Exception("找不到内部消息id" + msgId);
		}
		InnerIoMessage message = new InnerIoMessage(enm, readBytes);
		out.add(message);
		if (ServerConfigAbs.isMonitorMessage()) {
			LoggerService.getPlatformLog().warn("get-inner:{}",
					message.getMsgId().name());
		}
	}
}
