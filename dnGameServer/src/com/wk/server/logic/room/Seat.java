package com.wk.server.logic.room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import msg.BullMessage.BetOnCast;
import msg.BullMessage.BetOnSm;
import msg.BullMessage.BullResult;
import msg.BullMessage.FaLastPukeCast;
import msg.BullMessage.FinishPukeCast;
import msg.BullMessage.FinishPukeSm;
import msg.BullMessage.NextRoundCast;
import msg.BullMessage.NextRoundSm;
import msg.BullMessage.Puke;
import msg.BullMessage.QiangZhuangCast;
import msg.BullMessage.QiangZhuangSm;
import msg.BullMessage.RoundBeginCast;
import msg.BullMessage.SeetResult;
import msg.BullMessage.SeetResult.Builder;
import msg.BullMessage.UserRoundInfo;
import msg.RoomMessage.NsCm;
import msg.RoomMessage.OfflineCast;
import msg.RoomMessage.UserInfo;

import com.wk.bean.SystemConstantsAbs;
import com.wk.engine.config.ServerConfig;
import com.wk.enun.UserState;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.GameState;
import com.wk.logic.enm.MsgId;
import com.wk.puke.Pai;
import com.wk.server.ibatis.select.IncomeUserI;
import com.wk.server.logic.item.ItemTemplate;

/**
 * 座位
 * 
 * @author ems
 *
 */
public class Seat implements Comparable<Seat> {
	private final static byte[] nextRoundSm = NextRoundSm.newBuilder()
			.setCode(NTxt.SUCCESS).build().toByteArray();
	private static final byte[] finishPukeSm = FinishPukeSm.newBuilder()
			.setCode(NTxt.SUCCESS).build().toByteArray();
	private static final NsCm NSCM = NsCm.newBuilder().build();
	private final byte[] finishPukeCast;
	/** 房间 */
	private final RoomAbs room;
	/** 编号 */
	private final int id;
	/** 下标 */
	private final int index;
	/** 玩家 */
	private IncomeUserI user;
	/** 状态 */
	private final AtomicReference<GameState> gstate = new AtomicReference<>(
			GameState.noStart);
	/** 语音座位 */
	private int imSeatIndex;
	/** 积分 */
	private int coin;
	/** 玩家状态 **/
	private UserState userState;
	/** 同意解散房间 0无 1同意 2不同意 3等待 **/
	private MemberDis agreeDissolveRoom;
	/*** 消耗钻石 **/
	private int consumeDiamond;
	/** 座位大结算 */
	private final SeetResult.Builder seetResult = SeetResult.newBuilder();
	/** 通杀次数 */
	private int allWinTimes;
	/** 通赔次数 */
	private int allFailTimes;
	/** 牛牛次数 */
	private int niuNiuTimes;
	/** 无牛次数 */
	private int noNiuTImes;
	/** 胜利次数 */
	private int winTimes;
	/** 地理信息 */
	private NsCm ns;
	/** 前几场游戏负分太多？ */
	private boolean gameMinusTooMuch;
	// /////////////每局缓存/////////////////////
	/***/
	private final RoundBeginCast.Builder roundBeginCast = RoundBeginCast
			.newBuilder();
	/** 发牌信息 */
	private final FaLastPukeCast.Builder faLastPukeCastBuilder = FaLastPukeCast
			.newBuilder();
	/***/
	private final BullResult.Builder bullResult = BullResult.newBuilder();
	/** 下注积分 */
	private BetTimesType betTimes;
	/** 是否抢庄 */
	private boolean qiang;

	public Seat(RoomAbs room, int id) {
		super();
		this.room = room;
		this.id = id;
		this.index = this.id - 1;
		this.finishPukeCast = FinishPukeCast.newBuilder()
				.setSeetIndex(this.getId()).build().toByteArray();
	}

	public int getId() {
		return this.id;
	}

	public int getIndex() {
		return this.index;
	}

	public void init() {
		this.user = null;
		this.gstate.set(GameState.noStart);
		this.imSeatIndex = 0;
		this.coin = this.room.getPType().getInitCoin();
		this.userState = UserState.online;
		this.agreeDissolveRoom = MemberDis.empty;
		this.consumeDiamond = 0;
		this.seetResult.clear();
		this.allWinTimes = 0;
		this.allFailTimes = 0;
		this.niuNiuTimes = 0;
		this.noNiuTImes = 0;
		this.winTimes = 0;
		this.ns = null;
		this.gameMinusTooMuch = false;
		this.nextJu();
	}

	/**
	 * 重新一局
	 */
	public void nextJu() {
		this.roundBeginCast.clear();
		this.faLastPukeCastBuilder.clear();
		this.bullResult.clear();
		this.betTimes = null;
		this.qiang = false;
	}

	public IncomeUserI getUser() {
		return this.user;
	}

	public long getUserUid() {
		return this.user == null ? SystemConstantsAbs.NoUid : this.user
				.getUid();
	}

	public String getUserNickname() {
		return this.user == null ? "" : this.user.getNickname();
	}

	public void addItem(ItemTemplate itemId, int itemNum, boolean isCheck,
			String log) {
		if (this.user == null) {
			return;
		}
		this.user.addItem(itemId, itemNum, isCheck, log);
		if (itemId == ItemTemplate.Diamond_ID && itemNum < 0) {
			this.consumeDiamond += (-itemNum);
		}
	}

	public void setUser(IncomeUserI user) {
		synchronized (this) {
			if (this.user != null) {
				this.user.setRoom(null);
			}
			this.user = user;
			if (this.user != null) {
				this.user.setRoom(this.room);
				this.userState = this.user.getState();
				this.gameMinusTooMuch = this.user.isGameMinusTooMuch();
			} else {
				this.userState = UserState.online;
				this.gameMinusTooMuch = false;
			}
		}
		this.room.updateGuildRoomNum();
	}

	public void setUserInfo(UserInfo.Builder userInfo) {
		if (this.user == null) {
			return;
		}
		userInfo.setUid(this.user.getUid()).setSeetIndex(this.getId())
				.setGstate(this.gstate.get().getType())
				.setUserState(this.getUserState().getType())
				.setImSeatIndex(imSeatIndex).setIsMaster(isMaster())
				.setHeadimg(ServerConfig.getHeadUrl(this.user.getUid()))
				.setNickname(this.user.getNickname()).setCoin(this.coin)
				.setSex(this.user.getSex()).setNs(ns == null ? NSCM : ns)
				.setIp(this.user.getIp());
	}

	public void setUserRoundInfo(UserRoundInfo.Builder userRound) {
		if (this.user == null) {
			return;
		}
		userRound.setSeetIndex(this.getId());
		if (this.isBetOned())
			userRound.setBetTimes(this.betTimes.getType());
		userRound.setChangeCoin(this.getChangeCoin());
		if (this.isQiangZhuanged())
			userRound.setQiang(this.qiang);
		GameState gameState = gstate.get();
		if (gameState == GameState.noQiangZhuang
				|| gameState == GameState.qiangZhuanged
				|| gameState == GameState.noBetOn
				|| gameState == GameState.betOned) {
			userRound.setFaLastPukeCast(FaLastPukeCast.newBuilder().addAllPuke(
					this.roundBeginCast.getPukeList()));
		}
		if (gameState == GameState.noFinish || gameState == GameState.finished
				|| gameState == GameState.noNextRound
				|| gameState == GameState.nextRounded)
			userRound.setFaLastPukeCast(this.faLastPukeCastBuilder.build());
		// NTxt.println(userRound.build());
	}

	public boolean isMaster() {
		return this.user != null && this.user.getUid() == room.getMasterId();
	}

	public boolean isBanker() {
		return this == room.getBankerSeat();
	}

	public boolean isPrepared() {
		return gstate.get() == GameState.prepared;
	}

	/** 是否已经操作过抢庄 */
	public boolean isQiangZhuanged() {
		return this.gstate.get() == GameState.qiangZhuanged;
	}

	/** 是否下注了 */
	public boolean isBetOned() {
		return this.gstate.get() == GameState.betOned;
	}

	public boolean isFinished() {
		return this.gstate.get() == GameState.finished;
	}

	public boolean isNextRounded() {
		return this.gstate.get() == GameState.nextRounded;
	}

	public boolean compareAndSetGstate(GameState expect, GameState update) {
		return gstate.compareAndSet(expect, update);
	}

	public void setGstate(GameState gameState) {
		gstate.set(gameState);
	}

	public void sendMessage(MsgId msgId, byte[] messageLiteOrBuilder) {
		if (this.getUser() != null && this.userState == UserState.online) {
			this.getUser().sendMessage(msgId, messageLiteOrBuilder);
		}
	}

	public void setImSeatIndex(int index) {
		this.imSeatIndex = index;
	}

	/**
	 * 下注
	 * 
	 * @param betTimesType
	 * @param isAuto
	 *            是否自动押注
	 * @return
	 */
	public boolean betCoin(BetTimesType betTimesType, boolean isAuto) {
		if (this.isBanker()) {
			return false;
		}
		boolean compareAndSet = this.compareAndSetGstate(GameState.noBetOn,
				GameState.betOned);
		if (compareAndSet) {
			this.betTimes = betTimesType;
			if (!isAuto) {
				this.sendMessage(
						MsgId.BetOnSm,
						BetOnSm.newBuilder().setCode(NTxt.SUCCESS)
								.setBetTimes(betTimesType.getType()).build()
								.toByteArray());
			}
			this.room.broadCast(MsgId.BetOnCast,
					BetOnCast.newBuilder().setSeetIndex(this.getId())
							.setBetTimes(betTimesType.getType()).build()
							.toByteArray());
		}
		return compareAndSet;
	}

	/**
	 * 发牌
	 * 
	 * @param pais
	 *            牌列表
	 * @param niuIndexs
	 *            哪几个拼成牛
	 * @param niuType
	 *            牛几
	 */
	public void setPukes(List<Pai> pais, NiuType niuType, int[] niuIndexs) {
		//设置该局为第几局
		roundBeginCast.setRound(this.room.getRound());
		//把发来的五张牌遍历出来
		for (Pai pai : pais) {
			//？
			if (roundBeginCast.getPukeCount() < 4)
				roundBeginCast.addPuke(pai.getPuke());
		}
		this.sendMessage(MsgId.RoundBeginCast, roundBeginCast.build()
				.toByteArray());
//<<<<<<< .mine
		/*//计算牌型，仅计算 顺子 同花 葫芦 五小牛 五花牛 炸弹 同花顺 这七种类型
		NiuType niuType = NiuType.calcPaiXing(pais);
		int[] niuIndexs = null;
		if (niuType == null)
			for (int[] array : niuArrays) {
				int point = pais.get(array[0]).getPoint()
						+ pais.get(array[1]).getPoint()
						+ pais.get(array[2]).getPoint();
				//条件如果成立，就是有牛
				if (point == 10 || point == 20 || point == 30) {
					niuIndexs = new int[] { array[0] + 1, array[1] + 1,
							array[2] + 1 };
					int tniu = pais.get(array[3]).getPoint()
							+ pais.get(array[4]).getPoint();
					if (tniu == 20 && point == 30) {
						niuType = NiuType.niuNiu;
					} else {
						niuType = NiuType.getEnum(tniu > 10 ? tniu - 10 : tniu);
					}
					break;
				}
			}
		if (niuType == null) {
			niuType = NiuType.zero;*/
//=======
		if (niuType == NiuType.zero) {
//>>>>>>> .r4205
			//记录无牛次数
			this.noNiuTImes++;
		}
		if (niuType.getType() >= NiuType.niuNiu.getType()) {
			//记录牛牛次数
			this.niuNiuTimes++;
		}
		int maxIndex = 0;
		for (int index = 0; index < pais.size(); index++) {
			if (pais.get(index).biggerThan(pais.get(maxIndex))) {
				maxIndex = index;
			}
		}
		for (Pai pai : pais)
			this.faLastPukeCastBuilder.addPuke(pai.getPuke());
		this.faLastPukeCastBuilder.setNiu(niuType.getType());
		this.faLastPukeCastBuilder.setMaxIndex(maxIndex);
		if (niuIndexs != null) {
			for (int niuIndex : niuIndexs)
				this.faLastPukeCastBuilder.addNiuIndex(niuIndex);
		}
	}

	@Override
	public int compareTo(Seat st) {
		if (this.faLastPukeCastBuilder.getNiu() >= st.faLastPukeCastBuilder
				.getNiu()) {
			if (this.faLastPukeCastBuilder.getNiu() == st.faLastPukeCastBuilder
					.getNiu())
				return getMaxPai().compareTo(st.getMaxPai());
			else
				return 1;
		} else {
			return -1;
		}
	}

	public Pai getMaxPai() {
		return Pai.get(this.faLastPukeCastBuilder.getPukeList().get(
				this.getMaxIndex()));
	}

	public boolean biggerThan(Seat st) {
		return this.compareTo(st) > 0;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
		OfflineCast.Builder offlineCast = OfflineCast.newBuilder()
				.setIsOk(this.getUserState().getType())
				.setSeetIndex(this.getId());
		room.broadCast(MsgId.OfflineCast, offlineCast.build().toByteArray(),
				this);
		if (this.userState == UserState.offline) {
			this.room.checkAllOffLine();
		} else {
			this.room.wakeupGame();
		}
	}

	public UserState getUserState() {
		return userState;
	}

	/** 改变积分 */
	public void addCoin(int c) {
		if (c != 0) {
			this.coin += c;
		}
		if (c > 0)
			this.winTimes++;
	}

	public int getCoin() {
		return this.coin;
	}

	public int getMinusCoin() {
		return this.coin - this.room.getPType().getInitCoin();
	}

	public int getBetCoin() {
		return this.betTimes.getType();
	}

	public MemberDis getAgreeDissolveRoom() {
		return agreeDissolveRoom;
	}

	public void setAgreeDissolveRoom(MemberDis i) {
		this.agreeDissolveRoom = i;
	}

	public int getConsumeDiamond() {
		return this.consumeDiamond;
	}

	public String getUserIp() {
		return this.user == null ? "" : this.user.getIp();
	}

	public void faLastPuke() {
		this.sendMessage(MsgId.FaLastPukeCast, faLastPukeCastBuilder.build()
				.toByteArray());
	}

	public boolean qiang(boolean qiang, boolean isAuto) {
		boolean compareAndSet = this.compareAndSetGstate(
				GameState.noQiangZhuang, GameState.qiangZhuanged);
		if (compareAndSet) {
			this.qiang = qiang;
			if (!isAuto) {
				this.sendMessage(MsgId.QiangZhuangSm, QiangZhuangSm
						.newBuilder().setCode(NTxt.SUCCESS).setQiang(qiang)
						.build().toByteArray());
			}
			this.room.broadCast(MsgId.QiangZhuangCast, QiangZhuangCast
					.newBuilder().setSeetIndex(this.getId()).setQiang(qiang)
					.build().toByteArray());
		}
		return compareAndSet;
	}

	/**
	 * 完成拼牛
	 * 
	 * @param isAuto
	 * @return
	 */
	public boolean finishPuke(boolean isAuto) {
		boolean compareAndSet = this.compareAndSetGstate(GameState.noFinish,
				GameState.finished);
		if (compareAndSet) {
			if (!isAuto) {
				this.sendMessage(MsgId.FinishPukeSm, finishPukeSm);
			}
			this.room.broadCast(MsgId.FinishPukeCast, finishPukeCast);
		}
		return compareAndSet;
	}

	public boolean nextRound(boolean isAuto) {
		boolean compareAndSet = this.compareAndSetGstate(GameState.noNextRound,
				GameState.nextRounded);
		if (compareAndSet) {
			if (!isAuto) {
				this.sendMessage(MsgId.NextRoundSm, nextRoundSm);
			}
			this.room.broadCast(MsgId.NextRoundCast, NextRoundCast.newBuilder()
					.setSeetIndex(this.getId()).build().toByteArray());
		}
		return compareAndSet;
	}

	public NiuType getNiu() {
		return NiuType.getEnum(this.faLastPukeCastBuilder.getNiu());
	}

	public int getMaxIndex() {
		return this.faLastPukeCastBuilder.getMaxIndex();
	}

	public Iterable<? extends Puke> getPukes() {
		return this.faLastPukeCastBuilder.getPukeList();
	}

	/**
	 * 不是庄家计算结果
	 */
	public void calcXianJiaResult() {
		if (this.isBanker())
			return;
		int rs;
		if (this.room.getBankerSeat().biggerThan(this)) {
			int times = this.room.getBankerSeat().getNiu()
					.getTimes(this.room.getPType());
			rs = -this.getBetCoin() * times;
		} else {
			int times = this.getNiu().getTimes(this.room.getPType());
			rs = this.getBetCoin() * times;
		}
		this.addCoin(rs);
		bullResult.setSeetIndex(this.getId()).setCoin(this.getCoin())
				.setChangeCoin(rs).addAllPuke(this.getPukes())
				.setNiu(this.getNiu().getType()).setIndex(this.getMaxIndex());
	}

	public BullResult getBullResult() {
		return bullResult.build();
	}

	public void calcZhuangJiaResult() {
		if (!this.isBanker())
			return;
		int rs = 0;
		boolean allWin = true;
		boolean allFail = true;
		for (Seat st : this.room.getSeats()) {
			if (st.isBanker())
				continue;
			rs -= st.getChangeCoin();
			if (st.getChangeCoin() > 0) {
				allWin = false;
			}
			if (st.getChangeCoin() < 0) {
				allFail = false;
			}
		}
		this.addCoin(rs);
		if (allWin)
			this.allWinTimes++;
		if (allFail)
			this.allFailTimes++;
		bullResult.setSeetIndex(this.getId()).setCoin(this.getCoin())
				.setChangeCoin(rs).addAllPuke(this.getPukes())
				.setNiu(this.getNiu().getType()).setIndex(this.getMaxIndex());
	}

	/**
	 * 下一个位置
	 * 
	 * @return
	 */
	public Seat nextOne() {
		int ind = this.room.getIndex(this) + 1;
		return this.room.getSeats()
				.get(ind == this.room.getUserNum() ? 0 : ind);
	}

	public int getChangeCoin() {
		return this.bullResult.getChangeCoin();
	}

	public SeetResult getSeetResult() {
		return seetResult.setSeetIndex(this.getId())
				.setCoin(this.getMinusCoin()).setAllWinTimes(this.allWinTimes)
				.setAllFailTimes(this.allFailTimes)
				.setNiuNiuTimes(this.niuNiuTimes)
				.setNoNiuTImes(this.noNiuTImes).setWinTimes(this.winTimes)
				.build();
	}

	public void cacheMsg(byte[] bytes) {
		if (this.getUser() != null && this.userState == UserState.online) {
			this.getUser().cacheGameOverCast(bytes);
		}
	}

	public boolean getQiang() {
		return qiang;
	}

	public void setNs(NsCm ns) {
		this.ns = ns;
	}

	public boolean isGameMinusTooMuch() {
		return gameMinusTooMuch;
	}

}