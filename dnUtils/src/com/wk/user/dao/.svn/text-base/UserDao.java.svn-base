package com.wk.user.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.FriendBean;
import com.wk.db.IbatisDbServer;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.enun.DistrictType;
import com.wk.enun.PlatformType;
import com.wk.user.bean.UserBean;

public class UserDao {
	/**
	 * 
	 * @param puid
	 *            平台唯一ID
	 * @param plat
	 *            平台类型
	 * @return
	 * @throws SQLException
	 */
	public static UserBean queryUser(String puid, PlatformType plat)
			throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("puid", puid);
		paramMap.put("platId", plat.getType());
		UserBean userBean = (UserBean) IbatisDbServer.getGmSqlMapper()
				.queryForObject("user.queryPlatUser", paramMap);
		if (userBean != null) {
			userBean.bitSetClear();
		}
		return userBean;
	}

	/**
	 * 查询玩家
	 * 
	 * @param sqlMapClient
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public static UserBean queryUser(SqlMapClient sqlMapClient, long uid)
			throws SQLException {
		UserBean userBean = (UserBean) sqlMapClient.queryForObject(
				"user.queryUser", uid);
		if (userBean != null) {
			userBean.bitSetClear();
		}
		return userBean;
	}

	/**
	 * 登录检测玩家
	 * 
	 * @param sqlMapClient
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public static UserBean checkUser(SqlMapClient sqlMapClient, long uid,
			DistrictType districtType) throws SQLException {
		UserBean userBean = (UserBean) sqlMapClient.queryForObject(
				"user.checkUser", uid);
		if (userBean != null) {
			userBean.bitSetClear();
		}
		return userBean;
	}

	/**
	 * 更新玩家ID
	 * 
	 * @param uid
	 * @param nUid
	 * @return
	 * @throws SQLException
	 */
	private static int updateUserId(long uid, long nUid) throws SQLException {
		if (nUid < ServerConfigAbs.getStartuid()) {
			return 0;
		}
		return IbatisDbServer.getGmSqlMapper().update("user.updateUserId", uid);
	}

	/**
	 * 批量更新玩家数据
	 * 
	 * @param dbCache
	 * @return
	 * @throws SQLException
	 */
	public static int updateBatch(
			ConcurrentHashMap<Long, ? extends UserBean> dbCache)
			throws SQLException {
		int count = 0;
		ArrayList<PreparedStatement> list = new ArrayList<>();
		Connection conn = IbatisDbServer.getGmSqlMapper().getDataSource()
				.getConnection();
		conn.setAutoCommit(false);
		try {
			for (Iterator<? extends UserBean> iterator = dbCache.values()
					.iterator(); iterator.hasNext();) {
				UserBean next = iterator.next();
				try {
					PreparedStatement prepareStatement = next
							.createPreparedStatement(conn);
					if (prepareStatement != null) {
						list.add(prepareStatement);
					}
					iterator.remove();
				} catch (Exception e) {
					LoggerService.getLogicLog().error(e.getMessage(), e);
				}
			}
			for (PreparedStatement a : list) {
				count += a.executeUpdate();
			}
			conn.commit();
			return count;
		} finally {
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				conn.close();
			}
			for (PreparedStatement a : list) {
				if (a != null && !a.isClosed()) {
					a.close();
				}
			}
		}
	}

	/**
	 * 更新玩家数据
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public static int update(UserBean user) throws SQLException {
		int update = 0;
		try {
			update = user.update();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return update;
	}

	/**
	 * 插入玩家
	 * 
	 * @param user
	 * @return 自增ID
	 * @throws SQLException
	 */
	public static long insertUser(UserBean user) throws SQLException {
		return (long) IbatisDbServer.getGmSqlMapper().insert("user.insertUser",
				user);
	}

	/**
	 * 昵称查询玩家
	 * 
	 * @param sqlMapper
	 * @param nickname
	 * @return
	 * @throws SQLException
	 */
	public static List<UserBean> queryUserByNickname(SqlMapClient sqlMapper,
			String nickname) throws SQLException {
		List<UserBean> userBeanList = (List<UserBean>) sqlMapper.queryForList(
				"user.queryUserByNickname", nickname);
		if (userBeanList != null) {
			for (UserBean userBean : userBeanList) {
				userBean.bitSetClear();
			}
		}
		return userBeanList;
	}

	/**
	 * 查询活跃人数
	 * 
	 * @param sqlMapper
	 * @param day
	 * @return
	 * @throws SQLException
	 */
	public static int queryActiveUser(SqlMapClient sqlMapper, int day)
			throws SQLException {
		int sum = 0;
		Object obj = sqlMapper.queryForObject("user.queryActiveUser", day);
		if (obj != null) {
			sum = (int) obj;
		}
		return sum;
	}

	/**
	 * 查询总用户量
	 * 
	 * @param sqlMapper
	 * @return
	 * @throws SQLException
	 */
	public static int queryTotal(SqlMapClient sqlMapper) throws SQLException {
		int sum = 0;
		Object obj = sqlMapper.queryForObject("user.queryTotal");
		if (obj != null) {
			sum = (int) obj;
		}
		return sum;
	}

	/**
	 * 查询新增玩家数
	 * 
	 * @param sqlMapClient
	 * @param day
	 * @return
	 * @throws SQLException
	 */
	public static int queryRegister(SqlMapClient sqlMapper, int day)
			throws SQLException {
		int sum = 0;
		Object obj = (int) sqlMapper.queryForObject("user.queryRegister", day);
		if (obj != null) {
			sum = (int) obj;
		}
		return sum;
	}

	/**
	 * 查询代理下玩家某段时间的新增玩家数
	 */
	public static int myPlayerCount(SqlMapClient sqlMapClient, int agency,
			int day) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("day", day);
		int sum = 0;
		Object obj = (int) sqlMapClient.queryForObject("user.myPlayerCount",
				map);
		if (obj != null) {
			sum = (int) obj;
		}
		return sum;
	}

	/**
	 * 查询代理下的玩家
	 */
	public static List<UserBean> myUsers(SqlMapClient sqlMapClient, int agency,
			int flag) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("flag", flag);
		List<UserBean> beans = sqlMapClient.queryForList("user.myUsers", map);
		return beans;
	}

	/**
	 * 玩家绑定邀请码
	 */
	public static void updateUser(SqlMapClient sqlMapClient, int agency,
			long uid) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agency", agency);
		map.put("uid", uid);
		sqlMapClient.update("user.updateUser", map);
	}
}
