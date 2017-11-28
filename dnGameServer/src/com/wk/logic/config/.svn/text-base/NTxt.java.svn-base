package com.wk.logic.config;

import msg.RoomMessage.ChatSm;
import msg.RoomMessage.DissolveRoomSm;
import msg.RoomMessage.ImInfoSaveSm;
import msg.RoomMessage.ImSm;
import msg.RoomMessage.JoinRoomSm;
import msg.RoomMessage.LeaveRoomSm;
import msg.RoomMessage.MemberDissolveRoomSm;
import msg.RoomMessage.NsSm;
import msg.RoomMessage.PrepareRoomSm;
import msg.RoomMessage.ProxyRoomsDissovleSm;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wk.bean.NTxtAbs;

/***
 * 提示信息
 * 
 * @author ems
 *
 */
// 自动生成开始
public class NTxt extends NTxtAbs {
	public static final String PROXY_DISSOLVE_ROOM = "代理解散房间还钻";
	public static final String SPECIAL_DISSOLVE_ROOM = "非正常解散房间还钻";
	public static final String PROXY_CREATE_ROOM = "代理创建房间扣钻";
	public static final String GM_ADD_DIAMOND = "GM工具加钻石";
	public static final String PROXY_ADD_PLAYER_DIAMOND = "代理给玩家加钻";
	public static final String CHARGE_ADD_DIAMOND = "充值加钻";
	public static final String ROOM_START_MASTER_CONSUME_DIAMOND = "房间开始房主消耗钻石";
	public static final String ROOM_START_PLAYER_CONSUME_DIAMOND = "房间开始玩家消耗钻石";
	public static final String CONTINUE_BANKER_CONSUME_DIAMOND = "连庄消耗钻石";

	public static final byte[] MEMBER_DISSOLVE_SUCCESS = MemberDissolveRoomSm
			.newBuilder().setCode(NTxt.SUCCESS).build().toByteArray();
	public static final byte[] DISSOVE_ROOM_SUCCESS = DissolveRoomSm
			.newBuilder().setCode(NTxt.SUCCESS).build().toByteArray();
	public static final byte[] IM_INFO_SAVE_SUCCESS = ImInfoSaveSm.newBuilder()
			.setCode(NTxt.SUCCESS).build().toByteArray();
	public static final byte[] LEAVE_ROOM_SUCCESS = LeaveRoomSm.newBuilder()
			.setCode(NTxt.SUCCESS).build().toByteArray();
	public static final byte[] PREPARE_ROOM_SUCCESS = PrepareRoomSm
			.newBuilder().setCode(NTxt.SUCCESS).build().toByteArray();
	public static final byte[] CHAT_SUCCESS = ChatSm.newBuilder()
			.setCode(NTxt.SUCCESS).build().toByteArray();
	public static final byte[] IM_SUCCESS = ImSm.newBuilder()
			.setCode(NTxt.SUCCESS).build().toByteArray();
	public static final byte[] PROXY_ROOMS_DISSOLVE_SUCCESS = ProxyRoomsDissovleSm
			.newBuilder().setCode(SUCCESS).build().toByteArray();
	public static final byte[] NS_SUCCESS = NsSm.newBuilder().setCode(SUCCESS)
			.build().toByteArray();

	/** 解析code */
	public static Object getCode(byte[] bytes) {
		try {
			return JoinRoomSm.newBuilder().mergeFrom(bytes).getCode();
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
			return bytes;
		}
	}

	/** 找不到玩家 */
	public static final int LOGIN_NOT_FOUND_USER = 101;
	/** 找不到房间 **/
	public static final int NOT_FOUND_ROOM = 102;
	/** 服务器爆满,请稍后尝试 **/
	public static final int LOGIN_SERVER_FULL = 103;
	/** 无权执行此操作 */
	public static final int NO_RIGHT_TO_DO = 104;
	/** 找不到座位 */
	public static final int NOT_FOUND_SEAT = 105;
	/** 玩家不在线 */
	public static final int USER_NOT_ONLINE = 106;
	/** 服务器消息处理频繁 **/
	public static final int SERVER_MESSAGE_FULL = 107;
	/** 操作超时 **/
	public static final int OVER_TIME = 108;
	/** 没有准备 */
	public static final int NO_PREPEARE = 109;
	/** 此刻不能执行此操作,不在状态机中或已经结束！ */
	public static final int OPERATION_RUNSEED_OVER_TIME_OR_NEAR_COMPLETED = 110;
	/** 服务器错误 */
	public static final int LOGIN_SERVER_ID_ERROR = 111;
	/** 已经登录 **/
	public static final int LOGIN_ALREADY = 112;
	/** 已经加入了房间 */
	public static final int JOIN_ROOM_ALREADY = 113;
	/** 创房没有指定切牌类型 */
	public static final int CREATE_ROOM_NO_CUT_PUCK_TYPE = 114;
	/** 连不上gs */
	public static final int GS_CONNECT_EMPTY = 115;
	/** 登陆超时 */
	public static final int LOGIN_OVER_TIME = 116;
	/** 上次登陆服务器id错误 */
	public static final int LAST_LOGIN_SERVER_ID_ERROR = 117;
	/** 无服务器id */
	public static final int LOGIN_USER_NO_SERVER_ID = 118;
	/** 已经包含 */
	public static final int LOGIN_USER_CONTAINS_ALREADY = 119;
	/** 已经在房间中 */
	public static final int ALREADY_IN_ROOM = 120;
	/** 牌局进行中 */
	public static final int FORBIDDEN_GAME_RUNNING = 121;
	/** 房主不能离开房间 */
	public static final int LEAVE_ROOM_MASTER_CAN_NOT_LEAVE = 122;
	/** 加房间无剩余位置 */
	public static final int JOIN_ROOM_NO_LEFT_SEAT = 123;
	/** 未开放 **/
	public static final int NOT_IMPLEMENT = 129;
	/** 已经请求过解散房间 **/
	public static final int MEMBER_DISSOLVE_ALREADY = 130;
	/** 不在这个状态 **/
	public static final int NO_IN_STATE = 131;
	/** 已经准备好 **/
	public static final int PREPARED_ALREADY = 132;
	/** 游戏已经结束 **/
	public static final int GAME_OVER = 133;
	/** 已经选择了是否下庄 **/
	public static final int OFF_BANKER_ALLREADY = 134;
	/*** 已经进行过解散操作 **/
	public static final int MEMBER_DISSOLVEROOM_AGREE_ALREADY = 135;
	/** 游戏记录为空 ***/
	public static final int GAME_RECORD_EMPTY = 136;
	/*** 登录session错误 **/
	public static final int LOGIN_SESSIONCODE_ERROR = 137;
	/*** 登录异常 **/
	public static final int LOGIN_EXCEPTION = 138;
	/** 充值找不到用户 **/
	public static final int CHARGE_NOT_FOUND_USER = 139;
	/*** 切服登录找不到gs ***/
	public static final int SW_LOGIN__GS_CONNECT_EMPTY = 140;
	/** 游戏记录sql异常 **/
	public static final int GAME_RECORD_SQL_EXCEPTION = 141;
	/** 切服登录异常 **/
	public static final int SW_LOGIN_EXCEPTION = 142;
	/** 创建房间失败，已经在房间中 ***/
	public static final int CREATE_ROOM_ALREADY_IN_ROOM = 143;
	/** 创建房间异常 ***/
	public static final int CREATE_ROOM_EXCEPTION = 144;
	/** 加房间预处理，已经在房间中 ***/
	public static final int JOIN_ROOM_BEFORE_ALREADY_IN_ROOM = 145;
	/** 加房间预处理，找不到房间 ***/
	public static final int JOIN_ROOM_BEFORE_NOT_FOUND_ROOM = 146;
	/** 加房间预处理，切服配置为空 ***/
	public static final int JOIN_ROOM_BEFORE_SW_EMPTY = 147;
	/** 加房间失败，已经在房间中 ****/
	public static final int JOIN_ROOM_ALREADY_IN_ROOM = 148;
	/** 准备失败，不在此状态中 ****/
	public static final int PREPARE_ROOM_NO_IN_STATE = 149;
	/** 连庄失败，不在此状态中 ****/
	/** 解散房间失败，无权限做此操作 ****/
	public static final int DISSOLVE_ROOM_NO_RIGHT_TO_DO = 155;
	/** 游戏进行中，不允许解散 ****/
	public static final int DISSOLVE_ROOM_FORBIDDEN_GAME_RUNNING = 156;
	/** 找不到玩家 ****/
	public static final int NOT_FOUND_USER = 158;
	/*** 请求解散操作错误 **/
	public static final int MEMBER_DISSOLVEROOM_COMMON_ERROR = 159;
	/** 代理开房不能超过十个 **/
	public static final int PROXY_CREATE_ROOM_NOT_ENOUGH = 160;
	/*** 代理开房钻石数不足 ***/
	public static final int PROXY_CREATE_ROOM_DIAMOND_NOT_ENOUGH = 161;
	/** 代理创房找不到玩法类型 ***/
	public static final int PROXY_CREATE_ROOM_PTYPE_EMPTY = 162;
	/** 创房找不到玩法类型 ***/
	public static final int CREATE_ROOM_PTYPE_EMPTY = 163;
	/*** 代理创房异常 **/
	public static final int PROXY_CREATE_ROOM_EXCEPTION = 164;

	/*** 创房钻石数不足 ***/
	public static final int CREATE_ROOM_DIAMOND_NOT_ENOUGH = 165;
	/*** 加入房间找不到 **/
	public static final int JOIN_ROOM_NOT_FOUND_ROOM = 166;
	/** 加入房间钻石不足 **/
	public static final int JOIN_ROOM_DIAMOND_NOT_ENOUGH = 167;
	/*** 服务器维护中，暂时关闭开房 ***/
	public static final int CREATE_ROOM_SERVER_FIXING = 169;
	/** 找不到牌 **/
	public static final int DA_PAI_NOT_FOUND_PAI = 200;
	/** 不能选择报听 */
	public static final int BAO_TING_CAN_NOT = 201;
	/** 不能选择自摸 */
	public static final int ZI_MO_PAI_CAN_NOT = 202;
	/** 不能暗杠 */
	public static final int AN_GANG_PAI_CAN_NOT = 203;
	/** 不能明杠 */
	public static final int MING_GANG_PAI_CAN_NOT = 204;
	/** 不能打牌 */
	public static final int DA_PAI_PAI_CAN_NOT = 205;
	/** 不能接炮 */
	public static final int JIE_PAO_CAN_NOT = 206;
	/** 不能接杠 */
	public static final int JIE_GANG_CAN_NOT = 207;
	/** 不能碰牌 */
	public static final int PENG_PAI_CAN_NOT = 208;
	/** 不能过 */
	public static final int OVER_CAN_NOT = 209;
	/** 报听自摸不要只能出抓的牌 */
	public static final int DA_PAI_BAOTING_ZIMO = 210;
	/** 不能选择抢杠 */
	public static final int QIANG_GANG_CAN_NOT = 211;
	/** 不能碰牌 */
	public static final int CHI_PAI_CAN_NOT = 212;
	/** 预处理已经在房间中 */
	public static final int BEFORE_ROOM_ALREADY = 213;
	/** 预处理找不到切服数据 */
	public static final int BEFORE_SW_EMPTY = 214;
	/** 服务器爆满，无法再创建房间 */
	public static final int PROXY_CREATE_ROOM_CAN_NOT = 215;
	/** 双登 */
	public static final int LOGIN_USER_ALREADY_ONLINE = 216;
	/** 服务器繁忙 */
	public static final int SERVER_BUSY = 217;
	/** 俱乐部名额已满，无法创建 **/
	public static final int JULEBU_FULL = 218;
	/** 创建俱乐部异常 **/
	public static final int CREATE_JULEBU_EXCEPTION = 219;
	/** 进入俱乐部异常 **/
	public static final int JULEBU_EXCEPTION = 220;
	/** 找不到俱乐部 **/
	public static final int NO_FOUND_GUILD = 221;
	/** 不是俱乐部成员 **/
	public static final int NOT_GUILD_MEMBER = 222;
	/** 不是俱乐部会长或副会长或成员 **/
	public static final int NOT_GUILD_MASTER_OR_ASSIST_OR_MEMBER = 223;
	/** 不是俱乐部会长或副会长 **/
	public static final int NOT_GUILD_MASTER_OR_ASSIST = 224;
	/** 不是俱乐部会长 **/
	public static final int NOT_GUILD_MASTER = 225;
	/** 找不到成员职位 **/
	public static final int NO_FOUND_MEMBER_JOB = 226;
	/** 公会切服处理请求类型错误 */
	public static final int GUILDGTGTYPE_ERROR = 227;
	/** 应该无需切服 **/
	public static final int NOT_NEED_SW = 228;
	/** 找不到公会成员 **/
	public static final int NO_FOUND_MEMBER = 229;
	/** 加载公会成员失败 **/
	public static final int CAN_NOT_LOAD_MEMBER = 230;
	/** 玩家未申请 */
	public static final int NOT_APPLY = 231;
	/** 未加载玩家 */
	public static final int UN_LOAD_USER = 232;
	/** 无法创建公会房间 **/
	public static final int CAN_NOT_CREATE_GUILD_ROOM = 233;
	/** 房间编号错误 **/
	public static final int JOIN_ROOM_NUM_ERROR = 234;
	/** 钻石不足 **/
	public static final int NOT_ENOUGH_DIAMOND = 235;
	/** 找不到公会房间 **/
	public static final int NOT_FOUND_GUILD_ROOM = 236;
	/** 俱乐部人数已满 **/
	public static final int JULEBU_MEMBER_FULL = 237;
	/** 翻页错误 **/
	public static final int PAGE_ERROR = 238;
	/** 不是副会长或成员 **/
	public static final int NOT_GUILD_ASSIST_OR_MEMBER = 239;
	/** 申请数已满 **/
	public static final int APPLY_NUM_MAX = 240;
	/** 玩法设置错误 */
	public static final int PLAY_TYPE_SET_ERROR = 241;
	/** 区域类型错误 **/
	public static final int DISTRICT_ERROR = 242;
	/** 不属于公会成员 */
	public static final int JOIN_ROOM_NOT_GUILD_MEMBER = 243;
	/** 无权限创建俱乐部 **/
	public static final int NO_RIGHT_CREATEJ = 244;
	/** 无权限加倍 */
	public static final int BET_ON_NO_RIGHT_TO_DO = 245;
	/** 不在押注状态中 **/
	public static final int BET_ON_NO_IN_STATE = 246;
	/** 已经押注 **/
	public static final int BET_ON_ALREADY = 247;
	/** 押注类型错误 **/
	public static final int BET_ON_TYPE_ERROR = 248;
	/** 不在状态中 **/
	public static final int NOT_IN_STATE = 249;
	/** 已经完成拼牛 **/
	public static final int FINISH_PUKE_ALREADY = 250;
	/** 已经选择了下一局 */
	public static final int ALREADY_NEXT_ROUND = 251;
	/** 已经操作了抢庄 **/
	public static final int QIANG_ZHUANG_ALREADY = 252;
	/** 无法加载用户数据 */
	public static final int CAN_NOT_LOAD_USER = 253;
}
// 自动生成结束