package com.jery.ngsp.server.scheduletask.type;

import java.util.concurrent.TimeUnit;

import com.jery.ngsp.server.scheduletask.ScheduleTask;
import com.jery.ngsp.server.scheduletask.ScheduleTaskTime;

/**
 * 秒任务
 * 
 * 延迟 seconds
 * 
 * @author ems
 *
 */
public abstract class SecondTask extends ScheduleTask {
	private final ScheduleTaskTime initialDelay;
	private final ScheduleTaskTime nextPeriod;

	/**
	 * 延迟 seconds
	 * 
	 * 隔断seconds
	 * 
	 * @param seconds
	 */
	public SecondTask(int seconds) {
		this(seconds, seconds);
	}

	/**
	 * 延迟 delay
	 * 
	 * 隔断seconds
	 * 
	 * @param seconds
	 */
	public SecondTask(int delay, int seconds) {
		this.initialDelay = new ScheduleTaskTime(delay, TimeUnit.SECONDS);
		this.nextPeriod = new ScheduleTaskTime(seconds, TimeUnit.SECONDS);
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
