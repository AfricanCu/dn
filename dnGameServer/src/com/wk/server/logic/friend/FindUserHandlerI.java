package com.wk.server.logic.friend;

import msg.InnerMessage.UserGsToGs;
import msg.LoginMessage.GameRecord;
import msg.RoomMessage.JulebuRoom;
import msg.RoomMessage.PlayType;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.db.IbatisDbServer;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.inner.gstogs.UserGTGHandler;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.MsgId;
import com.wk.logic.enm.UserGTGType;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.ibatis.select.User;
import com.wk.server.logic.guild.Guild;
import com.wk.server.logic.item.ItemTemplate;
import com.wk.server.logic.login.UserManager;
import com.wk.server.logic.room.RoomAbs;
import com.wk.server.logic.room.RoomManager;
import com.wk.server.logic.room.RoomModule;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;

/**
 * 查找玩家处理器接口
 * 
 * @author ems
 *
 */
public abstract class FindUserHandlerI extends HandlerResultI {

	/**
	 * 代理游戏记录
	 * 
	 * @author ems
	 */
	public static class ProxyRecordHandler extends FindUserHandlerI {

		public ProxyRecordHandler(UserGTGType type, long uid,
				GameRecord gameRecord) {
			super(type, uid, 0, NOT_NEED_LOAD, NEED_SW);
			this.gameRecord = gameRecord;
			handle();
		}

		public ProxyRecordHandler(UserGTGType type, UserGsToGs userGsToGs) {
			super(type, userGsToGs, NOT_NEED_LOAD);
			this.gameRecord = userGsToGs.getGameRecord();
			handle();
		}

		@Override
		public int currentServerProcess(User user) {
			user.recordProxyGame(gameRecord);
			return NTxt.SUCCESS;
		}

		@Override
		public int currentServerUnloadProcess(UserBean userBean) {
			userBean.recordProxyGame(gameRecord);
			return NTxt.SUCCESS;
		}

		@Override
		public void handleCode(int code) {
			// TODO Auto-generated method stub
		}

	}

	/**
	 * 改变玩家钻石
	 * 
	 * @author ems
	 *
	 */
	public static class ChangeDiamondHandler extends FindUserHandlerI {
		public ChangeDiamondHandler(UserGTGType type, long uid, int diamond) {
			super(type, uid, diamond, NOT_NEED_LOAD, NEED_SW);
			handle();
		}

		public ChangeDiamondHandler(UserGTGType type, UserGsToGs userGsToGs) {
			super(type, userGsToGs, NOT_NEED_LOAD);
			handle();
		}

		@Override
		public int currentServerProcess(User user) {
			if (getDiamond() < 0 && user.getDiamond() < Math.abs(getDiamond())) {
				return NTxt.NOT_ENOUGH_DIAMOND;
			}
			user.addItem(ItemTemplate.Diamond_ID, getDiamond(), true, getType()
					.getName());
			return NTxt.SUCCESS;
		}

		@Override
		public int currentServerUnloadProcess(UserBean userBean) {
			if (getDiamond() < 0
					&& userBean.getDiamond() < Math.abs(getDiamond())) {
				return NTxt.NOT_ENOUGH_DIAMOND;
			}
			userBean.changeDiamond(getDiamond());
			return NTxt.SUCCESS;
		}

		@Override
		public void handleCode(int code) {
			// TODO Auto-generated method stub
		}
	}

	/**
	 * 加入俱乐部房间，没有缓存房间创建一个
	 * 
	 * @author ems
	 *
	 */
	public static class JoinJulebuRoomHandler extends FindUserHandlerI {
		/** 加入俱乐部房间玩家 */
		private final IncomeUserI joinRoomUser;
		/** 俱乐部 */
		private final Guild guild;
		/** 房间编号 **/
		private final int num;

		public JoinJulebuRoomHandler(IncomeUserI joinRoomUser, Guild guild,
				int num, int diamond) {
			super(UserGTGType.CreateJulebuRoomConsumeDiamond, guild
					.getMasterUid(), diamond, NOT_NEED_LOAD, NEED_SW);
			this.joinRoomUser = joinRoomUser;
			this.guild = guild;
			this.num = num;
			handle();
		}

		public JoinJulebuRoomHandler(UserGTGType type, UserGsToGs userGsToGs) {
			super(type, userGsToGs, NOT_NEED_LOAD);
			this.joinRoomUser = null;
			this.guild = null;
			this.num = 0;
			handle();
		}

		@Override
		public int currentServerProcess(User user) {
			if (getDiamond() > 0) {
				return NTxt.INFO_ERROR;
			}
			if (user.getDiamond() < Math.abs(getDiamond())) {
				return NTxt.NOT_ENOUGH_DIAMOND;
			}
			user.addItem(ItemTemplate.Diamond_ID, getDiamond(), true, getType()
					.getName());
			return NTxt.SUCCESS;
		}

		@Override
		public int currentServerUnloadProcess(UserBean userBean) {
			if (getDiamond() > 0) {
				return NTxt.INFO_ERROR;
			}
			if (userBean.getDiamond() < Math.abs(getDiamond())) {
				return NTxt.NOT_ENOUGH_DIAMOND;
			}
			userBean.changeDiamond(getDiamond());
			return NTxt.SUCCESS;
		}

		@Override
		public void handleCode(int code) {
			if (joinRoomUser == null) {
				return;
			}
			if (code == NTxt.SUCCESS) {
				RoomAbs room = RoomManager.julebuCreateRoom(this.getUid(),
						JulebuRoom.newBuilder().setId(guild.getId())
								.setNum(num).build(),
						PlayType.newBuilder(guild.gPlayType()).build()// copy一份
						);
				if (room == null) {
					joinRoomUser.sendMessage(MsgId.JoinRoomSm, MsgId.JoinRoomCm
							.gRErrMsg(NTxt.CAN_NOT_CREATE_GUILD_ROOM));
				}
				guild.setRoomId(num, room.getId());
				if (RoomModule.getInstance().getRoom(joinRoomUser) != null) {
					joinRoomUser
							.sendMessage(MsgId.JoinRoomSm, MsgId.JoinRoomCm
									.gRErrMsg(NTxt.BEFORE_ROOM_ALREADY));
					return;
				}
				byte[] joinRoom = room.joinRoom(joinRoomUser, true);
				if (joinRoom != null)
					joinRoomUser.sendMessage(MsgId.JoinRoomSm, joinRoom);
			} else {
				joinRoomUser.sendMessage(MsgId.JoinRoomSm,
						MsgId.JoinRoomCm.gRErrMsg(code));
			}
		}
	}

	public static final boolean NEED_LOAD = true;
	public static final boolean NOT_NEED_LOAD = false;
	public static final boolean NEED_SW = true;
	public static final boolean NOT_NEED_SW = false;
	/** */
	private final UserGTGType type;
	/** 玩家ID */
	private final long uid;
	/** 是否需要加载 */
	private final boolean needLoad;
	/** 是否需要切服 */
	private final boolean needSw;
	/** 钻石数 */
	private final int diamond;
	/** 游戏记录 */
	protected GameRecord gameRecord;

	/**
	 * 请求处理
	 * 
	 * @param type
	 *            操作类型
	 * @param uid
	 *            玩家ID
	 * @param diamond
	 *            改变的钻石数
	 * @param needLoad
	 *            是否需要加载
	 * @param needSw
	 *            不在当前服是否需要切服处理
	 */
	public FindUserHandlerI(UserGTGType type, long uid, int diamond,
			boolean needLoad, boolean needSw) {
		super();
		this.type = type;
		this.uid = uid;
		this.diamond = diamond;
		this.needLoad = needLoad;
		this.needSw = needSw;
	}

	/**
	 * 切服请求处理
	 * 
	 * @param userGsToGs
	 *            切服请求
	 * @param needLoad
	 *            是否需要加载
	 */
	public FindUserHandlerI(UserGTGType type, UserGsToGs userGsToGs,
			boolean needLoad) {
		super();
		this.type = type;
		this.uid = userGsToGs.getUid();
		this.diamond = userGsToGs.getDiamond();
		this.needLoad = needLoad;
		this.needSw = false;
	}

	public long getUid() {
		return uid;
	}

	public boolean isNeedLoad() {
		return needLoad;
	}

	public int getDiamond() {
		return diamond;
	}

	public UserGTGType getType() {
		return type;
	}

	/**
	 * 切服处理
	 * 
	 * @param userBean
	 *            玩家数据
	 */
	public final void swProcess(UserBean userBean) {
		UserGTGHandler.getInstance().process(this, uid, userBean.getServerId(),
				type, diamond, this.gameRecord);
	}

	/**
	 * 玩家在当前服处理
	 * 
	 * @param user
	 */
	public abstract int currentServerProcess(User user);

	/**
	 * 在无需加载的处理
	 */
	public abstract int currentServerUnloadProcess(UserBean userBean);

	protected void handle() {
		try {
			User user = UserManager.getInstance().getUser(this.uid);
			if (user != null) {
				this.setCode(currentServerProcess(user));
				return;
			}
			UserBean userBean = UserDao.queryUser(
					IbatisDbServer.getGmSqlMapper(), this.uid);
			if (userBean == null) {
				LoggerService.getLogicLog().error("空UserBean!uid:{}", this.uid);
				setCode(NTxt.NOT_FOUND_USER);
				return;
			}
			if (userBean.getServerId() == ServerConfig.serverId) {// 在当前服
				if (needLoad) {
					User loadUser = UserManager.getInstance().createNewUser(
							userBean);
					this.setCode(currentServerProcess(loadUser));
					loadUser.save();
				} else {
					this.setCode(currentServerUnloadProcess(userBean));
					userBean.save();
				}
			} else {
				if (needSw)
					this.swProcess(userBean);
				else
					this.setCode(NTxt.NOT_NEED_SW);
			}
		} catch (Exception e) {
			LoggerService.getPlatformLog().error(e.getMessage(), e);
			setCode(NTxt.EXCEPTION);
		}
	}

}
