package gm.server.logic.charge;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.ChargeBean;
import com.wk.bean.SystemConstantsAbs;
import com.wk.db.IbatisDbServer;
import com.wk.db.dao.ChargeDao;
import com.wk.enun.ChargeStatus;
import com.wk.enun.DistrictType;
import com.wk.template.ShopDiamondTemplate;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;

public class GenOrderIdServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ServletOutputStream outputStream = resp.getOutputStream();
		JSONObject result = new JSONObject();
		genOrderId(req, resp, result);
		outputStream.write(result.toString().getBytes());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	private void genOrderId(HttpServletRequest req, HttpServletResponse resp,
			JSONObject result) {
		int pay_id = Integer.parseInt(req.getParameter("pay_id"));
		long uid = Long.parseLong(req.getParameter("uid"));
		int agency = StringUtils.isBlank(req.getParameter("agency")) ? SystemConstantsAbs.NoAgency
				: Integer.parseInt(req.getParameter("agency"));
		try {
			ShopDiamondTemplate shopDiamondTemplate = ShopDiamondTemplate
					.getShopDiamondTemplate(pay_id);
			if (shopDiamondTemplate == null) {
				LoggerService.getChargeLog().error("生成订单号,找不到商品!pay_id：{}",
						pay_id);
				result.put("code", IosSandboxChargeServlet.IOS_FAIL_INT);
				return;
			}
			UserBean queryUser = UserDao.queryUser(
					IbatisDbServer.getGmSqlMapper(), uid);
			if (queryUser == null) {
				LoggerService.getChargeLog().error("生成订单号,找不到用户!uid:{}", uid);
				result.put("code", IosSandboxChargeServlet.IOS_FAIL_INT);
				return;
			}
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND);
			int milis = cal.get(Calendar.MILLISECOND);
			String order_id = String.format("%s%s%s%s%s%s%s%s", pay_id, year,
					month < 10 ? "0" + month : month, day < 10 ? "0" + day
							: day, hour < 10 ? "0" + hour : hour,
					minute < 10 ? "0" + minute : minute, second < 10 ? "0"
							+ second : second, milis < 10 ? "00" + milis
							: (milis < 100 ? "0" + milis : milis));
			ChargeBean charge = new ChargeBean(order_id, uid,
					shopDiamondTemplate.getDistrictType(),
					agency != SystemConstantsAbs.NoAgency ? agency : queryUser
							.getMyAgency(),
					shopDiamondTemplate.getTotalDiamond(), "", "",
					ChargeStatus.unHandle, new Date(), new Timestamp(
							System.currentTimeMillis()));
			ChargeDao.insert(IbatisDbServer.getGmSqlMapper(), charge);
			result.put("code", IosSandboxChargeServlet.IOS_SUCCESS_INT).put(
					"order_id", order_id);// 奇葩处理一下，，code相反
		} catch (Exception e) {
			result.put("code", IosSandboxChargeServlet.IOS_FAIL_INT);
			LoggerService.getChargeLog().error(e.getMessage(), e);
		}
	}
}
