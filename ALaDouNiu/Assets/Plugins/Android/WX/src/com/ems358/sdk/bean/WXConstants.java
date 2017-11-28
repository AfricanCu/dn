package com.ems358.sdk.bean;

public class WXConstants {
	// 下面两个配置用于微信登陆
	// // APP_ID,请手动更改
	// public static final String APP_ID = "wxd435112b72ce8334";
	// // APP_KEY,请手动更改
	// public static final String APP_KEY = "363fd5f88543c79c60ce5ba6d462e2d2";

	// APP_ID,请手动更改
	public static String APP_ID = "";
	// APP_KEY,请手动更改
	public static String APP_KEY = "111";

	// API_KEY
	public static final String API_KEY = "111";

	public static class ShowMsgActivity {
		public static final String STitle = "showmsg_title";
		public static final String SMessage = "showmsg_message";
		public static final String BAThumbData = "showmsg_thumb_data";
	}

	// 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用sendReq时传入，由微信终端回传，state字符串长度不能超�?1K
	public static String state;
	// 登陆凭证
	public static WXAccessToken token;
	// 微信个人公开信息
	public static WXUser user;

	// 微信code 只能使用一次
	public static String code;
}
