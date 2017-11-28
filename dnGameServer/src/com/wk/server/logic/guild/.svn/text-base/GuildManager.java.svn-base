package com.wk.server.logic.guild;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import msg.GuildMessage.AgreeApplyCm;
import msg.GuildMessage.ApplyJulebuCm;
import msg.GuildMessage.ApplyMemberListCm;
import msg.GuildMessage.ClearWinnerCm;
import msg.GuildMessage.CreateJulebuBeforeCm;
import msg.GuildMessage.CreateJulebuBeforeSm;
import msg.GuildMessage.CreateJulebuCm;
import msg.GuildMessage.CreateJulebuSm;
import msg.GuildMessage.Detail;
import msg.GuildMessage.DisagreeApplyCm;
import msg.GuildMessage.DissolveJulebuCm;
import msg.GuildMessage.InJulebuBeforeCm;
import msg.GuildMessage.InJulebuCm;
import msg.GuildMessage.InJulebuSm;
import msg.GuildMessage.InfoSetCm;
import msg.GuildMessage.JulebuMemberListCm;
import msg.GuildMessage.JulebuRecordCm;
import msg.GuildMessage.KickJulebuMemberCm;
import msg.GuildMessage.LevelupMemberCm;
import msg.GuildMessage.LevelupMemberSm;
import msg.GuildMessage.OtherSetCm;
import msg.GuildMessage.PlaySetCm;
import msg.GuildMessage.PlaySetSm;
import msg.GuildMessage.QuitJulebuCm;
import msg.GuildMessage.SearchMemberCm;
import msg.GuildMessage.TableDetailCm;
import msg.GuildMessage.TableDetailSm;
import msg.GuildMessage.TableInfo;
import msg.GuildMessage.TableInfoCm;
import msg.GuildMessage.TableInfoSm;
import msg.GuildMessage.WinnerCm;
import msg.RoomMessage;
import msg.RoomMessage.PlayType;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.inner.GsSysModule;
import com.wk.engine.util.LRUMap;
import com.wk.guild.bean.GuildBean;
import com.wk.guild.dao.GuildDao;
import com.wk.guild.enm.MemberJobType;
import com.wk.logic.config.ConfigTemplate;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.MsgId;
import com.wk.logic.enm.SwType;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.logic.room.RoomAbs;
import com.wk.server.logic.room.RoomManager;
import com.wk.server.logic.room.RoomModule;
import com.wk.server.logic.room.Seat;
import com.wk.util.TimeTaskUtil;

public class GuildManager {
	private static final GuildManager instance = new GuildManager();

	public static GuildManager getInstance() {
		return instance;
	}

	/**
	 * 公会数据缓存LRU
	 * 
	 * key 公会ID value 公会
	 */
	private final Map<Integer, Guild> guildMap = Collections
			.synchronizedMap(new LRUMap<Integer, Guild>(GuildModule
					.getInstance().getMaxLRUSize()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean removeEldestEntry(
						java.util.Map.Entry<Integer, Guild> eldest) {
					boolean removeEldestEntry = super.removeEldestEntry(eldest);
					if (!removeEldestEntry) {
						return false;
					}
					Guild guild = eldest.getValue();
					if (guild.cannotRemove())
						this.setEldest(eldest);
					LoggerService.getGuildlogs().warn(
							"超出guildMap最大,移除公会,id:{},name:{}", guild.getId(),
							guild.getName());
					return true;
				}
			});
	/** 公会回收栈 */
	private final Stack<Guild> guildStack = new Stack<>();

	public Guild getGuild(int id) {
		return guildMap.get(id);
	}

	public Guild getAndReloadGuild(int id) {
		Guild guild = getGuild(id);
		if (guild == null) {
			try {
				GuildBean queryGuild = GuildDao.queryGuild(id);
				if (queryGuild == null) {
					return null;
				} else if (queryGuild.getServerId() == ServerConfig.serverId) {
					guild = FindGuildHandlerI.createNewGuild(queryGuild);
					put(guild.getId(), guild);
				}
			} catch (Exception e) {
				LoggerService.getPlatformLog().error(e.getMessage(), e);
				return null;
			}
		}
		return guild;
	}

	public void put(int id, Guild guild) {
		this.guildMap.put(id, guild);
	}

	public Guild removeGuild(int id) {
		final Guild remove = guildMap.remove(id);
		if (remove != null) {
			TimeTaskUtil.getTaskmanager().submitOneTimeTask(new Runnable() {
				@Override
				public void run() {
					GuildManager.this.guildStack.push(remove);
				}
			}, 10, TimeUnit.SECONDS);
		} else {
			String format = String.format("%s,移除缓存，找不到公会！%s",
					new Exception().getStackTrace()[0], id);
			System.err.println(format);
			LoggerService.getLogicLog().error(format);
		}
		return remove;
	}

	protected Guild getStackGuild() {
		if (!this.guildStack.isEmpty()) {
			return this.guildStack.pop();
		}
		return null;
	}

	public byte[] createJulebuBefore(IncomeUserI user,
			CreateJulebuBeforeCm messageLite) {
		if (!user.isCreateJ()) {
			return MsgId.CreateJulebuBeforeCm.gRErrMsg(NTxt.NO_RIGHT_CREATEJ);
		}
		PlayType playType = RoomManager
				.checkPlayType(messageLite.getPlayType());
		if (playType == null) {
			return MsgId.CreateJulebuBeforeCm.gRErrMsg(NTxt.INFO_ERROR);
		}
		if (RoomModule.getInstance().getRoom(user) != null) {
			return MsgId.CreateJulebuBeforeCm
					.gRErrMsg(NTxt.BEFORE_ROOM_ALREADY);
		}
		if (user.getCreateGuildServerId() != ServerConfig.serverId) {
			RoomMessage.SwServer.Builder sw = GsSysModule.getInstance().getSw(
					user.getCreateGuildServerId(), SwType.createJulebu, user);
			if (sw == null) {
				return MsgId.CreateJulebuBeforeCm
						.gRErrMsg(NTxt.BEFORE_SW_EMPTY);
			}
			return CreateJulebuBeforeSm.newBuilder()
					.setCode(NTxt.SERVER_NEED_SWITCH).setSw(sw)
					.setPlayType(playType).build().toByteArray();
		} else {
			byte[] createJulebu = createJulebu(user, playType);
			user.sendMessage(MsgId.CreateJulebuSm, createJulebu);
			return null;
		}
	}

	public byte[] inJulebuBefore(IncomeUserI user,
			InJulebuBeforeCm genMessageLite) {
		int id = genMessageLite.getId();
		if (RoomModule.getInstance().getRoom(user) != null) {
			return MsgId.InJulebuBeforeCm.gRErrMsg(NTxt.BEFORE_ROOM_ALREADY);
		}
		new FindGuildHandlerI.InJulebuBeforeHandler(id, user);
		return null;
	}

	public byte[] createJulebu(IncomeUserI user, CreateJulebuCm messageLite) {
		PlayType playType = RoomManager
				.checkPlayType(messageLite.getPlayType());
		if (playType == null) {
			return MsgId.CreateJulebuCm.gRErrMsg(NTxt.INFO_ERROR);
		}
		return createJulebu(user, playType);
	}

	private byte[] createJulebu(IncomeUserI user, PlayType playType) {
		if (!user.isCreateJ()) {
			return MsgId.CreateJulebuCm.gRErrMsg(NTxt.NO_RIGHT_CREATEJ);
		}
		if (user.getCreateGuildServerId() != ServerConfig.serverId) {
			return MsgId.CreateJulebuCm.gRErrMsg(NTxt.SERVER_NEED_SWITCH);
		}
		if (user.getMyGuildSize() > ConfigTemplate.getConfigTemplate()
				.getGuildCreateRoomMax()) {
			return MsgId.CreateJulebuCm.gRErrMsg(NTxt.JULEBU_FULL);
		}
		try {
			Guild guild = new Guild();
			guild.setPlayType(playType);
			guild.setName(user.getNickname());
			guild.initMaster(user.getUid(), user.getNickname());
			guild.setServerId(ServerConfig.serverId);
			guild.setRegisterTime(new Date());
			guild.insert();
			user.createMyJulebu(guild);
			put(guild.getId(), guild);
			return CreateJulebuSm.newBuilder().setCode(NTxt.SUCCESS)
					.setPlayType(playType).setId(guild.getId())
					.setName(guild.getName()).build().toByteArray();
		} catch (Exception e) {
			LoggerService.getGuildlogs().error(e.getMessage(), e);
			return MsgId.CreateJulebuCm.gRErrMsg(NTxt.JULEBU_EXCEPTION);
		}
	}

	public byte[] inJulebu(IncomeUserI user, InJulebuCm genMessageLite) {
		int id = genMessageLite.getId();
		return inJulebu(user, id);
	}

	public byte[] inJulebu(IncomeUserI user, int id) {
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.InJulebuCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssistOrMember(user.getUid())) {
			return MsgId.InJulebuCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST_OR_MEMBER);
		}
		return InJulebuSm.newBuilder().setCode(NTxt.SUCCESS)
				.setId(guild.getId()).setName(guild.getName())
				.setNotice(guild.getNotice() == null ? "" : guild.getNotice())
				.setJob(guild.getMemberJob(user.getUid()))
				.setPlayType(guild.gPlayType())
				.setPlayTypeDesc(guild.getPlayTypeDesc())
				.setProhibitIp(guild.getProhibitIp()).build().toByteArray();
	}

	public byte[] applyJulebu(IncomeUserI user, ApplyJulebuCm genMessageLite) {
		int id = genMessageLite.getId();
		if (user.getJoinGuildSize() > ConfigTemplate.getConfigTemplate()
				.getGuildCreateRoomMax()) {
			return MsgId.ApplyJulebuCm.gRErrMsg(NTxt.JULEBU_FULL);
		}
		new FindGuildHandlerI.ApplyJulebuHandler(id, user);
		return null;
	}

	public byte[] applyMemberList(IncomeUserI user,
			ApplyMemberListCm genMessageLite) {
		int id = genMessageLite.getId();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.ApplyMemberListCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssist(user.getUid())) {
			return MsgId.ApplyMemberListCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST);
		}
		return guild.applyMemberList();
	}

	public byte[] agreeApply(IncomeUserI master, AgreeApplyCm genMessageLite) {
		int id = genMessageLite.getId();
		long uid = genMessageLite.getUid();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.AgreeApplyCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssist(master.getUid())) {
			return MsgId.AgreeApplyCm.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST);
		}
		if (!guild.isApply(uid)) {
			return MsgId.DisagreeApplyCm.gRErrMsg(NTxt.NOT_APPLY);
		}
		new FindMemberHandlerI.AgreeApply(uid, "", guild, master);
		return null;
	}

	public byte[] disagreeApply(IncomeUserI master,
			DisagreeApplyCm genMessageLite) {
		int id = genMessageLite.getId();
		long uid = genMessageLite.getUid();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.DisagreeApplyCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssist(master.getUid())) {
			return MsgId.DisagreeApplyCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST);
		}
		if (!guild.isApply(uid)) {
			return MsgId.DisagreeApplyCm.gRErrMsg(NTxt.NOT_APPLY);
		}
		new FindMemberHandlerI.DisagreeApply(uid, "", guild, master);
		return null;
	}

	public byte[] julebuMemberList(IncomeUserI user,
			JulebuMemberListCm genMessageLite) {
		int id = genMessageLite.getId();
		int index = genMessageLite.getIndex();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.JulebuMemberListCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssistOrMember(user.getUid())) {
			return MsgId.JulebuMemberListCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST_OR_MEMBER);
		}
		return guild.julebuMemberList(index);
	}

	public byte[] infoSet(IncomeUserI user, InfoSetCm genMessageLite) {
		int id = genMessageLite.getId();
		String name = genMessageLite.getName();
		String notice = genMessageLite.getNotice();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.JulebuMemberListCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssist(user.getUid())) {
			return MsgId.JulebuMemberListCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST);
		}
		return guild.infoSet(name, notice);
	}

	public byte[] playSet(IncomeUserI user, PlaySetCm genMessageLite) {
		int id = genMessageLite.getId();
		PlayType playType = genMessageLite.getPlayType();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.PlaySetCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssist(user.getUid())) {
			return MsgId.PlaySetCm.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST);
		}
		try {
			guild.setPlayType(playType);
		} catch (Exception e) {
			LoggerService.getGuildlogs().error(e.getMessage(), e);
			return MsgId.PlaySetCm.gRErrMsg(NTxt.INFO_ERROR);
		}
		return PlaySetSm.newBuilder().setCode(NTxt.SUCCESS)
				.setId(guild.getId()).setPlayType(playType).build()
				.toByteArray();
	}

	public byte[] otherSet(IncomeUserI user, OtherSetCm genMessageLite) {
		int id = genMessageLite.getId();
		boolean prohibitIp = genMessageLite.getProhibitIp();
		Guild guild = getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.OtherSetCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssist(user.getUid())) {
			return MsgId.OtherSetCm.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST);
		}
		return guild.otherSet(prohibitIp);
	}

	public byte[] dissolveJulebu(IncomeUserI user,
			DissolveJulebuCm genMessageLite) {
		int id = genMessageLite.getId();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.DissolveJulebuCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMaster(user.getUid())) {
			return MsgId.DissolveJulebuCm.gRErrMsg(NTxt.NOT_GUILD_MASTER);
		}
		return guild.dissolveJulebu(user);
	}

	/**
	 * 退出俱乐部
	 * 
	 * @param user
	 * @param genMessageLite
	 * @return
	 */
	public byte[] quitJulebu(IncomeUserI user, QuitJulebuCm genMessageLite) {
		int id = genMessageLite.getId();
		new FindGuildHandlerI.QuitJulebuHandler(id, user);
		return null;
	}

	public byte[] tableInfo(IncomeUserI user, TableInfoCm genMessageLite) {
		int id = genMessageLite.getId();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.TableInfoCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssistOrMember(user.getUid())) {
			return MsgId.TableInfoCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST_OR_MEMBER);
		}
		TableInfoSm.Builder tableInfoSm = TableInfoSm.newBuilder()
				.setCode(NTxt.SUCCESS).setId(id);
		TableInfo.Builder tableInfo = TableInfo.newBuilder();
		for (int index = 0; index < guild.getRoomCacheList().size(); index++) {
			int[] roomIdCache = guild.getRoomCacheList().get(index);
			tableInfo.setIndex(index + 1).setNum(roomIdCache[1])
					.setRun(roomIdCache[2] == 1);
			tableInfoSm.addTable(tableInfo);
		}
//		NTxt.println(tableInfoSm.build());
		return tableInfoSm.build().toByteArray();
	}

	public byte[] kickJulebuMember(IncomeUserI master,
			KickJulebuMemberCm genMessageLite) {
		int id = genMessageLite.getId();
		long uid = genMessageLite.getUid();
		int index = genMessageLite.getIndex();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.KickJulebuMemberCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (guild.isAssist(master.getUid())) {
			if (!guild.isMember(uid)) {
				return MsgId.KickJulebuMemberCm.gRErrMsg(NTxt.NOT_GUILD_MEMBER);
			}

		} else if (guild.isMaster(master.getUid())) {
			if (!guild.isAssistOrMember(uid)) {
				return MsgId.KickJulebuMemberCm
						.gRErrMsg(NTxt.NOT_GUILD_ASSIST_OR_MEMBER);
			}
		} else {
			return MsgId.KickJulebuMemberCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST);
		}
		new FindMemberHandlerI.KickJulebuMember(uid, "", guild, master, index);
		return null;
	}

	public byte[] levelupMember(IncomeUserI user, LevelupMemberCm genMessageLite) {
		int id = genMessageLite.getId();
		long uid = genMessageLite.getUid();
		int job = genMessageLite.getJob();
		MemberJobType jobType = MemberJobType.getEnum(job);
		if (jobType == null) {
			return MsgId.LevelupMemberCm.gRErrMsg(NTxt.NO_FOUND_MEMBER_JOB);
		}
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.LevelupMemberCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMaster(user.getUid())) {
			return MsgId.LevelupMemberCm.gRErrMsg(NTxt.NOT_GUILD_MASTER);
		}
		if (guild.levelupMember(uid, jobType))
			return LevelupMemberSm.newBuilder().setCode(NTxt.SUCCESS).setId(id)
					.setUid(uid).setJob(jobType.getType()).build()
					.toByteArray();
		else
			return LevelupMemberSm.newBuilder().setCode(NTxt.FAIL).build()
					.toByteArray();
	}

	public byte[] julebuRecord(IncomeUserI user, JulebuRecordCm genMessageLite) {
		int id = genMessageLite.getId();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.JulebuRecordCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssistOrMember(user.getUid())) {
			return MsgId.JulebuRecordCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST_OR_MEMBER);
		}
		return guild.getGameRecord();
	}

	public byte[] searchMember(IncomeUserI user, SearchMemberCm messageLt) {
		int id = messageLt.getId();
		String nickname = messageLt.getNickname();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.SearchMemberCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssistOrMember(user.getUid())) {
			return MsgId.SearchMemberCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST_OR_MEMBER);
		}
		return guild.searchMember(user, nickname);
	}

	public byte[] winner(IncomeUserI user, WinnerCm messageLt) {
		int id = messageLt.getId();
		int index = messageLt.getIndex();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.WinnerCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssistOrMember(user.getUid())) {
			return MsgId.WinnerCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST_OR_MEMBER);
		}
		return guild.winner(index);
	}

	public byte[] clearWinner(IncomeUserI user, ClearWinnerCm messageLt) {
		int id = messageLt.getId();
		String infoList = messageLt.getInfo();
		int index = messageLt.getIndex();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.ClearWinnerCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssist(user.getUid())) {
			return MsgId.ClearWinnerCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST);
		}
		return guild.clearWinner(infoList, index);
	}

	public byte[] tableDetail(IncomeUserI user, TableDetailCm messageLt) {
		int id = messageLt.getId();
		int index = messageLt.getIndex();
		Guild guild = this.getAndReloadGuild(id);
		if (guild == null) {
			return MsgId.TableDetailCm.gRErrMsg(NTxt.NO_FOUND_GUILD);
		}
		if (!guild.isMasterOrAssistOrMember(user.getUid())) {
			return MsgId.TableDetailCm
					.gRErrMsg(NTxt.NOT_GUILD_MASTER_OR_ASSIST_OR_MEMBER);
		}
		int roomId = guild.getRoomId(index);
		if (roomId == SystemConstants.NoRoomId) {
			return TableDetailSm.newBuilder().setCode(NTxt.SUCCESS).setId(id)
					.setIndex(index).build().toByteArray();
		} else {
			RoomAbs room = RoomModule.getInstance().getRoom(roomId);
			if (room == null) {
				return MsgId.TableDetailCm.gRErrMsg(NTxt.NOT_FOUND_GUILD_ROOM);
			}
			TableDetailSm.Builder tableDetailSm = TableDetailSm.newBuilder()
					.setCode(NTxt.SUCCESS).setId(id).setIndex(index);
			if (room.getUserNum() > 0) {
				Detail.Builder detail = Detail.newBuilder();
				for (Seat seat : room.getSeats()) {
					if (seat.getUser() != null) {
						detail.setHeadimg(
								ServerConfig.getHeadUrl(seat.getUserUid()))
								.setIp(seat.getUserIp())
								.setNickname(seat.getUserNickname())
								.setUid(seat.getUserUid());
						tableDetailSm.addDetail(detail.build());
					}
				}
			}
			return tableDetailSm.build().toByteArray();
		}
	}
}
