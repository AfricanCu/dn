package com.wk.engine;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.event.EventManager;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.IoMessage;
import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.util.IbatisUtil;

public class ModuleManager {

	/**
	 * 模块列表
	 */
	private static final ArrayList<ModuleAbs<?, ?>> list = new ArrayList<>();

	/**
	 * 系统模块列表
	 */
	private static final ArrayList<SystemModuleAbs> sysModuleList = new ArrayList<>();

	/**
	 * 加入模块
	 * 
	 * @param array
	 * @throws Exception
	 */
	public static void addSystemModuleAbs(SystemModuleAbs[] array)
			throws Exception {
		for (SystemModuleAbs module : array) {
			IbatisUtil.invoke(module.getClass(), "instance", module);
			sysModuleList.add(module);
			LoggerService.getPlatformLog().warn("初始化---{}---SystemModuleAbs",
					module.getName());
		}
	}

	private static void addModuleI(ModuleAbs<?, ?> module) throws Exception {
		IbatisUtil.invoke(module.getClass(), "instance", module);
		module.init();
		EventManager.getInstance().registerGameEventMonitor(
				module.getGameEventList());
		list.add(module);
		LoggerService.getPlatformLog().warn("加入---{}---模块", module.getName());
	}

	/**
	 * 加入模块
	 * 
	 * @param array
	 * @throws Exception
	 */
	public static void addModuleI(ModuleAbs<?, ?>[] array) throws Exception {
		for (ModuleAbs<?, ?> moduleAbs : array) {
			addModuleI(moduleAbs);
		}
	}

	/**
	 * 处理内部消息
	 * 
	 * @param channel
	 * @param message
	 */
	public static void processInnerMsg(Channel channel, InnerIoMessage message) {
		SystemModuleAbs moduleITmp = null;
		for (SystemModuleAbs moduleI : sysModuleList) {
			if (moduleI.getUpperMsgId() >= message.getMsgIdValue()
					&& moduleI.getDownerMsgId() <= message.getMsgIdValue()) {
				moduleITmp = moduleI;
				break;
			}
		}
		ChannelAttachmentAbs channelAttachment = channel.attr(
				SystemConstants.CHANNEL_ATTR_KEY).get();
		if (moduleITmp != null) {
			try {
				moduleITmp.processMsg(channel, message, channelAttachment);
			} catch (Exception e) {
				LoggerService.getLogicLog().error(e.getMessage(), e);
			}
		} else {
			LoggerService.getLogicLog().error("未处理内部消息id：！！！！！{}",
					message.getMsgId());
		}
	}

	/**
	 * 处理客户端消息
	 * 
	 * @param channel
	 * @param message
	 * @throws Exception
	 */
	public static void processMsg(Channel channel, IoMessage message) {
		ModuleAbs<?, ?> moduleITmp = null;
		for (ModuleAbs<?, ?> moduleI : list) {
			if (moduleI.getUpperMsgId() >= message.getMsgIdValue()
					&& moduleI.getDownerMsgId() <= message.getMsgIdValue()) {
				moduleITmp = moduleI;
				break;
			}
		}
		ChannelAttachmentAbs channelAttachment = channel.attr(
				SystemConstants.CHANNEL_ATTR_KEY).get();
		try {
			if (moduleITmp != null) {
				try {
					moduleITmp.processMsg(channel, message, channelAttachment);
				} catch (Exception e) {
					LoggerService.getLogicLog().error(e.getMessage(), e);
				}
			} else {
				LoggerService.getLogicLog().error("未处理消息!msgId：{}",
						message.getMsgId());
			}
		} finally {
			channelAttachment.decrementAndGetMessageNum();
		}
	}

	public static List<ModuleAbs<?, ?>> getModuleList() {
		return list;
	}
}
