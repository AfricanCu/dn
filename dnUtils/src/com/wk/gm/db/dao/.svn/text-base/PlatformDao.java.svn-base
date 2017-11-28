package com.wk.gm.db.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wk.bean.PlatformBean;
import com.wk.db.IbatisDbServer;

public class PlatformDao {

	//添加平台数据
	public static Object insertPlatformData(PlatformBean bean)throws SQLException{
		return IbatisDbServer.getGmSqlMapper().insert("platform.insertPlatformData", bean);
	}
	
	//查询平台总额数据
	public static List<PlatformBean> cosmprehensive(String beginTime,String endTime)throws SQLException{
		Map<String,Object> sqlMap=new HashMap<String, Object>();
		sqlMap.put("beginTime", beginTime);
		sqlMap.put("endTime", endTime);
		List<PlatformBean> beans=IbatisDbServer.getGmSqlMapper().queryForList("platform.cosmprehensive", sqlMap);
		return beans;
	}
}
