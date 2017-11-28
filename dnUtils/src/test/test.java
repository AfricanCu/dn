package test;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.jery.ngsp.server.log.LoggerService;

public class test {

	public static void main2(String[] args) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(Long.MAX_VALUE);
		System.out.println(instance.getTime());
		System.err.println(instance.get(Calendar.YEAR));

	}

	public static void main1(String[] args) throws InterruptedException {
		LoggerService.initDef();
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
				10) {
			@Override
			protected <V> RunnableScheduledFuture<V> decorateTask(
					Callable<V> callable, RunnableScheduledFuture<V> task) {
				return super.decorateTask(callable, task);
			}

			@Override
			protected <V> RunnableScheduledFuture<V> decorateTask(
					Runnable runnable, RunnableScheduledFuture<V> task) {
				return super.decorateTask(runnable, task);
			}
		};
		scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
		scheduledThreadPoolExecutor.purge();
		TimeUnit unit = TimeUnit.SECONDS;
		Runnable command2 = new Runnable() {
			long nanoTime = System.nanoTime();

			@Override
			public void run() {
				int nextInt = ThreadLocalRandom.current().nextInt(1200);
				try {
					Thread.sleep(nextInt);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LoggerService.getLogicLog().error(
						(System.nanoTime() - nanoTime) + "ns ," + nextInt
								+ "  ");
			}
		};
		long period = 1;
		long initialDelay = 0;
		ScheduledFuture<?> scheduleAtFixedRate = scheduledThreadPoolExecutor
				.scheduleAtFixedRate(command2, initialDelay, period, unit);

	}
}
