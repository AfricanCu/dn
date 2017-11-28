package com.wk.engine.inner.gstogs;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import com.google.protobuf.MessageLite;
import com.wk.server.ibatis.select.IncomeUserI;

/**
 * 异步处理接口
 * 
 * @author ems
 *
 * @param <BAK>
 */
public abstract class DeferredHandleI<BAK extends MessageLite> implements
		DoneCallback<BAK>, FailCallback<Object> {

	/**
	 * 连接不上gs的处理
	 */
	public abstract void notFoundGs();

}