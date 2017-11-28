/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wk.engine.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import com.google.protobuf.MessageLiteOrBuilder;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.logic.enm.MsgId;

/**
 * 消息管理器
 *
 * 发送消息
 *
 * @author lixing
 */
public class MessageManager {

	/**
	 * 发送
	 * 
	 * 组装为{@link IoMessage}
	 * 
	 * @param channel
	 * @param msgId
	 *            消息id
	 * @param bytes
	 *            protobuf 对象,将获取它的byte[]
	 */
	public static void sendMessage(Channel channel, final MsgId msgId,
			byte[] bytes) {
		if (channel != null && channel.isActive() && msgId != null
				&& bytes != null) {
			try {
				IoMessage message = new IoMessage(msgId, bytes);
				ChannelFuture flush = channel.write(message);
				if (ServerConfigAbs.isMonitorMessage()) {
					flush.addListener(new GenericFutureListener<Future<? super Void>>() {
						@Override
						public void operationComplete(
								Future<? super Void> future) throws Exception {
							//if (!msgId.name().endsWith("Cast"))
								LoggerService.getPlatformLog().error(
										"send -- msgId:{} {} {}",
										new Object[] {
												msgId,
												msgId.getType(),
												future.isSuccess()
														&& future.isDone() });
						}
					});
				}
				channel.flush();
			} catch (Exception ex) {
				LoggerService.getPlatformLog().error(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * 发送
	 * 
	 * 组装为{@link IoMessage}
	 * 
	 * @param channel
	 * @param msgId
	 *            消息id
	 * @param liteorBuilder
	 *            protobuf 对象,将获取它的byte[]
	 */
	public static void sendMessage(Channel channel, final MsgId msgId,
			MessageLiteOrBuilder liteorBuilder) {
		if (channel != null && channel.isActive() && msgId != null
				&& liteorBuilder != null) {
			try {
				IoMessage message = new IoMessage(msgId, liteorBuilder);
				ChannelFuture flush = channel.write(message);
				if (ServerConfigAbs.isMonitorMessage()) {
					flush.addListener(new GenericFutureListener<Future<? super Void>>() {
						@Override
						public void operationComplete(
								Future<? super Void> future) throws Exception {
							if (!msgId.name().endsWith("Cast"))
								LoggerService.getPlatformLog().error(
										"send -- msgId:{} {} {}",
										new Object[] {
												msgId,
												msgId.getType(),
												future.isSuccess()
														&& future.isDone() });
						}
					});
				}
				channel.flush();
			} catch (Exception ex) {
				LoggerService.getPlatformLog().error(ex.getMessage(), ex);
			}
		}
	}

	public static void sendMessage(int count, Channel channel,
			final MsgId msgId, MessageLiteOrBuilder liteorBuilder) {
		if (channel != null && channel.isActive() && msgId != null
				&& liteorBuilder != null) {
			try {
				IoMessage message = new IoMessage(msgId, liteorBuilder);
				for (int i = 0; i < count; i++) {
					ChannelFuture future = channel.write(message);
					if (ServerConfigAbs.isMonitorMessage()) {
						future.addListener(new GenericFutureListener<Future<? super Void>>() {
							@Override
							public void operationComplete(
									Future<? super Void> future)
									throws Exception {
								if (!msgId.name().endsWith("Cast"))
									LoggerService
											.getPlatformLog()
											.error("send -- msgId:{} {} {}",
													new Object[] {
															msgId,
															msgId.getType(),
															future.isSuccess()
																	&& future
																			.isDone() });
							}
						});
					}
				}
				channel.flush();
			} catch (Exception ex) {
				LoggerService.getPlatformLog().error(ex.getMessage(), ex);
			}
		}
	}

}
