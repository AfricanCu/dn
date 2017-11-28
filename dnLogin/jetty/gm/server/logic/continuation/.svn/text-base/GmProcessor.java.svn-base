package gm.server.logic.continuation;

import gm.server.logic.config.ServerTemplate;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import msg.InnerMessage.GmBusToGs;
import msg.InnerMessage.GmBusToGsbk;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.db.IbatisDbServer;
import com.wk.engine.net.InnerMsgId;
import com.wk.enun.GmType;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;

public class GmProcessor extends
		ContinuationProcessor<String, GmBusToGsbk, GmContinuationListener> {

	private static final GmProcessor instance = new GmProcessor();

	public static GmProcessor getInstance() {
		return instance;
	}

	/** 请求id生成器 */
	private final AtomicInteger gen = new AtomicInteger();

	/**
	 * gm通知
	 * 
	 * @param resp
	 * @param req
	 * @param result
	 */
	public void gmNotify(HttpServletResponse resp, HttpServletRequest req,
			JSONObject result) {
		int gmType = Integer.parseInt(req.getParameter("gmType"));
		String serverIdStr = req.getParameter("serverId");
		int serverId = serverIdStr == null ? -1 : Integer.parseInt(serverIdStr);
		String uidStr = req.getParameter("uid");
		long uid = uidStr == null ? -1 : Long.parseLong(uidStr);
		String scriptJsonStr = req.getParameter("script");
		result.put("gmType", gmType);
		userReq(GmType.getEnum(gmType), serverId, uid, scriptJsonStr, resp,
				req, result);
	}

	/**
	 * 加入请求队列
	 * 
	 * @param nickname
	 * 
	 * @param uid
	 * @param resp
	 * @param req
	 * @param result
	 * @param scriptJsonStr
	 */
	public void userReq(GmType gmType, int serverId, long uid,
			String scriptJsonStr, HttpServletResponse resp,
			HttpServletRequest req, JSONObject result) {
		try {
			if (gmType == null) {
				result.put("code", NTxtAbs.INFO_ERROR);
				LoggerService.getChargeLog().error("gm类型错误！");
				return;
			}
			ServerTemplate goodServerTemplate = null;
			if (uid != -1) {
				UserBean queryUser = UserDao.queryUser(
						IbatisDbServer.getGmSqlMapper(), uid);
				if (queryUser == null) {
					result.put("code", NTxtAbs.USER_DATA_EMPTY);
					return;
				}
				goodServerTemplate = ServerTemplate
						.getCanLoginServerTemplate(queryUser.getServerId());
			} else if (serverId != -1) {
				goodServerTemplate = ServerTemplate.getServerTemplate(serverId);
			} else {
				result.put("code", NTxtAbs.INFO_ERROR);
				LoggerService.getChargeLog().error("信息填写不足！");
				return;
			}
			if (goodServerTemplate == null) {
				result.put("code", NTxtAbs.SERVER_ID_NOT_FOUND);
				LoggerService.getChargeLog().error("找不到合适服务器！");
				return;
			}
			Continuation continuation = ContinuationSupport
					.getContinuation(req);
			if (!continuation.isInitial()) {
				result.put("code", NTxtAbs.CONTINUATION_NOT_INITIAL);
				LoggerService.getChargeLog().error("continuation还未初始化完成！");
				return;
			}
			String reqId = String.format("req%s", gen.incrementAndGet());
			GmContinuationListener value = new GmContinuationListener(req,
					resp, result, continuation, reqId);
			this.put(reqId, value);
			goodServerTemplate.sendMessage(InnerMsgId.GmBusToGs, GmBusToGs
					.newBuilder().setReqId(reqId).setGmType(gmType.getType())
					.setUid(uid).setJson(scriptJsonStr));
		} catch (SQLException e) {
			result.put("code", NTxtAbs.SQL_EXCEPTION);
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return;
		}
	}

	public void gmbk(GmBusToGsbk genMessageLite) {
		this.remove(genMessageLite.getReqId(), genMessageLite);
		LoggerService.getLogicLog().error("gm返回：{}", genMessageLite.toString());
	}
}
