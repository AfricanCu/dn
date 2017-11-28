package com.jery.ngsp.server.scheduletask.type;

import java.util.concurrent.TimeUnit;

import com.jery.ngsp.server.scheduletask.ScheduleTask;
import com.jery.ngsp.server.scheduletask.ScheduleTaskTime;

public abstract class MinuteTask extends ScheduleTask {
	private final ScheduleTaskTime initialDelay;
	private final ScheduleTaskTime nextPeriod;

	public MinuteTask(int minutes) {
		this(minutes, minutes);
	}

	public MinuteTask(int delay, int minutes) {
		this.initialDelay = new ScheduleTaskTime(delay, TimeUnit.MINUTES);
		this.nextPeriod = new ScheduleTaskTime(minutes, TimeUnit.MINUTES);
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