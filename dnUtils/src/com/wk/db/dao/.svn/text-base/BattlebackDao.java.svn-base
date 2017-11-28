package com.wk.db.dao;

import java.sql.SQLException;

import com.wk.bean.BattlebackBean;
import com.wk.db.IbatisDbServer;

public class BattlebackDao {

	/**
	 * 查询战斗回放
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static BattlebackBean queryRecord(int id) throws SQLException {
		BattlebackBean friend = (BattlebackBean) IbatisDbServer
				.getGmSqlMapper().queryForObject("battleback.queryBattleback",
						id);
		return friend;
	}

	/**
	 * 插入战斗回放
	 * 
	 * @param battleback
	 * @return
	 * @throws SQLException
	 */
	public static int insertBattleback(BattlebackBean battleback)
			throws SQLException {
		return (Integer) IbatisDbServer.getGmSqlMapper().insert(
				"battleback.insertBattleback", battleback);
	}
}
