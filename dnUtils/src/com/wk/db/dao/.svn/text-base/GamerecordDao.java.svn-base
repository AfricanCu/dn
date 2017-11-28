package com.wk.db.dao;

import java.sql.SQLException;

import com.wk.bean.GamerecordBean;
import com.wk.db.IbatisDbServer;

public class GamerecordDao {
	/**
	 * 查询游戏记录
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static GamerecordBean queryRecord(int id) throws SQLException {
		GamerecordBean friend = (GamerecordBean) IbatisDbServer
				.getGmSqlMapper().queryForObject("gamerecord.queryGamerecord",
						id);
		return friend;
	}

	/**
	 * 删除游戏记录
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static int insertGamerecord(GamerecordBean gamerecord)
			throws SQLException {
		return (Integer) IbatisDbServer.getGmSqlMapper().insert(
				"gamerecord.insertGamerecord", gamerecord);
	}

	/**
	 * 删除游戏记录
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static int deleteGamerecord(int id) throws SQLException {
		return IbatisDbServer.getGmSqlMapper().delete(
				"gamerecord.deleteGamerecord", id);
	}
}
