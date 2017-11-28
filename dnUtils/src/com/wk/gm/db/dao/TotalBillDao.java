package com.wk.gm.db.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wk.db.IbatisDbServer;
import com.wk.gm.bean.TotalBillBean;

public class TotalBillDao {
	
	/**
	 * 插入总账单数据
	 * @param bean
	 * @throws SQLException
	 */
	public static void insertTotalBill(TotalBillBean billBean)throws SQLException{
		IbatisDbServer.getGmSqlMapper().insert("totalBill.insertTotalBill", billBean);
	}
	
	
	/**
	 * 查询总账单
	 * @param agency
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public static List<TotalBillBean> queryTotalBill(int agency ,String startTime,String endTime)throws SQLException{
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<TotalBillBean> beans= (List<TotalBillBean>) IbatisDbServer.getGmSqlMapper().queryForList("totalBill.queryTotalBill", map);
		return beans;
	}
	
	/**
	 * 修改账单
	 */
	public static void updateTotalBill(int id,float money)throws SQLException{
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("money", money);
		IbatisDbServer.getGmSqlMapper().update("totalBill.updateTotalBill", map);
	}
	
	/***
	 * 通过id查询总账单
	 */
	public static TotalBillBean queryIdTotalBill(int id ,int invitation)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("invitation", invitation);
		TotalBillBean bean=(TotalBillBean) IbatisDbServer.getGmSqlMapper().queryForObject("totalBill.queryIdTotalBill", map);
		return bean;
	}
	
	/***
	 * 通过代理查询总收益金额,
	 */
	public static float totalRevenue(int invitation ,Boolean b)throws SQLException{
		int flag=0;
		if(b!=true){
			flag=1;
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("flag", flag);
		map.put("invitation", invitation);
		Object object=IbatisDbServer.getGmSqlMapper().queryForObject("totalBill.totalRevenue", map);
		float totalRevenue=0;
		if(object!=null){
			totalRevenue=(float) object;
		}
		return totalRevenue;
	}
	
	public static List<TotalBillBean> queryTotalBillPage(int invitation,int page)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("invitation", invitation);
		map.put("page", page);
		List<TotalBillBean> list=IbatisDbServer.getGmSqlMapper().queryForList("totalBill.queryTotalBillPage", map);
		return list;
	}
}
