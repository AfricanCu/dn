package com.ems358.sdk.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ems358.sdk.bean.WXAccessToken;
import com.ems358.sdk.bean.WXUser;

public class ParseJson {

	public static WXAccessToken getAccessToken(String result) {
		try {
			JSONObject jb = new JSONObject(result);
			String access_token = jb.getString("access_token");
			String expires_in = jb.getString("expires_in");
			String refresh_token = jb.getString("refresh_token");
			String openid = jb.getString("openid");
			String scope = jb.getString("scope");
			WXAccessToken token = new WXAccessToken(access_token, expires_in,
					refresh_token, openid, scope);
			return token;
		} catch (Exception e) {
			Log.e("test", e.getMessage().toString());
			return null;
		}
	}

	public static WXUser getWXUserINfo(String result) {
		try {
			L.i("开始解析");
			JSONObject jb = new JSONObject(result);
			String openid = jb.getString("openid");
			String nickname = jb.getString("nickname");
			String headimgurl = jb.getString("headimgurl");
			int sex = jb.getInt("sex");
			String city = jb.getString("city");
			String country = jb.getString("country");
			String unionid = jb.getString("unionid");
			WXUser info = new WXUser(openid, nickname, headimgurl, sex, city,
					country, unionid);
			L.i("开始解析完成");
			return info;
		} catch (Exception e) {
			Log.e("test", e.getMessage().toString());
			return null;
		}
	}

	public static String getTranId(String result) {
		try {
			JSONObject jb = new JSONObject(result);
			int code = jb.getInt("code");
			if (code == 0) {
				String order_id = jb.getString("order_id");
				return order_id;
			} else {
				return "";
			}
		} catch (Exception e) {
			Log.e("test", e.getMessage().toString());
			return "";
		}
	}

	public static HashMap<String, Object> parseOrderInfo(String json) {
		try {
			JSONObject jb = new JSONObject(json);
			HashMap<String, Object> map = new HashMap<String, Object>();
			ParseJson.JsonObject2HashMap(jb, map);
			L.i("test", map.toString());
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void JsonObject2HashMaps(JSONObject jo,
			List<Map<?, ?>> rstList) {
		for (Iterator<String> keys = jo.keys(); keys.hasNext();) {
			try {
				String key1 = keys.next();
				System.out.println("key1---" + key1 + "------" + jo.get(key1)
						+ (jo.get(key1) instanceof JSONObject) + jo.get(key1)
						+ (jo.get(key1) instanceof JSONArray));
				if (jo.get(key1) instanceof JSONObject) {

					JsonObject2HashMaps((JSONObject) jo.get(key1), rstList);
					continue;
				}
				if (jo.get(key1) instanceof JSONArray) {
					JsonArray2HashMap((JSONArray) jo.get(key1), rstList);
					continue;
				}
				System.out.println("key1:" + key1 + "----------jo.get(key1):"
						+ jo.get(key1));
				json2HashMaps(key1, jo.get(key1), rstList);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	public static void JsonObject2HashMap(JSONObject jo, Map<String, Object> rst) {
		for (Iterator<String> keys = jo.keys(); keys.hasNext();) {
			try {
				String key1 = keys.next();
				System.out.println("key1---" + key1 + "------" + jo.get(key1)
						+ (jo.get(key1) instanceof JSONObject) + jo.get(key1)
						+ (jo.get(key1) instanceof JSONArray));
				System.out.println("key1:" + key1 + "----------jo.get(key1):"
						+ jo.get(key1));
				json2HashMap(key1, jo.get(key1), rst);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	public static void JsonArray2HashMap(JSONArray joArr,
			List<Map<?, ?>> rstList) {
		for (int i = 0; i < joArr.length(); i++) {
			try {
				if (joArr.get(i) instanceof JSONObject) {

					JsonObject2HashMaps((JSONObject) joArr.get(i), rstList);
					continue;
				}
				if (joArr.get(i) instanceof JSONArray) {

					JsonArray2HashMap((JSONArray) joArr.get(i), rstList);
					continue;
				}
				System.out.println("Excepton~~~~~");

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	public static void json2HashMaps(String key, Object value,
			List<Map<?, ?>> rstList) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(key, value);
		rstList.add(map);
	}

	public static void json2HashMap(String key, Object value,
			Map<String, Object> rst) {
		rst.put(key, value);
	}
}
