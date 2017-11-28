package com.wk.gm.db.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.wk.db.IbatisDbServer;
import com.wk.gm.bean.AddDiamondBean;

public class AddDiamondDao {

	/**
	 * 查询加钻记录
	 * 
	 * @param sqlMapClient
	 * @param proxy
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public static List<AddDiamondBean> queryAddDiamond(String proxy,
			String beginTime, String endTime) throws SQLException {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("proxy", proxy);
		hashMap.put("beginTime", beginTime);
		hashMap.put("endTime", endTime);
		List<AddDiamondBean> list = IbatisDbServer.getGmSqlMapper()
				.queryForList("addDiamond.queryAddDiamond", hashMap);
		return list;
	}

	/***
	 * 插入加钻记录
	 * 
	 * @param sqlMapClient
	 * @param addDiamond
	 * @return
	 * @throws SQLException
	 */
	public static Object insert(AddDiamondBean addDiamond) throws SQLException {
		return IbatisDbServer.getGmSqlMapper().insert(
				"addDiamond.insertAddDiamond", addDiamond);
	}

}
