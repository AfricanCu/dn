package com.wk.server.logic.room;

import io.netty.channel.Channel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import msg.BullMessage.BetOnCm;
import msg.BullMessage.QiangZhuangCm;
import msg.BullMessage.ReconnectCm;
import msg.RoomMessage.ChatCm;
import msg.RoomMessage.CreateRoomBeforeCm;
import msg.RoomMessage.CreateRoomCm;
import msg.RoomMessage.DissolveRoomCm;
import msg.RoomMessage.ImCm;
import msg.RoomMessage.JoinRoomBeforeCm;
import msg.RoomMessage.JoinRoomCm;
import msg.RoomMessage.MemberDissolveRoomCm;
import msg.RoomMessage.PrepareRoomCm;
import msg.RoomMessage.ProxyCreateRoomBeforeCm;
import msg.RoomMessage.ProxyCreateRoomCm;
import msg.RoomMessage.ProxyRoomsDissovleCm;

import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.db.IbatisDbServer;
import com.wk.engine.ModuleAbs;
import com.wk.engine.event.EventAbs;
import com.wk.engine.event.EventType;
import com.wk.engine.net.IoMessage;
import com.wk.logic.config.DissolveRoomType;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.MsgId;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.ibatis.select.User;

public class RoomModule extends ModuleAbs<Integer, RoomAbs> {
	private static final long serialVersionUID = 1L;
	private static RoomModule instance;

	public static RoomModule getInstance() {
		return instance;
	}

	/**
	 * <房间id,房间>
	 */
	private final ConcurrentHashMap<Integer, RoomAbs> roomMap = new ConcurrentHashMap<>();
	/**
	 * 缓存的房间对象 <人数,stack>
	 */
	private final Stack<RoomAbs> cacheRoomStack = new Stack<RoomAbs>();

	/**
	 * 获取当前所在的房间
	 * 
	 * @param user
	 * @return
	 */
	public RoomAbs getRoom(IncomeUserI user) {
		RoomAbs room = user.getRoom();
		return room;
	}

	/**
	 * 获取缓存的房间
	 * 
	 * @param roomId
	 * @return
	 */
	public RoomAbs getRoom(int roomId) {
		return this.roomMap.get(roomId);
	}

	public void putRoom(RoomAbs room) {
		this.roomMap.put(room.getId(), room);
	}

	public void removeRoom(int id) {
		this.roomMap.remove(id);
	}

	public Stack<RoomAbs> getCacheRoomStack() {
		return cacheRoomStack;
	}

	private final EventAbs user_LogIn = new EventAbs(EventType.User_LogIn) {
		@Override
		public void eventNotify(User user, Object... params) {
		}
	};

	private final EventAbs User_LogOut = new EventAbs(EventType.User_LogOut) {
		@Override
		public void eventNotify(User user, Object... params) {
		}
	};

	private EventAbs shutDown = new EventAbs(EventType.ShutDown) {
		@Override
		public void eventNotify(User user, Object... params) {
			try {
				IbatisDbServer.getGmSqlMapper().startTransaction();
				try {
					for (Iterator<Entry<Integer, RoomAbs>> iterator = roomMap
							.entrySet().iterator(); iterator.hasNext();) {
						Entry<Integer, RoomAbs> type = iterator.next();
						try {
							type.getValue().dissolveRoom(
									DissolveRoomType.SERVER_STOP);
						} catch (Exception e) {
							LoggerService.getLogicLog()
									.error(e.getMessage(), e);
							LoggerService.getLogicLog().error(
									"没有重置房间成功！roomId:{}", type.getKey());
						}
					}
					IbatisDbServer.getGmSqlMapper().commitTransaction();
				} finally {
					IbatisDbServer.getGmSqlMapper().endTransaction();
				}
			} catch (SQLException e) {
				LoggerService.getLogicLog().error(e.getMessage(), e);
			}
		}
	};

	@Override
	public List<EventAbs> getGameEventList() {
		return Arrays.asList(user_LogIn, User_LogOut, shutDown);
	}

	@Override
	public void init() throws Exception {
		TimeConfig.init();
	}

	@Override
	public void backDb() throws SQLException {
	}

	@Override
	public byte[] processMessage(Channel channel, IoMessage message)
			throws Exception {
		return null;
	}

	@Override
	public byte[] processMessage(IncomeUserI user, IoMessage message)
			throws Exception {
		MessageLite messageLite = message.genMessageLite();
		switch (message.getMsgId()) {
		case CreateRoomBeforeCm:
			return RoomManager.createRoomBefore(user,
					(CreateRoomBeforeCm) messageLite);
		case CreateRoomCm:
			return RoomManager.createRoom(user, (CreateRoomCm) messageLite);
		case ProxyCreateRoomBeforeCm:
			return RoomManager.proxyCreateRoomBefore(user,
					(ProxyCreateRoomBeforeCm) messageLite);
		case ProxyCreateRoomCm:
			return RoomManager.proxyCreateRoom(user,
					(ProxyCreateRoomCm) messageLite);
		case ProxyRoomsDissovleCm:
			return RoomManager.proxyRoomsDissovle(user,
					(ProxyRoomsDissovleCm) messageLite);
		case JoinRoomBeforeCm:
			return RoomManager.joinRoomBefore(user,
					(JoinRoomBeforeCm) messageLite);
		case JoinRoomCm:
			return RoomManager.joinRoom(user, (JoinRoomCm) messageLite);
		case PrepareRoomCm:
			return RoomManager.prepareRoom(user, (PrepareRoomCm) messageLite);
		case NextRoundCm:
			return RoomManager.nextRound(user);
		default:
			return this.processRoomMessage(user, message);
		}
	}

	/**
	 * 处理游戏中消息
	 * 
	 * @param user
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private byte[] processRoomMessage(IncomeUserI user, IoMessage message)
			throws Exception {
		MsgId msgId = message.getMsgId();
		MessageLite genMessageLite = message.genMessageLite();
		RoomAbs room = getRoom(user);
		if (room == null) {
			LoggerService.getLogicLog().error("找不到房间！uid:{},msgId:{}",
					user.getUid(), message.getMsgId());
			return msgId.gRErrMsg(NTxt.NOT_FOUND_ROOM);
		}
		Seat seat = room.getSeat(user);
		if (seat == null) {
			LoggerService.getLogicLog().error("找不到玩家位置！uid:{},msgId:{}",
					user.getUid(), message.getMsgId());
			return msgId.gRErrMsg(NTxt.NOT_FOUND_SEAT);
		}
		if (room.isGameOver()) {
			return msgId.gRErrMsg(NTxt.GAME_OVER);
		}
		switch (msgId) {
		case ReconnectCm:
			return room.reconnect(seat, (ReconnectCm) message.genMessageLite());
		case ChatCm:
			return room.chat(seat, (ChatCm) message.genMessageLite());
		case ImCm:
			return room.im(seat, (ImCm) genMessageLite);
		default:
			break;
		}
		if (room.isStart()) {
			switch (msgId) {
			case MemberDissolveRoomCm:
				return room.memberDissolveRoom(seat,
						(MemberDissolveRoomCm) genMessageLite);
			case QiangZhuangCm:
				return room.qiangZhuang(seat, (QiangZhuangCm) genMessageLite);
			case BetOnCm:
				return room.betOn(seat, (BetOnCm) genMessageLite);
			case FinishPukeCm:
				return room.finishPuke(seat);
			default:
				return msgId.gRErrMsg(NTxt.NOT_IMPLEMENT);
			}
		} else {
			switch (msgId) {
			case DissolveRoomCm:
				return room.dissolveRoom(seat, (DissolveRoomCm) genMessageLite);
			case LeaveRoomCm:
				return room.leave(seat);
			default:
				return msgId.gRErrMsg(NTxt.NOT_IMPLEMENT);
			}
		}
	}

}
