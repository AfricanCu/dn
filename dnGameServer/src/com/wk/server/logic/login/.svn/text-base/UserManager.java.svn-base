package com.wk.server.logic.login;

import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.db.IbatisDbServer;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.util.LRUMap;
import com.wk.enun.DistrictType;
import com.wk.logic.config.NTxt;
import com.wk.server.ibatis.select.User;
import com.wk.server.logic.room.RoomModule;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;
import com.wk.util.TimeTaskUtil;

public class UserManager {

	private static final UserManager instance = new UserManager();

	public static UserManager getInstance() {
		return instance;
	}

	/**
	 * 用户数据缓存LRU
	 * 
	 * key 用户名 value 用户
	 */
	private final Map<Long, User> userMap = Collections
			.synchronizedMap(new LRUMap<Long, User>(LoginModule.getInstance()
					.getMaxLRUSize()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean removeEldestEntry(
						java.util.Map.Entry<Long, User> eldest) {
					boolean removeEldestEntry = super.removeEldestEntry(eldest);
					if (!removeEldestEntry) {
						return false;
					}
					User user = eldest.getValue();
					if (RoomModule.getInstance().getRoom(user) != null) {
						this.setEldest(eldest);
						LoggerService.getLogicLog().warn(
								"超出userMap最大,玩家有房间！ uid:{},nick:{}",
								user.getUid(), user.getNickname());
					} else {
						if (user.isOnline()) {
							user.getChannel().close();
							LoggerService.getLogicLog().warn(
									"超出userMap最大,移除在线玩家,uid:{},nick:{}",
									user.getUid(), user.getNickname());
						} else {
							LoggerService.getLogicLog().warn(
									"超出userMap最大,移除离线玩家,无房间 uid:{}，nick:{}",
									user.getUid(), user.getNickname());
						}
					}
					return true;
				}
			});
	/** user回收栈 */
	private final Stack<User> userStack = new Stack<>();

	/**
	 * 怕切服没有正常断线这里要加判断，所有获取user都需从这里调用
	 * 
	 * @param uid
	 * @return
	 */
	public User getUser(long uid) {
		User user = userMap.get(uid);
		if (user != null && user.getServerId() != ServerConfig.serverId) {
			this.removeUser(uid);
			return null;
		} else
			return user;
	}

	public int getUserMapSize() {
		return userMap.size();
	}

	public User removeUser(long uid) {
		final User remove = userMap.remove(uid);
		if (remove != null) {
			TimeTaskUtil.getTaskmanager().submitOneTimeTask(new Runnable() {
				@Override
				public void run() {
					UserManager.this.userStack.push(remove);
				}
			}, 10, TimeUnit.SECONDS);
		} else {
			String format = String.format("%s,移除缓存，找不到玩家！%s",
					new Exception().getStackTrace()[0], uid);
			System.err.println(format);
			LoggerService.getLogicLog().error(format);
		}
		return remove;
	}

	/**
	 * 重新加载玩家数据
	 * 
	 * @param uid
	 * @return 如果已经登录返回登录的
	 * @throws Exception
	 * 
	 */
	public User reLoadUser(long uid) throws Exception {
		User user = this.getUser(uid);
		if (user != null && user.isOnline()) {
			return user;
		}
		UserBean bean = UserDao.queryUser(IbatisDbServer.getGmSqlMapper(), uid);
		if (bean == null) {
			LoggerService.getLogicLog().error("找不到玩家！uid:{}", uid);
			return null;
		}
		if (bean.getServerId() == SystemConstants.NoServerId) {
			LoggerService.getLogicLog().error(
					"玩家装载前必须要指定一个服务器ID，非法的装载！uid:{} nickname:{}", uid,
					bean.getNickname());
			return null;
		}
		if (bean.getServerId() != ServerConfig.serverId) {
			LoggerService.getLogicLog().error(
					"正常登陆错误！不是这个服务器登陆，userServerId:{}!=serverId:{}",
					bean.getServerId(), ServerConfig.serverId);
			return null;
		}
		if (user != null) {
			user.overWrite(bean);
		} else {
			user = createNewUser(bean);
		}
		userMap.put(user.getUid(), user);
		return user;
	}

	public User createNewUser(UserBean bean) throws Exception {
		User user = this.getStackUser();
		if (user != null) {
			user.reset();
			user.overWrite(bean);
		} else {
			user = new User(bean);
		}
		return user;
	}

	private User getStackUser() {
		if (!this.userStack.isEmpty()) {
			return this.userStack.pop();
		}
		return null;
	}

}
