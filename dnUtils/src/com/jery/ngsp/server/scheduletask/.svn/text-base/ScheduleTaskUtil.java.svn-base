package com.jery.ngsp.server.scheduletask;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ScheduleTaskUtil {
	/**
	 * 延迟多少纳米
	 * 
	 * @param calendar
	 * @param nextPeriod
	 * @return
	 */
	public static long getDelayNanos(Calendar calendar,
			ScheduleTaskTime nextPeriod) {
		long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
		if (delay < 0L) {
			delay += nextPeriod.getTimeInMIllis();
		}
		long toNanos = TimeUnit.MILLISECONDS.toNanos(delay);
		toNanos = toNanos <= 0L ? 0L : toNanos;
		return toNanos;
	}

}