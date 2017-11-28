package com.wk.server.logic.guild;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import msg.GuildMessage.ApplyMember;
import msg.GuildMessage.ApplyMemberListSm;
import msg.GuildMessage.ClearWinnerSm;
import msg.GuildMessage.DissolveJulebuSm;
import msg.GuildMessage.InfoSetSm;
import msg.GuildMessage.JulebuMember;
import msg.GuildMessage.JulebuMemberListSm;
import msg.GuildMessage.OtherSetSm;
import msg.GuildMessage.SearchMemberSm;
import msg.GuildMessage.Winner;
import msg.GuildMessage.WinnerSm;
import msg.RoomMessage.PlayType;

import org.json.JSONArray;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.inner.BusSysModule;
import com.wk.guild.bean.GuildBean;
import com.wk.guild.dao.GuildDao;
import com.wk.logic.config.NTxt;
import com.wk.logic.config.TimeConfig;
import com.wk.logic.enm.MsgId;
import com.wk.logic.enm.PType;
import com.wk.logic.enm.RoundType;
import com.wk.mj.MjTools;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.logic.friend.FindUserHandlerI;
import com.wk.server.logic.room.RoomAbs;
import com.wk.server.logic.room.RoomModule;
import com.wk.util.TimeTaskUtil;

public class Guild extends GuildBean {
	private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private ScheduledFuture<?> updateMemberJulebuTask;

	public Guild() {
		super(GuildModule.getInstance());
	}

	public Guild(GuildBean bean) throws Exception {
		this();
		this.reset();
		this.overWrite(bean);
	}

	public void reset() {
		super.reset();
	}

	/**
	 * 申请列表
	 * 
	 * @return
	 */
	public byte[] applyMemberList() {
		ApplyMemberListSm.Builder applyMemberListSm = ApplyMemberListSm
				.newBuilder().setCode(NTxt.SUCCESS).setId(this.getId());
		if (!this.apply.isEmpty()) {
			ApplyMember.Builder applyMember = ApplyMember.newBuilder();
			for (Entry<Long, JSONArray> entry : this.apply.entrySet()) {
				JSONArray value = entry.getValue();
				applyMember.setUid(entry.getKey())
						.setName(value.optString(nickname_index))
						.setHeadimg(ServerConfig.getHeadUrl(entry.getKey()));
				applyMemberListSm.addApply(applyMember);
			}
		}
		return applyMemberListSm.build().toByteArray();
	}

	public byte[] infoSet(String name, String notice) {
		rwLock.writeLock().lock();
		try {
			if (!name.equals(this.getName())) {
				this.setName(name);
				updateMemberJulebu(true);
			}
			if (!notice.equals(this.getNotice()))
				this.setNotice(notice);
			return InfoSetSm.newBuilder().setCode(NTxt.SUCCESS)
					.setId(this.getId()).setName(name).setNotice(notice)
					.build().toByteArray();
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	/**
	 * 更新成员俱乐部信息
	 * 
	 * @param rightNow
	 *            是否立即执行
	 */
	private void updateMemberJulebu(boolean rightNow) {
		if (rightNow) {
			for (Entry<Long, JSONArray> e : this.member.entrySet()) {
				Long uid = e.getKey();
				new FindMemberHandlerI.UpdateJulebu(uid, null, this, null);
			}
		} else {
			if (updateMemberJulebuTask == null)
				updateMemberJulebuTask = TimeTaskUtil.getTaskmanager()
						.submitOneTimeTask(new Runnable() {
							public void run() {
								for (Entry<Long, JSONArray> e : Guild.this.member
										.entrySet()) {
									Long uid = e.getKey();
									new FindMemberHandlerI.UpdateJulebu(uid,
											null, Guild.this, null);
								}
								Guild.this.updateMemberJulebuTask = null;
							}
						}, 30, TimeUnit.SECONDS);
		}
	}

	public byte[] otherSet(boolean prohibitIp) {
		rwLock.writeLock().lock();
		try {
			this.setProhibitIp(prohibitIp);
			return OtherSetSm.newBuilder().setCode(NTxt.SUCCESS).setId(id)
					.setProhibitIp(prohibitIp).build().toByteArray();
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	public byte[] dissolveJulebu(IncomeUserI master) {
		rwLock.writeLock().lock();
		try {
			try {
				GuildDao.deteleGuild(this.getId());
			} catch (SQLException e) {
				LoggerService.getGuildlogs().error(e.getMessage(), e);
				return MsgId.DissolveJulebuCm.gRErrMsg(NTxt.JULEBU_EXCEPTION);
			}
			for (Entry<Long, JSONArray> e : this.member.entrySet()) {
				Long uid = e.getKey();
				if (uid == this.getMasterUid())
					continue;
				JSONArray value = e.getValue();
				new FindMemberHandlerI.DissolveJulebu(uid,
						value.optString(nickname_index), this);
			}
			master.removeMyJulebu(this.getId());
			GuildManager.getInstance().removeGuild(getId());
			return DissolveJulebuSm.newBuilder().setCode(NTxt.SUCCESS)
					.setId(this.getId()).build().toByteArray();
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	public byte[] joinRoom(IncomeUserI user, int num) {
		if (num <= 0 || num > MjTools.getGuildCreateRoomMax()) {
			LoggerService.getGuildlogs().error("无此房间！num:{}", num);
			return MsgId.JoinRoomCm.gRErrMsg(NTxt.JOIN_ROOM_NUM_ERROR);
		}
		rwLock.writeLock().lock();
		try {
			int roomId = getRoomId(num);
			if (roomId == SystemConstants.NoRoomId) {
				if (BusSysModule.getInstance().isCloseCreateRoom()) {
					return MsgId.JoinRoomCm
							.gRErrMsg(NTxt.CREATE_ROOM_SERVER_FIXING);
				}
				RoundType roundType = RoundType.getEnum(this.gPlayType()
						.getPType());
				new FindUserHandlerI.JoinJulebuRoomHandler(user, this, num,
						-roundType.getNeedDiamond());
				return null;
			} else {
				RoomAbs room = RoomModule.getInstance().getRoom(roomId);
				if (room == null) {
					return MsgId.JoinRoomCm.gRErrMsg(NTxt.NOT_FOUND_GUILD_ROOM);
				}
				if (this.getProhibitIp() && room.hasSameIp(user.getIp())) {
					return MsgId.JoinRoomCm.gRErrMsg(NTxt.SERVER_BUSY);
				}
				
				byte[] joinRoom = room.joinRoom(user, true);
				return joinRoom;
			}
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	public byte[] searchMember(IncomeUserI user, String nickname) {
		SearchMemberSm.Builder searchMemberSm = SearchMemberSm.newBuilder()
				.setCode(NTxt.SUCCESS).setId(this.getId())
				.setNickname(nickname);
		JulebuMember.Builder julebuMember = JulebuMember.newBuilder();
		for (Entry<Long, JSONArray> mem : this.member.entrySet()) {
			JSONArray value = mem.getValue();
			if (value.optString(nickname_index).matches(
					String.format(".*%s.*", nickname))) {
				julebuMember.setUid(mem.getKey())
						.setName(value.optString(nickname_index))
						.setHeadimg(ServerConfig.getHeadUrl(mem.getKey()))
						.setJob(value.optInt(job_index));
				searchMemberSm.addMember(julebuMember);
			}
		}
		return searchMemberSm.build().toByteArray();
	}

	public byte[] julebuMemberList(int index) {
		return JulebuMemberListSm.newBuilder().setCode(NTxt.SUCCESS)
				.setId(this.getId()).setIndex(index)
				.addAllMember(turnToPage(index)).setTotalNum(getActiveSize())
				.setPageNum(TimeConfig.getOnePageNum()).build().toByteArray();
	}

	/** 翻到第几页 从1开始 */
	public List<JulebuMember> turnToPage(int index) {
		ArrayList<JulebuMember> arrayList = new ArrayList<>();
		int activeSize = getActiveSize();
		int totalIndex = (int) Math.ceil(activeSize
				/ (double) TimeConfig.getOnePageNum());
		if (index <= 0 || index > totalIndex) {
			LoggerService.getLogicLog().error("index错误！{}", index);
			return arrayList;
		}
		if (index == 1) {// 每次打开第一页刷新一下活跃度？
			// TODO
			this.refreshMemberActiveNum();
		}
		int fromIndex = (index - 1) * TimeConfig.getOnePageNum();
		int endIndex = index == totalIndex ? activeSize : index
				* TimeConfig.getOnePageNum();
		JulebuMember.Builder julebuMember = JulebuMember.newBuilder();
		for (int count = fromIndex; count < endIndex; count++) {
			JSONArray value = this.activeList.get(count);
			julebuMember
					.setUid(value.optLong(uid_index))
					.setName(value.optString(nickname_index))
					.setHeadimg(
							ServerConfig.getHeadUrl(value.optLong(uid_index)))
					.setJob(value.optInt(job_index))
					.setActive(value.optInt(activeNum_index));
			arrayList.add(julebuMember.build());
		}
		return arrayList;
	}

	public byte[] winner(int index) {
		return WinnerSm.newBuilder().setCode(NTxt.SUCCESS).setId(this.getId())
				.setIndex(index).setPageNum(TimeConfig.getOnePageNum())
				.setTotalNum(getWinnerSize())
				.addAllWinner(winnerTurnToPage(index)).build().toByteArray();
	}

	public ArrayList<Winner> winnerTurnToPage(int index) {
		ArrayList<Winner> arrayList = new ArrayList<>();
		int winnerSize = getWinnerSize();
		int totalIndex = (int) Math.ceil(winnerSize
				/ (double) TimeConfig.getOnePageNum());
		if (index < 0 || index > totalIndex) {
			return arrayList;
		}
		int fromIndex = (index - 1) * TimeConfig.getOnePageNum();
		int endIndex = index == totalIndex ? winnerSize : index
				* TimeConfig.getOnePageNum();
		Winner.Builder winner = Winner.newBuilder();
		for (int count = fromIndex; count < endIndex; count++) {
			JSONArray mem = this.winnerList.get(count);
			winner.setUid(mem.optLong(uid_index))
					.setHeadimg(ServerConfig.getHeadUrl(mem.optLong(uid_index)))
					.setNickname(mem.optString(nickname_index))
					.setNum(mem.optInt(winnerNum_index));
			arrayList.add(winner.build());
		}
		return arrayList;
	}

	public byte[] clearWinner(String infoList, int index) {
		rwLock.writeLock().lock();
		try {
			String[] split = infoList.split("#");
			for (int i = 0; i < split.length; i += 2) {
				this.changeMemberWinNum(Long.parseLong(split[i]),
						-Integer.parseInt(split[i + 1]));
			}
			return ClearWinnerSm.newBuilder().setCode(NTxt.SUCCESS)
					.setId(this.getId()).setIndex(index)
					.setPageNum(TimeConfig.getOnePageNum())
					.setTotalNum(getWinnerSize())
					.addAllWinner(winnerTurnToPage(index)).build()
					.toByteArray();
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	@Override
	public void setPlayType(PlayType playType) throws Exception {
		super.setPlayType(playType);
		this.updateMemberJulebu(true);
	}

	/**
	 * 总人数
	 * 
	 * @return
	 */
	public int getSumNum() {
		int sumNum = 0;
		for (int[] roomCache : this.getRoomCacheList()) {
			if (roomCache[1] != 0)
				sumNum += roomCache[1];
		}
		return sumNum;
	}

	public void setRoomNum(int num, int userNum, int status) {
		super.setRoomNum(num, userNum, status);
		this.updateMemberJulebu(true);
	}

}
