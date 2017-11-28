package com.wk.I;

import io.netty.buffer.ByteBuf;

/**
 * 序列化接口
 * 
 * @author ems
 *
 */
public interface ByteBufferSerializable {

	/**
	 * 写入（序列化）
	 * 
	 * @param out
	 * @throws Exception
	 */
	public void writeExternal(ByteBuf out) throws Exception;

	/**
	 * 读出（反序列化）
	 * 
	 * @param in
	 * @throws Exception
	 */
	public void readExternal(ByteBuf in) throws Exception;
}
