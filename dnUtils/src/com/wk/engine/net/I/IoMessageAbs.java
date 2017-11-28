package com.wk.engine.net.I;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.wk.engine.util.ProtobufUtils;

/**
 * 消息对象 的抽象
 * 
 * @author ems
 *
 */
public abstract class IoMessageAbs<T extends MsgIdI> {

	protected final byte[] msg;
	protected final T msgId;

	/**
	 * 
	 * @param msgId
	 * @param msg
	 * @throws Exception
	 */
	public IoMessageAbs(T msgId, byte[] msg) throws Exception {
		if (msgId == null) {
			throw new Exception("空消息id!");
		}
		if (msg == null) {
			throw new Exception("空消息内容!");
		}
		this.msgId = msgId;
		this.msg = msg;
	}

	/**
	 * 
	 * @param msgId
	 * @param liteorBuilder
	 * @throws Exception
	 */
	public IoMessageAbs(T msgId, MessageLiteOrBuilder liteorBuilder)
			throws Exception {
		if (msgId == null) {
			throw new Exception("空消息id!");
		}
		if (liteorBuilder == null) {
			throw new Exception("空消息内容!");
		}
		this.msgId = msgId;
		if (liteorBuilder instanceof MessageLite) {
			this.msg = ((MessageLite) liteorBuilder).toByteArray();
		} else if (liteorBuilder instanceof MessageLite.Builder) {
			this.msg = ((MessageLite.Builder) liteorBuilder).build()
					.toByteArray();
		} else
			throw new Exception(String.format("错误的类类型：%s",
					liteorBuilder.getClass()));
	}

	/**
	 * 获取消息内容
	 * 
	 * @return
	 */
	public byte[] getMsg() {
		return msg;
	}

	/**
	 * 获取消息id
	 * 
	 * @return
	 */
	public T getMsgId() {
		return this.msgId;
	}

	/**
	 * 获取消息id号
	 * 
	 * @return
	 */
	public short getMsgIdValue() {
		return this.msgId.getType();
	}

	/**
	 * 生成 protobuf 消息对象
	 * 
	 * @return protobuf 消息对象
	 * @throws InvalidProtocolBufferException
	 */
	public MessageLite genMessageLite() throws InvalidProtocolBufferException {
		return ProtobufUtils.getProtobuf(this.getMsg(), this.getMsgId()
				.getDefaultInst());
	}
}
