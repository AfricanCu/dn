package com.wk.guild.bean;

import org.json.JSONObject;

/** 这个不能写成非静态内部类 */
public class GuildJSONObjectEx extends JSONObject {
	protected GuildBean guildBean;
	private final int db_key;

	public GuildJSONObjectEx(GuildBean guildBean, int db_key) {
		super();
		this.guildBean = guildBean;
		this.db_key = db_key;
	}

	public GuildJSONObjectEx(GuildBean guildBean, int db_key, String dbca) {
		super(dbca);
		this.guildBean = guildBean;
		this.db_key = db_key;
	}

	public JSONObject put(String key, Object value)
			throws org.json.JSONException {
		JSONObject put = super.put(key, value);
		if (this.guildBean != null) {// 这里 加个判断才行不然 抛空
			this.guildBean.bitSet.set(this.db_key);
		}
		return put;
	};
}
