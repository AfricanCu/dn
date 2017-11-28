package com.wk.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;

import com.google.protobuf.MessageLite;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.util.ProtobufUtils;
import com.wk.http.HttpUtilTools;

public class ToolsUtil extends ToolsUtilAbs {

	/**
	 * GS HTTP 请求到 Bus
	 * 
	 * @param innerIoMessage
	 * @return
	 * @throws Exception
	 */
	public static MessageLite gsToBusHttp(InnerIoMessage innerIoMessage)
			throws Exception {
		InnerMsgId msgId = innerIoMessage.getMsgId();
		byte[] msg = innerIoMessage.getMsg();
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeShort(msgId.getType());
		buffer.writeInt(msg.length);
		buffer.writeBytes(msg);
		byte[] sendHttp = HttpUtilTools.post("http://"
				+ ServerConfig.loginserverAddress + ":"
				+ ServerConfig.loginserverPort + "/Login/innerServlet",
				buffer.array(), true);
		return ProtobufUtils.getProto(sendHttp, msgId.getResMsgId()
				.getDefaultInst());
	}

	public static void main(String[] args) {
		Deferred<?, ?, ?> deferred = new DeferredObject<>();
	}

}
