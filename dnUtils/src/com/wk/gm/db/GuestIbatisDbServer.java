package com.wk.gm.db;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Properties;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.jery.ngsp.server.log.LoggerService;

/**
 * guest用户
 * 
 * 数据库服务器
 *
 * @author lixing
 */
public class GuestIbatisDbServer {

	private static SqlMapClient gameSqlMapper;

	/**
	 * <pre>
	 * 初始化数据库
	 * 
	 * 1.创建流水表，没有才创建 2.设置参数
	 * </pre>
	 *
	 * @throws SQLException
	 */
	public static void initDB(String ibatisConfig) throws SQLException {
		try {
			Properties props = new Properties();
			Reader reader = Resources.getUrlAsReader("file:" + ibatisConfig);
			gameSqlMapper = SqlMapClientBuilder
					.buildSqlMapClient(reader, props);
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException("创建ibatis game SqlMapClient时发生错误！" + e,
					e);
		}
		LoggerService.getLogicLog().warn("完成加载 game charge ibatis配置！");
	}

	public static SqlMapClient getSqlMapper() {
		return gameSqlMapper;
	}

}
