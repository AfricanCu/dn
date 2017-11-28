package com.ems358.sdk;


public class SCG {
	// 没有选择任何平台，警告：platform和payway 不能同时为空
	public static final int PLATFORM_NONE = 0;
	// 微信平台
	public static final int PLATFORM_WX = 1;
	// QQ平台
	public static final int PLATFORM_QQ = 2;
	// 微博平台
	public static final int PLATFORM_WB = 3;
	// 支付宝平台
	public static final int PLATFORM_ALI = 4;
	// 易宝平台
	public static final int PLATFORM_YB = 5;

	// 取消请求
	public static final int ReqState_cancel = -2;
	// 请求失败
	public static final int ReqState_faild = -1;
	// 请求成功
	public static final int ReqState_ok = 0;
	// 请求等待中
	public static final int ReqState_wait = 2;

	// 以弹框支付的方式
	public static final int Pay_Dialog = 1;
	// 以界面支付的方式
	public static final int Pay_Activity = 2;
	// 直接调用支付的方式
	public static final int Pay_None= 3;

	// 是否集成微信支付
	public static boolean useWxpay = true;
	// 是否集成支付宝支付
	public static boolean useAlipay = true;
	// 是否集成易宝支付
	public static boolean useYbpay = true;

}
