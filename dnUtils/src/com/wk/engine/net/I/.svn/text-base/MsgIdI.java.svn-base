package com.wk.engine.net.I;

import java.lang.reflect.InvocationTargetException;

import com.google.protobuf.MessageLite;

/**
 * 消息id接口
 * 
 * @author ems
 *
 */
public interface MsgIdI {
	/**
	 * Returns the name of this enum constant, exactly as declared in its enum
	 * declaration.
	 *
	 * <b>Most programmers should use the {@link #toString} method in preference
	 * to this one, as the toString method may return a more user-friendly
	 * name.</b> This method is designed primarily for use in specialized
	 * situations where correctness depends on getting the exact name, which
	 * will not vary from release to release.
	 *
	 * @return the name of this enum constant
	 */
	public String name();

	/**
	 * 消息id
	 * 
	 * @return
	 */
	public short getType();

	/**
	 * 消息收发间隔
	 * 
	 * @return
	 */
	public long getPeriod();

	/**
	 * 消息名
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 获取消息对应的 protobuf 默认实例
	 * 
	 * @return
	 */
	public MessageLite getDefaultInst();

	/**
	 * 获取返回 消息id
	 * 
	 * @return
	 */
	public MsgIdI getResMsgId();

	/**
	 * 生成消息对应 protobuf.builder对象
	 * 
	 * @return
	 */
	public MessageLite.Builder genMessageLiteBuilder();

	/**
	 * 生成返回错误消息
	 * 
	 * @param code
	 *            错误码
	 * @return
	 */
	public byte[] gRErrMsg(int code) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;
}
