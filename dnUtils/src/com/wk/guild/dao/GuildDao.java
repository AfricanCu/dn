package com.wk.guild.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.db.IbatisDbServer;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.enun.PlatformType;
import com.wk.guild.bean.GuildBean;
import com.wk.user.bean.UserBean;

public class GuildDao {

	/**
	 * 查询公会
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static GuildBean queryGuild(int id) throws SQLException {
		try {
			GuildBean userBean = (GuildBean) IbatisDbServer.getGmSqlMapper()
					.queryForObject("guild.queryGuild", id);
			if (userBean != null) {
				userBean.bitSetClear();
			}
			return userBean;
		} catch (SQLException e) {
			throw e;
		}
	}

	/**
	 * 批量更新公会数据
	 * 
	 * @param dbCache
	 * @return
	 * @throws SQLException
	 */
	public static int updateBatch(
			ConcurrentHashMap<Integer, ? extends GuildBean> dbCache)
			throws SQLException {
		int count = 0;
		ArrayList<PreparedStatement> list = new ArrayList<>();
		Connection conn = IbatisDbServer.getGmSqlMapper().getDataSource()
				.getConnection();
		conn.setAutoCommit(false);
		try {
			for (Iterator<? extends GuildBean> iterator = dbCache.values()
					.iterator(); iterator.hasNext();) {
				GuildBean next = iterator.next();
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

	public static int update(GuildBean guild) throws SQLException {
		int update = 0;
		try {
			update = guild.update();
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
	 * 插入公会
	 * 
	 * @param guild
	 * @return 自增ID
	 * @throws SQLException
	 */
	public static int insertGuild(GuildBean guild) throws SQLException {
		return (int) IbatisDbServer.getGmSqlMapper().insert(
				"guild.insertGuild", guild);
	}

	/***
	 * 
	 * @param id
	 * @return 影响的行数
	 * @throws SQLException
	 */
	public static int deteleGuild(int id) throws SQLException {
		return IbatisDbServer.getGmSqlMapper().delete("guild.deleteGuild", id);
	}
}
