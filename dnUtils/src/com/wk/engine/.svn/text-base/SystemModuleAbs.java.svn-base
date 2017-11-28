package com.wk.engine;

import io.netty.channel.Channel;

import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.I.ChannelAttachmentAbs;

/**
 * 系统模块抽象
 * 
 * @author ems
 *
 */
public abstract class SystemModuleAbs {
	private String name;// 模块名
	private short downerMsgId;// 消息id下限（包含）
	private short upperMsgId;// 消息id上限（包含）

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getUpperMsgId() {
		return upperMsgId;
	}

	public void setUpperMsgId(short upperMsgId) {
		this.upperMsgId = upperMsgId;
	}

	public short getDownerMsgId() {
		return downerMsgId;
	}

	public void setDownerMsgId(short downerMsgId) {
		this.downerMsgId = downerMsgId;
	}

	/**
	 * 
	 * @param channel
	 * @param msg
	 * @param channelAttachment
	 *            附加信息
	 * @throws Exception
	 */
	public abstract void processMsg(Channel channel, InnerIoMessage msg,
			ChannelAttachmentAbs channelAttachment) throws Exception;
}
