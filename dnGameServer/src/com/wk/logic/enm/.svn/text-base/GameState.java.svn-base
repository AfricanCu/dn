package com.wk.logic.enm;

/**
 * 游戏状态
 * 
 * 0未准备 1准备 2未抢庄 3已抢庄 4未押注 5已押注 6未完成拼牛 7已完成拼牛 8未选继续 9已选继续
 * 
 * @author ems
 *
 */
public enum GameState {
	/** 未开始 */
	noStart(0, "未准备"),
	/** 已经准备 */
	prepared(1, "已准备"),
	/***/
	noQiangZhuang(2, "未抢庄"),
	/***/
	qiangZhuanged(3, "已抢庄"),
	/***/
	noBetOn(4, "未押注"),
	/***/
	betOned(5, "已押注"),
	/***/
	noFinish(6, "未完成拼牛"),
	/***/
	finished(7, "已完成拼牛"),
	/***/
	noNextRound(8, "未选继续"),
	/***/
	nextRounded(9, "已选继续"), ;

	private int type;
	private String name;

	private GameState(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	// 自动生成开始
public static GameState getEnum(int type){
switch(type) {
case 0:
  return noStart;
case 1:
  return prepared;
case 2:
  return noQiangZhuang;
case 3:
  return qiangZhuanged;
case 4:
  return noBetOn;
case 5:
  return betOned;
case 6:
  return noFinish;
case 7:
  return finished;
case 8:
  return noNextRound;
case 9:
  return nextRounded;
default:
  return null;
}
}// 自动生成结束
}