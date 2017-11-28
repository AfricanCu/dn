package com.wk.server.logic.room;

/**
 * 自动执行任务类型
 * 
 * @author ems
 *
 */
public enum RoomTaskType {
	/***/
	qiangZhuangAuto(1, "自动抢庄", 6) {
		public Runnable getTask(final RoomAbs roomBase) {
			return new Runnable() {
				@Override
				public void run() {
					roomBase.qiangZhuangAuto();
				}
			};
		}
	},
	/***/
	betOnAuto(2, "自动押注", 7) {
		@Override
		public Runnable getTask(final RoomAbs roomBase) {
			return new Runnable() {
				@Override
				public void run() {
					roomBase.betOnAuto();
				}
			};
		}
	},
	/***/
	finishPukeAuto(3, "自动完成", 5) {
		@Override
		public Runnable getTask(final RoomAbs roomBase) {
			return new Runnable() {
				@Override
				public void run() {
					roomBase.finishPukeAuto();
				}
			};
		}
	},
	/***/
	nextRoundAuto(4, "自动准备下一局", 5) {
		@Override
		public Runnable getTask(final RoomAbs roomBase) {
			return new Runnable() {
				@Override
				public void run() {
					roomBase.nextRoundAuto();
				}
			};
		}
	},
	/***/
	;
	private final int type;
	private final String name;
	private final int timeInSecond;

	private RoomTaskType(int type, String name, int timeInSecond) {
		this.type = type;
		this.name = name;
		this.timeInSecond = timeInSecond;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getTimeInSecond() {
		return timeInSecond;
	}

	/**
	 * 获取自动执行任务
	 * 
	 * @param roomBase
	 * @return
	 */
	public abstract Runnable getTask(RoomAbs roomBase);

	// 自动生成开始
public static RoomTaskType getEnum(int type){
switch(type) {
case 1:
  return qiangZhuangAuto;
case 2:
  return betOnAuto;
case 3:
  return finishPukeAuto;
case 4:
  return nextRoundAuto;
default:
  return null;
}
}// 自动生成结束
}