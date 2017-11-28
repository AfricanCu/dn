package com.wk.gm.db.dao;

import java.sql.SQLException;
import java.util.HashMap;

import com.wk.gm.db.GuestIbatisDbServer;

public class StatisticsDao {
	/**
	 * 总注册人数
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static String registerTotal(int diamond) throws SQLException {
		return (String) GuestIbatisDbServer.getSqlMapper().queryForObject(
				"statistics.registerTotal", diamond);
	}

	/**
	 * 注册统计
	 * 
	 * @param typeStr
	 * @param hashMap
	 * @return
	 * @throws SQLException
	 */
	public static String registerStat(String typeStr,
			HashMap<String, Object> hashMap) throws SQLException {
		return (String) GuestIbatisDbServer.getSqlMapper().queryForObject(
				typeStr, hashMap);
	}
}
