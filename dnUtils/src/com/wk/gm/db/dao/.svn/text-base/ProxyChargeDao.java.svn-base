package com.wk.gm.db.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.wk.db.IbatisDbServer;
import com.wk.gm.bean.ProxyChargeBean;

public class ProxyChargeDao {
	
	/**
	 * 删除订单
	 */
	public static int delProxyCharge(String orderId)throws SQLException{
		return IbatisDbServer.getGmSqlMapper().delete("proxyCharge.delProxyCharge", orderId);
	}
	/**
	 * 更新充值订单状态
	 * 
	 * @return 影响行数
	 * @throws SQLException
	 */
	public static int updateStatus(String orderId) throws SQLException {
		return IbatisDbServer.getGmSqlMapper().update(
				"proxyCharge.updateStatus", orderId);
	}
	
	/**
	 * 修改订单状态
	 */

	public static int updateStatusTwo(String orderId)throws SQLException{
		return IbatisDbServer.getGmSqlMapper().update("proxyCharge.updateStatusTwo", orderId);
	}
	/**
	 * 查询充值记录
	 * 
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	public static ProxyChargeBean queryCharge(String orderId)
			throws SQLException {
		return (ProxyChargeBean) IbatisDbServer.getGmSqlMapper()
				.queryForObject("proxyCharge.queryProxyCharge", orderId);
	}

	/**
	 * 查询充值记录列表
	 * 
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	public static List<ProxyChargeBean> queryChargeList(String uid,
			String beginTime, String endTime) throws SQLException {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", uid);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		return (List<ProxyChargeBean>) IbatisDbServer.getGmSqlMapper()
				.queryForList("proxyCharge.queryProxyChargeList", map);
	}

	/**
	 * 插入充值记录
	 * 
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	public static Object insertProxyCharge(ProxyChargeBean proxyChargeBean)
			throws SQLException {
		return IbatisDbServer.getGmSqlMapper().insert(
				"proxyCharge.insertCharge", proxyChargeBean);
	}

	/**
	 * 查询充值金额
	 * 
	 * @param sqlMapClient
	 * @param status
	 * @param chargeTime
	 * @return
	 * @throws SQLException
	 */
	public static int queryCredit(int status, String chargeTime)
			throws SQLException {
		int sum = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("chargeTime", chargeTime);
		Object obj = IbatisDbServer.getGmSqlMapper().queryForObject(
				"proxyCharge.queryCredit", map);
		if (obj != null) {
			sum = (int) obj;
		}
		return sum;
	}
}
