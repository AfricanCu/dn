package com.wk.gm.db.dao;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wk.db.IbatisDbServer;
import com.wk.gm.bean.AgencyBroadcast;
public class AgencyBroadcastDao {

	/**
	 * 查询代理的个人广播
	 */
	public static List<AgencyBroadcast> queryBroadcast(int agency ,int page)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("page", page);
		List<AgencyBroadcast> list=IbatisDbServer.getGmSqlMapper().queryForList("agencyBroadcast.queryBroadcast", map);
		return list;
	}
	
	/**
	 * 查询代理的玩家列表
	 */
	public static List<AgencyBroadcast> queryPlayer(int agency,int page)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("page", page);
		List<AgencyBroadcast> list=IbatisDbServer.getGmSqlMapper().queryForList("agencyBroadcast.queryPlayer", map);
		return list;
	}
	
	/**
	 * 我的玩家个数
	 */
	public static int myPlayer(int agency)throws SQLException{
		Object object=IbatisDbServer.getGmSqlMapper().queryForObject("agencyBroadcast.myPlayer", agency);
		int count=0;
		if(object!=null){
			count=(int) object;
		}
		return count;
	}
	
	/**
	 * 代理的直充玩家个数
	 */
	public static int qbPlayerCount(int agency)throws SQLException{
		Object object=IbatisDbServer.getGmSqlMapper().queryForObject("agencyBroadcast.qbPlayerCount", agency);
		int count=0;
		if(object!=null){
			count=(int) object;
		}
		return count;
	}
	
	/**
	 * 查询代理的旗下代理列表
	 */
	public static List<AgencyBroadcast> queryAgency(int parent,int page)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("parent", parent);
		map.put("page", page);
		List<AgencyBroadcast> list=IbatisDbServer.getGmSqlMapper().queryForList("agencyBroadcast.queryAgency", map);
		return list;
	}
	
	/**
	 * 查询房卡记录
	 */
	public static List<AgencyBroadcast> queryCardRecords(int agency ,int page)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("page", page);
		List<AgencyBroadcast> list=IbatisDbServer.getGmSqlMapper().queryForList("agencyBroadcast.queryCardRecords", map);
		return list;
	}
	
	/**
	 * 插入个人广播
	 */
	public static void  insertBroadcast(AgencyBroadcast agencyBroadcast)throws SQLException{
		IbatisDbServer.getGmSqlMapper().insert("agencyBroadcast.insertBroadcast", agencyBroadcast);
	}
}
