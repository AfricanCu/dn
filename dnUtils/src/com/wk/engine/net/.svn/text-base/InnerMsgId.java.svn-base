package com.wk.engine.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import msg.InnerMessage;

import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.net.I.MsgIdI;

/**
 * 内部消息id
 * 
 * @author ems
 *
 */
public enum InnerMsgId implements MsgIdI {
	// 自动生成开始
/**用户返回 -1997**/
UserGsToGsbk(-1997,"用户返回",InnerMessage.UserGsToGsbk.getDefaultInstance(),null,10000),
/**用户请求 -1998**/
UserGsToGs(-1998,"用户请求",InnerMessage.UserGsToGs.getDefaultInstance(),UserGsToGsbk,10000),
/**公会返回 -1999**/
GuildGsToGsbk(-1999,"公会返回",InnerMessage.GuildGsToGsbk.getDefaultInstance(),null,10000),
/**公会请求 -2000**/
GuildGsToGs(-2000,"公会请求",InnerMessage.GuildGsToGs.getDefaultInstance(),GuildGsToGsbk,10000),
/**gm返回 -989**/
GmBusToGsbk(-989,"gm返回",InnerMessage.GmBusToGsbk.getDefaultInstance(),null,10000),
/**gm请求 -990**/
GmBusToGs(-990,"gm请求",InnerMessage.GmBusToGs.getDefaultInstance(),GmBusToGsbk,10000),
/**充值返回 -991**/
ChargeBusToGsbk(-991,"充值返回",InnerMessage.ChargeBusToGsbk.getDefaultInstance(),null,10000),
/**充值请求 -992**/
ChargeBusToGs(-992,"充值请求",InnerMessage.ChargeBusToGs.getDefaultInstance(),ChargeBusToGsbk,10000),
/**踢人返回 -993**/
KickBusToGsbk(-993,"踢人返回",InnerMessage.KickBusToGsbk.getDefaultInstance(),null,10000),
/**踢人请求 -994**/
KickBusToGs(-994,"踢人请求",InnerMessage.KickBusToGs.getDefaultInstance(),KickBusToGsbk,10000),
/**服务器列表广播返回 -995**/
ServerListCastBusToGsbk(-995,"服务器列表广播返回",InnerMessage.ServerListCastBusToGsbk.getDefaultInstance(),null,10000),
/**服务器列表广播请求 -996**/
ServerListCastBusToGs(-996,"服务器列表广播请求",InnerMessage.ServerListCastBusToGs.getDefaultInstance(),ServerListCastBusToGsbk,10000),
/**服务器状态返回 -997**/
ServerStatusBusToGsbk(-997,"服务器状态返回",InnerMessage.ServerStatusBusToGsbk.getDefaultInstance(),null,10000),
/**服务器状态请求 -998**/
ServerStatusBusToGs(-998,"服务器状态请求",InnerMessage.ServerStatusBusToGs.getDefaultInstance(),ServerStatusBusToGsbk,10000),
/**gs登陆bus返回 -999**/
GsLoginBusHttpbk(-999,"gs登陆bus返回",InnerMessage.GsLoginBusHttpbk.getDefaultInstance(),null,10000),
/**gs登陆bus -1000**/
GsLoginBusHttp(-1000,"gs登陆bus",InnerMessage.GsLoginBusHttp.getDefaultInstance(),GsLoginBusHttpbk,10000),
// 自动生成结束
	/** 心跳 -1 **/
	HeartBeat(-1, "心跳", null, null, 30000),
	/** 关服返回 -32767 **/
	ShutDownbk(Short.MIN_VALUE + 1, "关服返回", null, null, 10000),
	/** 关服 -32768 **/
	ShutDown(Short.MIN_VALUE, "关服", null, null, 10000);
	private final short type;
	private final String name;
	/** 单位（毫秒） */
	private final long period;
	private final MessageLite defaultInst;
	private final InnerMsgId resMsgId;
	/** setCode方法，返回消息都要求有这个方法 */
	private Method setCodeMethod;

	/**
	 * 
	 * @param type
	 * @param name
	 * @param period
	 *            限制时间间隔
	 * @param clazz
	 * @param resMsgId
	 */
	private InnerMsgId(int type, String name, MessageLite clazz,
			InnerMsgId resMsgId, long period) {
		this.type = (short) type;
		this.name = name;
		this.period = period;
		this.defaultInst = clazz;
		this.resMsgId = resMsgId;
		if (this.resMsgId != null) {
			Class<? extends MessageLite.Builder> clazs = this.resMsgId
					.genMessageLiteBuilder().getClass();
			try {
				this.setCodeMethod = clazs.getMethod("setCode", int.class);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		} else {
			this.setCodeMethod = null;
		}
	}

	public short getType() {
		return type;
	}

	public long getPeriod() {
		return period;
	}

	public String getName() {
		return name;
	}

	public InnerMsgId getResMsgId() {
		return resMsgId;
	}

	public MessageLite getDefaultInst() {
		return this.defaultInst;
	}

	public MessageLite.Builder genMessageLiteBuilder() {
		return defaultInst.newBuilderForType();
	}

	public byte[] gRErrMsg(int code) {
		MessageLite.Builder liteorBuilder = this.resMsgId
				.genMessageLiteBuilder();
		try {
			setCodeMethod.invoke(liteorBuilder, code);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
		}
		return liteorBuilder.build().toByteArray();
	}

	private final static Map<Short, InnerMsgId> typeMap = new HashMap<>();
	private final static Map<Class<? extends MessageLite>, InnerMsgId> clazzMap = new HashMap<>();
	static {
		InnerMsgId[] missionStateArr = InnerMsgId.values();
		for (int i = 0; i < missionStateArr.length; i++) {
			InnerMsgId missionState = missionStateArr[i];
			typeMap.put(missionState.getType(), missionState);
			if (missionState.getDefaultInst() != null)
				clazzMap.put(missionState.getDefaultInst().getClass(),
						missionState);
		}
	}

	/**
	 *
	 * @param type
	 * @return
	 */
	public static InnerMsgId getEnum(short type) {
		return typeMap.get(type);
	}

	/**
	 * 
	 */
	public static InnerMsgId getEnum(Class<? extends MessageLite> type) {
		return clazzMap.get(type);
	}

	public static void targetMe() {
		System.err.println(String.format("%s - 生成成功！",
				new Exception().getStackTrace()[0]));
	}
}
