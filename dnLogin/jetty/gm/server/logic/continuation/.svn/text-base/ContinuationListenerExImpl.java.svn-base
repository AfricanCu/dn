package gm.server.logic.continuation;

import gm.server.BusConfig;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationListener;
import org.json.JSONObject;

import com.google.protobuf.MessageLite;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.bean.SystemConstantsAbs;

/**
 * 异步监听器
 * 
 * @author ems
 * 
 * @param <K>
 *            id类型
 * @param <V>
 *            异步监听器类型
 * @param <BAK>
 *            返回消息类型
 */
public abstract class ContinuationListenerExImpl<K, BAK extends MessageLite>
		implements ContinuationListener {
	protected final Continuation continuation;
	protected final HttpServletResponse resp;
	protected final HttpServletRequest req;
	protected final JSONObject result;
	protected final K key;
	protected final ContinuationProcessor<K, BAK, ?> processor;

	/**
	 * 
	 * @param req
	 * @param resp
	 * @param result
	 * @param continuation
	 * @param key
	 * @param processor
	 */
	public ContinuationListenerExImpl(HttpServletRequest req,
			HttpServletResponse resp, JSONObject result,
			Continuation continuation, K key,
			ContinuationProcessor<K, BAK, ?> processor) {
		this.continuation = continuation;
		this.resp = resp;
		this.req = req;
		this.result = result;
		this.key = key;
		this.processor = processor;
	}

	/**
	 * 执行HttpServletResponse的异步等待操作
	 */
	public void suspend() {
		continuation.addContinuationListener(this);
		continuation.setTimeout(BusConfig.asyncWaitTimeoutInMs);
		continuation.suspend(resp);
	}

	/**
	 * 消息返回处理
	 * 
	 * @param res
	 */
	public void onDone(BAK res) {
		try {
			try {
				bak(res);
			} catch (Exception e) {
				LoggerService.getLogicLog().error(e.getMessage(), e);
				result.put("code", NTxtAbs.EXCEPTION);
			}
		} finally {
			if (continuation != null && !continuation.isInitial()) {
				continuation.complete();
			}
		}
	}

	/**
	 * 消息返回处理
	 * 
	 * @param genMessageLite
	 */
	public abstract void bak(BAK genMessageLite) throws Exception;

	@Override
	public void onTimeout(Continuation continuation) {// 超时处理
		try {
			ContinuationListenerExImpl<K, BAK> remove = processor.remove(
					this.key, null);
			if (remove != null) {
				if (!this.result.has("code")) {
					this.result.put("code", NTxtAbs.CONTINUATION_TIMEOUT);
				} else {
					LoggerService.getLogicLog().error("已经包含了code!");
				}
			}
			LoggerService.getLogicLog().error("超时处理{}", key);
		} finally {
			if (continuation != null && !continuation.isInitial()) {
				continuation.complete();
			}
		}
	}

	@Override
	public void onComplete(Continuation continuation) {
		try {
			if (result.length() > 0 && !result.has(SystemConstantsAbs.complete)) {
				resp.getOutputStream().write(this.result.toString().getBytes());
				this.result.put(SystemConstantsAbs.complete, true);
				LoggerService.getLogicLog().error("slow,完成:{}", key);
			} else {
				LoggerService.getLogicLog().error("重复完成或者result空:{},result:{}",
						key, result);
			}
		} catch (IOException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
		}
	}
}