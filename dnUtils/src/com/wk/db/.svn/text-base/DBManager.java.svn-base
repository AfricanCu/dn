package com.wk.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import com.ibatis.sqlmap.engine.datasource.DbcpDataSourceFactory;
import com.jery.ngsp.server.log.LoggerService;

/**
 * 需要在类路径下准备数据库连接配置文件dbcp.properties
 * 
 * @author 宋信强
 * @mail songxinqiang123@gmail.com
 * 
 * @time 2013-12-27
 * 
 */
public class DBManager {
	private static final String configFile = "dbcp.properties";
	private static DataSource dataSource;
	static {
		Properties dbProperties = new Properties();
		try {
			dbProperties.load(DBManager.class.getClassLoader()
					.getResourceAsStream(configFile));
			HashMap<String, String> map = new HashMap<>();
			for (Entry<Object, Object> entry : dbProperties.entrySet()) {
				map.put((String) entry.getKey(), (String) entry.getValue());
			}
			DbcpDataSourceFactory factory = new DbcpDataSourceFactory();
			factory.initialize(map);
			dataSource = factory.getDataSource();
			Connection conn = getConn();
			DatabaseMetaData mdm = conn.getMetaData();
			LoggerService.getPlatformLog().info(
					"Connected to " + mdm.getDatabaseProductName() + " "
							+ mdm.getDatabaseProductVersion());
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
		}
	}

	private DBManager() {
	}

	/**
	 * 获取链接，用完后记得关闭
	 * 
	 * @see {@link DBManager#closeConn(Connection)}
	 * @return
	 */
	public static final Connection getConn() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
		}
		return conn;
	}

	/**
	 * 关闭连接
	 * 
	 * @param conn
	 *            需要关闭的连接
	 */
	public static void closeConn(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				conn.close();
			}
		} catch (SQLException e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			Connection conn = DBManager.getConn();
			System.out.print(i + "   ");
			DBManager.closeConn(conn);
		}
		long end = System.currentTimeMillis();
		System.out.println("用时：" + (end - begin));
	}
}