package com.jery.ngsp.server.scheduletask;

import java.util.concurrent.TimeUnit;

public class ScheduleTaskTime {
	private long time;
	private TimeUnit timeUnit;
	private long timeInMIllis;
	private long timeInNanos;

	public ScheduleTaskTime(long time, TimeUnit timeUnit) {
		this.time = time;
		this.timeUnit = timeUnit;
		this.timeInMIllis = this.timeUnit.toMillis(this.time);
		this.timeInNanos = this.timeUnit.toNanos(this.time);
	}

	public long getTimeInMIllis() {
		return timeInMIllis;
	}

	public long getTimeInNanos() {
		return timeInNanos;
	}

}