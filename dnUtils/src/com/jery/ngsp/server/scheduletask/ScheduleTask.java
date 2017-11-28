package com.jery.ngsp.server.scheduletask;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 时效
 * 
 * @author ems
 *
 */
public abstract class ScheduleTask implements Runnable {
	/** 时效是否提交 */
	protected final AtomicBoolean isSubmit = new AtomicBoolean(false);
	/** 时效是否完成 */
	protected final AtomicBoolean isComplete = new AtomicBoolean(false);
	/** 提交时效 */
	protected ScheduledFuture<?> scheduledFuture;

	public ScheduleTask() {
	}

	/** 重置时效 */
	public void reset() {
		this.setComplete(true);
		isSubmit.set(false);
		isComplete.set(false);
		scheduledFuture = null;
	}

	/**
	 * 初始执行时效时间间隔
	 * 
	 * @return
	 */
	public abstract ScheduleTaskTime getInitialDelay();

	/**
	 * 下一个执行时效时间间隔
	 * 
	 * @return
	 */
	public abstract ScheduleTaskTime getNextPeriod();

	public abstract boolean isPeriodChanged();

	/**
	 * 时效是否完成
	 * 
	 * @return
	 */
	public boolean isCompleted() {
		return this.isComplete.get();
	}

	/**
	 * 结束时效
	 * 
	 * @param mayInterruptIfRunning
	 *            如果时效在执行是否打断直接结束
	 */
	public void setComplete(boolean mayInterruptIfRunning) {
		this.isComplete.set(true);
		if (scheduledFuture != null) {
			scheduledFuture.cancel(mayInterruptIfRunning);
			scheduledFuture = null;
		}
	}

	/**
	 * 时效是否已经提交
	 * 
	 * @return
	 */
	public boolean isSubmit() {
		return isSubmit.get();
	}

	/**
	 * 设置任务提交状态
	 * 
	 * @return 是否设置成功
	 */
	public boolean setSubmit() {
		return this.isSubmit.compareAndSet(false, true);
	}

	/**
	 * 设置提交时效
	 * 
	 * @param scheduledFuture
	 *            提交时效
	 */
	public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		this.scheduledFuture = scheduledFuture;
	}

}