package com.wk.engine.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfigAbs;

/**
 * 内部消息管理器
 * 
 * @author ems
 *
 */
public class InnerMessageManager {
	/**
	 * 发送内部消息
	 * 
	 * 组装为{@link InnerMsgId}
	 * 
	 * @param channel
	 * @param msgId
	 *            消息id
	 * @param bytes
	 *            protobuf 对象,将获取它的byte[]
	 */
	public static void sendInnerMessage(Channel channel,
			final InnerMsgId msgId, byte[] bytes) {
		if (channel != null && msgId != null && bytes != null) {
			try {
				InnerIoMessage message = new InnerIoMessage(msgId, bytes);
				ChannelFuture future = channel.write(message);
				if (ServerConfigAbs.isMonitorMessage()) {
					future.addListener(new GenericFutureListener<Future<? super Void>>() {
						@Override
						public void operationComplete(
								Future<? super Void> future) throws Exception {
							LoggerService.getPlatformLog().error(
									"send InnerMsg:{} {}", msgId.name(),
									future.isSuccess());
						}
					});
				}
				channel.flush();
			} catch (Exception ex) {
				LoggerService.getPlatformLog().error(ex.getMessage(), ex);
			}
		}
	}
}
