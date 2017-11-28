import gm.server.BusConfig;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.FileSystems;

import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.enun.BusFuncType;
import com.wk.enun.GmType;
import com.wk.http.HttpUtilTools;
import com.wk.util.FileProcessor;

public class test {
	public static void main(String[] args) throws Exception {
		// System.err.println(UUID.randomUUID());
		String path = new File(test.class.getClassLoader().getResource("aa")
				.toURI()).getPath();
		String readString = FileProcessor.readString(FileSystems.getDefault()
				.getPath(path));
		// System.err.println(readString);
		// System.err.println(URLEncoder.encode(readString, "utf-8"));
		// System.err.println(URLDecoder.decode(readString, "utf-8").equals(
		// readString));
		// System.err.println(URLDecoder.decode(
		// URLDecoder.decode(readString, "utf-8"), "utf-8").equals(
		// readString));
		// System.err.println(Base64.decodeBase64(readString).length);
		// System.err.println(readString.length());
		// System.err.println(new String(Base64.decodeBase64(readString)));
		// String httpsPost = HttpUtilTools.httpsPost(
		// "https://sandbox.itunes.apple.com/verifyReceipt",

		// byte[] xx = Base64.decodeBase64(readString);
		// String httpsPost = HttpUtilTools.httpsPost(
		// "https://sandbox.itunes.apple.com/verifyReceipt", xx);
		// System.out.println(httpsPost);

		String ss =
		// "http://pay2.ylwqgame.com/xygpay/appstore/ldyh_notify1.php";
		// "http://192.168.0.42:8115/Login/iosSandboxChargeServlet";
		// "http://192.168.0.42:8118/Login/functionServlet";
		"http://120.78.174.39:8118/Login/functionServlet";
		String xx = ("uid=" + 265 + "&type=" + BusFuncType.gm.getName()
				+ "&gmType=" + GmType.chargeAddDiamond.getType() + "&script=" + URLEncoder
				.encode(new JSONObject().put("diamond", 1).toString(), "utf8"));
System.err.println(ss+"?"+xx);
		String post3 = HttpUtilTools.post(ss, xx);

		System.err.println(post3);

	}

	public static void main1(String[] args) throws Exception {
		// BusConfig.init();

		JSONObject json = null;
		for (int i = 0; i < 2; i++) {
			long currentTimeMillis = System.currentTimeMillis();
			try {
				json = HttpUtilTools
						.httpsGetJson(
								"https://api.weixin.qq.com/sns/oauth2/access_token",
								String.format(
										"appid=%s&secret=%s&code=%s&grant_type=authorization_code",
										BusConfig.AppID, BusConfig.AppSecret,
										"0112qiA518QoOO124VC51w7yA512qiAG")
										.getBytes(), true);

				// json = HttpUtilTools
				// .getJson(
				// String.format(
				// "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
				// BusConfig.AppID, BusConfig.AppSecret,
				// "fasewxzcvdd"), null, true);
				//
				// LoggerService.getLogicLog().error("{},{}", json,
				// System.currentTimeMillis() - currentTimeMillis);
			} finally {
				LoggerService.getLogicLog().error("{},{}", json,
						System.currentTimeMillis() - currentTimeMillis);
			}
		}
	}

}
