package com.jery.ngsp.server.scheduletask.type;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.jery.ngsp.server.scheduletask.ScheduleTask;
import com.jery.ngsp.server.scheduletask.ScheduleTaskTime;
import com.jery.ngsp.server.scheduletask.ScheduleTaskUtil;

public abstract class HourFixedTask extends ScheduleTask {
	private final ScheduleTaskTime initialDelay;
	private final ScheduleTaskTime nextPeriod;

	public HourFixedTask(int minute, int second) {
		if ((minute < 0) || (minute > 59)) {
			throw new IllegalArgumentException(
					"minute only in the range of 0 - 60");
		}
		if ((second < 0) || (second > 59)) {
			throw new IllegalArgumentException(
					"second only in the range of 0 - 60");
		}
		this.nextPeriod = new ScheduleTaskTime(1L, TimeUnit.HOURS);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, minute);
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

}