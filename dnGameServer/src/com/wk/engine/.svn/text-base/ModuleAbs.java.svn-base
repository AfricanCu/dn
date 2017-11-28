package com.wk.engine;

import io.netty.channel.Channel;

import java.util.List;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.event.EventAbs;
import com.wk.engine.net.IoMessage;
import com.wk.engine.net.MessageManager;
import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.MsgId;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.ibatis.select.User;

/**
 * 逻辑模块抽象类
 * 
 * @author Administrator
 *
 */
public abstract class ModuleAbs<K, V> extends ModuleAbsI<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 获取模块监听事件集合
	 * 
	 * @return
	 */
	public abstract List<EventAbs> getGameEventList();

	public void processMsg(Channel channel, IoMessage message,
			ChannelAttachmentAbs channelAttachment) throws Exception {
		User user = (User) channelAttachment.getCsConnectI();
		byte[] resMsg = null;
		MsgId resMsgId = message.getMsgId().getResMsgId();
		if (channelAttachment.getMessageNum() > SystemConstants.maxMessageNum) {
			resMsg = message.getMsgId().gRErrMsg(
					NTxt.SERVER_MESSAGE_FULL);
			LoggerService.getLogicLog().error("服务器处理消息频繁！{} {}",
					channelAttachment.getMessageNum(),
					SystemConstants.maxMessageNum);
		}
		int recordMsg = channelAttachment.recordMsg(message.getMsgId());
		if (recordMsg != NTxt.SUCCESS) {
			resMsg = message.getMsgId().gRErrMsg(recordMsg);
		}
		if (user == null) {
			if (resMsg == null) {
				try {
					resMsg = processMessage(channel, message);
				} catch (Exception e) {
					LoggerService.getLogicLog().error(e.getMessage(), e);
					resMsg = message.getMsgId().gRErrMsg(
							NTxt.EXCEPTION);
				}
			}
			MessageManager.sendMessage(channel, resMsgId, resMsg);
		} else {
			if (resMsg == null) {
				try {
					resMsg = processMessage(user, message);
				} catch (Exception e) {
					LoggerService.getLogicLog().error(e.getMessage(), e);
					resMsg = message.getMsgId().gRErrMsg(
							NTxt.EXCEPTION);
				}
			}
			user.sendMessage(resMsgId, resMsg);
		}
	}

	/**
	 * 处理消息
	 * 
	 * 针对还没有完成登录的玩家
	 * 
	 * @param channel
	 *            通道
	 * 
	 * @param message
	 * @return 返回的消息
	 */
	public abstract byte[] processMessage(Channel channel, IoMessage message)
			throws Exception;

	/**
	 * 处理消息
	 * 
	 * 针对完成登录的玩家
	 * 
	 * @param user
	 *            玩家
	 * 
	 * @param message
	 * @return 返回的消息
	 */
	public abstract byte[] processMessage(IncomeUserI user, IoMessage message)
			throws Exception;
}
