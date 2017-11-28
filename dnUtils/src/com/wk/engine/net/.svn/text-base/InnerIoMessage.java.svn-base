package com.wk.engine.net;

import com.google.protobuf.MessageLiteOrBuilder;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.net.I.IoMessageAbs;

/**
 * 内部消息
 * 
 * @author lixing 2015年3月30日
 */
public class InnerIoMessage extends IoMessageAbs<InnerMsgId> {
	public static final InnerIoMessage heart;
	static {
		InnerIoMessage innerIoMessage = null;
		try {
			innerIoMessage = new InnerIoMessage(InnerMsgId.HeartBeat,
					new byte[] {});
		} catch (Exception e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
		}
		heart = innerIoMessage;
	}

	public InnerIoMessage(InnerMsgId msgId, byte[] msg) throws Exception {
		super(msgId, msg);
	}

	public InnerIoMessage(InnerMsgId msgId, MessageLiteOrBuilder liteorBuilder)
			throws Exception {
		super(msgId, liteorBuilder);
	}

}
