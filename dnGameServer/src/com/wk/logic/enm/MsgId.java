package com.wk.logic.enm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import test.client.util.NoticeTextTemplate;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.net.IoMessage;
import com.wk.engine.net.I.MsgIdI;
import com.wk.engine.util.ProtobufUtils;

public enum MsgId implements MsgIdI {
	// 自动生成开始
/**桌子详情返回 1548**/
TableDetailSm(1548,"桌子详情返回",msg.GuildMessage.TableDetailSm.getDefaultInstance(),null,250),
/**桌子详情请求 1547**/
TableDetailCm(1547,"桌子详情请求",msg.GuildMessage.TableDetailCm.getDefaultInstance(),TableDetailSm,250),
/**搜索成员返回 1546**/
SearchMemberSm(1546,"搜索成员返回",msg.GuildMessage.SearchMemberSm.getDefaultInstance(),null,250),
/**搜索成员请求 1545**/
SearchMemberCm(1545,"搜索成员请求",msg.GuildMessage.SearchMemberCm.getDefaultInstance(),SearchMemberSm,250),
/**清除大赢家返回 1544**/
ClearWinnerSm(1544,"清除大赢家返回",msg.GuildMessage.ClearWinnerSm.getDefaultInstance(),null,250),
/**清除大赢家请求 1543**/
ClearWinnerCm(1543,"清除大赢家请求",msg.GuildMessage.ClearWinnerCm.getDefaultInstance(),ClearWinnerSm,250),
/**大赢家列表返回 1542**/
WinnerSm(1542,"大赢家列表返回",msg.GuildMessage.WinnerSm.getDefaultInstance(),null,250),
/**大赢家列表请求 1541**/
WinnerCm(1541,"大赢家列表请求",msg.GuildMessage.WinnerCm.getDefaultInstance(),WinnerSm,250),
/**俱乐部游戏记录返回 1540**/
JulebuRecordSm(1540,"俱乐部游戏记录返回",msg.GuildMessage.JulebuRecordSm.getDefaultInstance(),null,250),
/**俱乐部游戏记录请求 1539**/
JulebuRecordCm(1539,"俱乐部游戏记录请求",msg.GuildMessage.JulebuRecordCm.getDefaultInstance(),JulebuRecordSm,250),
/**晋升成员返回 1537**/
LevelupMemberSm(1537,"晋升成员返回",msg.GuildMessage.LevelupMemberSm.getDefaultInstance(),null,250),
/**晋升成员请求 1536**/
LevelupMemberCm(1536,"晋升成员请求",msg.GuildMessage.LevelupMemberCm.getDefaultInstance(),LevelupMemberSm,250),
/**踢俱乐部成员返回 1535**/
KickJulebuMemberSm(1535,"踢俱乐部成员返回",msg.GuildMessage.KickJulebuMemberSm.getDefaultInstance(),null,250),
/**踢俱乐部成员请求 1534**/
KickJulebuMemberCm(1534,"踢俱乐部成员请求",msg.GuildMessage.KickJulebuMemberCm.getDefaultInstance(),KickJulebuMemberSm,250),
/**成员列表返回 1533**/
JulebuMemberListSm(1533,"成员列表返回",msg.GuildMessage.JulebuMemberListSm.getDefaultInstance(),null,250),
/**成员列表请求 1532**/
JulebuMemberListCm(1532,"成员列表请求",msg.GuildMessage.JulebuMemberListCm.getDefaultInstance(),JulebuMemberListSm,250),
/**拒绝加入返回 1531**/
DisagreeApplySm(1531,"拒绝加入返回",msg.GuildMessage.DisagreeApplySm.getDefaultInstance(),null,250),
/**拒绝加入请求 1530**/
DisagreeApplyCm(1530,"拒绝加入请求",msg.GuildMessage.DisagreeApplyCm.getDefaultInstance(),DisagreeApplySm,250),
/**同意加入返回 1529**/
AgreeApplySm(1529,"同意加入返回",msg.GuildMessage.AgreeApplySm.getDefaultInstance(),null,250),
/**同意加入请求 1528**/
AgreeApplyCm(1528,"同意加入请求",msg.GuildMessage.AgreeApplyCm.getDefaultInstance(),AgreeApplySm,250),
/**申请人列表返回 1527**/
ApplyMemberListSm(1527,"申请人列表返回",msg.GuildMessage.ApplyMemberListSm.getDefaultInstance(),null,250),
/**申请人列表请求 1526**/
ApplyMemberListCm(1526,"申请人列表请求",msg.GuildMessage.ApplyMemberListCm.getDefaultInstance(),ApplyMemberListSm,250),
/**申请加入俱乐部返回 1525**/
ApplyJulebuSm(1525,"申请加入俱乐部返回",msg.GuildMessage.ApplyJulebuSm.getDefaultInstance(),null,250),
/**申请加入俱乐部请求 1524**/
ApplyJulebuCm(1524,"申请加入俱乐部请求",msg.GuildMessage.ApplyJulebuCm.getDefaultInstance(),ApplyJulebuSm,250),
/**俱乐部大厅桌子信息返回 1523**/
TableInfoSm(1523,"俱乐部大厅桌子信息返回",msg.GuildMessage.TableInfoSm.getDefaultInstance(),null,250),
/**俱乐部大厅桌子信息请求 1522**/
TableInfoCm(1522,"俱乐部大厅桌子信息请求",msg.GuildMessage.TableInfoCm.getDefaultInstance(),TableInfoSm,250),
/**退出俱乐部返回 1521**/
QuitJulebuSm(1521,"退出俱乐部返回",msg.GuildMessage.QuitJulebuSm.getDefaultInstance(),null,250),
/**退出俱乐部请求 1520**/
QuitJulebuCm(1520,"退出俱乐部请求",msg.GuildMessage.QuitJulebuCm.getDefaultInstance(),QuitJulebuSm,250),
/**进入俱乐部大厅返回 1519**/
InJulebuSm(1519,"进入俱乐部大厅返回",msg.GuildMessage.InJulebuSm.getDefaultInstance(),null,250),
/**进入俱乐部大厅请求 1518**/
InJulebuCm(1518,"进入俱乐部大厅请求",msg.GuildMessage.InJulebuCm.getDefaultInstance(),InJulebuSm,250),
/**进入俱乐部大厅预处理返回 1517**/
InJulebuBeforeSm(1517,"进入俱乐部大厅预处理返回",msg.GuildMessage.InJulebuBeforeSm.getDefaultInstance(),null,250),
/**进入俱乐部大厅预处理请求 1516**/
InJulebuBeforeCm(1516,"进入俱乐部大厅预处理请求",msg.GuildMessage.InJulebuBeforeCm.getDefaultInstance(),InJulebuBeforeSm,250),
/**解散俱乐部返回 1515**/
DissolveJulebuSm(1515,"解散俱乐部返回",msg.GuildMessage.DissolveJulebuSm.getDefaultInstance(),null,250),
/**解散俱乐部请求 1514**/
DissolveJulebuCm(1514,"解散俱乐部请求",msg.GuildMessage.DissolveJulebuCm.getDefaultInstance(),DissolveJulebuSm,250),
/**其他设置返回 1513**/
OtherSetSm(1513,"其他设置返回",msg.GuildMessage.OtherSetSm.getDefaultInstance(),null,250),
/**其他设置请求 1512**/
OtherSetCm(1512,"其他设置请求",msg.GuildMessage.OtherSetCm.getDefaultInstance(),OtherSetSm,250),
/**玩法设置返回 1511**/
PlaySetSm(1511,"玩法设置返回",msg.GuildMessage.PlaySetSm.getDefaultInstance(),null,250),
/**玩法设置请求 1510**/
PlaySetCm(1510,"玩法设置请求",msg.GuildMessage.PlaySetCm.getDefaultInstance(),PlaySetSm,250),
/**信息设置返回 1509**/
InfoSetSm(1509,"信息设置返回",msg.GuildMessage.InfoSetSm.getDefaultInstance(),null,250),
/**信息设置请求 1508**/
InfoSetCm(1508,"信息设置请求",msg.GuildMessage.InfoSetCm.getDefaultInstance(),InfoSetSm,250),
/**创建俱乐部返回 1507**/
CreateJulebuSm(1507,"创建俱乐部返回",msg.GuildMessage.CreateJulebuSm.getDefaultInstance(),null,250),
/**创建俱乐部请求 1506**/
CreateJulebuCm(1506,"创建俱乐部请求",msg.GuildMessage.CreateJulebuCm.getDefaultInstance(),CreateJulebuSm,250),
/**创建俱乐部预处理返回 1505**/
CreateJulebuBeforeSm(1505,"创建俱乐部预处理返回",msg.GuildMessage.CreateJulebuBeforeSm.getDefaultInstance(),null,250),
/**创建俱乐部预处理请求 1504**/
CreateJulebuBeforeCm(1504,"创建俱乐部预处理请求",msg.GuildMessage.CreateJulebuBeforeCm.getDefaultInstance(),CreateJulebuBeforeSm,250),
/**更新俱乐部广播 1502**/
UpdateJulebuCast(1502,"更新俱乐部广播",msg.GuildMessage.UpdateJulebuCast.getDefaultInstance(),null,250),
/**删除俱乐部广播 1501**/
DelJulebuCast(1501,"删除俱乐部广播",msg.GuildMessage.DelJulebuCast.getDefaultInstance(),null,250),
/**加俱乐部广播 1500**/
AddJulebuCast(1500,"加俱乐部广播",msg.GuildMessage.AddJulebuCast.getDefaultInstance(),null,250),
/**下一轮广播 523**/
NextRoundCast(523,"下一轮广播",msg.BullMessage.NextRoundCast.getDefaultInstance(),null,250),
/**下一轮返回 522**/
NextRoundSm(522,"下一轮返回",msg.BullMessage.NextRoundSm.getDefaultInstance(),null,250),
/**下一轮请求 521**/
NextRoundCm(521,"下一轮请求",msg.BullMessage.NextRoundCm.getDefaultInstance(),NextRoundSm,250),
/**押注开始广播 520**/
BetOnBeginCast(520,"押注开始广播",msg.BullMessage.BetOnBeginCast.getDefaultInstance(),null,250),
/**抢庄开始广播 519**/
QzBeginCast(519,"抢庄开始广播",msg.BullMessage.QzBeginCast.getDefaultInstance(),null,250),
/**断线重连返回 518**/
ReconnectSm(518,"断线重连返回",msg.BullMessage.ReconnectSm.getDefaultInstance(),null,250),
/**断线重连请求 517**/
ReconnectCm(517,"断线重连请求",msg.BullMessage.ReconnectCm.getDefaultInstance(),ReconnectSm,250),
/**游戏结束推送 516**/
GameOverCast(516,"游戏结束推送",msg.BullMessage.GameOverCast.getDefaultInstance(),null,250),
/**牛结算推送 515**/
BullResultCast(515,"牛结算推送",msg.BullMessage.BullResultCast.getDefaultInstance(),null,250),
/**亮牌推送 514**/
FinishPukeCast(514,"亮牌推送",msg.BullMessage.FinishPukeCast.getDefaultInstance(),null,250),
/**亮牌返回 513**/
FinishPukeSm(513,"亮牌返回",msg.BullMessage.FinishPukeSm.getDefaultInstance(),null,250),
/**亮牌请求 512**/
FinishPukeCm(512,"亮牌请求",msg.BullMessage.FinishPukeCm.getDefaultInstance(),FinishPukeSm,250),
/**发最后牌广播 511**/
FaLastPukeCast(511,"发最后牌广播",msg.BullMessage.FaLastPukeCast.getDefaultInstance(),null,250),
/**玩家押注推送 510**/
BetOnCast(510,"玩家押注推送",msg.BullMessage.BetOnCast.getDefaultInstance(),null,250),
/**押注返回 509**/
BetOnSm(509,"押注返回",msg.BullMessage.BetOnSm.getDefaultInstance(),null,250),
/**押注请求 508**/
BetOnCm(508,"押注请求",msg.BullMessage.BetOnCm.getDefaultInstance(),BetOnSm,250),
/**庄广播 507**/
BankerCast(507,"庄广播",msg.BullMessage.BankerCast.getDefaultInstance(),null,250),
/**抢庄广播 506**/
QiangZhuangCast(506,"抢庄广播",msg.BullMessage.QiangZhuangCast.getDefaultInstance(),null,250),
/**抢庄返回 505**/
QiangZhuangSm(505,"抢庄返回",msg.BullMessage.QiangZhuangSm.getDefaultInstance(),null,250),
/**抢庄请求 504**/
QiangZhuangCm(504,"抢庄请求",msg.BullMessage.QiangZhuangCm.getDefaultInstance(),QiangZhuangSm,250),
/**开局信息推送 503**/
RoundBeginCast(503,"开局信息推送",msg.BullMessage.RoundBeginCast.getDefaultInstance(),null,250),
/**玩家解散房间广播 502**/
MemberDissolveRoomCast(502,"玩家解散房间广播",msg.RoomMessage.MemberDissolveRoomCast.getDefaultInstance(),null,0),
/**玩家解散房间返回 501**/
MemberDissolveRoomSm(501,"玩家解散房间返回",msg.RoomMessage.MemberDissolveRoomSm.getDefaultInstance(),null,0),
/**玩家解散房间请求 500**/
MemberDissolveRoomCm(500,"玩家解散房间请求",msg.RoomMessage.MemberDissolveRoomCm.getDefaultInstance(),MemberDissolveRoomSm,250),
/**代理创建房间预处理返回 241**/
ProxyCreateRoomBeforeSm(241,"代理创建房间预处理返回",msg.RoomMessage.ProxyCreateRoomBeforeSm.getDefaultInstance(),null,0),
/**代理创建房间预处理请求 240**/
ProxyCreateRoomBeforeCm(240,"代理创建房间预处理请求",msg.RoomMessage.ProxyCreateRoomBeforeCm.getDefaultInstance(),ProxyCreateRoomBeforeSm,250),
/**经纬广播 239**/
NsCast(239,"经纬广播",msg.RoomMessage.NsCast.getDefaultInstance(),null,0),
/**经纬返回 238**/
NsSm(238,"经纬返回",msg.RoomMessage.NsSm.getDefaultInstance(),null,0),
/**经纬请求 237**/
NsCm(237,"经纬请求",msg.RoomMessage.NsCm.getDefaultInstance(),NsSm,250),
/**房主创建房间预处理返回 234**/
CreateRoomBeforeSm(234,"房主创建房间预处理返回",msg.RoomMessage.CreateRoomBeforeSm.getDefaultInstance(),null,0),
/**房主创建房间预处理请求 233**/
CreateRoomBeforeCm(233,"房主创建房间预处理请求",msg.RoomMessage.CreateRoomBeforeCm.getDefaultInstance(),CreateRoomBeforeSm,250),
/**代解散房返回 232**/
ProxyRoomsDissovleSm(232,"代解散房返回",msg.RoomMessage.ProxyRoomsDissovleSm.getDefaultInstance(),null,0),
/**代解散房请求 231**/
ProxyRoomsDissovleCm(231,"代解散房请求",msg.RoomMessage.ProxyRoomsDissovleCm.getDefaultInstance(),ProxyRoomsDissovleSm,250),
/**房间状态改变推送 230**/
ProxyRoomsUpdateCast(230,"房间状态改变推送",msg.RoomMessage.ProxyRoomsUpdateCast.getDefaultInstance(),null,0),
/**解散房间推送 229**/
ProxyRoomsDelCast(229,"解散房间推送",msg.RoomMessage.ProxyRoomsDelCast.getDefaultInstance(),null,0),
/**增加房间推送 228**/
ProxyRoomsAddCast(228,"增加房间推送",msg.RoomMessage.ProxyRoomsAddCast.getDefaultInstance(),null,0),
/**房间列表推送 227**/
ProxyRoomsCast(227,"房间列表推送",msg.RoomMessage.ProxyRoomsCast.getDefaultInstance(),null,0),
/**代开房返回 226**/
ProxyCreateRoomSm(226,"代开房返回",msg.RoomMessage.ProxyCreateRoomSm.getDefaultInstance(),null,0),
/**代开房请求 225**/
ProxyCreateRoomCm(225,"代开房请求",msg.RoomMessage.ProxyCreateRoomCm.getDefaultInstance(),ProxyCreateRoomSm,250),
/**语音广播 224**/
ImCast(224,"语音广播",msg.RoomMessage.ImCast.getDefaultInstance(),null,0),
/**语音返回 223**/
ImSm(223,"语音返回",msg.RoomMessage.ImSm.getDefaultInstance(),null,0),
/**语音请求 222**/
ImCm(222,"语音请求",msg.RoomMessage.ImCm.getDefaultInstance(),ImSm,250),
/**聊天广播 221**/
ChatCast(221,"聊天广播",msg.RoomMessage.ChatCast.getDefaultInstance(),null,0),
/**聊天返回 220**/
ChatSm(220,"聊天返回",msg.RoomMessage.ChatSm.getDefaultInstance(),null,0),
/**聊天请求 219**/
ChatCm(219,"聊天请求",msg.RoomMessage.ChatCm.getDefaultInstance(),ChatSm,250),
/**解散房间广播 218**/
DissolveRoomCast(218,"解散房间广播",msg.RoomMessage.DissolveRoomCast.getDefaultInstance(),null,250),
/**语音房间信息保存广播 217**/
ImInfoSaveCast(217,"语音房间信息保存广播",msg.RoomMessage.ImInfoSaveCast.getDefaultInstance(),null,0),
/**语音房间信息保存返回 216**/
ImInfoSaveSm(216,"语音房间信息保存返回",msg.RoomMessage.ImInfoSaveSm.getDefaultInstance(),null,0),
/**语音房间信息保存请求 215**/
ImInfoSaveCm(215,"语音房间信息保存请求",msg.RoomMessage.ImInfoSaveCm.getDefaultInstance(),ImInfoSaveSm,250),
/**断线广播 214**/
OfflineCast(214,"断线广播",msg.RoomMessage.OfflineCast.getDefaultInstance(),null,0),
/**准备广播 213**/
PrepareRoomCast(213,"准备广播",msg.RoomMessage.PrepareRoomCast.getDefaultInstance(),null,0),
/**准备返回 212**/
PrepareRoomSm(212,"准备返回",msg.RoomMessage.PrepareRoomSm.getDefaultInstance(),null,0),
/**准备请求 211**/
PrepareRoomCm(211,"准备请求",msg.RoomMessage.PrepareRoomCm.getDefaultInstance(),PrepareRoomSm,250),
/**解散房间返回 210**/
DissolveRoomSm(210,"解散房间返回",msg.RoomMessage.DissolveRoomSm.getDefaultInstance(),null,0),
/**解散房间请求 209**/
DissolveRoomCm(209,"解散房间请求",msg.RoomMessage.DissolveRoomCm.getDefaultInstance(),DissolveRoomSm,250),
/**离开房间返回 208**/
LeaveRoomSm(208,"离开房间返回",msg.RoomMessage.LeaveRoomSm.getDefaultInstance(),null,0),
/**离开房间请求 207**/
LeaveRoomCm(207,"离开房间请求",msg.RoomMessage.LeaveRoomCm.getDefaultInstance(),LeaveRoomSm,250),
/**房间加入推送 206**/
JoinRoomCast(206,"房间加入推送",msg.RoomMessage.JoinRoomCast.getDefaultInstance(),null,0),
/**加入房间返回 205**/
JoinRoomSm(205,"加入房间返回",msg.RoomMessage.JoinRoomSm.getDefaultInstance(),null,0),
/**加入房间请求 204**/
JoinRoomCm(204,"加入房间请求",msg.RoomMessage.JoinRoomCm.getDefaultInstance(),JoinRoomSm,250),
/**房主加入房间预处理返回 203**/
JoinRoomBeforeSm(203,"房主加入房间预处理返回",msg.RoomMessage.JoinRoomBeforeSm.getDefaultInstance(),null,0),
/**房主加入房间预处理请求 202**/
JoinRoomBeforeCm(202,"房主加入房间预处理请求",msg.RoomMessage.JoinRoomBeforeCm.getDefaultInstance(),JoinRoomBeforeSm,250),
/**房主创建房间返回 201**/
CreateRoomSm(201,"房主创建房间返回",msg.RoomMessage.CreateRoomSm.getDefaultInstance(),null,0),
/**房主创建房间请求 200**/
CreateRoomCm(200,"房主创建房间请求",msg.RoomMessage.CreateRoomCm.getDefaultInstance(),CreateRoomSm,250),
/**proxy游戏记录返回 11**/
ProxyRecordCast(11,"proxy游戏记录返回",msg.LoginMessage.ProxyRecordCast.getDefaultInstance(),null,250),
/**游戏记录返回 10**/
GameRecordSm(10,"游戏记录返回",msg.LoginMessage.GameRecordSm.getDefaultInstance(),null,250),
/**游戏记录请求 9**/
GameRecordCm(9,"游戏记录请求",msg.LoginMessage.GameRecordCm.getDefaultInstance(),GameRecordSm,250),
/**充值成功推送 8**/
ChargeCast(8,"充值成功推送",msg.LoginMessage.ChargeCast.getDefaultInstance(),null,250),
/**记录推送 7**/
PlayerRecordCast(7,"记录推送",msg.LoginMessage.PlayerRecordCast.getDefaultInstance(),null,250),
/**个人信息推送 6**/
PlayerCast(6,"个人信息推送",msg.LoginMessage.PlayerCast.getDefaultInstance(),null,250),
/**切服登录返回 4**/
SwLoginSm(4,"切服登录返回",msg.LoginMessage.SwLoginSm.getDefaultInstance(),null,0),
/**切服登录请求 3**/
SwLoginCm(3,"切服登录请求",msg.LoginMessage.SwLoginCm.getDefaultInstance(),SwLoginSm,250),
/**返回登录 2**/
LoginSm(2,"返回登录",msg.LoginMessage.LoginSm.getDefaultInstance(),null,0),
/**请求登录 1**/
LoginCm(1,"请求登录",msg.LoginMessage.LoginCm.getDefaultInstance(),LoginSm,250),
// 自动生成结束
	/** 心跳挂起10分钟请求 -4 **/
	HeartPause(-4, "心跳挂起10分钟请求", null, null, 10000),
	/** 心跳挂起10分钟恢复 -5 **/
	HeartResume(-5, "心跳挂起10分钟恢复", null, null, 10000), ;
	;
	private final short type;
	private final String name;
	/** 消息默认实例 */
	private final MessageLite defaultInst;
	private final MsgId resMsgId;
	/** 消息发送间隔限制 单位（毫秒） */
	private final long period;
	/** setCode方法，返回消息都要求有这个方法 */
	private Method setCodeMethod;
	/** 缓存错误返回消息 **/
	private final HashMap<Integer, byte[]> errorResCacheMap = new HashMap<Integer, byte[]>(
			0);

	private MsgId(int type, String name, MessageLite clazz, MsgId resMsgId,
			long period) {
		this.type = (short) type;
		this.name = name;
		this.defaultInst = clazz;
		this.resMsgId = resMsgId;
		this.period = period;
		if (this.resMsgId != null
				&& !SystemConstants.exceptNameList.contains(this.resMsgId
						.name())) {
			Class<? extends MessageLite.Builder> clazs = this.resMsgId
					.genMessageLiteBuilder().getClass();
			Class<? extends MessageLite> liteClazs = this.resMsgId.defaultInst
					.getClass();
			try {
				this.setCodeMethod = clazs.getMethod("setCode", int.class);
				Field field = liteClazs.getField("CODE_FIELD_NUMBER");
				int CODE_FIELD_NUMBER = field.getInt(this.defaultInst);
				if (CODE_FIELD_NUMBER != 1) {
					throw new IllegalArgumentException(
							"消息返回code字段下标错误！!=1 CODE_FIELD_NUMBER="
									+ CODE_FIELD_NUMBER);
				}
			} catch (NoSuchMethodException | SecurityException
					| NoSuchFieldException | IllegalArgumentException
					| IllegalAccessException e) {
				LoggerService.getPlatformLog().error(e.getMessage(), e);
			}
		} else {
			this.setCodeMethod = null;
		}
	}

	public short getType() {
		return type;
	}

	public long getPeriod() {
		return period;
	}

	public String getName() {
		return name;
	}

	public MessageLite getDefaultInst() {
		return defaultInst;
	}

	/**
	 * 生成proto builder
	 */
	public MessageLite.Builder genMessageLiteBuilder() {
		return defaultInst.newBuilderForType();
	}

	public MsgId getResMsgId() {
		return resMsgId;
	}

	public Method getSetCodeMethod() {
		return setCodeMethod;
	}

	public byte[] gRErrMsg(int code) {
		byte[] bs = this.errorResCacheMap.get(code);
		if (bs == null) {
			MessageLite.Builder liteorBuilder = this.resMsgId
					.genMessageLiteBuilder();
			try {
				setCodeMethod.invoke(liteorBuilder, code);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LoggerService.getPlatformLog().error(e.getMessage(), e);
			}
			bs = liteorBuilder.build().toByteArray();
			this.errorResCacheMap.put(code, bs);
		}
		String format = String.format("%s,消息:%s,%s,error:%s,code:%s",
				new Exception().getStackTrace()[1], this.name, this.name(),
				NoticeTextTemplate.getNoticeText(code), code);
		LoggerService.getLogicLog().error(format);
		System.err.println(format);
		return bs;
	}

	public static MessageLite getMessageLite(IoMessage message)
			throws InvalidProtocolBufferException {
		return ProtobufUtils.getProtobuf(message.getMsg(), message.getMsgId()
				.getDefaultInst());
	}

	private final static Map<Short, MsgId> typeMap = new HashMap<>();
	static {
		MsgId[] missionStateArr = MsgId.values();
		for (int i = 0; i < missionStateArr.length; i++) {
			MsgId missionState = missionStateArr[i];
			typeMap.put(missionState.getType(), missionState);
		}
	}

	/**
	 *
	 * @param type
	 * @return
	 */
	public static MsgId getEnum(short type) {
		return typeMap.get(type);
	}

}
