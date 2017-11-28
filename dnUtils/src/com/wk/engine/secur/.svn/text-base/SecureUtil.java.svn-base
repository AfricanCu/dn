package com.wk.engine.secur;

import org.json.JSONObject;

import com.wk.user.bean.UserBean;

/**
 * 安全类
 * 
 * @author ems
 *
 */
public class SecureUtil {

	/**
	 * 生成登陆sessionCode
	 * 
	 * <pre>
	 * { 
	 * 	uid:long,
	 * 	time:long
	 * }
	 * </pre>
	 * 
	 * @param user
	 * @return
	 */
	public static String genSessionCode(UserBean user) {
		JSONObject json = new JSONObject();
		json.put("uid", user.getUid());
		json.put("time", System.currentTimeMillis());
		return json.toString();
	}

	/**
	 * 解析登陆sessionCode
	 * 
	 * <pre>
	 * { 
	 * 	uid:long,
	 * 	time:long
	 * }
	 * </pre>
	 */

	public static JSONObject explainSessionCode(String code) {
		JSONObject json = new JSONObject(code);
		return json;
	}

	/**
	 * 生成服务器列表 sessionCode
	 * 
	 * <pre>
	 * { 
	 * 	uid:long,
	 * 	accessTime:long,
	 * 	refreshTime:long
	 * }
	 * </pre>
	 * 
	 * @param user
	 * @return
	 */
	public static String genServerListSessionCode(UserBean user) {
		JSONObject json = new JSONObject();
		json.put("uid", user.getUid());
		json.put("accessTime", user.getAccessTime().getTime());
		json.put("refreshTime", user.getRefreshTime().getTime());
		return json.toString();
	}

	/**
	 * 解析sessionCode
	 * 
	 * <pre>
	 * { 
	 * 	uid:long,
	 * 	accessTime:long,
	 * 	refreshTime:long
	 * }
	 * </pre>
	 * 
	 * @param code
	 * @return
	 */
	public static JSONObject explainServerListSessionCode(String code) {
		JSONObject json = new JSONObject(code);
		return json;
	}

	/**
	 * 生成loginTime
	 * 
	 * <pre>
	 * String.valueOf(System.currentTimeMillis())
	 * </pre>
	 * 
	 * @return
	 */
	public static String genLoginTime() {
		return String.valueOf(System.currentTimeMillis());
	}
}
