package com.wk.engine.inner.gstogs;

import java.util.concurrent.ConcurrentHashMap;

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;

import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.CheckTask;
import com.wk.engine.inner.GsConnect;
import com.wk.engine.inner.GsSysModule;
import com.wk.engine.net.InnerMsgId;
import com.wk.server.logic.friend.FindUserHandlerI;
import com.wk.server.logic.friend.HandlerResultI;
import com.wk.server.logic.guild.FindGuildHandlerI;
import com.wk.server.logic.guild.FindMemberHandlerI;
import com.wk.util.IntegerIdGenerator;
import com.wk.util.LongIdGenerator;

/**
 * 
 * gs1与gs2之间交互
 * 
 * @author ems
 *
 * @param <SEND>
 *            发送请求
 * @param <BAK>
 *            返回结果
 */
public abstract class GsToGsHandler<SEND extends MessageLite, BAK extends MessageLite> {
	private static final IntegerIdGenerator idGen = IntegerIdGenerator
			.createIdGenerator(0);

	public int genReqId() {
		return idGen.generate();
	}

	/**
	 * 拷贝玩家数据等待消息返回队列
	 * 
	 * [玩家id,deferred]
	 */
	protected final ConcurrentHashMap<Long, CheckTask<BAK, Object, Object>> deferredMap = new ConcurrentHashMap<>();

	/**
	 * 请求处理
	 * 
	 * @param messageLite
	 *            请求的消息
	 * @param gs
	 * @return 返回结果消息
	 */
	public abstract BAK requestSwServerProcess(SEND messageLite, GsConnect gs);

	/**
	 * 收到 gs 发来切换处理消息
	 * 
	 * @param genMessageLite
	 * @param gs
	 * @return
	 */
	public abstract int swServerProcess(SEND genMessageLite, GsConnect gs);

	/**
	 * 结果消息处理
	 * 
	 * @param messageLitebk
	 *            结果消息
	 * @param gs
	 */
	public abstract void responseSwServerProcess(BAK messageLitebk, GsConnect gs);

	// 反射影响性能？
	// {
	// Class<? extends MessageLite> clazz = messageLitebk.getClass();
	// try {
	// Method method = clazz.getMethod("getUsername");
	// String username = (String) method.invoke(messageLitebk);
	// responseResolve(username, messageLitebk);
	// } catch (NoSuchMethodException | SecurityException
	// | IllegalAccessException | IllegalArgumentException
	// | InvocationTargetException e) {
	// LoggerService.getLogicLog().error(e.getMessage(), e);
	// }
	// }
	/**
	 * 
	 * @param username
	 * @param messageLitebk
	 */
	public void responseResolve(long uid, BAK messageLitebk) {
		CheckTask<BAK, Object, Object> check = this.deferredMap.remove(uid);
		if (check == null) {
			LoggerService.getLogicLog().error("deferredMap empty:{}", uid);
			return;
		}
		check.resolve(messageLitebk);
	}

	/**
	 * 公会切服处理
	 * 
	 * @param messageLite
	 *            发送的消息
	 * @param reqId
	 *            请求iD
	 * @param serverId
	 *            切的目标GS服务器id
	 * @param handlerI
	 *            异步处理器
	 */
	public void swHandle(final SEND messageLite, long reqId, int serverId,
			HandlerResultI handlerI) {
		DeferredHandleI<BAK> deferredHandleI = getDefaultDeferredHandler(handlerI);
		swHandle(messageLite, reqId, serverId, deferredHandleI);
	}

	private void swHandle(final SEND messageLite, long reqId, int serverId,
			DeferredHandleI<BAK> deferredHandleI) {
		GsConnect gs = GsSysModule.getInstance().getGs(serverId);
		if (gs == null) {
			LoggerService.getLogicLog().error("gs empty! msg:{}",
					messageLite.toString());
			deferredHandleI.notFoundGs();
			return;
		}
		Deferred<BAK, Object, Object> defered = new DeferredObject<>();
		defered.promise().done(deferredHandleI).fail(deferredHandleI);
		this.deferredMap
				.put(reqId, new CheckTask<BAK, Object, Object>(defered));
		gs.sendMessage(InnerMsgId.getEnum(messageLite.getClass()), messageLite);
		LoggerService.getLogicLog().debug("DeferredPut,reqId:{}", reqId);
	}

	/**
	 * 默认的异步处理器
	 * 
	 * @param handlerI
	 * @return
	 */
	public abstract DeferredHandleI<BAK> getDefaultDeferredHandler(
			HandlerResultI handlerI);
}
