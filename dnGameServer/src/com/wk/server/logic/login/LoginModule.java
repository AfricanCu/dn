package com.wk.server.logic.login;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import msg.GuildMessage.JulebuMember;
import msg.InnerMessage.KickBusToGsbk;
import msg.InnerMessage.ServerStatusBusToGsbk;
import msg.LoginMessage;
import msg.LoginMessage.GameRecordCm;
import msg.LoginMessage.GameRecordSm;
import msg.LoginMessage.Julebu;
import msg.LoginMessage.LoginSm;
import msg.LoginMessage.SwLoginCm;
import msg.LoginMessage.SwLoginSm;

import org.eclipse.jetty.util.ConcurrentHashSet;
import org.json.JSONObject;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.GamerecordBean;
import com.wk.bean.NTxtAbs;
import com.wk.db.IbatisDbServer;
import com.wk.db.dao.GamerecordDao;
import com.wk.engine.ModuleAbs;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.event.EventAbs;
import com.wk.engine.event.EventManager;
import com.wk.engine.event.EventType;
import com.wk.engine.inner.BusConnect;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.net.IoMessage;
import com.wk.engine.net.handler.SimpleChannelDuplexHandler;
import com.wk.engine.util.LRUMap;
import com.wk.enun.ServerStatus;
import com.wk.enun.UserState;
import com.wk.logic.config.ConfigTemplate;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.MsgId;
import com.wk.logic.enm.SwType;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.ibatis.select.User;
import com.wk.server.logic.room.RoomAbs;
import com.wk.server.logic.room.RoomModule;
import com.wk.template.DistrictTemplate;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;
import com.wk.util.TimeTaskUtil;

public class LoginModule extends ModuleAbs<Long, UserBean> {

	private static final long serialVersionUID = 1L;
	private static LoginModule instance;

	public static LoginModule getInstance() {
		return instance;
	}

	private class ConcurrentHashSetEx extends ConcurrentHashSet<Channel> {

		@Override
		public boolean remove(Object o) {
			boolean re = super.remove(o);
			if (re)
				LoginModule.this.changeStatus();
			return re;
		}

		@Override
		public boolean add(Channel e) {
			boolean ad = super.add(e);
			if (ad) {
				LoginModule.this.changeStatus();
			}
			return ad;
		}
	}

	/** LRU Map最高缓存数 */
	private final int maxLRUSize;
	/** 服务器满人数大小 */
	private final int fullSize;
	private final int busySize;
	private final int smoothSize;
	private ServerStatus status = ServerStatus.smooth;
	/** 在线channel Set */
	private final ConcurrentHashSet<Channel> onlineSet = new ConcurrentHashSetEx();
	/** 有房间的玩家数目统计 */
	private final AtomicInteger roomUserNumAtomic = new AtomicInteger();

	public LoginModule(int maxLRUSize, int fullSize, int busySize,
			int smoothSize) {
		super();
		this.maxLRUSize = maxLRUSize;
		this.fullSize = fullSize;
		this.busySize = busySize;
		this.smoothSize = smoothSize;
	}

	@Override
	public void init() throws Exception {
	}

	public byte[] processMessage(Channel channel, IoMessage message)
			throws Exception {
		switch (message.getMsgId()) {
		case LoginCm:
			return login(channel,
					(LoginMessage.LoginCm) message.genMessageLite(), null,
					SystemConstants.NoServerId);
		case SwLoginCm:
			return SwLoginManager.getInstance().swLogin(channel,
					(LoginMessage.SwLoginCm) message.genMessageLite());
		default:
			return null;
		}
	}

	@Override
	public byte[] processMessage(IncomeUserI user, IoMessage message)
			throws Exception {
		switch (message.getMsgId()) {
		case GameRecordCm:
			return gameRecord(user, (GameRecordCm) message.genMessageLite());
		default:
			return null;
		}
	}

	public int getMaxLRUSize() {
		return maxLRUSize;
	}

	public byte[] gameRecord(IncomeUserI user, GameRecordCm genMessageLite) {
		int index = genMessageLite.getIndex();
		try {
			GamerecordBean queryFriend = GamerecordDao.queryRecord(index);
			if (queryFriend == null)
				return MsgId.GameRecordCm.gRErrMsg(NTxt.GAME_RECORD_EMPTY);
			else {
				user.sendMessage(MsgId.GameRecordSm, GameRecordSm.newBuilder()
						.mergeFrom(queryFriend.getData()).setIndex(index)
						.setCode(NTxt.SUCCESS));
				return null;
			}
		} catch (SQLException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return MsgId.GameRecordCm.gRErrMsg(NTxt.GAME_RECORD_SQL_EXCEPTION);
		} catch (InvalidProtocolBufferException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return MsgId.GameRecordCm.gRErrMsg(NTxt.EXCEPTION);
		}
	}

	/**
	 * 
	 * @param channel
	 * @param loginCm
	 * @param swLoginSm
	 *            这个不为空表示切服登录
	 * @param sId
	 *            如果是切服装载，这里指定从哪个服切过来，否则填SystemConstants.NoServerId|0
	 * @return 登录失败返回消息，成功返回null
	 * @throws Exception
	 */
	public byte[] login(Channel channel, LoginMessage.LoginCm loginCm,
			SwLoginSm.Builder swLoginSm, int sId) {
		try {
			long uid = loginCm.getUid();
			String sessionCode = loginCm.getSessionCode();
			long loginTime = Long.parseLong(loginCm.getLoginTime());
			if (Math.abs(System.currentTimeMillis() - loginTime) > SystemConstants.Login_Over_Time) {
				return MsgId.LoginCm.gRErrMsg(NTxt.LOGIN_OVER_TIME);
			}
			if (onlineSet.contains(channel)) {
				return MsgId.LoginCm.gRErrMsg(NTxt.LOGIN_ALREADY);
			}
			User user = UserManager.getInstance().reLoadUser(uid);
			if (user == null) {
				LoggerService.getLogicLog().error("非法登陆！uid:{},sId:{}", uid,
						sId);
				return MsgId.LoginCm.gRErrMsg(NTxt.LOGIN_NOT_FOUND_USER);
			}
			if (user.isOnline()) {
				LoggerService.getLogicLog().error("双登！uid:{}", user.getUid());
				return MsgId.LoginCm.gRErrMsg(NTxt.LOGIN_USER_ALREADY_ONLINE);
			}
			if (!user.getSessionCode().equals(sessionCode)) {
				LoggerService.getLogicLog().error("sessionCode:{}!={}",
						user.getSessionCode(), sessionCode);
				return MsgId.LoginCm.gRErrMsg(NTxt.LOGIN_SESSIONCODE_ERROR);
			}
			if (!onlineSet.add(channel)) {
				return MsgId.LoginCm.gRErrMsg(NTxt.LOGIN_USER_CONTAINS_ALREADY);
			}
			user.registerClient(channel);
			RoomAbs room = RoomModule.getInstance().getRoom(user);
			LoginSm.Builder loginSm = LoginSm.newBuilder()
					.setCode(NTxt.SUCCESS).setNickname(user.getNickname())
					.setHeadimg(ServerConfig.getHeadUrl(uid))
					.setNotice(ConfigTemplate.getConfigTemplate().getNotice())
					.setCreateJ(user.isCreateJ());
			if (room != null) {
				loginSm.setRoomId(room.getIdStr());
			}
			user.assembleJulebuMsg(loginSm);
			if (swLoginSm == null) {
				user.sendMessage(MsgId.LoginSm, loginSm);
			} else {
				swLoginSm.setCode(NTxt.SUCCESS);
				swLoginSm.setLoginSm(loginSm);
				user.sendMessage(MsgId.SwLoginSm, swLoginSm);
			}
			user.save();
			user.sendPlayer().sendPlayerRecordCast();
			user.sendProxyRecordCast().sendProxyRoomsCast();
			EventManager.getInstance().processEvent(EventType.User_LogIn, user);
			return null;
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return MsgId.LoginCm.gRErrMsg(NTxt.LOGIN_EXCEPTION);
		}
	}

	public void removeOnline(Channel channel) {
		this.onlineSet.remove(channel);
	}

	public void serverStatusbk() {
		long maxMemory = Runtime.getRuntime().maxMemory()
				/ SystemConstants.One_M;
		long totalMemory = Runtime.getRuntime().totalMemory()
				/ SystemConstants.One_M;
		long freeMemory = Runtime.getRuntime().freeMemory()
				/ SystemConstants.One_M;
		int sessionCount = SimpleChannelDuplexHandler.getInstance()
				.getSessionCount();
		int onlineNum = onlineSet.size();
		int cacheSize = UserManager.getInstance().getUserMapSize();
		int roomUserNum = roomUserNumAtomic.get();
		ServerStatusBusToGsbk.Builder serverStatusGB = ServerStatusBusToGsbk
				.newBuilder()
				.setCode(NTxtAbs.SUCCESS)
				.setOnlineNum(onlineNum)
				.setCacheSize(cacheSize)
				.setRoomUserNum(roomUserNum)
				.setSessionCount(sessionCount)
				// //////////////////////
				.setMaxMemory(maxMemory)
				.setTotalMemory(totalMemory)
				.setFreeMemory(freeMemory)
				.setStatus(status.getType())
				.setSwCode(
						new JSONObject().put("uuid",
								UUID.randomUUID().toString()).toString());
		BusConnect.getInstance().sendMessage(InnerMsgId.ServerStatusBusToGsbk,
				serverStatusGB);
		String format = String
				.format("当前在线人数：%s,缓存数据数：%s,在房间人数：%s,netty会话数：%s\t最大内存：%sM,分配内存：%sM,空闲内存：%sM,状态：%s",
						onlineNum, cacheSize, roomUserNum, sessionCount,
						maxMemory, totalMemory, freeMemory,
						this.status.getName());
		LoggerService.getLogicLog().warn(format);
		// 玩家已经没有房间了离线了，，但是还在缓存中，这个有点奇怪
		// for (User user : this.userMap.values()) {
		// if (user.getRoomId() == 0) {
		// System.err.println(String.format("%s,%s,%s",
		// new Exception().getStackTrace()[0], user,
		// user.isOnline()));
		// }
		// }
	}

	@Override
	public void backDb() throws SQLException {
		if (this.isEmpty()) {
			return;
		}
		int updateBatch = UserDao.updateBatch(this);
		LoggerService.getLogicLog().error("批量更新玩家数据,size:{}", updateBatch);
	}

	private EventAbs shutDown = new EventAbs(EventType.ShutDown) {
		@Override
		public void eventNotify(User user, Object... params) {
			for (Channel channel : onlineSet) {
				try {
					channel.close().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private EventAbs User_LogOut = new EventAbs(EventType.User_LogOut) {
		@Override
		public void eventNotify(User user, Object... params) {
		}
	};

	@Override
	public List<EventAbs> getGameEventList() {
		return Arrays.asList(shutDown, User_LogOut);
	}

	public ServerStatus getStatus() {
		return this.status;
	}

	public void kick(long uid) {
		User user = UserManager.getInstance().getUser(uid);
		if (user == null) {
			BusConnect.getInstance().sendMessage(
					InnerMsgId.KickBusToGsbk,
					KickBusToGsbk.newBuilder().setCode(NTxtAbs.SUCCESS)
							.setUid(uid).build().toByteArray());
			LoggerService.getLogicLog().warn("重登，踢人下线，找不到玩家！uid:{}", uid);
			return;
		}
		Channel channel = user.getChannel();
		if (channel == null) {
			try {
				user.setState(UserState.offline);
				user.save();
			} catch (SQLException e) {
				LoggerService.getLogicLog().warn(e.getMessage(), e);
			}
			BusConnect.getInstance().sendMessage(
					InnerMsgId.KickBusToGsbk,
					KickBusToGsbk.newBuilder().setCode(NTxtAbs.SUCCESS)
							.setUid(uid).build().toByteArray());
			LoggerService.getLogicLog().warn("重登，踢人下线,找不到channel!uid:{}", uid);
			return;
		}
		user.setCauseClose(CloseType.kick);
		try {
			user.unregisterClient();
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			BusConnect.getInstance().sendMessage(
					InnerMsgId.KickBusToGsbk,
					KickBusToGsbk.newBuilder().setCode(NTxtAbs.SUCCESS)
							.setUid(uid).build().toByteArray());
		}
		LoggerService.getLogicLog().warn("重登，踢人..等待中..！");
		NTxt.println(String.format("--%s %s %s %s %s", channel,
				channel.isOpen(), channel.isRegistered(), channel.isActive(),
				channel.isWritable()));
		channel.close().addListener(
				new GenericFutureListener<Future<? super Void>>() {
					@Override
					public void operationComplete(Future<? super Void> future)
							throws Exception {
						LoggerService.getLogicLog().warn("双登，关你妹 ！{},{}",
								future.isDone(), future.isSuccess());
					}
				});
		NTxt.println(String.format("%s %s %s", channel, channel.isActive(),
				channel.isWritable()));
	}

	/** 在房间人数减1 */
	public int decrementRoomUser() {
		int decrementAndGet = this.roomUserNumAtomic.decrementAndGet();
		this.changeStatus();
		return decrementAndGet;
	}

	/** 在房间人数加1 */
	public int incrementRoomUser() {
		int incrementAndGet = this.roomUserNumAtomic.incrementAndGet();
		this.changeStatus();
		return incrementAndGet;
	}

	/** 改变服务器状态 */
	public void changeStatus() {
		int halfOnlineNum = this.onlineSet.size() / 2;
		int roomUserNum = this.roomUserNumAtomic.get();
		int maxNum = halfOnlineNum > roomUserNum ? halfOnlineNum : roomUserNum;
		ServerStatus status = null;
		if (maxNum <= smoothSize) {
			status = ServerStatus.smooth;
		} else if (maxNum <= busySize) {
			status = ServerStatus.busy;
		} else if (maxNum <= fullSize) {
			status = ServerStatus.full;
		} else {
			status = ServerStatus.very_full;
		}
		if (this.status != status) {
			this.status = status;
			serverStatusbk();
		}
	}

}
