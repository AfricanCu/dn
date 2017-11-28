package gm.server.logic.continuation;

import gm.server.logic.charge.IosSandboxChargeServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import msg.InnerMessage.ChargeBusToGsbk;

import org.eclipse.jetty.continuation.Continuation;
import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;

/**
 * 充值异步监听器
 */
public class ChargeContinuationListener extends
		ContinuationListenerExImpl<String, ChargeBusToGsbk> {

	private final boolean isIOS;

	public ChargeContinuationListener(HttpServletRequest req,
			HttpServletResponse resp, JSONObject result,
			Continuation continuation, String orderId, boolean isIOS) {
		super(req, resp, result, continuation, orderId, ChargeProcessor
				.getInstance());
		this.isIOS = isIOS;
	}

	@Override
	public void bak(ChargeBusToGsbk genMessageLite) throws Exception {
		if (genMessageLite.getCode() != NTxtAbs.SUCCESS) {
			if (this.isIOS) {
				result.put("code", IosSandboxChargeServlet.IOS_FAIL);
			} else
				result.put("code", NTxtAbs.FAIL);
			LoggerService.getLogicLog().error("orderId:{} 返回错误！{}", this.key,
					genMessageLite.getCode());
		} else {
			if (this.isIOS) {
				result.put("code", IosSandboxChargeServlet.IOS_SUCCESS);
			} else {
				result.put("code", NTxtAbs.SUCCESS);
			}
			LoggerService.getLogicLog().error("orderId:{} 返回成功！{}", this.key,
					genMessageLite.getCode());
		}
	}
}