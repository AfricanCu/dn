package com.wk.server.logic.room;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import msg.BullMessage.BankerCast;
import msg.BullMessage.GameOverCast;
import msg.BullMessage.ReconnectCm;
import msg.BullMessage.ReconnectSm;
import msg.LoginMessage.GameRecord;
import msg.RoomMessage.ChatCast;
import msg.RoomMessage.ChatCm;
import msg.RoomMessage.DissolveRoomCm;
import msg.RoomMessage.ImCast;
import msg.RoomMessage.ImCm;
import msg.RoomMessage.ImInfoSaveCm;
import msg.RoomMessage.JoinRoomCast;
import msg.RoomMessage.JoinRoomSm;
import msg.RoomMessage.JulebuRoom;
import msg.RoomMessage.MemberDissolveRoomCast;
import msg.RoomMessage.MemberDissolveRoomCm;
import msg.RoomMessage.NsCast;
import msg.RoomMessage.NsCm;
import msg.RoomMessage.PlayType;
import msg.RoomMessage.PrepareRoomCast;
import msg.RoomMessage.ProxyRoom;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.BattlebackBean;
import com.wk.bean.GamerecordBean;
import com.wk.bean.SystemConstantsAbs;
import com.wk.db.dao.BattlebackDao;
import com.wk.db.dao.GamerecordDao;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.enun.UserState;
import com.wk.guild.enm.WinnerNumType;
import com.wk.logic.config.DissolveRoomType;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.BankerMode;
import com.wk.logic.enm.CreateRoomType;
import com.wk.logic.enm.GameState;
import com.wk.logic.enm.MsgId;
import com.wk.logic.enm.PType;
import com.wk.logic.enm.RoundType;
import com.wk.logic.enm.UserGTGType;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.ibatis.select.User;
import com.wk.server.logic.friend.FindUserHandlerI;
import com.wk.server.logic.guild.Guild;
import com.wk.server.logic.guild.GuildManager;
import com.wk.server.logic.item.ItemTemplate;
import com.wk.server.logic.login.UserManager;
import com.wk.util.TimeTaskUtil;

public abstract class RoomBase extends RoomBaseAbs {
	/** 读写锁 */
	protected final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	/** 座位列表 */
	private final ArrayList<Seat> seats = new ArrayList<>();
	/** 缓存座位列表 */
	private final List<Seat> cacheSeatList = new ArrayList<Seat>();
	/** 玩法 */
	private PlayType playType;
	/** 创房类型 */
	private CreateRoomType createRoomType;
	/** 俱乐部房间 ，，如果不是俱乐部房间，，JulebuRoom.getDefaultInstance() **/
	private JulebuRoom julebuRoom;
	/** 房主id */
	private long masterId;
	/** 代理开房 */
	private ProxyRoom proxyRoom;
	/** 房间id */
	private int id;
	private String idStr;
	/** 庄家位置 */
	private Seat bankerSeat;
	/** 语音房间id */
	private String imId;
	/** 游戏是否结束 */
	private boolean gameOver;
	/** 对游戏发生改变的时间 */
	private long changeTimeInMillis;
	/** 第几把 */
	private int round;
	/** 是否已经开始 */
	private boolean start;
	/** 玩家同意解散房间推送 **/
	private final MemberDissolveRoomCast.Builder memberDissolveRoomCast = MemberDissolveRoomCast
			.newBuilder();
	/** 发起解散时间 */
	private long memberDissolveTimeInMillis;
	/*** 同意解散时效 **/
	private ScheduledFuture<?> memberDissolveRoomTask;
	/** 暂停之后自动解散房间时效 */
	private ScheduledFuture<?> pauseDissolveRoomTask;
	/** 创房时间 **/
	private long createTimeInMillis;
	/** 是否初始化 */
	private boolean isInit;

	public RoomBase() {
	}

	/**
	 * 房间初始化
	 * 
	 * @param playType
	 *            玩法
	 * @param createRoomType
	 *            创房类型
	 * @param masterId
	 *            房主UID
	 * @param julebuRoom
	 *            俱乐部房间
	 * @param id
	 *            房间ID
	 * @throws Exception
	 */
	public void init(PlayType playType, CreateRoomType createRoomType,
			long masterId, JulebuRoom julebuRoom, int id) throws Exception {
		super.init();
		this.seats.clear();
		this.playType = playType;
		this.createRoomType = createRoomType;
		this.julebuRoom = julebuRoom;
		this.masterId = masterId;
		this.id = id;
		this.idStr = ServerConfig.getRoomStr(this.id);
		this.bankerSeat = null;
		this.imId = RoomUtils.genImId();
		this.gameOver = false;
		this.changeTimeInMillis = System.currentTimeMillis();
		this.round = 0;
		this.start = false;
		this.memberDissolveRoomCast.clear();
		this.memberDissolveTimeInMillis = 0l;
		RoomUtils.checkNullTask(this.memberDissolveRoomTask, "时效数据没有清干净！");
		this.memberDissolveRoomTask = null;
		RoomUtils.checkNullTask(this.pauseDissolveRoomTask, "时效数据没有清干净！");
		this.pauseDissolveRoomTask = null;
		this.createTimeInMillis = System.currentTimeMillis();
		this.initEx();
		this.isInit = true;
		this.proxyRoomsAddCast();
	}

	/**
	 * 初始化扩展
	 * 
	 * @throws Exception
	 */
	public abstract void initEx() throws Exception;

	public PlayType getPlayType() {
		return playType;
	}

	public RoundType getRoundType() {
		return RoundType.getEnum(this.playType.getRound());
	}

	public BankerMode getBankerMode() {
		return BankerMode.getEnum(this.playType.getBankerMode());
	}

	public PType getPType() {
		return PType.getEnum(this.playType.getPType());
	}

	public JulebuRoom getJulebuRoom() {
		return julebuRoom;
	}

	private Guild getGuild() {
		if (isBelongGuild()) {
			Guild guild = GuildManager.getInstance().getGuild(getGuildId());
			if (guild == null) {
				LoggerService.getGuildlogs().error("缓存中找不到公会！严重错误！guildId:{}",
						getGuildId());
			}
			return guild;
		} else
			return null;
	}

	/**
	 * 更新俱乐部房间数据
	 */
	public void updateGuildRoomNum() {
		Guild guild = getGuild();
		if (guild != null)
			guild.setRoomNum(getNum(), this.getUserNum(), this.getStatus());
	}

	/**
	 * 属于公会会开房
	 * 
	 * @return
	 */
	public boolean isBelongGuild() {
		return this.createRoomType == CreateRoomType.JULEBU;
	}

	public int getGuildId() {
		return julebuRoom.getId();
	}

	/**
	 * 俱乐部房间编号
	 * 
	 * @return
	 */
	public int getNum() {
		return julebuRoom.getNum();
	}

	/**
	 * 加入座位
	 * 
	 * @param user
	 * @return 失败返回NULL
	 */
	//这个方法将在加入房间joinRoom中被调用
	private Seat addSeat(IncomeUserI user) {
		Seat seat = this.removeAndGetCacheSeat();
		if (seat == null) {
			return null;
		}
		seat.init();
		this.getSeats().add(seat);
		seat.setUser(user);
		return seat;
	}

	/** 移除座位 */
	private void removeSeat(Seat seat) {
		this.getSeats().remove(seat);
		seat.setUser(null);
		this.addCacheSeat(seat);
	}

	/** 移除所有的座位 **/
	private void removeAllSeat() {
		for (Seat st : this.getSeats()) {
			st.setUser(null);
			this.addCacheSeat(st);
		}
		this.getSeats().clear();
	}

	/** 向缓存中加入一个座位 */
	public void addCacheSeat(Seat seat) {
		cacheSeatSort.insert(this.cacheSeatList, seat);
	}

	/** 从缓存中获取一个座位 */
	public Seat removeAndGetCacheSeat() {
		if (this.cacheSeatList.isEmpty())
			return null;
		return this.cacheSeatList.remove(0);
	}

	public Seat getMasterSeat() {
		for (Seat st : this.seats) {
			if (st.isMaster()) {
				return st;
			}
		}
		return null;
	}

	public Seat getSeat(IncomeUserI user) {
		for (Seat seat : this.getSeats()) {
			if (seat.getUser() == user) {
				return seat;
			}
		}
		return null;
	}

	public ArrayList<Seat> getSeats() {
		return this.seats;
	}

	public Seat getSeatByIndex(int index) {
		return this.seats.get(index);
	}

	public Seat getSeatById(int id) {
		for (Seat st : this.seats) {
			if (st.getId() == id) {
				return st;
			}
		}
		return null;
	}

	public int getIndex(Seat seat) {
		return this.getSeats().indexOf(seat);
	}

	/**
	 * 玩家数
	 * 
	 * @return
	 */
	public int getUserNum() {
		return this.seats.size();
	}

	public long getMasterId() {
		return masterId;
	}

	public void setMasterId(long masterId) {
		this.masterId = masterId;
	}

	/**
	 * 是否属于房主创房
	 * 
	 * @return
	 */
	private boolean isMasterCreateRoom() {
		return this.getMasterSeat() != null && !this.isBelongGuild();
	}

	public int getId() {
		return this.id;
	}

	public String getIdStr() {
		return this.idStr;
	}

	/** 是否庄家 **/
	public boolean isBanker(Seat seat) {
		return this.bankerSeat != null && seat == this.bankerSeat;
	}

	public void setBankerSeat(Seat seat) {
		this.bankerSeat = seat;
	}

	/** 下个坐庄 */
	public void nextBanker() {
		switch (getBankerMode()) {
		case gudingzhuang:
			if (this.getBankerSeat() == null) {
				this.setBankerSeat(this.getSeats().get(0));
			}
			this.broadCast(MsgId.BankerCast, BankerCast.newBuilder()
					.setSeetIndex(this.getBankerSeat().getId()).build()
					.toByteArray());
			break;
		case lunzhuang:
			if (this.getBankerSeat() == null) {
				this.setBankerSeat(this.getSeats().get(0));
			} else {
				if (this.getBankerSeat().getNiu() == NiuType.zero) {
					this.setBankerSeat(this.getBankerSeat().nextOne());
				}
			}
			this.broadCast(MsgId.BankerCast, BankerCast.newBuilder()
					.setSeetIndex(this.getBankerSeat().getId()).build()
					.toByteArray());
			break;
		case qiangzhaung:
			this.setBankerSeat(null);
			break;
		default:
			break;
		}
		this.nextJu();
	}

	/**
	 * 语音房间id
	 * 
	 * @return
	 */
	public String getImId() {
		return imId;
	}

	/**
	 * 游戏是否结束
	 * 
	 * @return
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * 记录对游戏有改变的操作时间
	 */
	public void recordChange() {
		this.changeTimeInMillis = System.currentTimeMillis();
	}

	public long getChangeTimeInMillis() {
		return changeTimeInMillis;
	}

	/**
	 * 下一局开始
	 * 
	 * @throws Exception
	 */
	protected void nextJu() {
		super.nextJu();
		for (Seat st : this.seats) {
			st.nextJu();
		}
		++this.round;
	}

	public boolean isStart() {
		return this.start;
	}

	/**
	 * 0开始 1未开始
	 * 
	 * @return
	 */
	public int getStatus() {
		if (this.isGameOver())
			return 0;
		return this.start ? 1 : 0;
	}

	/**
	 * 游戏开始
	 */
	protected void start() {
		if (this.isMasterCreateRoom()) {
			getMasterSeat().addItem(ItemTemplate.Diamond_ID,
					-this.getRoundType().getNeedDiamond(), true,
					NTxt.ROOM_START_MASTER_CONSUME_DIAMOND);
		}
		this.start = true;
	}

	/**
	 * 暂停游戏
	 * 
	 * @param seconds
	 **/
	public void pauseGame(final DissolveRoomType dissolveRoomType) {
		long seconds;
		switch (dissolveRoomType) {
		case NOBODY_DISSOLVE:
			seconds = TimeConfig.getNobodyDissolveTimeInSecond();
			break;
		case JULEBU_HAVEONE_NOT_START_DISSOLVE:
			seconds = TimeConfig.getJulebuHaveOneNotStartDissolveTimeInSecond();
			break;
		case COMMON_HAVEONE_NOT_START_DISSOLVE:
			seconds = TimeConfig.getCommonHaveOneNotStartDissolveTimeInSecond();
			break;
		default:
			NTxt.println("未实现这个自动解散！");
			return;
		}
		RoomUtils.checkNullTask(this.pauseDissolveRoomTask, "时效数据没有清干净！");
		this.pauseDissolveRoomTask = TimeTaskUtil.getTaskmanager()
				.submitOneTimeTask(new Runnable() {
					@Override
					public void run() {
						RoomBase.this.dissolveRoom(dissolveRoomType);
						RoomBase.this.pauseDissolveRoomTask = null;
					}
				}, seconds, TimeUnit.SECONDS);
		LoggerService.getLogicLog().error("暂停游戏");
	}

	/** 唤醒游戏 **/
	public void wakeupGame() {
		if (this.pauseDissolveRoomTask != null) {
			this.pauseDissolveRoomTask.cancel(true);
			this.pauseDissolveRoomTask = null;
			LoggerService.getLogicLog().error("唤醒游戏");
		}
	}

	/**
	 * 是否初始化好
	 * 
	 * @return
	 */
	public boolean isInit() {
		return isInit;
	}

	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}

	/**
	 * 缓存广播
	 * 
	 * @param msgId
	 * @param bytes
	 */
	public void broadCastCache(byte[] bytes) {
		for (Seat seat : seats) {
			seat.cacheMsg(bytes);
		}
	}

	/**
	 * 广播
	 * 
	 * @param msgId
	 * @param bytes
	 */
	public void broadCast(MsgId msgId, byte[] bytes) {
		for (Seat seat : seats) {
			seat.sendMessage(msgId, bytes);
		}
	}

	/**
	 * 广播
	 * 
	 * @param msgId
	 * @param bytes
	 */
	public void broadCast(MsgId msgId, byte[] bytes, Seat exceptSeat) {
		for (Seat seat : seats) {
			if (seat != exceptSeat)
				seat.sendMessage(msgId, bytes);
		}
	}

	/**
	 * 加入房间
	 * 
	 * @param user
	 * @param sendJoinSuccess
	 *            是否发送加入成功
	 * @return
	 */
	public byte[] joinRoom(IncomeUserI user, boolean sendJoinSuccess) {
		//游戏如已开始，提示游戏进行中
		if (this.isStart()) {
			return MsgId.JoinRoomCm.gRErrMsg(NTxt.FORBIDDEN_GAME_RUNNING);
		}
		//玩家数如果大于5， 提示无剩余位置
		if (this.getUserNum() > SEAT_MAX) {
			return MsgId.JoinRoomCm.gRErrMsg(NTxt.JOIN_ROOM_NO_LEFT_SEAT);
		}
		//玩家如果在房间中，提示已经在房间中
		for (Seat st : this.getSeats()) {
			if (st.getUserUid() == user.getUid()) {
				return MsgId.JoinRoomCm.gRErrMsg(NTxt.ALREADY_IN_ROOM);
			}
		}
		if (RoomModule.getInstance().getRoom(user) != null) {
			return MsgId.JoinRoomCm.gRErrMsg(NTxt.ALREADY_IN_ROOM);
		}
		//写锁
		rwLock.writeLock().lock();
		try {
			if (RoomModule.getInstance().getRoom(user) != null) {
				return MsgId.JoinRoomCm.gRErrMsg(NTxt.ALREADY_IN_ROOM);
			}
			if (this.isStart()) {
				return MsgId.JoinRoomCm.gRErrMsg(NTxt.FORBIDDEN_GAME_RUNNING);
			}
			if (this.getUserNum() > SEAT_MAX) {
				return MsgId.JoinRoomCm.gRErrMsg(NTxt.JOIN_ROOM_NO_LEFT_SEAT);
			}
			for (Seat st : this.getSeats()) {
				if (st.getUserUid() == user.getUid()) {
					return MsgId.JoinRoomCm.gRErrMsg(NTxt.ALREADY_IN_ROOM);
				}
			}
			//加入座位 
			Seat seat = addSeat(user);
			if (seat == null) {
				return MsgId.JoinRoomCm.gRErrMsg(NTxt.JOIN_ROOM_NO_LEFT_SEAT);
			}
			if (sendJoinSuccess) {
				JoinRoomSm.Builder joinRoomSm = JoinRoomSm.newBuilder()
						.setCode(NTxt.SUCCESS).setSeatIndex(seat.getId())
						.setChatRoomId(this.getImId())
						.setPlayType(this.getPlayType())
						.setRoomId(this.getIdStr())
						.setJulebuRoom(this.getJulebuRoom());
				for (Seat st : this.getSeats()) {
					st.setUserInfo(joinRoomSm.addUsersBuilder());
				}
				user.sendMessage(MsgId.JoinRoomSm, joinRoomSm);
				JoinRoomCast.Builder joinRoomCast = JoinRoomCast.newBuilder();
				seat.setUserInfo(joinRoomCast.getAddUserBuilder());
				this.broadCast(MsgId.JoinRoomCast, joinRoomCast.build()
						.toByteArray(), seat);
				this.proxyRoomsUpdateCast();
			}
			this.wakeupGame();
			return null;
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	private void retDiamond(DissolveRoomType reason) {
		if (reason.isNormal()) {// 正常解散
			return;
		}
		// TODO
		LoggerService.getLogicLog().error("还钻！！！！！");
		try {
			switch (this.createRoomType) {
			case JULEBU:
				new FindUserHandlerI.ChangeDiamondHandler(
						UserGTGType.RetDiamondToGuildMaster,
						this.getMasterId(), this.getRoundType()
								.getNeedDiamond());
				break;
			case MASTER:
				getMasterSeat().addItem(ItemTemplate.Diamond_ID,
						this.getRoundType().getNeedDiamond(), true,
						NTxt.SPECIAL_DISSOLVE_ROOM);
				break;
			case PROXY:
				new FindUserHandlerI.ChangeDiamondHandler(
						UserGTGType.RetDiamondToProxy, this.getMasterId(), this
								.getRoundType().getNeedDiamond());
				break;
			default:
				NTxt.println(String.format("未实现还钻！%s", this.createRoomType));
				break;
			}
		} catch (Exception ee) {
			LoggerService.getRoomlogs().error(ee.getMessage(), ee);
		}
	}

	private boolean isAllPrepared() {
		if (this.getUserNum() < MIN_START) {
			return false;
		} else {
			for (Seat st : this.getSeats()) {
				if (!st.isPrepared()) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * 房间准备请求
	 * 
	 * @param seat
	 * @return
	 */
	public byte[] prepareRoom(Seat seat) {
		rwLock.writeLock().lock();
		try {
			return prepare(seat, false);
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	/**
	 * @param seat
	 * @param isCastToMe
	 *            是否发送给自己
	 * @return
	 */
	protected byte[] prepare(Seat seat, boolean isCastToMe) {
		if (this.isGameOver()) {
			return MsgId.PrepareRoomCm.gRErrMsg(NTxt.GAME_OVER);
		}
		if (!seat.compareAndSetGstate(GameState.noStart, GameState.prepared)) {
			return MsgId.PrepareRoomCm.gRErrMsg(NTxt.NOT_IN_STATE);
		}
		if (isCastToMe) {
			this.broadCast(MsgId.PrepareRoomCast, PrepareRoomCast.newBuilder()
					.setSeatIndex(seat.getId()).build().toByteArray());
		} else
			this.broadCast(MsgId.PrepareRoomCast, PrepareRoomCast.newBuilder()
					.setSeatIndex(seat.getId()).build().toByteArray(), seat);
		if (this.isAllPrepared()) {
			seat.sendMessage(MsgId.PrepareRoomSm, NTxt.PREPARE_ROOM_SUCCESS);
			this.start();
			return null;
		} else {
			return NTxt.PREPARE_ROOM_SUCCESS;
		}
	}

	/**
	 * 离开房间请求
	 * 
	 * @param seat
	 * @return
	 */
	public byte[] leave(Seat seat) {
		if (!this.isProxy() && seat.isMaster()) {
			return MsgId.LeaveRoomCm
					.gRErrMsg(NTxt.LEAVE_ROOM_MASTER_CAN_NOT_LEAVE);
		}
		if (this.isStart()) {
			return MsgId.LeaveRoomCm.gRErrMsg(NTxt.FORBIDDEN_GAME_RUNNING);
		}
		rwLock.writeLock().lock();
		try {
			if (!this.isProxy() && seat.isMaster()) {
				return MsgId.LeaveRoomCm
						.gRErrMsg(NTxt.LEAVE_ROOM_MASTER_CAN_NOT_LEAVE);
			}
			if (this.isStart()) {
				return MsgId.LeaveRoomCm.gRErrMsg(NTxt.FORBIDDEN_GAME_RUNNING);
			}
			IncomeUserI user = seat.getUser();
			int id2 = seat.getId();
			this.removeSeat(seat);
			user.sendMessage(MsgId.LeaveRoomSm, NTxt.LEAVE_ROOM_SUCCESS);
			this.broadCast(MsgId.JoinRoomCast, JoinRoomCast.newBuilder()
					.setDelSeatIndex(id2).build().toByteArray());
			this.proxyRoomsUpdateCast();
			this.checkAllOffLine();
			return null;
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	/**
	 * 解散房间请求
	 * 
	 * @param seat
	 * @param genMessageLite
	 */
	public byte[] dissolveRoom(Seat seat, DissolveRoomCm genMessageLite) {
		if (!seat.isMaster()) {
			return MsgId.DissolveRoomCm
					.gRErrMsg(NTxt.DISSOLVE_ROOM_NO_RIGHT_TO_DO);
		}
		IncomeUserI user = seat.getUser();
		if (this.isStart()) {
			return MsgId.DissolveRoomCm
					.gRErrMsg(NTxt.DISSOLVE_ROOM_FORBIDDEN_GAME_RUNNING);
		}
		this.dissolveRoom(DissolveRoomType.DISSOLVE_OPERATION);
		user.sendMessage(MsgId.DissolveRoomSm, NTxt.DISSOVE_ROOM_SUCCESS);
		return null;
	}

	/**
	 * 解散房间
	 * 
	 * @param reason
	 *            解散原因
	 * @return
	 */
	public void dissolveRoom(DissolveRoomType reason) {
		rwLock.writeLock().lock();
		if (!isInit()) {
			return;
		}
		setInit(false);
		try {
			this.gameOver = true;
			Guild guild = getGuild();
			if (guild != null) {
				guild.setRoomId(getNum(), SystemConstantsAbs.NoRoomId);
			}
			if (this.isStart()) {
				dis();
				GameOverCast.Builder gameOverCast = GameOverCast.newBuilder();
				Seat winner = this.getSeatByIndex(0);
				for (Seat seat : seats) {
					if (seat.getMinusCoin() > winner.getMinusCoin()) {
						winner = seat;
					}
					gameOverCast.addRs(seat.getSeetResult());
				}
				for (Seat seat : seats) {
					if (winner.getMinusCoin() > 0
							&& seat.getMinusCoin() == winner.getMinusCoin()) {
						gameOverCast.addSeetIndex(seat.getId());
					}
				}
				if (guild != null) {
					List<Integer> seetIndexList = gameOverCast
							.getSeetIndexList();
					WinnerNumType type = WinnerNumType.getEnum(seetIndexList
							.size());
					for (int seatIndex : seetIndexList) {
						guild.changeMemberWinNum(getSeatByIndex(seatIndex)
								.getUserUid(), type.getAdd());
					}
					for (Seat seat : seats)
						guild.addMemberActiveNum(seat.getUserUid());
				}
				if (reason == DissolveRoomType.GAME_OVER_NO_NEXT_BANKER)
					this.broadCastCache(gameOverCast.build().toByteArray());
				else {
					this.broadCast(MsgId.GameOverCast, gameOverCast.build()
							.toByteArray());
				}
				int index = 0;
				try {
					index = GamerecordDao.insertGamerecord(new GamerecordBean(
							0, this.gameRecordSm.build().toByteArray()));
				} catch (Exception e) {
					LoggerService.getRoomlogs().error(
							String.format(
									"insertGamerecord!roomid:%s,%s,error:%s",
									this.idStr, reason.getDesc(),
									e.getMessage()), e);
				}
				GameRecord.Builder gameRecordBuilder = GameRecord.newBuilder()
						.setRound(this.gameRecordSm.getRoundCount())
						.setRoomId(this.idStr)
						.setTime(System.currentTimeMillis()).setIndex(index);
				for (Seat seat : seats) {
					gameRecordBuilder.addNickname(seat.getUserNickname())
							.addCoin(seat.getMinusCoin());
				}
				GameRecord gameRecord = gameRecordBuilder.build();
				for (Seat seat : seats) {
					seat.getUser().recordGame(gameRecord);
				}
				if (guild != null) {
					guild.recordGame(gameRecord);
				}
				if (this.isProxy())
					new FindUserHandlerI.ProxyRecordHandler(
							UserGTGType.proxyRecord, this.getMasterId(),
							gameRecord);
			}
			this.retDiamond(reason);
			// ////////////
			if (reason == DissolveRoomType.MEMBER_DISSOLVE_ALL_AGREE) {// 放到520之后，，客户端要求
				this.broadCast(MsgId.MemberDissolveRoomCast,
						this.memberDissolveRoomCast.setIsOk(1).build()
								.toByteArray());
				this.resetMember();
			}
			if (!this.isStart())
				for (Seat seat : seats) {
					seat.sendMessage(MsgId.DissolveRoomCast,
							SystemConstants.dissolveRoomCast);
				}
			this.removeAllSeat();
			this.updateGuildRoomNum();
			// //////////这个一定在最后执行！
			try {
				RoomModule.getInstance().removeRoom(this.id);
			} catch (Exception e) {
				LoggerService.getRoomlogs().error(
						String.format("移除房间错误!roomid:%s,%s,error:%s",
								this.idStr, reason.getDesc(), e.getMessage()),
						e);
			}
			LoggerService.getRoomlogs().warn("移除房间！roomid:{},{}", this.idStr,
					reason.getDesc());
			NTxt.println(reason.getDesc());
			this.proxyRoomsDelCast();
		} finally {
			rwLock.writeLock().unlock();
			wakeupGame();
		}
	}

	/**
	 * 游戏开始了，解散处理
	 */
	public void dis() {
	}

	public byte[] reconnect(Seat seat, ReconnectCm genMessageLite) {
		rwLock.readLock().lock();
		try {
			seat.setUserState(UserState.online);
			ReconnectSm.Builder reconnectSm = ReconnectSm.newBuilder().setCode(
					NTxt.SUCCESS);
			for (Seat st : this.getSeats()) {
				if (st.getUser() != null)
					st.setUserInfo(reconnectSm.addUsersBuilder());
			}
			reconnectSm.setPlayType(this.getPlayType())
					.setRoomId(this.getIdStr()).setChatRoomId(this.getImId());
			for (Seat st : this.getSeats()) {
				st.setUserRoundInfo(reconnectSm.addUsersRoundBuilder());
			}
			reconnectSm.setStart(this.isStart());
			reconnectSm.setRound(this.getRound());
			reconnectSm.setJulebuRoom(this.getJulebuRoom());
			reconnectSm.setBullResultCast(this.bullResultCast.build());
			if (this.getBankerSeat() != null)
				reconnectSm.setBankerIndex(this.getBankerSeat().getId());
			seat.sendMessage(MsgId.ReconnectSm, reconnectSm.build()
					.toByteArray());
			if (memberDissolveRoomCast.getAgreeSeetIndexCount() != 0)
				seat.sendMessage(MsgId.MemberDissolveRoomCast,
						this.memberDissolveRoomCast.build().toByteArray());
		} finally {
			rwLock.readLock().unlock();
		}
		return null;
	}

	public Seat getBankerSeat() {
		return this.bankerSeat;
	}

	public int getRound() {
		return this.round;
	}

	public int getTotalRound() {
		return this.getRoundType().getNum();
	}

	/**
	 * 玩家解散房间请求
	 * 
	 * @param seat
	 * @param genMessageLite
	 * @return
	 */
	public byte[] memberDissolveRoom(Seat seat,
			MemberDissolveRoomCm genMessageLite) {
		boolean agree = genMessageLite.getAgree();
		rwLock.writeLock().lock();
		try {
			if (!agree && seat.getAgreeDissolveRoom() == MemberDis.empty) {
				return MsgId.MemberDissolveRoomCm
						.gRErrMsg(NTxt.MEMBER_DISSOLVEROOM_COMMON_ERROR);
			}
			if (seat.getAgreeDissolveRoom() == MemberDis.empty) {// 第一个请求解散的
				for (Seat st : this.seats) {
					st.setAgreeDissolveRoom(MemberDis.waiting);
				}
				memberDissolveRoomCast.clear();
				this.memberDissolveTimeInMillis = System.currentTimeMillis();
				RoomUtils.checkNullTask(this.memberDissolveRoomTask,
						"时效数据没有清干净！");
				this.memberDissolveRoomTask = TimeTaskUtil.getTaskmanager()
						.submitOneTimeTask(
								new Runnable() {
									@Override
									public void run() {
										for (Seat st : seats) {
											if (st.getAgreeDissolveRoom() == MemberDis.waiting) {
												st.setAgreeDissolveRoom(MemberDis.agree);
												memberDissolveRoomCast
														.addAgreeSeetIndex(st
																.getId());
											}
										}
										checkMemberDissolve();
									}

								},
								TimeConfig.getMemberDissolveRoomTimeInMillis(),
								TimeUnit.MILLISECONDS);
			}
			if (seat.getAgreeDissolveRoom() != MemberDis.waiting) {
				return MsgId.MemberDissolveRoomCm
						.gRErrMsg(NTxt.MEMBER_DISSOLVEROOM_AGREE_ALREADY);
			}
			if (agree) {
				seat.setAgreeDissolveRoom(MemberDis.agree);
				memberDissolveRoomCast.addAgreeSeetIndex(seat.getId());
			} else {
				seat.setAgreeDissolveRoom(MemberDis.disagree);
				memberDissolveRoomCast.addDisagreeSeetIndex(seat.getId());
			}
			seat.sendMessage(MsgId.MemberDissolveRoomSm,
					NTxt.MEMBER_DISSOLVE_SUCCESS);
			checkMemberDissolve();
			return null;
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	private void checkMemberDissolve() {
		int agCont = 0;
		int diagCount = 0;
		for (Seat st : this.seats) {
			if (st.getAgreeDissolveRoom() == MemberDis.agree) {
				agCont++;
			} else if (st.getAgreeDissolveRoom() == MemberDis.disagree) {
				diagCount++;
			}
		}
		float halfNum = this.getUserNum() / 2.0f;
		if (agCont > halfNum) {
			this.dissolveRoom(DissolveRoomType.MEMBER_DISSOLVE_ALL_AGREE);
		} else if (diagCount >= halfNum) {
			this.broadCast(MsgId.MemberDissolveRoomCast,
					this.memberDissolveRoomCast.setIsOk(2).build()
							.toByteArray());
			this.resetMember();
		} else {
			this.broadCast(
					MsgId.MemberDissolveRoomCast,
					this.memberDissolveRoomCast
							.setIsOk(0)
							.setMemberDisInMillis(
									TimeConfig
											.getMemberDissolveRoomTimeInMillis()
											- (System.currentTimeMillis() - this.memberDissolveTimeInMillis))
							.build().toByteArray());
		}
	}

	private void resetMember() {
		for (Seat st : this.getSeats()) {
			st.setAgreeDissolveRoom(MemberDis.empty);
		}
		this.memberDissolveRoomCast.clear();
		this.memberDissolveTimeInMillis = 0l;
		this.memberDissolveRoomTask.cancel(true);
		this.memberDissolveRoomTask = null;
	}

	/**
	 * im 信息保存请求
	 * 
	 * @param seat
	 * @param genMessageLite
	 * @return
	 */
	public byte[] imInfoSave(Seat seat, ImInfoSaveCm genMessageLite) {
		int index = genMessageLite.getIndex();
		seat.setImSeatIndex(index);
		return NTxt.IM_INFO_SAVE_SUCCESS;
	}

	public byte[] chat(Seat seat, ChatCm genMessageLite) {
		int id2 = genMessageLite.getId();
		String content = genMessageLite.getContent();
		int type = genMessageLite.getType();
		seat.sendMessage(MsgId.ChatSm, NTxt.CHAT_SUCCESS);
		this.broadCast(MsgId.ChatCast, ChatCast.newBuilder().setId(id2)
				.setContent(content).setType(type).setSeetIndex(seat.getId())
				.build().toByteArray());
		return null;
	}

	public byte[] im(Seat seat, ImCm genMessageLite) {
		String fileid = genMessageLite.getFileid();
		seat.sendMessage(MsgId.ImSm, NTxt.IM_SUCCESS);
		this.broadCast(MsgId.ImCast,
				ImCast.newBuilder().setSeetIndex(seat.getId())
						.setFileid(fileid).build().toByteArray(), seat);
		return null;
	}

	public byte[] ns(Seat seat, NsCm genMessageLite) {
		seat.sendMessage(MsgId.NsSm, NTxt.NS_SUCCESS);
		seat.setNs(genMessageLite);
		this.broadCast(
				MsgId.NsCast,
				NsCast.newBuilder().setSeetIndex(seat.getId())
						.setNs(genMessageLite).build().toByteArray(), seat);

		return null;
	}

	/** 检测是否都下线了 **/
	public void checkAllOffLine() {
		boolean allOffline = true;
		for (Seat st : seats) {
			if (st.getUser() != null && st.getUserState() == UserState.online) {
				allOffline = false;
				break;
			}
		}
		if (allOffline && !this.isStart()) {
			if (!this.isProxy() && this.getUserNum() == 0) {
				this.pauseGame(DissolveRoomType.NOBODY_DISSOLVE);
			} else if (this.isBelongGuild())
				this.pauseGame(DissolveRoomType.JULEBU_HAVEONE_NOT_START_DISSOLVE);
			else
				this.pauseGame(DissolveRoomType.COMMON_HAVEONE_NOT_START_DISSOLVE);
		}
	}

	public boolean hasSameIp(String ip) {
		for (Seat seat : this.seats) {
			if (seat.getUserIp().equals(ip)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 保存房间数据
	 */
	public void save() {
		// TODO Auto-generated method stub
	}

	/**
	 * 记录战斗回放到数据库
	 * 
	 * @return
	 */
	public int recordBattleback() {
		for (Seat st : this.seats) {
			this.battleBackSm.addRs(st.getChangeCoin());
		}
		int insertBattleback = -1;
		try {
			insertBattleback = BattlebackDao
					.insertBattleback(new BattlebackBean(0, this.battleBackSm
							.build().toByteArray()));
		} catch (SQLException e) {
			LoggerService.getRoomlogs().error(e.getMessage(), e);
		}
		return insertBattleback;
	}

	public boolean isProxy() {
		return this.createRoomType == CreateRoomType.PROXY;
	}

	/**
	 * 代理房间增加
	 */
	public void proxyRoomsAddCast() {
		if (!isProxy()) {
			return;
		}
		this.genProxyRoom();
		User user = UserManager.getInstance().getUser(this.masterId);
		if (user != null && user.isOnline()) {
			user.addProxy(this.getId(), this.getProxyRoom());
		}
	}

	/**
	 * 代理房间解散
	 */
	public void proxyRoomsDelCast() {
		if (!isProxy()) {
			return;
		}
		User user = UserManager.getInstance().getUser(this.masterId);
		if (user != null && user.isOnline()) {
			user.removeProxy(this.getId());
		}
	}

	/**
	 * 代理房间更新
	 */
	public void proxyRoomsUpdateCast() {
		if (!isProxy()) {
			return;
		}
		this.genProxyRoom();
		User user = UserManager.getInstance().getUser(this.masterId);
		if (user != null && user.isOnline()) {
			user.updateProxyRoom(this.getId(), this.getProxyRoom(), true);
		}
	}

	private void genProxyRoom() {
		ProxyRoom.Builder proxyRoom = ProxyRoom.newBuilder()
				.setRoomId(this.getIdStr()).setPlayType(playType)
				.setState(this.isStart() ? 2 : 1)
				.setTime(this.getCreateTimeInMillis());
		for (Seat seat : this.seats) {
			if (seat.getUser() != null) {
				proxyRoom.addUserBuilder()
						.setHeadimg(ServerConfig.getHeadUrl(seat.getUserUid()))
						.setNickname(seat.getUser().getNickname())
						.setUid(seat.getUser().getUid());
			}
		}
		this.proxyRoom = proxyRoom.build();
	}

	public long getCreateTimeInMillis() {
		return createTimeInMillis;
	}

	public ProxyRoom getProxyRoom() {
		return this.proxyRoom;
	}

}
