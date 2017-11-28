package com.jery.ngsp.server.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadFactory implements ThreadFactory {
	static final AtomicInteger poolNumber = new AtomicInteger(1);
	final ThreadGroup group;
	final AtomicInteger threadNumber = new AtomicInteger(1);
	final String namePrefix;

	public MyThreadFactory(String poolName) {
		SecurityManager s = System.getSecurityManager();
		this.group = (s != null ? s.getThreadGroup() : Thread.currentThread()
				.getThreadGroup());
		this.namePrefix = (poolName + "[" + poolNumber.getAndIncrement() + "]-thread-");
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(this.group, r, this.namePrefix
				+ this.threadNumber.getAndIncrement(), 0L);

		if (t.isDaemon()) {
			t.setDaemon(false);
		}
		if (t.getPriority() != 5) {
			t.setPriority(5);
		}
		return t;
	}
}