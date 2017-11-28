package com.jery.ngsp.server.scheduletask.type;

import java.util.concurrent.TimeUnit;

import com.jery.ngsp.server.scheduletask.ScheduleTask;
import com.jery.ngsp.server.scheduletask.ScheduleTaskTime;

public abstract class MilliSecondTask extends ScheduleTask {
	private final ScheduleTaskTime initialDelay;
	private final ScheduleTaskTime nextPeriod;

	public MilliSecondTask(int millisSecond) {
		this(millisSecond, millisSecond);
	}

	public MilliSecondTask(int delay, int millisSecond) {
		this.initialDelay = new ScheduleTaskTime(delay, TimeUnit.MILLISECONDS);
		this.nextPeriod = new ScheduleTaskTime(millisSecond,
				TimeUnit.MILLISECONDS);
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