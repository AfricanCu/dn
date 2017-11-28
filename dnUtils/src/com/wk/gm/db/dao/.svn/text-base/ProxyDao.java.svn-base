package com.wk.gm.db.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wk.db.IbatisDbServer;
import com.wk.gm.bean.ProxyBean;

public class ProxyDao {

	

	/**
	 * 查询proxy
	 * 
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public static ProxyBean queryProxy(String uid) throws SQLException {
		ProxyBean proxyBean = (ProxyBean) IbatisDbServer.getGmSqlMapper()
				.queryForObject("proxy.queryProxy", uid);
		if (proxyBean != null) {
			proxyBean.bitSetClear();
		}
		return proxyBean;
	}
	
	/**
	 * 注册查询代理
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public static ProxyBean zcQueryProxy(String uid)throws SQLException{
		ProxyBean proxyBean = (ProxyBean) IbatisDbServer.getGmSqlMapper()
				.queryForObject("proxy.zcQueryProxy", uid);
		return proxyBean;
	}

	/**
	 * 查询proxy
	 * 
	 * @param sqlMapClient
	 * @param uids
	 * @return
	 * @throws SQLException
	 */
	public static List<ProxyBean> queryProxys(List<String> uids)
			throws SQLException {
		if (uids.isEmpty()) {
			return new ArrayList<ProxyBean>();
		}
		List<ProxyBean> proxyBeans = (List<ProxyBean>) IbatisDbServer
				.getGmSqlMapper().queryForList("proxy.queryProxys", uids);
		for (ProxyBean proxyBean : proxyBeans) {
			proxyBean.bitSetClear();
		}
		return proxyBeans;
	}

	public static int update(ProxyBean proxy) throws SQLException {
		int update = 0;
		try {
			update = proxy.update();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return update;
	}

	public static Object insertProxy(ProxyBean proxy) throws SQLException {
		return IbatisDbServer.getGmSqlMapper().insert("proxy.insertProxy",
				proxy);
	}

	public static List<ProxyBean> queryAllProxys() throws SQLException {
		List<ProxyBean> proxyBeans = (List<ProxyBean>) IbatisDbServer
				.getGmSqlMapper().queryForList("proxy.queryAllProxys");
		for (ProxyBean proxyBean : proxyBeans) {
			proxyBean.bitSetClear();
		}
		return proxyBeans;
	}
	
	/**
	 * 查询邀请码是否存在
	 * 
	 * @param sqlMapClient
	 * @param invitation
	 * @return
	 * @throws Exception
	 */
	public static int queryInvitation(int invitation) throws Exception {
		int sum = (int) IbatisDbServer.getGmSqlMapper().queryForObject(
				"proxy.queryInvitation", invitation);
		return sum;
	}
	
	
	
	/***
	 * 查询代理信息
	 * @param invitation
	 * @return
	 * @throws SQLException
	 */
	public static ProxyBean queryAgency(int invitation)throws SQLException{
		ProxyBean bean=(ProxyBean) IbatisDbServer.getGmSqlMapper().queryForObject("proxy.queryAgency", invitation);
		return bean;
	}
	
	/**
	 * 查询旗下代理个数
	 */
	public static int agencyCount(int invitation,int district)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("invitation", invitation);
		map.put("district", district);
		int count=0;
		Object object=IbatisDbServer.getGmSqlMapper().queryForObject("proxy.agencyCount", map);
		if(object!=null){
			count=(int) object;
		}
		return count;
	}
	
	/**
	 * 查询今日新增代理个数
	 */
	public static int queryDateAgencyCount(int invitation)throws SQLException{
		int count=0;
		Object object=IbatisDbServer.getGmSqlMapper().queryForObject("proxy.queryDateAgencyCount", invitation);
		if(object!=null){
			count=(int) object;
		}
		return count;
	}
	

	
	/**
	 * 查询下级的代理
	 */
	public static List<ProxyBean> queryAgenys(int invitation)throws SQLException{
		List<ProxyBean> beans=IbatisDbServer.getGmSqlMapper().queryForList("proxy.queryAgenys", invitation);
		return beans;
	}
	
	/**
	 * 查询下级的代理2
	 */
	public static List<ProxyBean> queryAgenyTwos(int invitation,int page)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("invitation", invitation);
		map.put("page", page);
		List<ProxyBean> beans=IbatisDbServer.getGmSqlMapper().queryForList("proxy.queryAgenyTwos", map);
		return beans;
	}
	
	/**
	 * 通过授权码查找代理
	 */
	public static ProxyBean queryAgenyTwo(int authorization)throws SQLException{
		ProxyBean bean=(ProxyBean) IbatisDbServer.getGmSqlMapper().queryForObject("proxy.queryAgenyTwo", authorization);
		return bean;
	}
	
	/**
	 * 查询授权码是否存在
	 */
	public static int queryuthorization(int authorization)throws SQLException{
		int count=0;
		Object object=IbatisDbServer.getGmSqlMapper().queryForObject("proxy.queryuthorization", authorization);
		if(object!=null){
			count=(int) object;
		}
		return count;
	}
	
	/**
	 * 激活代理
	 */
	public static void activateAgency(String uid,int flag)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("flag", flag);
		IbatisDbServer.getGmSqlMapper().update("proxy.activateAgency", map);
	}
	
	/**
	 * 删除代理
	 */
	public static void delAgency(String uid)throws SQLException{
		IbatisDbServer.getGmSqlMapper().delete("proxy.delAgency", uid);
	}
	
	/**
	 * 修改代理已拥有，买卖钻石数量
	 */
	public static int updateDiamond(String uid,int diamond,int buyCard,int sellCard)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("diamond", diamond);
		map.put("buyCard", buyCard);
		map.put("sellCard", sellCard);
		return IbatisDbServer.getGmSqlMapper().update("proxy.updateDiamond", map);
	}
	
	/**
	 * 修改代理已拥有，买卖钻石数量
	 */
	public synchronized static  int updateDiamondTwo(int invitation,int diamond,int buyCard,int sellCard)throws SQLException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("invitation", invitation);
		map.put("diamond", diamond);
		map.put("buyCard", buyCard);
		map.put("sellCard", sellCard);
		return IbatisDbServer.getGmSqlMapper().update("proxy.updateDiamondTwo", map);
	}
}
