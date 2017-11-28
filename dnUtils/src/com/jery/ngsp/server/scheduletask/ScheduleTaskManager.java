package com.jery.ngsp.server.scheduletask;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ScheduleTaskManager {
	protected final AtomicBoolean isStart = new AtomicBoolean(false);

	public boolean isStart() {
		return isStart.get();
	}

	/**
	 * 
	 * @param threadPoolName
	 *            线程池名字
	 * @param threadSize
	 *            线程个数
	 * @param java
	 *            是否使用java自带的定时任务器
	 */
	public abstract void start(String threadPoolName, int threadSize,
			boolean java);

	/**
	 * 提交时效任务
	 * 
	 * @param task
	 */
	public abstract void submitTask(ScheduleTask task);

	/**
	 * 提交执行一次任务
	 * 
	 * @param task
	 * @param delay
	 * @param unit
	 * @return
	 */
	public abstract ScheduledFuture<?> submitOneTimeTask(Runnable task,
			long delay, TimeUnit unit);

	public abstract void shutdown(long timeout) throws InterruptedException;

	public abstract void log();
}