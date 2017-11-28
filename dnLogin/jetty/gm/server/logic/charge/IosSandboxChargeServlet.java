package gm.server.logic.charge;

import gm.server.BusConfig;
import gm.server.logic.continuation.ChargeProcessor;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.SystemConstantsAbs;
import com.wk.http.HttpUtilTools;

public class IosSandboxChargeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String IOS_SUCCESS = "0";
	public static final String IOS_FAIL = "1";
	public static final String bundle_id = "com.ShiKeCao.NiuNiu";
	/** 验证回执超时再试 **/
	public static final long ti = 5000;
	public static final int max = 10;
	public static final int IOS_FAIL_INT = 1;
	public static final int IOS_SUCCESS_INT = 0;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf8");
		LoggerService.getChargeLog().error("IosSandboxChargeServlet");
		ServletOutputStream outputStream = resp.getOutputStream();
		JSONObject result = new JSONObject();
		if (!BusConfig.isIosSandboxChargeNotify()) {
			return;
		}
		iosChargeNotify("https://sandbox.itunes.apple.com/verifyReceipt", req,
				resp, result);
		if (result.length() > 0 && !result.has(SystemConstantsAbs.complete)) {
			outputStream.write(result.toString().getBytes());
			result.put(SystemConstantsAbs.complete, true);
			LoggerService.getChargeLog().error("fast,充值通知结果：{}",
					result.toString());
		}
	}

	public static void iosChargeNotify(String url, HttpServletRequest req,
			HttpServletResponse resp, JSONObject result) {
		String order_id = req.getParameter("order_id");
		String apple_receipt = req.getParameter("apple_receipt");
		try {
			String ret = getRet(url, apple_receipt, 0);
			// String path = new File(IosSandboxChargeServlet.class
			// .getClassLoader().getResource("receipt").toURI()).getPath();
			// String ret = FileProcessor.readString(FileSystems.getDefault()
			// .getPath(path));
			JSONObject jsonObject = new JSONObject(ret);
			if (!jsonObject.has("status")) {
				result.put("code", IOS_FAIL);
				return;
			}
			int status = jsonObject.getInt("status");
			if (status == 0) {
				JSONObject receipt = jsonObject.optJSONObject("receipt");
				if (!BusConfig.isDebug()
						&& !receipt.optString("bundle_id").equals(bundle_id)) {
					result.put("code", IOS_FAIL);
					LoggerService.getChargeLog().error("错误的bundle_id：{}",
							bundle_id);
					return;
				}
				JSONArray in_app = receipt.optJSONArray("in_app");
				JSONObject arrOne = in_app.optJSONObject(0);
				String transaction_id = arrOne.optString("transaction_id");
				ChargeProcessor.getInstance().chargeNotify(order_id,
						transaction_id, resp, req, result, true);
				LoggerService.getChargeLog().error("result:{}", result);
			} else {
				result.put("code", IOS_FAIL);
			}
		} catch (Exception e) {
			result.put("code", IOS_FAIL);
			LoggerService.getChargeLog().error(
					String.format(
							"ios充值通知失败！order_id:%s,apple_receipt:%s,error:%s",
							order_id, apple_receipt, e.getMessage()), e);
		}
	}

	public static void main(String[] args) {
		String ret = getRet("https://sandbox.itunes.apple.com/verifyReceipt",
				"x=1", 1);
		System.err.println(ret);
	}

	private static String getRet(String url, String apple_receipt, int count) {
		try {
			if (count > max) {
				LoggerService.getChargeLog().error("验证超过:{}次,apple_receipt:{}",
						max, apple_receipt);
				return new JSONObject().put("status", 1111).toString();
			}
			return HttpUtilTools.httpsPost(url,
					Base64.decodeBase64(apple_receipt));
		} catch (SocketTimeoutException e) {
			LoggerService.getChargeLog().error(
					String.format("ios回执验证超时！apple_receipt:%s,error:%s",
							apple_receipt, e.getMessage()), e);
			try {
				Thread.sleep(ti);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return getRet(url, apple_receipt, ++count);
		} catch (Exception e) {
			LoggerService.getChargeLog().error(
					String.format("ios回执验证异常！apple_receipt:%s,error:%s",
							apple_receipt, e.getMessage()), e);
			try {
				Thread.sleep(ti);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return getRet(url, apple_receipt, ++count);
		}
	}
}
