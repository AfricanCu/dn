package com.wk.server.ibatis.select;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import msg.GuildMessage.AddJulebuCast;
import msg.GuildMessage.DelJulebuCast;
import msg.GuildMessage.UpdateJulebuCast;
import msg.LoginMessage.GameRecord;
import msg.LoginMessage.Julebu;
import msg.LoginMessage.LoginSm;
import msg.LoginMessage.PlayerCast;
import msg.RoomMessage.ProxyRoom;
import msg.RoomMessage.ProxyRoomsAddCast;
import msg.RoomMessage.ProxyRoomsCast;
import msg.RoomMessage.ProxyRoomsDelCast;
import msg.RoomMessage.ProxyRoomsUpdateCast;

import org.json.JSONObject;

import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.SystemConstantsAbs;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.event.EventType;
import com.wk.engine.net.I.CsConnectI;
import com.wk.enun.UserState;
import com.wk.guild.bean.GuildBean;
import com.wk.logic.config.NTxt;
import com.wk.logic.config.TimeConfig;
import com.wk.logic.enm.MsgId;
import com.wk.server.logic.guild.Guild;
import com.wk.server.logic.guild.GuildManager;
import com.wk.server.logic.guild.enm.RemoveJoinJulebuType;
import com.wk.server.logic.item.ItemTemplate;
import com.wk.server.logic.login.LoginModule;
import com.wk.server.logic.login.UserManager;
import com.wk.server.logic.room.GameTimesType;
import com.wk.server.logic.room.RoomAbs;
import com.wk.server.logic.room.RoomModule;
import com.wk.server.logic.room.RoomUtils;
import com.wk.server.logic.room.Seat;
import com.wk.user.bean.UserBean;
import com.wk.util.TimeTaskUtil;

/**
 * 玩家收益
 * 
 * @author ems
 */
public abstract class IncomeUserI extends UserBean implements CsConnectI<MsgId> {
	/** 读写锁 */
	private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	// tmp
	/** 个人信息消息Builder */
	private PlayerCast.Builder playerCast = null;
	/** 是否需要发送玩家信息更新 */
	private boolean isNeedSendPlayerSm = false;
	/** 玩家当前所在房间 */
	private RoomAbs room = null;
	/** 心跳从什么时候挂起 */
	private long heartPause = 0;
	/** 缓存游戏结束消息 */
	private byte[] gameOverCast;
	/** 代理开房更新任务 **/
	private ScheduledFuture<?> proxyRoomTask;

	public IncomeUserI() {
		super(LoginModule.getInstance());
	}

	public void reset() {
		super.reset();
		this.playerCast = null;
		this.isNeedSendPlayerSm = false;
		this.room = null;
		this.heartPause = 0;
		this.gameOverCast = null;
		RoomUtils.checkNullTask(this.proxyRoomTask, "时效数据未请干净！");
		this.proxyRoomTask = null;
	}

	@Override
	public void recordGame(GameRecord record) {
		super.recordGame(record);
		this.sendPlayerRecordCast();
	}

	@Override
	public void recordProxyGame(GameRecord record) {
		super.recordProxyGame(record);
		this.sendProxyRecordCast();
	}

	/**
	 * 加入多组道具
	 * 
	 * @param items
	 * @param isCheck
	 *            是否检测发送
	 */
	public void addItems(Map<ItemTemplate, Integer> items, boolean isCheck,
			String log) {
		rwLock.writeLock().lock();
		try {
			for (Entry<ItemTemplate, Integer> entry : items.entrySet()) {
				addItem(entry.getKey(), entry.getValue(), false, false, log);
			}
		} finally {
			rwLock.writeLock().unlock();
		}
		if (isCheck) {
			this.checkSendPlayer();
		}
	}

	/**
	 * 加入道具
	 * 
	 * @param districtType
	 * @param itemId
	 * @param itemNum
	 * @param isCheck
	 *            是否检测发送
	 */
	public void addItem(ItemTemplate itemId, int itemNum, boolean isCheck,
			String log) {
		this.addItem(itemId, itemNum, isCheck, true, log);
	}

	/**
	 * 加入道具
	 * 
	 * @param districtType
	 * @param itemId
	 * @param itemNum
	 * @param isCheck
	 *            是否检测发送
	 * @param isWriteLock
	 *            是否写锁
	 */
	private void addItem(ItemTemplate itemId, int itemNum, boolean isCheck,
			boolean isWriteLock, String log) {
		if (itemNum == 0) {
			return;
		}
		if (isWriteLock)
			rwLock.writeLock().lock();
		try {
			switch (itemId) {
			case Diamond_ID:
				this.changeDiamond(itemNum);
				break;
			default:
				break;
			}
		} finally {
			if (isWriteLock)
				rwLock.writeLock().unlock();
		}
		LoggerService.getItemlogs().warn(
				"uid:{},nickname:{},itemId:{},itemNum:{},log:{}",
				new Object[] { this.uid, this.nickname, itemId, itemNum, log });
		if (isCheck) {
			this.checkSendPlayer();
		}
	}

	/**
	 * 玩家处理事件
	 * 
	 * @param eventType
	 * @param params
	 */
	public void handleEvent(EventType eventType, Object... params) {
	}

	public PlayerCast.Builder getPlayerCast() {
		if (playerCast == null) {
			playerCast = PlayerCast.newBuilder().setDiamond(this.getDiamond());
		}
		return playerCast;
	}

	/**
	 * 发送玩家数据
	 * 
	 * @return
	 */
	public IncomeUserI sendPlayer() {
		this.sendMessage(MsgId.PlayerCast, getPlayerCast());
		return this;
	}

	/**
	 * 检测是否必要发送玩家数据
	 * 
	 * @return
	 */
	public IncomeUserI checkSendPlayer() {
		if (this.isNeedSendPlayerSm) {
			this.sendPlayer();
			this.isNeedSendPlayerSm = false;
		}
		return this;
	}

	/**
	 * 发送玩家记录数据
	 * 
	 * @return
	 */
	public IncomeUserI sendPlayerRecordCast() {
		this.sendMessage(MsgId.PlayerRecordCast, this.getGameRecordCast()
				.build().toByteArray());
		return this;
	}

	/**
	 * proxy 游戏记录
	 * 
	 * @return
	 */
	public IncomeUserI sendProxyRecordCast() {
		this.sendMessage(MsgId.ProxyRecordCast, this.getProxyRecordCast()
				.build().toByteArray());
		return this;
	}

	/**
	 * 当前所在房间
	 * 
	 * @return
	 */
	public RoomAbs getRoom() {
		return room;
	}

	public void setRoom(RoomAbs room) {
		this.room = room;
		if (this.room == null) {
			this.setRoomId(SystemConstantsAbs.NoRoomId);
			LoginModule.getInstance().decrementRoomUser();
			if (!this.isOnline()) {
				if (!this.cannotChangeServerId()) {
					UserManager.getInstance().removeUser(this.getUid());
				}
				String format = String.format("退出房间了，但玩家已经离线了！uid:%s,nick%s",
						this.uid, this.nickname);
				NTxt.warn(format, 2);
				LoggerService.getRoomlogs().error(format);
			}
		} else {
			this.setRoomId(this.room.getId());
			LoginModule.getInstance().incrementRoomUser();
		}
		try {
			this.save();
		} catch (SQLException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
		}
	}

	@Override
	public void setDiamond(int diamond) {
		super.setDiamond(diamond);
		this.getPlayerCast().setDiamond(diamond);
		setNeedSendPlayerSm();
	}

	public void setNeedSendPlayerSm() {
		if (this.isNeedSendPlayerSm) {
			return;
		}
		this.isNeedSendPlayerSm = true;
	}

	@Override
	public boolean isOnline() {
		return super.isOnline() && this.getState() == UserState.online;
	}

	public void setState(UserState state) {
		super.setState(state);
		if (state == UserState.offline) {
			if (room != null) {
				Seat seat = room.getSeat(this);
				if (seat == null) {
					NTxt.println("严重bug！找不到座位！");
				} else
					seat.setUserState(UserState.offline);
			}
			this.setLogoutTime(new Date(System.currentTimeMillis() - 1000));
		} else if (state == UserState.online) {
			this.setLoginTime(new Date());
		}
	}

	public void setHeartPause(long heartPause) {
		this.heartPause = heartPause;
	}

	public long getHeartPause() {
		return heartPause;
	}

	public void sendMessage(MsgId msgId, MessageLite.Builder builder) {
		this.sendMessage(msgId, builder.build().toByteArray());
	}

	public void sendMessage(MsgId msgId, MessageLite messageLite) {
		this.sendMessage(msgId, messageLite.toByteArray());
	}

	public void assembleJulebuMsg(LoginSm.Builder loginSm) {
		for (Entry<Integer, JSONObject> entry : this.myGuild.entrySet()) {
			Julebu assembleJulebu = assembleJulebu(entry.getKey(),
					entry.getValue());
			if (assembleJulebu != null)
				loginSm.addMy(assembleJulebu);
		}
		for (Entry<Integer, JSONObject> entry : this.joinGuild.entrySet()) {
			Julebu assembleJulebu = assembleJulebu(entry.getKey(),
					entry.getValue());
			if (assembleJulebu != null)
				loginSm.addJoin(assembleJulebu);
		}
	}

	/**
	 * 组装俱乐部
	 * 
	 * @param key
	 * @param value
	 * @return 如果当前玩家登陆了这个区域,则返回这个区域俱乐部,否则null
	 */
	public Julebu assembleJulebu(Integer key, JSONObject value) {
		Guild guild = GuildManager.getInstance().getGuild(key);
		return Julebu
				.newBuilder()
				.setId(key)
				.setName(value.optString(julebuName_index))
				.setHeadimg(
						ServerConfig.getHeadUrl(value
								.optLong(julebuMasterUid_index)))
				.setMasterUid(value.optLong(julebuMasterUid_index))
				.setMasterName(value.optString(julebuMasterName_index))
				.setState(value.optInt(julebuState_index))
				.setPlayType(value.optString(julebuPlayType_index))
				.setNum(guild != null ? guild.getSumNum() : 0).build();
	}

	@Override
	public JSONObject createMyJulebu(GuildBean guild) {
		JSONObject createMyJulebu = super.createMyJulebu(guild);
		if (createMyJulebu == null) {
			return createMyJulebu;
		}
		Julebu assembleJulebu = assembleJulebu(guild.getId(), createMyJulebu);
		if (assembleJulebu != null)
			this.sendMessage(MsgId.AddJulebuCast, AddJulebuCast.newBuilder()
					.setMy(assembleJulebu).setType(2));
		return createMyJulebu;
	}

	@Override
	public JSONObject removeMyJulebu(int guildId) {
		JSONObject removeMyJulebu = super.removeMyJulebu(guildId);
		if (removeMyJulebu == null) {
			return removeMyJulebu;
		}
		this.sendMessage(
				MsgId.DelJulebuCast,
				DelJulebuCast.newBuilder().setId(guildId)
						.setType(RemoveJoinJulebuType.dissolve.getType()));
		return removeMyJulebu;
	}

	@Override
	public JSONObject applyJulebu(GuildBean guild) {
		JSONObject applyJulebu = super.applyJulebu(guild);
		if (applyJulebu == null) {
			return applyJulebu;
		}
		Julebu assembleJulebu = assembleJulebu(guild.getId(), applyJulebu);
		if (assembleJulebu != null)
			this.sendMessage(MsgId.AddJulebuCast, AddJulebuCast.newBuilder()
					.setMy(assembleJulebu).setType(1));
		return applyJulebu;
	}

	public JSONObject removeJoinJulebu(int guildId, RemoveJoinJulebuType type) {
		JSONObject removeJoinJulebu = super.removeJoinJulebu(guildId);
		if (removeJoinJulebu == null) {
			return removeJoinJulebu;
		}
		this.sendMessage(
				MsgId.DelJulebuCast,
				DelJulebuCast.newBuilder().setId(guildId)
						.setType(type.getType()));
		return removeJoinJulebu;
	}

	@Override
	public JSONObject joinJulebu(int guildId) {
		JSONObject joinJulebu = super.joinJulebu(guildId);
		if (joinJulebu == null) {
			return joinJulebu;
		}
		Julebu assembleJulebu = assembleJulebu(guildId, joinJulebu);
		if (assembleJulebu != null)
			this.sendMessage(MsgId.UpdateJulebuCast, UpdateJulebuCast
					.newBuilder().setMy(assembleJulebu).setType(1));
		return joinJulebu;
	}

	@Override
	public JSONObject updateJulebuInfo(int guildId, String name,
			String playTypeDesc) {
		JSONObject updateJulebuInfo = super.updateJulebuInfo(guildId, name,
				playTypeDesc);
		if (updateJulebuInfo == null) {
			return updateJulebuInfo;
		}
		Julebu assembleJulebu = assembleJulebu(guildId, updateJulebuInfo);
		if (assembleJulebu != null) {
			this.sendMessage(MsgId.UpdateJulebuCast, UpdateJulebuCast
					.newBuilder().setMy(assembleJulebu).setType(2));
		}
		return updateJulebuInfo;
	}

	@Override
	public int getCreateGuildServerId() {
		int createGuildServerId = super.getCreateGuildServerId();
		return createGuildServerId == SystemConstantsAbs.NoServerId ? ServerConfig.serverId
				: createGuildServerId;
	}

	public abstract String getIp();

	@Override
	public String toString() {
		return String.format("uid:%s,nick:%s,roomId:%s,room:%s", this.getUid(),
				this.getNickname(), this.getRoomId(),
				this.getRoom() == null ? "" : this.getRoom().toString());
	}

	public void cacheGameOverCast(byte[] bytes) {
		this.gameOverCast = bytes;
	}

	/**
	 * 如果有大结算发送大结算
	 * 
	 * @return
	 */
	public boolean sendCacheGameOverCast() {
		if (this.gameOverCast != null) {
			this.sendMessage(MsgId.NextRoundSm,
					MsgId.NextRoundCm.gRErrMsg(NTxt.GAME_OVER));
			this.sendMessage(MsgId.GameOverCast, this.gameOverCast);
			this.gameOverCast = null;
			return true;
		}
		return false;
	}

	/**
	 * 发送代理开房列表
	 * 
	 * @return
	 */
	public IncomeUserI sendProxyRoomsCast() {
		if (!this.proxy.isEmpty()) {
			ProxyRoomsCast.Builder proxyRoomsCast = ProxyRoomsCast.newBuilder();
			for (Iterator<Integer> iter = this.proxy.keySet().iterator(); iter
					.hasNext();) {
				int roomId = iter.next();
				RoomAbs room = RoomModule.getInstance().getRoom(roomId);
				if (room != null && !room.isGameOver()) {
					proxyRoomsCast.addRooms(room.getProxyRoom());
				} else {
					iter.remove();
					this.setProxyKey();
					LoggerService.getLogicLog().error("房间已经解散！{}", roomId);
				}
			}
			this.sendMessage(MsgId.ProxyRoomsCast, proxyRoomsCast);
		}
		return this;
	}

	public void addProxy(int id, ProxyRoom proxyRoom) {
		super.addProxy(id, proxyRoom);
		ProxyRoomsAddCast.Builder proxyRoomsAddCast = ProxyRoomsAddCast
				.newBuilder();
		proxyRoomsAddCast.addRooms(proxyRoom);
		this.sendMessage(MsgId.ProxyRoomsAddCast, proxyRoomsAddCast);
	}

	public void removeProxy(int id) {
		super.removeProxy(id);
		ProxyRoomsDelCast.Builder proxyRoomsDelCast = ProxyRoomsDelCast
				.newBuilder().addRoomIds(ServerConfig.getRoomStr(id));
		this.sendMessage(MsgId.ProxyRoomsDelCast, proxyRoomsDelCast);
	}

	public void updateProxyRoom(int id, ProxyRoom proxyRoom, boolean rightNow) {
		super.addProxy(id, proxyRoom);
		if (rightNow) {
			ProxyRoomsUpdateCast.Builder proxyRoomsUpdateCast = ProxyRoomsUpdateCast
					.newBuilder();
			for (ProxyRoom rr : IncomeUserI.this.proxy.values()) {
				if (rr != null)
					proxyRoomsUpdateCast.addRooms(rr);
			}
			IncomeUserI.this.sendMessage(MsgId.ProxyRoomsUpdateCast,
					proxyRoomsUpdateCast.build().toByteArray());
		} else if (this.proxyRoomTask == null) {
			this.proxyRoomTask = TimeTaskUtil.getTaskmanager()
					.submitOneTimeTask(
							new Runnable() {
								@Override
								public void run() {
									ProxyRoomsUpdateCast.Builder proxyRoomsUpdateCast = ProxyRoomsUpdateCast
											.newBuilder();
									for (ProxyRoom rr : IncomeUserI.this.proxy
											.values()) {
										if (rr != null)
											proxyRoomsUpdateCast.addRooms(rr);
									}
									IncomeUserI.this.sendMessage(
											MsgId.ProxyRoomsUpdateCast,
											proxyRoomsUpdateCast.build()
													.toByteArray());
									IncomeUserI.this.proxyRoomTask = null;
								}
							}, TimeConfig.getUpdateProxyRoomTimeInSecond(),
							TimeUnit.SECONDS);
		}
	}

	/**
	 * 前几次游戏输分太多？
	 * 
	 * @return
	 */
	public boolean isGameMinusTooMuch() {
		List<GameRecord> recordList = this.getGameRecordCast().getRecordList();
		String nickname = this.getNickname();
		return GameTimesType.isGameMinusTooMuch(recordList, nickname);
	}
}
