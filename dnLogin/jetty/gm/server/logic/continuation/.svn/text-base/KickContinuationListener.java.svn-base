package gm.server.logic.continuation;

import gm.server.logic.FunctionServlet;
import gm.server.logic.config.ServerTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import msg.InnerMessage.KickBusToGsbk;

import org.eclipse.jetty.continuation.Continuation;
import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.db.IbatisDbServer;
import com.wk.enun.DistrictType;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;

/**
 * 踢人异步监听器
 */
public class KickContinuationListener extends
		ContinuationListenerExImpl<Long, KickBusToGsbk> {

	private final ServerTemplate lastServerT;
	private final FunctionServlet functionServlet;

	public KickContinuationListener(HttpServletRequest req,
			HttpServletResponse resp, JSONObject result,
			Continuation continuation, long uid, ServerTemplate lastServerT,
			FunctionServlet functionServlet) {
		super(req, resp, result, continuation, uid, KickProcessor.getInstance());
		this.lastServerT = lastServerT;
		this.functionServlet = functionServlet;
	}

	@Override
	public void bak(KickBusToGsbk res) throws Exception {
		if (res.getCode() != NTxtAbs.SUCCESS) {
			LoggerService.getLogicLog().error("返回错误！{}", res.toString());
			result.put("code", res.getCode());
			return;
		}
		UserBean muser = UserDao.queryUser(IbatisDbServer.getGmSqlMapper(),
				this.key);
		functionServlet.handle(req, lastServerT, result, muser);
	}
}