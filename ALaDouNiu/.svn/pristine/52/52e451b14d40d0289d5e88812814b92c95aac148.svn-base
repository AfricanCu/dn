package com.ems358.sdk.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.ems358.sdk.SCG;
import com.ems358.sdk.bean.User;
import com.ems358.sdk.bean.WXConstants;

import android.content.Context;

public class BuildJson {

	public static String buildWXUser(int platform, Context context) {
		String userid = null;
		String nickname = null;
		String headimgurl = null;
		try {
			JSONObject jb = new JSONObject();

			switch (platform) {
			case SCG.PLATFORM_WX:
				userid = WXConstants.user.getOpenid();
				nickname = WXConstants.user.getNickname();
				String url = WXConstants.user.getHeadimgurl();
				headimgurl = url.replace("/0", "/96");
				L.i(headimgurl);
				break;
			case SCG.PLATFORM_QQ:

				break;

			case SCG.PLATFORM_WB:

				break;
			default:
				break;
			}
			// 保存用户信息
			User user = new User(userid, nickname, headimgurl, platform);
			AccessTokenKeeper.writeCurrentLoginUser(user, context);
			// 生成json字符串
			jb.put("userid", userid);
			jb.put("nickname", nickname);
			jb.put("headimgurl", headimgurl);
			String jsonStr = jb.toString();
			// Log.i("test",Constants.info.getHeadimgurl());
			// json = json.replace("\\", "");
			return jsonStr;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String buildDefaultUser(String userid) {
		try {
			JSONObject jb = new JSONObject();
			// 生成json字符串
			jb.put("userid", userid);
			jb.put("nickname", "");
			jb.put("headimgurl", "");
			String jsonStr = jb.toString();
			return jsonStr;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
