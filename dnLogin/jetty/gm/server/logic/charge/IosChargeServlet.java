package gm.server.logic.charge;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.SystemConstantsAbs;

public class IosChargeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf8");
		ServletOutputStream outputStream = resp.getOutputStream();
		JSONObject result = new JSONObject();
		IosSandboxChargeServlet
				.iosChargeNotify("https://buy.itunes.apple.com/verifyReceipt",
						req, resp, result);
		if (result.length() > 0 && !result.has(SystemConstantsAbs.complete)) {
			outputStream.write(result.toString().getBytes());
			result.put(SystemConstantsAbs.complete, true);
			LoggerService.getChargeLog().error("充值通知结果：{}", result.toString());
		}
	}
}
