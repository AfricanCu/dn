package com.jery.ngsp.server.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import com.jery.ngsp.server.log.LoggerService;

public class MyRejectedExecutionHandler implements RejectedExecutionHandler {
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		if (!e.isShutdown()) {
			LoggerService.getPlatformLog().error(
					"线程池已满或者故障，执行任务超时,runnable = " + r + ",Thread = "
							+ Thread.currentThread().getName());
			r.run();
		}
	}
}