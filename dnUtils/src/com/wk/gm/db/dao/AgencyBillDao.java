package com.wk.gm.db.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wk.db.IbatisDbServer;
import com.wk.gm.bean.AgencyBillBean;

public class AgencyBillDao {
	
	/**
	 * 插入代理返佣数据
	 * @param bean
	 * @throws SQLException 
	 */
	public static void insertAgencyBill(AgencyBillBean bean) throws SQLException{
		IbatisDbServer.getGmSqlMapper().insert("agencyBill.insertAgencyBill", bean);
	}
	
	/**
	 * 按时间查询旗下代理总返利额
	 */
	public static float queryRebate(int agency,String startTime,String endTime)throws SQLException{
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		Object object=IbatisDbServer.getGmSqlMapper().queryForObject("agencyBill.queryRebate",map);
		float rebate=0;
		if(object!=null){
			rebate=(float) object;
		}

		return rebate;
	}
	
	/**
	 * 按时间查询旗下代理给的返利账单
	 */
	public static List<AgencyBillBean> queryAgencyBills(int agency,int page,String startTime,String endTime)throws SQLException{
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("page", page);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<AgencyBillBean> agencyBillBeans=IbatisDbServer.getGmSqlMapper().queryForList("agencyBill.queryAgencyBills", map);
		
		return agencyBillBeans;
	}
	
	/***
	 * 查总返利金额
	 */
	public static float queryRebateCount(int agency,int operation)throws SQLException{
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("operation", operation);
		Object object=IbatisDbServer.getGmSqlMapper().queryForObject("agencyBill.queryRebateCount",map);
		float rebate=0;
		if(object!=null){
			rebate=(float) object;
		}
		return rebate;
	}
} 
