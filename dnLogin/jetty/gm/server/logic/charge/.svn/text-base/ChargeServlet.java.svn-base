package gm.server.logic.charge;

import gm.server.logic.continuation.ChargeProcessor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.wk.bean.SystemConstantsAbs;

public class ChargeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ServletOutputStream outputStream = resp.getOutputStream();
		String orderId = req.getParameter("orderId");
		String apple_receipt = req.getParameter("apple_receipt");
		JSONObject result = new JSONObject();
		ChargeProcessor.getInstance().chargeNotify(orderId, apple_receipt,
				resp, req, result, false);
		if (result.length() > 0 && !result.has(SystemConstantsAbs.complete)) {
			outputStream.write(result.toString().getBytes("UTF-8"));
			result.put(SystemConstantsAbs.complete, true);
		}
	}
}
