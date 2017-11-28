package com.wk.gm.db.dao;
import java.sql.SQLException;
import java.util.List;

import com.wk.db.IbatisDbServer;
import com.wk.gm.bean.AnnouncementBean;
public class AnnouncementDao {

	/**
	 * 查询所有公告
	 * @return
	 * @throws SQLException
	 */
	public static List<AnnouncementBean> queryAnnouncementBean()throws SQLException{
		List<AnnouncementBean> list=IbatisDbServer.getGmSqlMapper().queryForList("AnnouncementBean.queryAnnouncementBean");
		return list;
	}
}
