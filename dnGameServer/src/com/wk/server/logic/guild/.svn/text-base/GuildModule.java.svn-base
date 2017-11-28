package com.wk.server.logic.guild;

import io.netty.channel.Channel;

import java.util.List;

import msg.GuildMessage.AgreeApplyCm;
import msg.GuildMessage.ApplyJulebuCm;
import msg.GuildMessage.ApplyMemberListCm;
import msg.GuildMessage.ClearWinnerCm;
import msg.GuildMessage.CreateJulebuBeforeCm;
import msg.GuildMessage.CreateJulebuCm;
import msg.GuildMessage.DisagreeApplyCm;
import msg.GuildMessage.DissolveJulebuCm;
import msg.GuildMessage.InJulebuBeforeCm;
import msg.GuildMessage.InJulebuCm;
import msg.GuildMessage.InfoSetCm;
import msg.GuildMessage.JulebuMemberListCm;
import msg.GuildMessage.JulebuRecordCm;
import msg.GuildMessage.KickJulebuMemberCm;
import msg.GuildMessage.LevelupMemberCm;
import msg.GuildMessage.OtherSetCm;
import msg.GuildMessage.PlaySetCm;
import msg.GuildMessage.QuitJulebuCm;
import msg.GuildMessage.SearchMemberCm;
import msg.GuildMessage.TableDetailCm;
import msg.GuildMessage.TableInfoCm;
import msg.GuildMessage.WinnerCm;

import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.ModuleAbs;
import com.wk.engine.event.EventAbs;
import com.wk.engine.net.IoMessage;
import com.wk.guild.bean.GuildBean;
import com.wk.guild.dao.GuildDao;
import com.wk.logic.config.NTxt;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.user.dao.UserDao;

public class GuildModule extends ModuleAbs<Integer, GuildBean> {
	private static final long serialVersionUID = 1L;
	private static GuildModule instance;

	public static GuildModule getInstance() {
		return instance;
	}

	/** 最大缓存多少公会 */
	private final int maxLRUSize;

	public GuildModule(int maxLRUSize) {
		super();
		this.maxLRUSize = maxLRUSize;
	}

	public int getMaxLRUSize() {
		return maxLRUSize;
	}

	@Override
	public List<EventAbs> getGameEventList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] processMessage(Channel channel, IoMessage message)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] processMessage(IncomeUserI user, IoMessage message)
			throws Exception {
		MessageLite messageLt = message.genMessageLite();
		switch (message.getMsgId()) {
		case CreateJulebuBeforeCm:
			return GuildManager.getInstance().createJulebuBefore(user,
					(CreateJulebuBeforeCm) messageLt);
		case CreateJulebuCm:
			return GuildManager.getInstance().createJulebu(user,
					(CreateJulebuCm) messageLt);
		case InfoSetCm:
			return GuildManager.getInstance().infoSet(user,
					(InfoSetCm) messageLt);
		case PlaySetCm:
			return GuildManager.getInstance().playSet(user,
					(PlaySetCm) messageLt);
		case OtherSetCm:
			return GuildManager.getInstance().otherSet(user,
					(OtherSetCm) messageLt);
		case DissolveJulebuCm:
			return GuildManager.getInstance().dissolveJulebu(user,
					(DissolveJulebuCm) messageLt);
		case InJulebuBeforeCm:
			return GuildManager.getInstance().inJulebuBefore(user,
					(InJulebuBeforeCm) messageLt);
		case InJulebuCm:
			return GuildManager.getInstance().inJulebu(user,
					(InJulebuCm) messageLt);
		case QuitJulebuCm:
			return GuildManager.getInstance().quitJulebu(user,
					(QuitJulebuCm) messageLt);
		case TableInfoCm:
			return GuildManager.getInstance().tableInfo(user,
					(TableInfoCm) messageLt);
		case ApplyJulebuCm:
			return GuildManager.getInstance().applyJulebu(user,
					(ApplyJulebuCm) messageLt);
		case ApplyMemberListCm:
			return GuildManager.getInstance().applyMemberList(user,
					(ApplyMemberListCm) messageLt);
		case AgreeApplyCm:
			return GuildManager.getInstance().agreeApply(user,
					(AgreeApplyCm) messageLt);
		case DisagreeApplyCm:
			return GuildManager.getInstance().disagreeApply(user,
					(DisagreeApplyCm) messageLt);
		case JulebuMemberListCm:
			return GuildManager.getInstance().julebuMemberList(user,
					(JulebuMemberListCm) messageLt);
		case KickJulebuMemberCm:
			return GuildManager.getInstance().kickJulebuMember(user,
					(KickJulebuMemberCm) messageLt);
		case LevelupMemberCm:
			return GuildManager.getInstance().levelupMember(user,
					(LevelupMemberCm) messageLt);
		case JulebuRecordCm:
			return GuildManager.getInstance().julebuRecord(user,
					(JulebuRecordCm) messageLt);
		case WinnerCm:
			return GuildManager.getInstance()
					.winner(user, (WinnerCm) messageLt);
		case ClearWinnerCm:
			return GuildManager.getInstance().clearWinner(user,
					(ClearWinnerCm) messageLt);
		case SearchMemberCm:
			return GuildManager.getInstance().searchMember(user,
					(SearchMemberCm) messageLt);
		case TableDetailCm:
			return GuildManager.getInstance().tableDetail(user,
					(TableDetailCm) messageLt);
		default:
			return message.getMsgId().gRErrMsg(NTxt.NOT_IMPLEMENT);
		}
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void backDb() throws Exception {
		if (this.isEmpty()) {
			return;
		}
		int updateBatch = GuildDao.updateBatch(this);
		LoggerService.getLogicLog().error("批量更新公会数据,size:{}", updateBatch);
	}
}
