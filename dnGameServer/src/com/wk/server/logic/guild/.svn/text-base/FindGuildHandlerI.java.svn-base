package com.wk.server.logic.guild;

import msg.GuildMessage.QuitJulebuSm;
import msg.InnerMessage.GuildGsToGs;
import msg.RoomMessage;
import msg.GuildMessage.ApplyJulebuSm;
import msg.GuildMessage.InJulebuBeforeSm;
import msg.InnerMessage.GuildGsToGsbk;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.inner.GsSysModule;
import com.wk.engine.inner.gstogs.DeferredHandleI;
import com.wk.engine.inner.gstogs.GuildGTGHandler;
import com.wk.guild.bean.GuildBean;
import com.wk.guild.dao.GuildDao;
import com.wk.logic.config.ConfigTemplate;
import com.wk.logic.config.NTxt;
import com.wk.logic.config.TimeConfig;
import com.wk.logic.enm.GuildGTGType;
import com.wk.logic.enm.MsgId;
import com.wk.logic.enm.SwType;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.logic.friend.HandlerResultI;
import com.wk.server.logic.guild.enm.RemoveJoinJulebuType;

/**
 * 公会已装载，在当前服务器
 * 
 * 公会未装载，在当前服务器注册的
 * 
 * 公会未装载，在另一个服务器上注册的
 * 
 * 这3个作为原子操作随意让实现者组装
 * 
 * @author Administrator
 *
 */
public abstract class FindGuildHandlerI extends HandlerResultI {
	/** 公会在当前服需装载 **/
	public static final boolean NEED_LOAD = true;
	/** 公会在当前服无需装载 **/
	public static final boolean NOT_NEED_LOAD = false;
	private final GuildGTGType type;
	/** 公会ID */
	private final int id;
	/** 发起请求的用户ID */
	private final long uid;
	/** 发起请求的用户昵称 */
	private final String nickname;
	/** 发起请求的用户 */
	private final IncomeUserI user;
	/** 请求的消息id */
	private final MsgId msgId;
	/** 公会在当前服是否需要装载 */
	private final boolean needLoad;
	/** 公会不在当前服是否需要切服 **/
	private final boolean needSw;

	/***
	 * 未切服请求
	 * 
	 * @param id
	 * @param user
	 * @param msgId
	 * @param needLoad
	 */
	public FindGuildHandlerI(GuildGTGType type, int id, IncomeUserI user,
			MsgId msgId, boolean needLoad) {
		super();
		this.type = type;
		this.id = id;
		this.uid = user.getUid();
		this.nickname = user.getNickname();
		this.user = user;
		this.msgId = msgId;
		this.needLoad = needLoad;
		this.needSw = true;
	}

	/**
	 * 切服请求
	 * 
	 * @param id
	 * @param uid
	 * @param nickname
	 * @param msgId
	 */
	public FindGuildHandlerI(GuildGTGType type, GuildGsToGs guildGsToGs,
			MsgId msgId, boolean needLoad) {
		super();
		this.type = type;
		this.id = guildGsToGs.getId();
		this.uid = guildGsToGs.getUid();
		this.nickname = guildGsToGs.getNickname();
		this.user = null;
		this.msgId = msgId;
		this.needLoad = needLoad;
		this.needSw = false;
	}

	/**
	 * 切服处理
	 * 
	 * @param queryGuild
	 *            查询到的俱乐部信息
	 */
	public void swProcess(GuildBean queryGuild) {
		GuildGTGHandler.getInstance().processGuild(getUid(), getId(),
				getNickname(), getData(), queryGuild.getServerId(), this, type);
	}

	public String getData() {
		return null;
	}

	/**
	 * 在当前服已装载处理
	 * 
	 * @param guild
	 *            俱乐部
	 */
	public abstract int currentServerProcess(Guild guild);

	/**
	 * 在当前服未加载处理
	 * 
	 * @param queryGuild
	 * @return
	 */
	public abstract int currentServerProcessUnload(GuildBean queryGuild);

	protected void handle() {
		try {
			Guild guild = GuildManager.getInstance().getGuild(id);
			if (guild == null) {
				GuildBean queryGuild = GuildDao.queryGuild(id);
				if (queryGuild == null) {
					setCode(NTxt.NO_FOUND_GUILD);
				} else if (queryGuild.getServerId() == ServerConfig.serverId) {
					if (this.needLoad) {
						guild = createNewGuild(queryGuild);
						GuildManager.getInstance().put(guild.getId(), guild);
						setCode(currentServerProcess(guild));
						queryGuild.save();
					} else {
						setCode(currentServerProcessUnload(queryGuild));
						queryGuild.save();
					}
				} else {
					if (needSw) {
						swProcess(queryGuild);
					} else {
						setCode(NTxt.NOT_NEED_SW);
					}
				}
			} else {
				setCode(currentServerProcess(guild));
			}
		} catch (Exception e) {
			LoggerService.getGuildlogs().error(e.getMessage(), e);
			setCode(NTxt.JULEBU_EXCEPTION);
		}
	}

	public static Guild createNewGuild(GuildBean bean) throws Exception {
		Guild guild = GuildManager.getInstance().getStackGuild();
		if (guild != null) {
			guild.reset();
			guild.overWrite(bean);
		} else {
			guild = new Guild(bean);
		}
		return guild;
	}

	public int getId() {
		return id;
	}

	public long getUid() {
		return uid;
	}

	public String getNickname() {
		return nickname;
	}

	public IncomeUserI getUser() {
		return user;
	}

	public MsgId getMsgId() {
		return msgId;
	}

	public MsgId getResMsgId() {
		return msgId.getResMsgId();
	}

	public boolean isNeedLoad() {
		return needLoad;
	}

	public void sendMsg(byte[] bytes) {
		if (this.user == null)
			return;
		user.sendMessage(getResMsgId(), bytes);
	}

	public static class InJulebuBeforeHandler extends FindGuildHandlerI {
		public InJulebuBeforeHandler(int id, IncomeUserI user) {
			super(null, id, user, MsgId.InJulebuBeforeCm, NEED_LOAD);
			handle();
		}

		@Override
		public void swProcess(GuildBean queryGuild) {
			RoomMessage.SwServer.Builder sw = GsSysModule.getInstance().getSw(
					queryGuild.getServerId(), SwType.inJulebu, this.getUser());
			if (sw == null) {
				setCode(NTxt.BEFORE_SW_EMPTY);
			} else {
				sendMsg(InJulebuBeforeSm.newBuilder().setSw(sw)
						.setCode(NTxt.SERVER_NEED_SWITCH).setId(this.getId())
						.build().toByteArray());
			}
		}

		@Override
		public int currentServerProcess(Guild guild) {
			byte[] inJulebu = GuildManager.getInstance().inJulebu(
					this.getUser(), this.getId());
			this.getUser().sendMessage(MsgId.InJulebuSm, inJulebu);
			return NTxt.SUCCESS;
		}

		@Override
		public int currentServerProcessUnload(GuildBean queryGuild) {
			return NTxt.SUCCESS;
		}

		@Override
		public void handleCode(int code) {
			if (code != NTxt.SUCCESS) {
				sendMsg(getMsgId().gRErrMsg(code));
			}
		}
	}

	public static class QuitJulebuHandler extends FindGuildHandlerI {
		public QuitJulebuHandler(int id, IncomeUserI user) {
			super(GuildGTGType.QuitJulebu, id, user, MsgId.QuitJulebuCm,
					NOT_NEED_LOAD);
			handle();
		}

		public QuitJulebuHandler(GuildGsToGs guildGsToGs) {
			super(GuildGTGType.QuitJulebu, guildGsToGs, MsgId.QuitJulebuCm,
					NOT_NEED_LOAD);
			handle();
		}

		@Override
		public int currentServerProcess(Guild guild) {
			if (guild.removeMember(this.getUid()) != null) {
				return NTxt.SUCCESS;
			} else
				return NTxt.FAIL;
		}

		@Override
		public int currentServerProcessUnload(GuildBean queryGuild) {
			if (queryGuild.removeMember(this.getUid()) != null) {
				return NTxt.SUCCESS;
			} else
				return NTxt.FAIL;
		}

		@Override
		public void handleCode(int code) {
			if (this.getUser() == null) {
				return;
			}
			if (code == NTxt.SUCCESS) {
				this.getUser().removeJoinJulebu(getId(),
						RemoveJoinJulebuType.quit);
				sendMsg(QuitJulebuSm.newBuilder().setCode(NTxt.SUCCESS)
						.setId(getId()).build().toByteArray());
			} else {
				sendMsg(getMsgId().gRErrMsg(code));
			}
		}
	}

	/**
	 * 申请俱乐部处理
	 * 
	 * @author ems
	 *
	 */
	public static class ApplyJulebuHandler extends FindGuildHandlerI {
		public ApplyJulebuHandler(int id, IncomeUserI user) {
			super(GuildGTGType.ApplyJulebu, id, user, MsgId.ApplyJulebuCm,
					NOT_NEED_LOAD);
			handle();
		}

		public ApplyJulebuHandler(GuildGsToGs guildGsToGs) {
			super(GuildGTGType.ApplyJulebu, guildGsToGs, MsgId.ApplyJulebuCm,
					NOT_NEED_LOAD);
			handle();
		}

		@Override
		public int currentServerProcess(Guild guild) {
			if (isFullGuild(guild)) {
				return NTxt.APPLY_NUM_MAX;
			}
			if (guild.apply(this.getUid(), this.getNickname())) {
				if (this.getUser() != null)
					this.getUser().applyJulebu(guild);
				return NTxt.SUCCESS;
			} else
				return NTxt.FAIL;
		}

		private boolean isFullGuild(GuildBean guild) {
			return guild.getApplySize() >= (ConfigTemplate.getConfigTemplate()
					.getGuildMemberMax() - guild.getMemberSize())
					|| guild.getApplySize() > TimeConfig
							.getGuildApplyMemberMax();
		}

		@Override
		public int currentServerProcessUnload(GuildBean queryGuild) {
			if (isFullGuild(queryGuild)) {
				return NTxt.APPLY_NUM_MAX;
			}
			if (queryGuild.apply(this.getUid(), this.getNickname())) {
				return NTxt.SUCCESS;
			} else
				return NTxt.FAIL;
		}

		@Override
		public void handleCode(int code) {
			if (this.getUser() == null) {
				return;
			}
			if (code == NTxt.SUCCESS) {
				sendMsg(ApplyJulebuSm.newBuilder().setCode(NTxt.SUCCESS)
						.setId(getId()).build().toByteArray());
			} else {
				sendMsg(getMsgId().gRErrMsg(code));
			}
		}
	}
}
