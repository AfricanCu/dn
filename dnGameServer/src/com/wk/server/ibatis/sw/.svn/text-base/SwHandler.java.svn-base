package com.wk.server.ibatis.sw;

import java.sql.SQLException;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.SystemConstantsAbs;
import com.wk.db.IbatisDbServer;
import com.wk.engine.config.ServerConfig;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.ibatis.select.User;
import com.wk.server.logic.login.LoginModule;
import com.wk.server.logic.login.UserManager;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;

/**
 * 可能需要退房/切服的操作
 * 
 * @author ems
 *
 */
public abstract class SwHandler {
	/** 结算的房间 id */
	protected final int roomId;

	public SwHandler(int roomId) {
		this.roomId = roomId;
	}

	/**
	 * 切服上线处理
	 * 
	 * @param userBean
	 *            最新的玩家数据
	 */
	public abstract void swOnlineProcess(UserBean userBean);

	/**
	 * 在当前服或下线处理
	 * 
	 * @param incomeUserI
	 */
	public abstract void currentServerOrOfflineProcess(IncomeUserI incomeUserI);

	/**
	 * 退房/切服处理
	 * 
	 * @param handler
	 */
	public void handle(IncomeUserI my) {
		if (this.roomId != SystemConstantsAbs.NoRoomId
				&& my.getRoomId() == this.roomId) {// 没有退房user是不会变的
			this.currentServerOrOfflineProcess(my);
			return;
		}
		User user = UserManager.getInstance().getUser(my.getUid());
		if (user != null) {
			this.currentServerOrOfflineProcess(user);
			return;
		}
		try {
			UserBean userBean = UserDao.queryUser(
					IbatisDbServer.getGmSqlMapper(), my.getUid());
			if (userBean.getServerId() == ServerConfig.serverId
					|| !userBean.isOnline()) {// 还在当前服，或者是已经下线的玩家
				if (userBean.getLoginTime() != my.getLoginTime()) {// 判断是否要重新解析user
					try {
						my.overWrite(userBean);
					} catch (Exception e) {
						LoggerService.getLogicLog().error(e.getMessage(), e);
					}
				}
				this.currentServerOrOfflineProcess(my);
				my.save();
				return;
			}
			this.swOnlineProcess(userBean);
		} catch (SQLException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
		}
	}
}