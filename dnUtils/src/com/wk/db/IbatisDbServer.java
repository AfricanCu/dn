package com.wk.db;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Properties;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.jery.ngsp.server.log.LoggerService;

/**
 * 数据库服务器
 *
 * @author lixing
 */
public class IbatisDbServer {

	/**
	 * SqlMapClient instances are thread safe, so you only need one. In this
	 * case, we'll use a static singleton. So sue me. ;-)
	 */
	private static SqlMapClient gmSqlMapper;

	/**
	 * <pre>
	 * 初始化数据库
	 * 
	 * </pre>
	 * 
	 * @param ibatisConfig
	 * @throws SQLException
	 */
	public static void initDB(String ibatisConfig) throws SQLException {
		LoggerService.getPlatformLog().warn("开始加载ibatis配置！");
		try {
			Properties props = new Properties();
			Reader reader = Resources.getUrlAsReader("file:" + ibatisConfig);
			gmSqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader, props);
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException("创建ibatis SqlMapClient时发生错误！" + e, e);
		}
		LoggerService.getPlatformLog().warn("完成加载ibatis配置！");
	}

	/**
	 * 数据库客户端
	 * 
	 * @return
	 */
	public static SqlMapClient getGmSqlMapper() {
		return gmSqlMapper;
	}
}
