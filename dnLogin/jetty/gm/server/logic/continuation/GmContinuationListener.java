package gm.server.logic.continuation;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import msg.InnerMessage.GmBusToGsbk;

import org.eclipse.jetty.continuation.Continuation;
import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.bean.SystemConstantsAbs;

/**
 * 充值异步监听器
 */
public class GmContinuationListener extends
		ContinuationListenerExImpl<String, GmBusToGsbk> {

	public GmContinuationListener(HttpServletRequest req,
			HttpServletResponse resp, JSONObject result,
			Continuation continuation, String reqId) {
		super(req, resp, result, continuation, reqId, GmProcessor.getInstance());
	}

	@Override
	public void bak(GmBusToGsbk genMessageLite) throws Exception {
		if (genMessageLite.getCode() != NTxtAbs.SUCCESS) {
			result.put("code", genMessageLite.getCode());
			LoggerService.getLogicLog().error("gm:{} 返回错误！{}", this.key,
					genMessageLite.getCode());
		} else {
			result.put("code", NTxtAbs.SUCCESS);
			LoggerService.getLogicLog().error("gm:{} 返回成功！{}", this.key,
					genMessageLite.getCode());
		}
	}

	@Override
	public void onComplete(Continuation continuation) {
		try {
			if (result.has("code") && !result.has(SystemConstantsAbs.complete)) {
				resp.getOutputStream().write(
						("jsonpCallback('"
								+ result.toString().replace("'", "\\'") + "')")
								.getBytes("UTF-8"));
				result.put(SystemConstantsAbs.complete, true);
			} else {
				LoggerService.getLogicLog().error(
						"重复完成或者result 没有code:{},result:{}", key, result);
			}
		} catch (IOException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
		}
	}
}