package com.jery.ngsp.server.worktask.impl;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jery.ngsp.server.log.LoggerService;
import com.jery.ngsp.server.thread.MyRejectedExecutionHandler;
import com.jery.ngsp.server.thread.MyThreadFactory;
import com.jery.ngsp.server.worktask.SynchronousWorkTaskPool;
import com.jery.ngsp.server.worktask.WorkTaskPoolManager;

public class WorkTaskPoolManagerImpl implements WorkTaskPoolManager {
	private static final WorkTaskPoolManagerImpl instance = new WorkTaskPoolManagerImpl();
	private AtomicBoolean isStart = new AtomicBoolean(false);
	private ThreadPoolExecutor executor;

	public static WorkTaskPoolManagerImpl getInstance() {
		return instance;
	}

	public void start(String threadPoolName, int corePoolSize,
			int maximumPoolSize, int keepAliveTime) {
		try {
			boolean compareAndSet = this.isStart.compareAndSet(false, true);
			if (!compareAndSet) {
				LoggerService.getPlatformLog().error("已经启动过了");
				return;
			}
			this.executor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>(), new MyThreadFactory(
							threadPoolName), new MyRejectedExecutionHandler());
			LoggerService.getPlatformLog().error(
					"------------------- 工作任务池 初始化成功 -------------------");
		} catch (Exception e) {
			LoggerService.getPlatformLog().error(
					"------------------- 工作任务池 初始化成功 -------------------");
			LoggerService.getPlatformLog().error(e.getMessage(), e);
			System.exit(1);
		}
	}

	public SynchronousWorkTaskPool createSynchronousWorkTaskPool() {
		return new SynchronousWorkTaskPoolImpl(this.executor);
	}

	public Future<?> submitAndExecute(Runnable runnable) {
		return this.executor.submit(runnable);
	}
}

/*
 * Location:
 * D:\Documents\workspace\GAME-SERVER-V1.0\libs\JERY-NGSP-IMPL-V1.0.jar
 * Qualified Name: com.jery.ngsp.server.worktask.impl.WorkTaskPoolManagerImpl
 * JD-Core Version: 0.6.0
 */