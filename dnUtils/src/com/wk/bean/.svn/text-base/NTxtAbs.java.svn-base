package com.wk.bean;

import com.jery.ngsp.server.log.LoggerService;

import msg.InnerMessage.GsLoginBusHttpbk;

/**
 * 提示信息
 * 
 * @author ems
 *
 */
public class NTxtAbs {
	public static final byte[] GSLOGIN_BUS_SUCCESS = GsLoginBusHttpbk
			.newBuilder().setCode(NTxtAbs.SUCCESS).build().toByteArray();
	/**
	 * fail
	 */
	public static final int FAIL = 0;
	/**
	 * ok
	 */
	public static final int SUCCESS = 1;

	/** 需要切服务器 **/
	public static final int SERVER_NEED_SWITCH = 2;
	/**
	 * 普通错误
	 */
	public static final int COMMON_ERROR = 3;
	/**
	 * 操作太频繁
	 */
	public static final int OPERATION_TOO_BUSY = 4;
	/**
	 * 异常
	 */
	public static final int EXCEPTION = 5;
	/**
	 * 异常
	 */
	public static final int SQL_EXCEPTION = 6;
	/**
	 * 验证码错误
	 */
	public static final int LOGIN_SESSION_CODE_ERROR = 7;
	/**
	 * 空了
	 */
	public static final int EMPTY = 10000;
	/**
	 * 脏词
	 */
	public static final int DIRTY_WORD = 10001;
	/**
	 * 敏感词
	 */
	public static final int SENSITIVE_WORD = 10002;
	/**
	 * 切服错误
	 */
	public static final int SW_SERVER_ERROR = 10003;
	/**
	 * 超时处理
	 */
	public static final int OVER_TIME = 10004;
	/**
	 * 
	 */
	public static final int access_token_Empty = 10010;
	public static final int openid_EMPTY = 10011;
	/**
	 * 失效
	 */
	public static final int INVALID = 10012;
	/**
	 * 命名错误
	 */
	public static final int NAME_ERROR = 10013;
	/**
	 * 名字长度错误
	 */
	public static final int NAME_LENGTH_ERROR = 10014;
	/**
	 * 名字重复
	 */
	public static final int NAME_REPEAT = 10015;
	/**
	 * 不能操作在线玩家的拷贝
	 */
	public static final int COPY_USER_CAN_NOT_COPY_ONLINE = 10016;
	/**
	 * 账号在另一个服务器登录，已经踢掉下线了，请重新登陆
	 */
	public static final int ACCOUNT_KICK_USER_ON_LINE = 10017;
	/**
	 * 异步处理未初始化
	 */
	public static final int CONTINUATION_NOT_INITIAL = 10018;
	/**
	 * 异步处理超时
	 * 
	 */
	public static final int CONTINUATION_TIMEOUT = 10019;
	/**
	 * 平台类型为空
	 */
	public static final int PLATFORM_TYPE_EMPTY = 10020;
	/**
	 * 找不到合适的服务器
	 */
	public static final int BEST_SERVER_LIST_EMPTY = 10021;
	/**
	 * 设备ID为空
	 */
	public static final int DEVICE_ID_EMPTY = 10022;
	/**
	 * 找不到玩家
	 */
	public static final int USER_DATA_EMPTY = 10023;
	/**
	 * 登录凭证为空
	 */
	public static final int CODE_EMPTY = 10024;
	/**
	 * 微信验证返回为空
	 */
	public static final int ACCESS_TOKEN_EMPTY = 10025;
	/**
	 * 微信玩家信息获取为空
	 */
	public static final int USER_INFO_EMPTY = 10026;
	/**
	 * 设备ID长度太大
	 */
	public static final int DEVICE_ID_TOO_LONG = 10027;
	/**
	 * 已经包含在踢人队列中
	 */
	public static final int KICK_PUT_CONTAINS = 10028;
	/**
	 * 已经失去服务器连接，无法踢人
	 */
	public static final int KICK_NOT_CONNECTED = 10029;
	/**
	 * 充值放入重复的订单号
	 */
	public static final int CHARGE_PUT_CONTAINS = 10030;
	/**
	 * 充值连不上gs
	 */
	public static final int CHARGE_NOT_CONNECTED = 10031;
	/** 找不到切换服务器 */
	public static final int SW_EMPTY = 10032;
	/** 找不到服务器 **/
	public static final int SERVER_ID_NOT_FOUND = 10033;
	/** 信息填写不足 **/
	public static final int INFO_ERROR = 10034;
	/*** gs登录bus服务器模板为空 **/
	public static final byte[] GSLOGINBUS_SERVER_TEMPLATE_EMPTY = GsLoginBusHttpbk
			.newBuilder().setCode(10035).build().toByteArray();
	/** sql注入 ***/
	public static final int SQL_CONTAINS_HIT = 10036;
	/** 找不到商品 **/
	public static final int SHOP_DIAMOND_TEMPLATE_NOT_FOUND = 10037;
	/** 查询订单号找不到 **/
	public static final int QUERY_ORDER_ID_NOT_FOUND = 10038;
	/*** 找不到代理用户 ***/
	public static final int PROXY_USER_NOT_FOUND = 10039;
	/*** 代理钻石不足 ***/
	public static final int PROXY_DIAMOND_NOT_ENOUGH = 10040;
	/*** 你的账号已经被封，联系GM解封 ***/
	public static final int YOUR_ACCOUNT_COLD = 10041;
	/** 区域类型为空 */
	public static final int DISTRICT_TYPE_EMPTY = 10042;

	/**
	 * 打印错误信息
	 * 
	 * 打印上一层路径
	 * 
	 * @param string
	 */
	public static void println(Object string) {
		String format = String.format("%s -%s",
				new Exception().getStackTrace()[1], string);
		System.err.println(format);
		LoggerService.getLogicLog().error(format);
	}

	/**
	 * 打印警告信息
	 * 
	 * @param warn
	 * @param upLevel
	 *            打印上几层路径
	 */
	public static void warn(Object warn, int upLevel) {
		String format = String.format("%s -%s",
				new Exception().getStackTrace()[upLevel], warn);
		System.err.println(format);
	}

}
