package com.wk.engine.net.util;

import io.netty.channel.Channel;

import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.logic.enm.MsgId;

/**
 * channel的 附加记录
 * 
 * @author ems
 *
 */
public class ChannelAttachment extends ChannelAttachmentAbs {

	public ChannelAttachment(Channel channel) {
		super(channel);
		for (MsgId msgId : MsgId.values()) {
			if (msgId.name().endsWith("Cm")) {
				mapByMsgId.put(msgId, 0l);
			}
		}
	}
}