package com.wk.server.logic.room;

import java.util.Comparator;
import java.util.concurrent.ScheduledFuture;

import msg.BackMessage.BattleBackSm;
import msg.BullMessage.BullResultCast;
import msg.LoginMessage.GameRecordSm;
import msg.LoginMessage.Round;
import msg.RoomMessage.DissolveRoomCast;
import msg.RoomMessage.ImInfoSaveCm;

import com.wk.logic.config.NTxt;
import com.wk.util.InsertSort;

public abstract class RoomBaseAbs {
	/*** 游戏记录 **/
	protected transient final GameRecordSm.Builder gameRecordSm = GameRecordSm
			.newBuilder();
	/** 小结算 */
	protected transient final BullResultCast.Builder bullResultCast = BullResultCast
			.newBuilder();
	/** 局记录 */
	protected transient final Round.Builder roundBuilder = Round.newBuilder();
	/** 战斗回放 */
	protected transient final BattleBackSm.Builder battleBackSm = BattleBackSm
			.newBuilder();
	/** 解散房间广播 */
	public static final byte[] dissolveRoomCast = DissolveRoomCast.newBuilder()
			.build().toByteArray();
	/** 最多多少个座位 */
	public static final int SEAT_MAX = 5;
	/** 最少几个人开始 */
	public static final int MIN_START = 2;
	/** 缓存座位排序器 */
	public final static InsertSort<Seat> cacheSeatSort = new InsertSort<>(
			new Comparator<Seat>() {
				@Override
				public int compare(Seat o1, Seat o2) {
					if (o1.getId() > o2.getId()) {
						return 1;
					} else
						return -1;
				}
			});
	/** 结算座位排序器 */
	public final static InsertSort<Seat> resultSeatSort = new InsertSort<>(
			new Comparator<Seat>() {
				@Override
				public int compare(Seat o1, Seat o2) {
					if (o1.getNiu().getType() > o2.getNiu().getType()) {
						return 1;
					} else
						return -1;
				}
			});

	/**
	 * 初始化
	 */
	public void init() {
		this.gameRecordSm.clear().setCode(NTxt.SUCCESS);
	}

	/**
	 * 下一局
	 */
	protected void nextJu() {
		this.bullResultCast.clear();
		this.roundBuilder.clear();
		this.battleBackSm.clear().setCode(NTxt.SUCCESS);
	}
}
