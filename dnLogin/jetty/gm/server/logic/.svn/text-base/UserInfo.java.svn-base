package gm.server.logic;

import gm.server.BusConfig;
import gm.server.util.DownloadImage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.enun.PlatformType;
import com.wk.http.HttpUtilTools;

/**
 * 获取微信用户信息
 * 
 * @author ems
 *
 */
public class UserInfo {
	/*** 通过code获取access_token的接口。 **/
	private static final String access_tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private static final String access_tokenParams = "appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	/****
	 * <pre>
	 * 此接口用于获取用户个人信息。开发者可通过OpenID来获取用户基本信息。特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众帐号，可通过获取用户基本信息中的unionid来区分用户的唯一性，
	 * 因为只要是同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，用户的unionid是唯一的。换句话说，同一用户，对同一个微信开放平台下的不同应用，unionid是相同的。
	 * 请注意，在用户修改微信头像后，旧的微信头像URL将会失效，因此开发者应该自己在获取用户信息后，将头像图片保存下来，避免微信头像URL失效后的异常情况。
	 * </pre>
	 */
	private static final String userinfoUrl = "https://api.weixin.qq.com/sns/userinfo";
	private static final String userinfoParams = "access_token=%s&openid=%s";
	/** 微信昵称过滤特殊字符正则表达式 **/
	private static final String specialCharRegex = "[\ud800\udc00-\udbff\udfff\ud800-\udfff]";
	/** 如果头像为空，给个默认头像 */
	private static final String defaltHeadImg = "gm/server/util/aaa.png";
	private static final URL defaltHeadImgUrl = UserInfo.class.getClassLoader()
			.getResource(defaltHeadImg);
	/** 昵称最大字符长度 */
	private static final int nicknameMaxLength = 80;

	private int code = NTxtAbs.SUCCESS;
	/** 接口调用凭证 */
	private String access_token;
	/** access_token接口调用凭证超时时间，单位（秒） */
	private int expires_in;
	/** 用户刷新access_token */
	private String refresh_token;
	/** 普通用户昵称 */
	private String nickname;
	/** 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。 */
	private String unionid;
	/** 普通用户性别，1为男性，2为女性 */
	private int sex;
	/** 普通用户个人资料填写的省份 */
	// private String province;
	/** 普通用户个人资料填写的城市 */
	// private String city;
	/** 国家，如中国为CN */
	// private String country;
	/** 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空 */
	private String headimgurl;

	/** 用户特权信息，json数组，如微信沃卡用户为（chinaunicom） */
	// private String privilege;

	/**
	 * 
	 * @param code
	 * @param enm
	 * @throws Exception
	 */
	public UserInfo(String code, PlatformType enm) throws Exception {
		if (enm == PlatformType.pc && BusConfig.isDebug()) {
			this.access_token = "";
			this.expires_in = Integer.MAX_VALUE;
			this.refresh_token = "";
			this.nickname = code;
			this.unionid = code;
			this.sex = 1;
			return;
		}
		if (enm == PlatformType.ios_visitor) {
			this.access_token = "";
			this.expires_in = Integer.MAX_VALUE;
			this.refresh_token = "";
			this.nickname = "";
			this.unionid = code;
			this.sex = 1;
			return;
		}
		if (enm == PlatformType.wx) {
			long currentTimeMillis = System.currentTimeMillis();
			JSONObject access_tokenJson = HttpUtilTools.httpsGetJson(
					access_tokenUrl,
					String.format(access_tokenParams, BusConfig.AppID,
							BusConfig.AppSecret, code).getBytes(), true);
			LoggerService.getLogicLog().error("access_token:{}",
					System.currentTimeMillis() - currentTimeMillis);
			if (access_tokenJson == null) {
				this.code = NTxtAbs.ACCESS_TOKEN_EMPTY;
				return;
			}
			this.access_token = access_tokenJson
					.optString("access_token", null);
			if (access_token == null) {
				LoggerService.getLogicLog().error(access_tokenJson.toString());
				this.code = NTxtAbs.access_token_Empty;
				return;
			}
			this.expires_in = access_tokenJson.optInt("expires_in");
			this.refresh_token = access_tokenJson.optString("refresh_token");
			// 普通用户的标识，对当前开发者帐号唯一
			String openid = access_tokenJson.optString("openid");
			// 用户授权的作用域，使用逗号（,）分隔
			// String scope = access_tokenJson.optString("scope");
			currentTimeMillis = System.currentTimeMillis();
			JSONObject userinfoJson = HttpUtilTools.httpsGetJson(userinfoUrl,
					String.format(userinfoParams, access_token, openid)
							.getBytes(), true);
			LoggerService.getLogicLog().error("userinfo:{}",
					System.currentTimeMillis() - currentTimeMillis);
			if (userinfoJson == null) {
				this.code = NTxtAbs.USER_INFO_EMPTY;
				return;
			}
			openid = userinfoJson.optString("openid", null);
			if (openid == null) {
				LoggerService.getLogicLog().error(userinfoJson.toString());
				this.code = NTxtAbs.openid_EMPTY;
				return;
			}
			this.nickname = userinfoJson.optString("nickname").replaceAll(
					specialCharRegex, "*");
			if (this.nickname.length() > nicknameMaxLength) {
				this.nickname = this.nickname.substring(0, nicknameMaxLength);
			}
			this.sex = userinfoJson.optInt("sex");
			// this.province = userinfoJson.optString("province");
			// this.city = userinfoJson.optString("city");
			// this.country = userinfoJson.optString("country");
			headimgurl = userinfoJson.optString("headimgurl");
			if (headimgurl != null && headimgurl.indexOf('/') != -1) {
				headimgurl = String.format("%s/64",
						headimgurl.substring(0, headimgurl.lastIndexOf('/')));
			}
			// this.privilege = userinfoJson.optString("privilege");
			this.unionid = userinfoJson.optString("unionid");
			return;
		}
		this.code = NTxtAbs.INFO_ERROR;
		return;
	}

	public static void main(String[] args) throws MalformedURLException {
		String xx = "💝💝💝💝";
		xx = xx.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "*");
		System.err.println(xx);
		// System.err.println(UserInfo.class.getClassLoader().getResource(
		// "gm/server/util/aaa.png"));
		// String headimgurl =
		// "http://wx.qlogo.cn/mmopen/UDa9R1yl9Ub9dzccxJlg3AVhyY3bT6LFE5L8CCejCC2icUPgIO5o56C3V6mtKe2U3JnG2TiavvpswxD2QEFCy8JfGDk6Gkd7r2/64";
		// System.err.println(headimgurl.substring(0,
		// headimgurl.lastIndexOf('/'))
		// + "/64");
		// System.err.println(new URL(UserInfo.class.getClassLoader()
		// .getResource("gm/server/util/aaa.png").toString()));
	}

	/**
	 * 下载图片缓存到本地
	 * 
	 * @param uid
	 * @param headImgTag
	 * @throws Exception
	 */
	public void downloadImage(long uid) throws Exception {
		DownloadImage.download(getHeadimgUrl(), uid + "", String.format(
				".%sWebRoot%s%s", File.separator, File.separator,
				ServerConfigAbs.getWechatheaderdir(uid)));
	}

	public String getHead() {
		return headimgurl;
	}

	/**
	 * 获取头像URL
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public URL getHeadimgUrl() throws MalformedURLException {
		if (headimgurl == null || headimgurl.indexOf('.') == -1) {
			return defaltHeadImgUrl;
		} else {
			return new URL(headimgurl);
		}
	}

	public int getSex() {
		return sex;
	}

	public int getCode() {
		return code;
	}

	public String getAccess_token() {
		return access_token;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public String getNickname() {
		return nickname;
	}

	public String getUnionid() {
		return unionid;
	}
}
