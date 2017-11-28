package com.wk.engine.net.I;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.engine.config.ServerConfigAbs;

/**
 * 附加信息抽象
 * 
 * @author ems
 *
 */
public abstract class ChannelAttachmentAbs {
	protected final Channel channel;
	protected final AtomicInteger messageNum = new AtomicInteger(0);
	protected CsConnectI<?> csConnectI;
	/**
	 * [消息id,收到的时间]
	 */
	protected final HashMap<MsgIdI, Long> mapByMsgId = new HashMap<>();

	public ChannelAttachmentAbs(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public CsConnectI<?> getCsConnectI() {
		return csConnectI;
	}

	/**
	 * 设置附加信息
	 * 
	 * @param csConnectI
	 */
	public void setCsConnectI(CsConnectI<?> csConnectI) {
		this.csConnectI = csConnectI;
	}

	public int incrementAndGetMessageNum() {
		return this.messageNum.incrementAndGet();
	}

	public int decrementAndGetMessageNum() {
		return this.messageNum.decrementAndGet();
	}

	public int getMessageNum() {
		return this.messageNum.get();
	}

	/**
	 * 记录消息
	 * 
	 * @param msgId
	 * @return 消息发送太频繁
	 */
	public int recordMsg(MsgIdI msgId) {
		Long time = this.mapByMsgId.get(msgId);
		if (time == null) {
			LoggerService.getLogicLog().error("消息名错误！{}", msgId.name());
			return NTxtAbs.COMMON_ERROR;
		}
		long currentTimeMillis = System.currentTimeMillis();
		this.mapByMsgId.put(msgId, currentTimeMillis);
		long millis = currentTimeMillis - time;
		if (millis < msgId.getPeriod()) {
			LoggerService.getLogicLog().error("消息发送太频繁！{} {} < {}",
					new Object[] { msgId.name(), millis, msgId.getPeriod() });
			if (!ServerConfigAbs.isDebug())
				return NTxtAbs.OPERATION_TOO_BUSY;
		}
		return NTxtAbs.SUCCESS;
	}

}
