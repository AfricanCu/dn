package com.wk.util;

import java.util.Calendar;

import com.jery.ngsp.server.scheduletask.type.DayFixedTask;
import com.wk.bean.NTxtAbs;

/**
 * 游戏每日任务
 */
public class GameDayTask extends DayFixedTask {

	public static final GameDayTask gameDayTask = new GameDayTask();
	/** 每天的开始 */
	private final Calendar dayBeginCal = Calendar.getInstance();
	/** 每周的开始 */
	private final Calendar weekBeginCal = Calendar.getInstance();

	private GameDayTask() {
		super(0, 0, 0);
		initDay();
	}

	private void initDay() {
		dayBeginCal.setTimeInMillis(System.currentTimeMillis());
		dayBeginCal.set(Calendar.HOUR_OF_DAY, 0);
		dayBeginCal.set(Calendar.MINUTE, 0);
		dayBeginCal.set(Calendar.SECOND, 0);
		dayBeginCal.set(Calendar.MILLISECOND, 0);
		weekBeginCal.setTimeInMillis(System.currentTimeMillis());
		weekBeginCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		weekBeginCal.set(Calendar.HOUR_OF_DAY, 0);
		weekBeginCal.set(Calendar.MINUTE, 0);
		weekBeginCal.set(Calendar.SECOND, 0);
		weekBeginCal.set(Calendar.MILLISECOND, 0);
	}

	@Override
	public void run() {
		initDay();
	}

	/**
	 * 每天的开始时间
	 * 
	 * @return
	 */
	public long getDayBeginTimeInMillis() {
		return dayBeginCal.getTimeInMillis();
	}

	/**
	 * 每周的开始时间
	 * 
	 * @return
	 */
	public long getWeekBeginTimeInMillis() {
		return weekBeginCal.getTimeInMillis();
	}

	/**
	 * 是否隔天
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static boolean isDayChange(long timeInMillis) {
		return timeInMillis < GameDayTask.gameDayTask.getDayBeginTimeInMillis();
	}

	/**
	 * 是否隔周
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static boolean isWeekChange(long timeInMillis) {
		return timeInMillis < GameDayTask.gameDayTask
				.getWeekBeginTimeInMillis();
	}

}
