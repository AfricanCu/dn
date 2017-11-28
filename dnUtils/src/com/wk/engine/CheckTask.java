package com.wk.engine;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jdeferred.Deferred;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.util.TimeTaskUtil;

/**
 * 异步处理检测任务
 * 
 * @author ems
 *
 * @param <D>
 * @param <F>
 * @param <P>
 */
public class CheckTask<D, F, P> implements Runnable {

	/** 一段时间后执行的任务 */
	public ScheduledFuture<?> submitOneTimeTask;
	/** 异步处理器 */
	public Deferred<D, F, P> defered;

	public CheckTask(Deferred<D, F, P> defered) {
		this.defered = defered;
		submitOneTimeTask = TimeTaskUtil.getTaskmanager().submitOneTimeTask(
				this, ServerConfigAbs.getHandleOverTimeInsecond(),
				TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		if (defered != null) {
			defered.reject(null);
			defered = null;
			LoggerService.getPlatformLog().warn("消息返回超时！");
		}
	}

	public void resolve(D genMessageLite) {
		if (this.defered != null) {
			this.defered.resolve(genMessageLite);
			this.submitOneTimeTask.cancel(true);
			this.defered = null;
		}
	}

}