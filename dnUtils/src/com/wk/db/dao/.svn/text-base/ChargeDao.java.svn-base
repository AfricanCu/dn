package com.wk.db.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.wk.bean.ChargeBean;

public class ChargeDao {

	/**
	 * 更新充值订单状态
	 * 
	 * @param sqlMapClient
	 * 
	 * @return 影响行数
	 * @throws SQLException
	 */
	public static int updateStatus(SqlMapClient sqlMapClient,
			String apple_receipt, String orderId) throws SQLException {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("apple_receipt", apple_receipt);
		hashMap.put("orderId", orderId);
		return sqlMapClient.update("charge.updateStatus", hashMap);
	}

	/**
	 * 查询充值记录
	 * 
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	public static ChargeBean queryCharge(SqlMapClient sqlMapClient,
			String orderId) throws SQLException {
		return (ChargeBean) sqlMapClient.queryForObject("charge.queryCharge",
				orderId);
	}

	/**
	 * 查询苹果订单ID
	 * 
	 * @param apple_receipt
	 *            苹果订单ID
	 * @return
	 * @throws SQLException
	 */
	public static ChargeBean queryApple_receipt(SqlMapClient sqlMapClient,
			String apple_receipt) throws SQLException {
		return (ChargeBean) sqlMapClient.queryForObject(
				"charge.queryApple_receipt", apple_receipt);
	}

	/***
	 * 按时间段和代理邀请码查询充值记录
	 * 
	 * @param sqlMapClient
	 * @param agency
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public static List<ChargeBean> queryPlayerCharges(
			SqlMapClient sqlMapClient, int uid, int agency, String beginTime,
			String endTime) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("agency", agency);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		List<ChargeBean> beans = sqlMapClient.queryForList(
				"charge.queryPlayerCharges", map);
		return beans;
	}

	/***
	 * 插入充值记录
	 * 
	 * @param charge
	 * @return
	 * @throws SQLException
	 */
	public static Object insert(SqlMapClient sqlMapClient, ChargeBean charge)
			throws SQLException {
		return sqlMapClient.insert("charge.insertCharge", charge);
	}

	/***
	 * 查询时间段的订单信息按照代理邀请码分类
	 * 
	 * @param sqlMapClient
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public static List<ChargeBean> queryCharges(SqlMapClient sqlMapClient,
			int status, String startTime, String endTime) throws SQLException {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("status", status);
		hashMap.put("startTime", startTime);
		hashMap.put("endTime", endTime);
		List<ChargeBean> beans = sqlMapClient.queryForList(
				"charge.queryCharges", hashMap);
		return beans;
	}

	/**
	 * 按时间查询钻石量和金额
	 * 
	 * @param sqlMapClient
	 * @param agency
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public static ChargeBean queryDiamondCount(SqlMapClient sqlMapClient,
			int agency, int status, String startTime, String endTime)
			throws SQLException {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("agency", agency);
		hashMap.put("status", status);
		hashMap.put("startTime", startTime);
		hashMap.put("endTime", endTime);
		ChargeBean bean = (ChargeBean) sqlMapClient.queryForObject(
				"charge.queryDiamondCount", hashMap);
		return bean;
	}
}
