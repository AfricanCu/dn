package com.jery.ngsp.server.scheduletask.impl;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jery.ngsp.server.log.LoggerService;
import com.jery.ngsp.server.scheduletask.ScheduleTask;
import com.jery.ngsp.server.scheduletask.ScheduleTaskManager;
import com.jery.ngsp.server.thread.MyRejectedExecutionHandler;
import com.jery.ngsp.server.thread.MyThreadFactory;
import com.wk.engine.config.ServerConfigAbs;

/**
 * jdk 时效管理器
 * 
 * @author ems
 *
 */
public class JavaScheduleTaskManager extends ScheduleTaskManager {

	private static final ScheduleTaskManager instance = new JavaScheduleTaskManager();

	public static ScheduleTaskManager getInstance() {
		return instance;
	}

	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

	public void start(String poolName, int corePoolSize, boolean java) {
		try {
			if (!this.isStart.compareAndSet(false, true)) {
				LoggerService.getPlatformLog().error("已经启动过了");
				return;
			}
			if (corePoolSize <= 0) {
				throw new IllegalArgumentException();
			}
			this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
					corePoolSize, new MyThreadFactory(poolName),
					new MyRejectedExecutionHandler());
			this.scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
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
		if (task.setSubmit()) {
			ScheduledFuture<?> scheduleWithFixedDelay = this.scheduledThreadPoolExecutor
					.scheduleAtFixedRate(task, task.getInitialDelay()
							.getTimeInNanos(), task.getNextPeriod()
							.getTimeInNanos(), TimeUnit.NANOSECONDS);
			task.setScheduledFuture(scheduleWithFixedDelay);
			LoggerService.getPlatformLog().warn("提交时效！");
		} else {
			LoggerService.getPlatformLog().warn("无法提交时效！！！！！！！！！！！！！");
		}
	}

	public ScheduledFuture<?> submitOneTimeTask(Runnable task, long delay,
			TimeUnit unit) {
		ScheduledFuture<?> schedule = this.scheduledThreadPoolExecutor
				.schedule(task, delay, unit);
		if (ServerConfigAbs.isMonitorPrint())
			LoggerService.getPlatformLog().warn("提交一次任务！");
		return schedule;
	}

	public void shutdown(long timeout) throws InterruptedException {
		if (this.scheduledThreadPoolExecutor != null) {
			this.scheduledThreadPoolExecutor.shutdown();
			this.scheduledThreadPoolExecutor.awaitTermination(timeout,
					TimeUnit.SECONDS);
		}
	}

	public void log() {
		if (this.scheduledThreadPoolExecutor != null) {
			// this.scheduledThreadPoolExecutor.purge();
			LoggerService.getPlatformLog().warn(
					String.format("完成任务数:%s,活跃线程数/核心线程数/高峰线程数:%s/%s/%s",
							this.scheduledThreadPoolExecutor
									.getCompletedTaskCount(),
							this.scheduledThreadPoolExecutor.getActiveCount(),
							this.scheduledThreadPoolExecutor.getCorePoolSize(),
							this.scheduledThreadPoolExecutor
									.getLargestPoolSize()));
		}
	}

}