package com.jery.ngsp.server.worktask.impl;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.jery.ngsp.server.worktask.SynchronousWorkTaskPool;

public class SynchronousWorkTaskPoolImpl implements SynchronousWorkTaskPool {
	private ConcurrentLinkedQueue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();
	private AtomicBoolean taskCompleted = new AtomicBoolean(true);
	private ThreadPoolExecutor executor;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	SynchronousWorkTaskPoolImpl(ThreadPoolExecutor executor) {
		this.executor = executor;
	}

	private Runnable taskRunnable = new Runnable() {
		public void run() {
			while (true) {
				SynchronousWorkTaskPoolImpl.this.lock.writeLock().lock();
				Runnable task = SynchronousWorkTaskPoolImpl.this.taskQueue
						.poll();
				if (task == null) {
					SynchronousWorkTaskPoolImpl.this.taskCompleted
							.compareAndSet(false, true);
					SynchronousWorkTaskPoolImpl.this.lock.writeLock().unlock();
					return;
				}
				SynchronousWorkTaskPoolImpl.this.lock.writeLock().unlock();
				task.run();
			}
		}
	};

	public boolean isEmpty() {
		return this.taskQueue.isEmpty();
	}

	public boolean submit(Runnable work) {
		this.taskQueue.offer(work);
		boolean submit = false;
		this.lock.writeLock().lock();
		try {
			if (this.taskCompleted.get()) {
				this.taskCompleted.compareAndSet(true, false);
				submit = true;
			}
		} finally {
			this.lock.writeLock().unlock();
		}
		if (submit) {
			this.executor.execute(this.taskRunnable);
		}
		return true;
	}
}

/*
 * Location:
 * D:\Documents\workspace\GAME-SERVER-V1.0\libs\JERY-NGSP-IMPL-V1.0.jar
 * Qualified Name:
 * com.jery.ngsp.server.worktask.impl.SynchronousWorkTaskPoolImpl JD-Core
 * Version: 0.6.0
 */