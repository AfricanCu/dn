package com.wk.guild.bean;

import org.json.JSONArray;
import org.json.JSONException;

/** 这个不能写成非静态内部类 */
public class GuildJSONArrayEx extends JSONArray {
	protected GuildBean guildBean;
	private final int db_key;

	public GuildJSONArrayEx(GuildBean guildBean, int db_key) {
		super();
		this.guildBean = guildBean;
		this.db_key = db_key;
	}

	public GuildJSONArrayEx(GuildBean guildBean, int db_key, String dbca) {
		super(dbca);
		this.guildBean = guildBean;
		this.db_key = db_key;
	}

	@Override
	public JSONArray put(int index, Object value) throws JSONException {
		JSONArray put = super.put(index, value);
		if (this.guildBean != null) {// 这里 加个判断才行不然 抛空
			this.guildBean.bitSet.set(this.db_key);
		}
		return put;
	}

	public JSONArray put(Object value) throws org.json.JSONException {
		JSONArray put = super.put(value);
		if (this.guildBean != null) {// 这里 加个判断才行不然 抛空
			this.guildBean.bitSet.set(this.db_key);
		}
		return put;
	};
}
