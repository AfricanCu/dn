package com.wk.gm.db.dao;

import java.sql.SQLException;

import com.wk.db.IbatisDbServer;
import com.wk.gm.bean.MinusCreditsBean;

public class MinusCreditsDao {

	/***
	 * 插入加钻记录
	 * 
	 * @param sqlMapClient
	 * @param minusCreditsBean
	 * @return
	 * @throws SQLException
	 */
	public static Object insert(MinusCreditsBean minusCreditsBean)
			throws SQLException {
		return IbatisDbServer.getGmSqlMapper().insert(
				"minusCredits.insertMinusCredits", minusCreditsBean);
	}

}
