package com.wk.server.logic.guild;

import org.json.JSONObject;

import test.client.util.NoticeTextTemplate;
import msg.GuildMessage.AgreeApplySm;
import msg.GuildMessage.DisagreeApplySm;
import msg.GuildMessage.KickJulebuMemberSm;
import msg.InnerMessage.GuildGsToGs;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.db.IbatisDbServer;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.inner.gstogs.GuildGTGHandler;
import com.wk.logic.config.NTxt;
import com.wk.logic.config.TimeConfig;
import com.wk.logic.enm.GuildGTGType;
import com.wk.logic.enm.MsgId;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.ibatis.select.User;
import com.wk.server.logic.friend.HandlerResultI;
import com.wk.server.logic.guild.enm.RemoveJoinJulebuType;
import com.wk.server.logic.login.UserManager;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;

/**
 * 公会成员在当前服务器
 * 
 * 公会成员另一个服务器
 * 
 * 公会成员未登陆
 * 
 * @author Administrator
 *
 */
public abstract class FindMemberHandlerI extends HandlerResultI {
	/** 公会成员在当前服需装载 **/
	public static final boolean NEED_LOAD = true;
	/** 公会成员在当前服无需装载 **/
	public static final boolean NOT_NEED_LOAD = false;
	/** 公会成员不在当前服需切服 **/
	public static final boolean NEED_SW = true;
	/** 公会成员不在当前服无需切服 **/
	public static final boolean NOT_NEED_SW = false;
	private final GuildGTGType type;
	/** 公会成员ID */
	private final long uid;
	/** 公会成员昵称 */
	private final String nickname;
	/***/
	private final Guild guild;
	/***/
	private final MsgId msgId;
	/** 操作者 */
	private final IncomeUserI master;
	/** 公会ID */
	private final int id;
	/** 公会成员在当前服是否需要装载 */
	private final boolean needLoad;
	/** 公会成员不在当前服是否需要切服 **/
	private final boolean needSw;

	/**
	 * 
	 * @param type
	 * @param uid
	 *            公会成员ID
	 * @param nickname
	 * @param guild
	 * @param msgId
	 * @param master
	 * @param needLoad
	 * @param needSw
	 */
	public FindMemberHandlerI(GuildGTGType type, long uid, String nickname,
			Guild guild, MsgId msgId, IncomeUserI master, boolean needLoad,
			boolean needSw) {
		super();
		this.type = type;
		this.uid = uid;
		this.nickname = nickname;
		this.guild = guild;
		this.id = guild.getId();
		this.msgId = msgId;
		this.master = master;
		this.needLoad = needLoad;
		this.needSw = needSw;
	}

	public FindMemberHandlerI(GuildGTGType type, GuildGsToGs guildGsToGs,
			boolean needLoad) {
		super();
		this.type = type;
		this.uid = guildGsToGs.getUid();
		this.nickname = guildGsToGs.getNickname();
		this.guild = null;
		this.id = guildGsToGs.getId();
		this.msgId = null;
		this.master = null;
		this.needLoad = needLoad;
		this.needSw = false;
	}

	/**
	 * 切服处理
	 * 
	 * @param userBean
	 *            查询到的公会成员信息
	 */
	public void swProcess(UserBean userBean) {
		GuildGTGHandler.getInstance().processMember(getUid(), getId(),
				getNickname(), getData(), userBean.getServerId(), this, type);
	}

	/** 数据 */
	public String getData() {
		return null;
	}

	/**
	 * 在当前服已装载处理
	 * 
	 * @param user
	 *            公会成员
	 */
	public abstract int currentServerProcess(IncomeUserI user);

	/**
	 * 在当前服未加载处理
	 * 
	 * @param userBean
	 * @return
	 */
	public abstract int currentServerProcessUnload(UserBean userBean);

	protected void handle() {

		try {
			User user = UserManager.getInstance().getUser(this.uid);
			if (user == null) {
				UserBean userBean = UserDao.queryUser(
						IbatisDbServer.getGmSqlMapper(), uid);
				if (userBean == null) {
					setCode(NTxt.NO_FOUND_MEMBER);
				} else if (userBean.getServerId() == ServerConfig.serverId) {
					if (this.needLoad) {
						User reloadUser = UserManager.getInstance().reLoadUser(
								uid);
						if (reloadUser != null) {
							setCode(currentServerProcess(reloadUser));
							reloadUser.save();
						} else {
							setCode(NTxt.CAN_NOT_LOAD_USER);
						}
					} else {
						setCode(currentServerProcessUnload(userBean));
						userBean.save();
					}
				} else {
					if (needSw) {
						swProcess(userBean);
					} else {
						setCode(NTxt.NOT_NEED_SW);
					}
				}
			} else {
				setCode(currentServerProcess(user));
			}
		} catch (Exception e) {
			LoggerService.getGuildlogs().error(e.getMessage(), e);
			setCode(NTxt.JULEBU_EXCEPTION);
		}
	}

	public long getUid() {
		return uid;
	}

	public String getNickname() {
		return nickname;
	}

	public int getId() {
		return id;
	}

	public Guild getGuild() {
		return guild;
	}

	public IncomeUserI getMaster() {
		return master;
	}

	public MsgId getMsgId() {
		return msgId;
	}

	/** 发消息给操作者 */
	public void sendMsgToMaster(byte[] bytes) {
		if (master != null) {
			master.sendMessage(getMsgId().getResMsgId(), bytes);
		}
	}

	public boolean isNeedLoad() {
		return needLoad;
	}

	public boolean isNeedSw() {
		return needSw;
	}

	/***
	 * 会长解散俱乐部通知成员
	 * 
	 * @author Administrator
	 *
	 */
	public static class DissolveJulebu extends FindMemberHandlerI {
		public DissolveJulebu(long uid, String nickname, Guild guild) {
			super(GuildGTGType.DissolveJulebu, uid, nickname, guild, null,
					null, NOT_NEED_LOAD, NEED_SW);
			handle();
		}

		public DissolveJulebu(GuildGsToGs guildGsToGs) {
			super(GuildGTGType.DissolveJulebu, guildGsToGs, NOT_NEED_LOAD);
			handle();
		}

		@Override
		public int currentServerProcess(IncomeUserI user) {
			user.removeJoinJulebu(getId(), RemoveJoinJulebuType.dissolve);
			return NTxt.SUCCESS;
		}

		@Override
		public int currentServerProcessUnload(UserBean userBean) {
			userBean.removeJoinJulebu(getId());
			return NTxt.SUCCESS;
		}

		@Override
		public void handleCode(int code) {
			// TODO Auto-generated method stub
		}
	}

	/***
	 * 会长剔除俱乐部成员
	 * 
	 * @author Administrator
	 *
	 */
	public static class KickJulebuMember extends FindMemberHandlerI {
		/** 第几页 */
		private final int index;

		public KickJulebuMember(long uid, String nickname, Guild guild,
				IncomeUserI master, int index) {
			super(GuildGTGType.KickJulebuMember, uid, nickname, guild,
					MsgId.KickJulebuMemberCm, master, NOT_NEED_LOAD, NEED_SW);
			this.index = index;
			handle();
		}

		public KickJulebuMember(GuildGsToGs guildGsToGs) {
			super(GuildGTGType.KickJulebuMember, guildGsToGs, NOT_NEED_LOAD);
			this.index = 0;
			handle();
		}

		@Override
		public int currentServerProcess(IncomeUserI user) {
			user.removeJoinJulebu(this.getId(), RemoveJoinJulebuType.kick);
			return NTxt.SUCCESS;
		}

		@Override
		public int currentServerProcessUnload(UserBean userBean) {
			userBean.removeJoinJulebu(getId());
			return NTxt.SUCCESS;
		}

		@Override
		public void handleCode(int code) {
			if (code == NTxt.SUCCESS) {
				if (this.getGuild() != null)
					this.getGuild().removeMember(this.getUid());
			}
			if (getMaster() == null) {
				return;
			}
			if (code == NTxt.SUCCESS) {
				sendMsgToMaster(KickJulebuMemberSm.newBuilder()
						.setCode(NTxt.SUCCESS).setId(this.getId())
						.setUid(this.getUid()).setIndex(index)
						.setPageNum(TimeConfig.getOnePageNum())
						.setTotalNum(this.getGuild().getActiveSize())
						.addAllMember(this.getGuild().turnToPage(index))
						.build().toByteArray());
			} else {
				sendMsgToMaster(this.getMsgId().gRErrMsg(code));
			}
		}
	}

	/***
	 * 拒绝申请
	 * 
	 * @author Administrator
	 *
	 */
	public static class DisagreeApply extends FindMemberHandlerI {
		public DisagreeApply(long uid, String nickname, Guild guild,
				IncomeUserI master) {
			super(GuildGTGType.DisagreeApply, uid, nickname, guild,
					MsgId.DisagreeApplyCm, master, NOT_NEED_LOAD, NEED_SW);
			handle();
		}

		public DisagreeApply(GuildGsToGs guildGsToGs) {
			super(GuildGTGType.DisagreeApply, guildGsToGs, NOT_NEED_LOAD);
			handle();
		}

		@Override
		public int currentServerProcess(IncomeUserI user) {
			user.removeJoinJulebu(getId(), RemoveJoinJulebuType.disagreeJoin);
			return NTxt.SUCCESS;
		}

		@Override
		public int currentServerProcessUnload(UserBean userBean) {
			userBean.removeJoinJulebu(getId());
			return NTxt.SUCCESS;
		}

		@Override
		public void handleCode(int code) {
			if (code == NTxt.SUCCESS) {
				if (this.getGuild() != null)
					this.getGuild().removeApply(this.getUid());
			}
			if (this.getMaster() == null) {
				return;
			}
			if (code == NTxt.SUCCESS) {
				sendMsgToMaster(DisagreeApplySm.newBuilder()
						.setCode(NTxt.SUCCESS).setId(this.getId())
						.setUid(this.getUid()).build().toByteArray());
			} else {
				sendMsgToMaster(getMsgId().gRErrMsg(code));
			}
		}
	}

	/***
	 * 同意申请
	 * 
	 * @author Administrator
	 *
	 */
	public static class AgreeApply extends FindMemberHandlerI {
		public AgreeApply(long uid, String nickname, Guild guild,
				IncomeUserI master) {
			super(GuildGTGType.AgreeApply, uid, nickname, guild,
					MsgId.AgreeApplyCm, master, NOT_NEED_LOAD, NEED_SW);
			handle();
		}

		public AgreeApply(GuildGsToGs guildGsToGs) {
			super(GuildGTGType.AgreeApply, guildGsToGs, NOT_NEED_LOAD);
			handle();
		}

		@Override
		public int currentServerProcess(IncomeUserI user) {
			if (user.joinJulebu(getId()) != null) {
				return NTxt.SUCCESS;
			} else
				return NTxt.FAIL;
		}

		@Override
		public int currentServerProcessUnload(UserBean userBean) {
			if (userBean.joinJulebu(getId()) != null) {
				return NTxt.SUCCESS;
			} else
				return NTxt.FAIL;
		}

		@Override
		public void handleCode(int code) {
			if (code == NTxt.SUCCESS) {
				if (this.getGuild() != null)
					this.getGuild().beenMember(getUid());
			}
			if (this.getMaster() == null) {
				return;
			}
			if (code == NTxt.SUCCESS) {
				sendMsgToMaster(AgreeApplySm.newBuilder().setCode(NTxt.SUCCESS)
						.setId(this.getId()).setUid(this.getUid()).build()
						.toByteArray());
			} else {
				sendMsgToMaster(getMsgId().gRErrMsg(code));
			}
		}
	}

	/***
	 * 更新俱乐部
	 * 
	 * @author Administrator
	 *
	 */
	public static class UpdateJulebu extends FindMemberHandlerI {
		private String guildName;
		private String guildPlayType;

		public UpdateJulebu(long uid, String nickname, Guild guild,
				IncomeUserI master) {
			super(GuildGTGType.UpdateJulebu, uid, nickname, guild, null,
					master, NOT_NEED_LOAD, NEED_SW);
			this.guildName = guild.getName();
			this.guildPlayType = guild.getPlayTypeDesc();
			handle();
		}

		public UpdateJulebu(GuildGsToGs guildGsToGs) {
			super(GuildGTGType.UpdateJulebu, guildGsToGs, NOT_NEED_LOAD);
			String[] split = guildGsToGs.getData().split("#");
			this.guildName = split[0];
			this.guildPlayType = split[1];
			handle();
		}

		@Override
		public String getData() {
			return String.format("%s#%s", this.guildName, this.guildPlayType);
		}

		@Override
		public int currentServerProcess(IncomeUserI user) {
			if (user.updateJulebuInfo(this.getId(), this.guildName,
					this.guildPlayType) == null) {
				return NTxt.NO_FOUND_GUILD;
			} else
				return NTxt.SUCCESS;
		}

		@Override
		public int currentServerProcessUnload(UserBean userBean) {
			if (userBean.updateJulebuInfo(this.getId(), this.guildName,
					this.guildPlayType) == null) {
				return NTxt.NO_FOUND_GUILD;
			} else
				return NTxt.SUCCESS;
		}

		@Override
		public void handleCode(int code) {
			if (code != NTxt.SUCCESS) {
				LoggerService.getGuildlogs().error("code:{}",
						NoticeTextTemplate.getNoticeText(code));
			}
		}
	}
}
