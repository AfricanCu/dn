package com.jery.ngsp.server.scheduletask.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.jery.ngsp.server.log.LoggerService;
import com.jery.ngsp.server.scheduletask.ScheduleTask;

public final class MyScheduledThreadPoolExecutor extends
		ScheduledThreadPoolExecutor {
	private static final AtomicLong sequencer = new AtomicLong();
	private static final long NANO_ORIGIN = System.nanoTime();

	MyScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory factory) {
		super(corePoolSize, factory);
	}

	/**
	 * 
	 * @param scheduleTask
	 *            任务
	 * @param initialDelay
	 *            初始延迟多少
	 * @param unit
	 *            初始延迟单位
	 */
	void submit(ScheduleTask scheduleTask, long initialDelay, TimeUnit unit) {
		if (scheduleTask == null || unit == null) {
			throw new NullPointerException();
		}
		if (initialDelay <= 0L)
			initialDelay = 0L;
		else {
			initialDelay = unit.toNanos(initialDelay);
		}
		long triggerTime = now() + initialDelay;
		RunnableScheduledFuture<Void> t = new ScheduledFutureTask<Void>(
				scheduleTask, triggerTime, scheduleTask.getNextPeriod()
						.getTimeInNanos());
		if (isShutdown()) {
			shutdownNow();
			getRejectedExecutionHandler().rejectedExecution(t, this);
			return;
		}
		if (getPoolSize() < getCorePoolSize()) {
			prestartCoreThread();
		}
		if (scheduleTask.setSubmit()) {
			super.getQueue().add(t);
		} else {
			LoggerService.getPlatformLog().warn("无法提交 ！！！！！！！！！！");
		}
	}

	private long now() {
		return System.nanoTime() - NANO_ORIGIN;
	}

	static class ScheduleTaskCallable<V> implements Callable<V> {
		private final ScheduleTask task;

		ScheduleTaskCallable(ScheduleTask task) {
			this.task = task;
		}

		public V call() {
			try {
				this.task.run();
			} catch (Throwable t) {
				LoggerService.getPlatformLog().error(t.getMessage(), t);
			}
			return null;
		}
	}

	private class ScheduledFutureTask<V> extends FutureTask<V> implements
			RunnableScheduledFuture<V> {
		/** Sequence number to break ties FIFO */
		private final long sequenceNumber;
		/** The time the task is enabled to execute in nanoTime units */
		private long time;
		private final ScheduleTask task;
		private volatile long period;

		ScheduledFutureTask(ScheduleTask task, long ns, long period) {
			super(task, null);
			this.task = task;
			this.time = ns;
			this.period = period;
			this.sequenceNumber = MyScheduledThreadPoolExecutor.sequencer
					.getAndIncrement();
		}

		public long getDelay(TimeUnit unit) {
			long d = unit.convert(this.time
					- MyScheduledThreadPoolExecutor.this.now(),
					TimeUnit.NANOSECONDS);
			return d;
		}

		public int compareTo(Delayed other) {
			if (other == this) {
				return 0;
			}
			if ((other instanceof ScheduledFutureTask)) {
				ScheduledFutureTask<?> x = (ScheduledFutureTask<?>) other;
				long diff = this.time - x.time;
				if (diff < 0L)
					return -1;
				if (diff > 0L)
					return 1;
				if (this.sequenceNumber < x.sequenceNumber) {
					return -1;
				}
				return 1;
			}
			long d = getDelay(TimeUnit.NANOSECONDS)
					- other.getDelay(TimeUnit.NANOSECONDS);
			return d < 0L ? -1 : d == 0L ? 0 : 1;
		}

		public boolean isPeriodic() {
			return this.period != 0L;
		}

		private void runPeriodic() {
			boolean ok = super.runAndReset();
			boolean down = MyScheduledThreadPoolExecutor.this.isShutdown();
			if ((ok)
					&& ((!down) || ((MyScheduledThreadPoolExecutor.this
							.getContinueExistingPeriodicTasksAfterShutdownPolicy()) && (!MyScheduledThreadPoolExecutor.this
							.isTerminating())))) {
				if (this.task.isCompleted()) {
					return;
				}
				if (this.task.isPeriodChanged()) {
					this.period = this.task.getNextPeriod().getTimeInNanos();
				}
				long p = this.period;
				if (p > 0L) {
					this.time += p;
				} else
					this.time = (MyScheduledThreadPoolExecutor.this.now() - p);
				MyScheduledThreadPoolExecutor.this.getQueue().add(this);
			}
		}

		public void run() {
			if (isPeriodic()) {
				runPeriodic();
			} else
				super.run();
		}
	}
}
