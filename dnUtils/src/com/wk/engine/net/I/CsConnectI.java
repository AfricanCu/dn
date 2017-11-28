package com.wk.engine.net.I;

import io.netty.channel.Channel;

/**
 * c/s 客户端接口
 * 
 * @author ems
 *
 */
public interface CsConnectI<T extends MsgIdI> {

	/**
	 * 检测客户端连接
	 * 
	 * @return
	 */
	public abstract Channel checkClient();

	/**
	 * 注册客户端
	 * 
	 * @param channel
	 * @throws Exception
	 */
	public abstract void registerClient(Channel channel) throws Exception;

	/**
	 * 注销客户端
	 * 
	 * @param channel
	 */
	public abstract void unregisterClient() throws Exception;

	/**
	 * 发送消息
	 * 
	 * @param msgId
	 * @param bytes
	 */
	public abstract void sendMessage(T msgId, byte[] bytes);
}
