package com.wk.engine.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.SystemConstantsAbs;
import com.wk.engine.net.I.IoMessageAbs;
import com.wk.engine.net.I.MsgIdI;

/**
 * <pre>
 * 将消息组装成{@link ByteBuf}发送
 * </pre>
 * 
 */
@Sharable
public class MessageToByteBufEncoder extends
		MessageToByteEncoder<IoMessageAbs<MsgIdI>> {

	@Override
	protected void encode(ChannelHandlerContext ctx, IoMessageAbs<MsgIdI> msg,
			ByteBuf out) throws Exception {
		byte[] bytebuf = msg.getMsg();
		// 2字节消息id + 消息内容长度
		short bodyLen = (short) (2 + bytebuf.length);
		if (bodyLen > SystemConstantsAbs.MAX_PACKAGE_LENGTH) {
			LoggerService.getPlatformLog().error("发送消息长度有误！{}，{}", bodyLen,
					bytebuf.length);
		}
		int cap = 2 + bodyLen;
		out.capacity(cap);
		out.ensureWritable(cap);
		out.writeShort(bodyLen);
		// 写入bodyLen长度字节的数据 （2字节消息id + 消息内容）
		out.writeShort(msg.getMsgIdValue());
		// if (bodyLen > 2)
		// LoggerService.getPlatformLog().error(
		// "发送消息{},,length:{}，{}",
		// new Object[] { msg.getMsgIdValue(), bodyLen,
		// bytebuf.readableBytes() });
		// 下面会改变msg buf readerIndex
		// bytebuf.readBytes(out, bytebuf.readableBytes());
		// 不会改变msg buf readerIndex
		// out.writeBytes(bytebuf, bytebuf.readerIndex(),
		// bytebuf.readableBytes());
		out.writeBytes(bytebuf);
	}
}
