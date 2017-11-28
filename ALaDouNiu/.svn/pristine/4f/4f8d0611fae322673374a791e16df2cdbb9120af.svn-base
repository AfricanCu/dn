/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ems358.sdk.util;

import com.ems358.sdk.bean.User;
import com.ems358.sdk.bean.WXAccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 该类定义了微博授权时所需要的参数。
 * 
 * @author SINA
 * @since 2013-10-07
 */
public class AccessTokenKeeper {
	// APP缓存参数
	public static final String PREFERENCES_NAME = "com_weibo_sdk_android";

	public static final String PREFERENCES_NICKNAME = "nickname";
	public static final String PREFERENCES_HEADEIMG = "headimg";
	public static final String PREFERENCES_USERID = "userid";

	// 微信token参数
	private static final String WX_OPENID = "openid";
	private static final String WX_ACCESS_TOKEN = "access_token";
	private static final String WX_EXPIRES_IN = "expires_in";
	private static final String WX_REFRESH_TOKEN = "refresh_token";
	private static final String WX_SCOPE = "scope";

	// 记录上次登陆的方式
	public static final String PLATFORM_LAST_LOGIN = "platform";

	/**
	 * 保存当前登陆平台信息以及个人信息
	 * 
	 * @param platform
	 * @param context
	 * @param token
	 */
	public static void writeCurrentLoginUser(User user, Context context) {
		if (null == context) {
			return;
		}
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(PREFERENCES_NICKNAME, user.getNickname());
		editor.putString(PREFERENCES_HEADEIMG, user.getHeadimgurl());
		editor.putString(PREFERENCES_USERID, user.getUserid());
		editor.putInt(PLATFORM_LAST_LOGIN, user.getPlatform());
		editor.commit();
	}

	/**
	 * 读取上次登陆平台信息以及个人信息
	 * 
	 * @param context
	 * @return
	 */
	public static User readLastLoginUser(Context context) {
		if (null == context) {
			return null;
		}
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		User user = new User();
		user.setNickname(pref.getString(PREFERENCES_NICKNAME, ""));
		user.setHeadimgurl(pref.getString(PREFERENCES_HEADEIMG, ""));
		user.setUserid(pref.getString(PREFERENCES_USERID, ""));
		user.setPlatform(pref.getInt(PLATFORM_LAST_LOGIN, 0));
		return user;
	}

	/**
	 * 保存 Token 对象到 SharedPreferences。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 * @param token
	 *            Token 对象
	 */
	public static void writeWXAccessToken(Context context, WXAccessToken token) {
		if (null == context || null == token) {
			return;
		}
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(WX_OPENID, token.getOpenid());
		editor.putString(WX_ACCESS_TOKEN, token.getAccess_token());
		editor.putString(WX_REFRESH_TOKEN, token.getRefresh_token());
		editor.putString(WX_EXPIRES_IN, token.getExpires_in());
		editor.putString(WX_SCOPE, token.getScope());
		// editor.putInt(PLATFORM_LAST_LOGIN, MyHolder.WX);
		editor.commit();
	}

	/**
	 * 从 SharedPreferences 读取 Token 信息。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 * 
	 * @return 返回 Token 对象
	 */
	public static WXAccessToken readWXAccessToken(Context context) {
		if (null == context) {
			return null;
		}

		WXAccessToken token = new WXAccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		token.setOpenid(pref.getString(WX_OPENID, ""));
		token.setAccess_token(pref.getString(WX_ACCESS_TOKEN, ""));
		token.setRefresh_token(pref.getString(WX_REFRESH_TOKEN, ""));
		token.setExpires_in(pref.getString(WX_EXPIRES_IN, ""));
		token.setScope(pref.getString(WX_SCOPE, ""));
		// Log.i("test",pref.getString(WX_REFRESH_TOKEN, ""));
		return token;
	}
	
	
	/**
	 * 其他平台的数据保存参考上面的微信
	 */
	

	/**
	 * 清空 SharedPreferences 中 Token信息。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 */
	public static void clear(Context context) {
		if (null == context) {
			return;
		}

		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
}
