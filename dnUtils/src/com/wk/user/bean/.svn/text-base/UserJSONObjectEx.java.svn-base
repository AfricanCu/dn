package com.wk.user.bean;

import org.json.JSONObject;

/** 这个不能写成非静态内部类 */
public class UserJSONObjectEx extends JSONObject {
	protected UserBean userBean;
	private final int db_key;

	public UserJSONObjectEx(UserBean userBean, int db_key) {
		super();
		this.userBean = userBean;
		this.db_key = db_key;
	}

	public UserJSONObjectEx(UserBean userBean, int db_key, String dbca) {
		super(dbca);
		this.userBean = userBean;
		this.db_key = db_key;
	}

	public JSONObject put(String key, Object value)
			throws org.json.JSONException {
		JSONObject put = super.put(key, value);
		if (this.userBean != null) {// 这里 加个判断才行不然 抛空
			this.userBean.bitSet.set(this.db_key);
		}
		return put;
	};
}
