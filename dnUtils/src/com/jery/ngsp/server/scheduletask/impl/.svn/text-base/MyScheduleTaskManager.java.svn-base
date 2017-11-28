package com.jery.ngsp.server.scheduletask.impl;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.jery.ngsp.server.log.LoggerService;
import com.jery.ngsp.server.scheduletask.ScheduleTask;
import com.jery.ngsp.server.scheduletask.ScheduleTaskManager;
import com.jery.ngsp.server.thread.MyThreadFactory;

/**
 * 我的一个时效任务管理器实现
 * 
 * @author ems
 *
 */
public abstract class MyScheduleTaskManager extends ScheduleTaskManager {

	private MyScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

	public void start(String poolName, int corePoolSize, boolean java) {
		try {
			boolean compareAndSet = this.isStart.compareAndSet(false, true);
			if (!compareAndSet) {
				LoggerService.getPlatformLog().error("已经启动过了");
				return;
			}
			if (corePoolSize <= 0) {
				throw new IllegalArgumentException();
			}
			this.scheduledThreadPoolExecutor = new MyScheduledThreadPoolExecutor(
					corePoolSize, new MyThreadFactory(poolName));
			LoggerService.getPlatformLog().error(
					"------------------- 时效任务 初始化成功 -------------------");
		} catch (Exception e) {
			LoggerService.getPlatformLog().error(
					"------------------- 时效任务 初始化失败 -------------------");
			LoggerService.getPlatformLog().error(e.getMessage(), e);
			System.exit(1);
		}
	}

	public void submitTask(ScheduleTask task) {
		this.scheduledThreadPoolExecutor.submit(task, task.getInitialDelay()
				.getTimeInNanos(), TimeUnit.NANOSECONDS);
		LoggerService.getPlatformLog().warn("提交时效！");
	}

	public void shutdown(long timeout) throws InterruptedException {
		if (this.scheduledThreadPoolExecutor != null)
			this.scheduledThreadPoolExecutor.shutdown();
	}

	public void log() {
		if (this.scheduledThreadPoolExecutor != null)
			LoggerService.getPlatformLog().warn(
					"任务数:"
							+ this.scheduledThreadPoolExecutor.getTaskCount()
							+ "活跃线程数/核心线程数/高峰线程数:"
							+ this.scheduledThreadPoolExecutor.getActiveCount()
							+ "/"
							+ this.scheduledThreadPoolExecutor
									.getCorePoolSize()
							+ "/"
							+ this.scheduledThreadPoolExecutor
									.getLargestPoolSize());
	}

	@Override
	public ScheduledFuture<?> submitOneTimeTask(Runnable task, long delay,
			TimeUnit unit) {
		return null;
	}
}