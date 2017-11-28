package com.jery.ngsp.server.scheduletask.type;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.jery.ngsp.server.scheduletask.ScheduleTask;
import com.jery.ngsp.server.scheduletask.ScheduleTaskTime;
import com.jery.ngsp.server.scheduletask.ScheduleTaskUtil;

public abstract class WeekFixedTask extends ScheduleTask {
	private final ScheduleTaskTime initialDelay;
	private final ScheduleTaskTime nextPeriod;

	public WeekFixedTask(TaskDayOfWeek dayOfWeek, int hourOfDay, int min,
			int second) {
		if ((hourOfDay < 0) || (hourOfDay > 23)) {
			throw new IllegalArgumentException(
					"hourOfDay only in the range of 0 - 23");
		}
		if ((min < 0) || (min > 59)) {
			throw new IllegalArgumentException(
					"minute only in the range of 0 - 60");
		}
		if ((second < 0) || (second > 59)) {
			throw new IllegalArgumentException(
					"second only in the range of 0 - 60");
		}
		this.nextPeriod = new ScheduleTaskTime(7L, TimeUnit.DAYS);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek.getKey());
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, second);
		long delayNanos = ScheduleTaskUtil.getDelayNanos(calendar,
				this.nextPeriod);
		this.initialDelay = new ScheduleTaskTime(delayNanos,
				TimeUnit.NANOSECONDS);
	}

	public final ScheduleTaskTime getInitialDelay() {
		return this.initialDelay;
	}

	public final ScheduleTaskTime getNextPeriod() {
		return this.nextPeriod;
	}

	public final boolean isPeriodChanged() {
		return false;
	}

	public static enum TaskDayOfWeek {
		MONDAY(2, "星期一"), TUESDAY(3, "星期二"), WEDNESDAY(4, "星期三"), THURSDAY(5,
				"星期四"), FRIDAY(6, "星期五"), SATURDAY(7, "星期六"), SUNDAY(1, "星期日");

		private int key;
		private String description;

		private TaskDayOfWeek(int key, String value) {
			this.key = key;
			this.description = value;
		}

		public int getKey() {
			return this.key;
		}

		public String getDescription() {
			return this.description;
		}

	}

}
