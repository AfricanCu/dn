package gm.server.logic.continuation;

import gm.server.logic.charge.IosSandboxChargeServlet;
import gm.server.logic.config.ServerTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import msg.InnerMessage.ChargeBusToGs;
import msg.InnerMessage.ChargeBusToGsbk;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.ChargeBean;
import com.wk.bean.NTxtAbs;
import com.wk.db.IbatisDbServer;
import com.wk.db.dao.ChargeDao;
import com.wk.engine.net.InnerMsgId;
import com.wk.enun.ChargeStatus;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;

/**
 * 充值处理器
 * 
 * @author ems
 *
 */
public class ChargeProcessor
		extends
		ContinuationProcessor<String, ChargeBusToGsbk, ChargeContinuationListener> {

	private static final ChargeProcessor instance = new ChargeProcessor();

	public static ChargeProcessor getInstance() {
		return instance;
	}

	/**
	 * 充值通知
	 * 
	 * @param orderId
	 * @param apple_receipt
	 *            回执
	 * @param resp
	 * @param req
	 * @param result
	 */
	public void chargeNotify(String orderId, String apple_receipt,
			HttpServletResponse resp, HttpServletRequest req,
			JSONObject result, boolean isIOS) {
		try {
			if (isIOS) {
				ChargeBean queryApple_receipt = ChargeDao.queryApple_receipt(
						IbatisDbServer.getGmSqlMapper(), apple_receipt);
				if (queryApple_receipt != null) {
					if (isIOS) {
						result.put("code", IosSandboxChargeServlet.IOS_FAIL);
					} else {
						result.put("code", NTxtAbs.FAIL);
					}
					LoggerService.getChargeLog().error(
							"apple_receipt:{},已经使用过的回执单！", orderId);
					return;
				}
			}
			int updateStatus = 0;
			ChargeBean queryCharge = null;
			IbatisDbServer.getGmSqlMapper().startTransaction();
			try {
				updateStatus = ChargeDao
						.updateStatus(IbatisDbServer.getGmSqlMapper(),
								apple_receipt, orderId);
				queryCharge = ChargeDao.queryCharge(
						IbatisDbServer.getGmSqlMapper(), orderId);
				IbatisDbServer.getGmSqlMapper().commitTransaction();
			} finally {
				IbatisDbServer.getGmSqlMapper().endTransaction();
			}
			if (queryCharge == null) {
				if (isIOS) {
					result.put("code", IosSandboxChargeServlet.IOS_FAIL);
				} else {
					result.put("code", NTxtAbs.FAIL);
				}
				LoggerService.getChargeLog().error("orderId:{},找不到充值记录！",
						orderId);
				return;
			}
			if (updateStatus != 1) {
				if (isIOS) {
					result.put("code", IosSandboxChargeServlet.IOS_FAIL);
				} else {
					result.put("code", NTxtAbs.FAIL);
				}
				LoggerService.getChargeLog().error("orderId:{},无法更新订单状态！",
						orderId);
				return;
			}
			if (queryCharge.getChargeStatus() != ChargeStatus.handled) {
				if (isIOS) {
					result.put("code", IosSandboxChargeServlet.IOS_FAIL);
				} else {
					result.put("code", NTxtAbs.FAIL);
				}
				LoggerService.getChargeLog().error("orderId:{},订单状态错误！",
						orderId);
				return;
			}
			UserBean queryUser = UserDao.queryUser(
					IbatisDbServer.getGmSqlMapper(), queryCharge.getUsername());
			if (queryUser == null) {
				if (isIOS) {
					result.put("code", IosSandboxChargeServlet.IOS_FAIL);
				} else {
					result.put("code", NTxtAbs.FAIL);
				}
				LoggerService.getChargeLog().error("orderId:{},找不到玩家名！",
						orderId, queryCharge.getUsername());
				return;
			}
			if (!queryUser.isOnline()) {
				int oldDiamond = queryUser.getDiamond();
				queryUser.changeDiamond(queryCharge.getDiamond());
				queryUser.save();
				if (isIOS) {
					result.put("code", IosSandboxChargeServlet.IOS_SUCCESS);
				} else {
					result.put("code", NTxtAbs.SUCCESS);
				}
				LoggerService.getChargeLog().error(
						"orderId:{}, oldDiamond:{}, diamond:{},订单处理成功！",
						new Object[] { orderId, oldDiamond,
								queryUser.getDiamond() });
				return;
			}
			ServerTemplate goodServerTemplate = ServerTemplate
					.getCanLoginServerTemplate(queryUser.getServerId());
			if (goodServerTemplate == null) {
				if (isIOS) {
					result.put("code", IosSandboxChargeServlet.IOS_FAIL);
				} else {
					result.put("code", NTxtAbs.FAIL);
				}
				LoggerService.getChargeLog().error(
						"orderId:{},无法连接上gs进行充值加钻石,订单处理失败！", orderId);
				return;
			}
			Continuation continuation = ContinuationSupport
					.getContinuation(req);
			if (!continuation.isInitial()) {
				if (isIOS) {
					result.put("code", IosSandboxChargeServlet.IOS_FAIL);
				} else {
					result.put("code", NTxtAbs.FAIL);
				}
				LoggerService.getChargeLog().error(
						"orderId:{},continuation还未初始化完成！", orderId);
				return;
			}
			ChargeContinuationListener listener = new ChargeContinuationListener(
					req, resp, result, continuation, orderId, isIOS);
			int code = this.putCharge(goodServerTemplate, orderId, listener);
			if (code != NTxtAbs.SUCCESS) {
				if (isIOS) {
					result.put("code", IosSandboxChargeServlet.IOS_FAIL);
				} else {
					result.put("code", NTxtAbs.FAIL);
				}
				LoggerService.getChargeLog().error(
						"orderId:{},code:{} putCharge 失败！", orderId, code);
				return;
			}
			goodServerTemplate.sendMessage(
					InnerMsgId.ChargeBusToGs,
					ChargeBusToGs.newBuilder().setOrderId(orderId)
							.setUid(queryUser.getUid())
							.setDistrict(queryCharge.getDistrict())
							.setDiamond(queryCharge.getDiamond()));
		} catch (Exception e) {
			if (isIOS) {
				result.put("code", IosSandboxChargeServlet.IOS_FAIL);
			} else {
				result.put("code", NTxtAbs.FAIL);
			}
			LoggerService.getChargeLog().error(
					String.format("orderId:%s,error:%s", orderId,
							e.getMessage()), e);
		}
	}

	/**
	 * 放入充值异步监听器
	 * 
	 * @param lastServerT
	 * @param orderId
	 * @param listener
	 * @return
	 */
	public int putCharge(ServerTemplate lastServerT, String orderId,
			ChargeContinuationListener listener) {
		if (this.contains(orderId)) {
			return NTxtAbs.CHARGE_PUT_CONTAINS;
		}
		if (lastServerT.checkClient() != null) {
			this.put(orderId, listener);
			return NTxtAbs.SUCCESS;
		}
		return NTxtAbs.CHARGE_NOT_CONNECTED;
	}

	public void chargebk(ChargeBusToGsbk genMessageLite) {
		String orderId = genMessageLite.getOrderId();
		int code = genMessageLite.getCode();
		this.remove(orderId, genMessageLite);
		LoggerService.getChargeLog().warn("orderId:{},code:{},服务器充值返回！",
				orderId, code);
	}

}
