package com.wk.server.logic.room;

import java.util.ArrayList;
import java.util.List;

import msg.LoginMessage.GameRecord;

import org.apache.commons.lang.StringUtils;

import com.wk.bean.NTxtAbs;
import com.wk.bean.SystemConstantsAbs;
import com.wk.logic.config.NTxt;

/**
 * 游戏次数输太多类型
 * 
 * @author ems
 *
 */
public class GameTimesType {
	private static List<GameTimesType> gameMinusTooMuchList;

	/**
	 * 
	 * @param gameMinusTooMuch
	 */
	public static void reset(String gameMinusTooMuch) {
		String[] split = gameMinusTooMuch.split(SystemConstantsAbs.sharp_SEP);
		gameMinusTooMuchList = new ArrayList<GameTimesType>();
		for (String s : split) {
			if (StringUtils.isBlank(s)) {
				continue;
			}
			String[] xx = s.split(SystemConstantsAbs.fen_SEP);
			gameMinusTooMuchList.add(new GameTimesType(Integer.parseInt(xx[0]),
					Integer.parseInt(xx[1])));
		}
	}

	/**
	 * 前几次游戏输分太多？
	 * 
	 * @return
	 */
	public static boolean isGameMinusTooMuch(List<GameRecord> recordList,
			String nickname) {
		for (GameTimesType gameTimesType : GameTimesType.values()) {
			int num = gameTimesType.getTimes();
			if (getGameSumFen(recordList, nickname, num) <= gameTimesType
					.getMinusFen()) {
				NTxtAbs.println(getGameSumFen(recordList, nickname, num));
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取前几场的总分数
	 * 
	 * @param recordList
	 *            游戏记录列表
	 * @param nickname
	 *            玩家昵称
	 * @param num
	 *            场次
	 * @return
	 */
	private static int getGameSumFen(List<GameRecord> recordList,
			String nickname, int num) {
		int sum = 0;
		for (int index = recordList.size() - 1; index >= 0; index--) {
			GameRecord record = recordList.get(index);
			int indexOf = record.getNicknameList().indexOf(nickname);
			if (indexOf != -1) {
				sum += record.getCoinList().get(indexOf);
			}
			if (--num <= 0) {
				return sum;
			}
		}
		return sum;
	}

	/** 场次 */
	private final int times;
	/** 负分多少 */
	private final int minusFen;

	private GameTimesType(int times, int minusFen) {
		this.times = times;
		this.minusFen = minusFen;
	}

	public int getTimes() {
		return times;
	}

	public int getMinusFen() {
		return minusFen;
	}

	public static List<GameTimesType> values() {
		return gameMinusTooMuchList;
	}
}
