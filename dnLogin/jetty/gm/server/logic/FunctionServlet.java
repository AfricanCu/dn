package gm.server.logic;

import gm.server.BusConfig;
import gm.server.logic.config.ServerTemplate;
import gm.server.logic.config.StaticDataManager;
import gm.server.logic.continuation.GmProcessor;
import gm.server.logic.continuation.KickContinuationListener;
import gm.server.logic.continuation.KickProcessor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import msg.InnerMessage.KickBusToGs;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.json.JSONObject;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.bean.SystemConstantsAbs;
import com.wk.db.IbatisDbServer;
import com.wk.db.dao.RoomDao;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.secur.SecureUtil;
import com.wk.enun.BusFuncType;
import com.wk.enun.DistrictType;
import com.wk.enun.PlatformType;
import com.wk.template.DistrictTemplate;
import com.wk.user.bean.UserBean;
import com.wk.user.dao.UserDao;
import com.wk.user.enm.ExpiresInType;
import com.wk.util.LogUtil;

/**
 * 功能servlet
 * 
 * @author lixing
 */
public class FunctionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/** 设备ID限定长度 **/
	private static final int DEVICE_ID_MAX_LENGTH = 250;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String req_type = req.getParameter("type");
		ServletOutputStream outputStream = resp.getOutputStream();
		JSONObject result = new JSONObject();
		BusFuncType enm = BusFuncType.getEnum(req_type);
		if (enm == null) {
			outputStream.write(result.put("code", NTxtAbs.EMPTY).toString()
					.getBytes("UTF-8"));
			LoggerService.getLogicLog().error("找不到请求类型，未实现！{}", req_type);
			return;
		}
		switch (enm) {
		case login:
			login(req, resp, result);
			break;
		case reload:
			try {
				StaticDataManager.init(BusConfig.csvDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case getAllServerInfo:
			ServerTemplate.getAllServerInfo(req, resp, result);
			outputStream.write(String.format("serverListCallback('%s')",
					result.toString().replace("'", "\\'")).getBytes("UTF-8"));
			return;
		case gm:
			GmProcessor.getInstance().gmNotify(resp, req, result);
			if (result.has("code") && !result.has(SystemConstantsAbs.complete)) {
				outputStream.write(String.format("jsonpCallback('%s')",
						result.toString().replace("'", "\\'"))
						.getBytes("UTF-8"));
				result.put(SystemConstantsAbs.complete, true);
			}
			return;
		default:
			break;
		}
		if (result.length() > 0 && !result.has(SystemConstantsAbs.complete)) {
			outputStream.write(result.toString().getBytes("UTF-8"));
			result.put(SystemConstantsAbs.complete, true);
		}
	}

	/**
	 * 目标服务器
	 * 
	 * @param result
	 *            组装目标服务器信息
	 * @param user
	 * @param serverTemplate
	 * @param districtType
	 * @throws SQLException
	 */
	private void target(JSONObject result, UserBean user,
			ServerTemplate serverTemplate) throws SQLException {
		user.setServerId(serverTemplate.getServerId());
		user.setSessionCode(SecureUtil.genSessionCode(user));
		user.save();
		result.put("sessionCode", user.getSessionCode());
		result.put("loginTime", SecureUtil.genLoginTime());
		result.put("serverIp", serverTemplate.getHost());
		result.put("port", serverTemplate.getPort());
		LoggerService.getLogicLog().warn("uid:{},SessionCode:{}",
				user.getUid(), user.getSessionCode());
	}

	/**
	 * 登录
	 * 
	 * 获取服务器列表
	 * 
	 * @param req
	 * @param resp
	 * @param result
	 */
	public void login(HttpServletRequest req, HttpServletResponse resp,
			JSONObject result) {
		try {
			String code = req.getParameter("code");
			String sessionCode = req.getParameter("sessionCode");
			int platId = Integer.parseInt(req.getParameter("platformId"));
			PlatformType enm = PlatformType.getEnum(platId);
			if (enm == PlatformType.android || enm == PlatformType.ios) {
				enm = PlatformType.wx;
			}
			String deviceId = req.getParameter("deviceId");
			if (enm == null) {
				result.put("code", NTxtAbs.PLATFORM_TYPE_EMPTY);
				return;
			}
			if (enm == PlatformType.pc && !BusConfig.isDebug()) {
				result.put("code", NTxtAbs.PLATFORM_TYPE_EMPTY);
				return;
			}
			if (deviceId == null) {
				result.put("code", NTxtAbs.DEVICE_ID_EMPTY);
				return;
			}
			if (deviceId.length() > DEVICE_ID_MAX_LENGTH) {
				result.put("code", NTxtAbs.DEVICE_ID_TOO_LONG);
				return;
			}
			UserBean user;
			if (!StringUtils.isBlank(sessionCode) && StringUtils.isBlank(code)) {// 走session
				JSONObject json = SecureUtil.explainSessionCode(sessionCode);
				long uid = json.optLong("uid");
				long accessTime = json.optLong("accessTime");
				long refreshTime = json.optLong("refreshTime");
				if (System.currentTimeMillis() - refreshTime > SystemConstantsAbs.days_29_timeInMillis) {
					result.put("code", NTxtAbs.INVALID);
					return;
				}
				user = UserDao.queryUser(IbatisDbServer.getGmSqlMapper(), uid);
				if (user == null) {
					result.put("code", NTxtAbs.USER_DATA_EMPTY);
					return;
				}
				if (!user.getServerListSessionCode().equals(sessionCode)) {
					LoggerService.getLogicLog().error(
							"serverListSessionCode:{}!={}",
							user.getServerListSessionCode(), sessionCode);
					result.put("code", NTxtAbs.LOGIN_SESSION_CODE_ERROR);
					return;
				}
			} else {
				if (StringUtils.isBlank(code)) {
					result.put("code", NTxtAbs.CODE_EMPTY);
					return;
				}
				UserInfo userInfo;
				try {
					long currentTimeMillis = System.currentTimeMillis();
					userInfo = new UserInfo(code, enm);
					LoggerService.getLogicLog().error("耗时：{}",
							System.currentTimeMillis() - currentTimeMillis);
				} catch (Exception e) {
					result.put("code", NTxtAbs.SQL_EXCEPTION);
					LoggerService.getLogicLog().error(e.getMessage(), e);
					return;
				}
				if (userInfo.getCode() != NTxtAbs.SUCCESS) {
					result.put("code", userInfo.getCode());
					return;
				}
				String puid = userInfo.getUnionid();
				user = UserDao.queryUser(puid, enm);
				if (user == null) {
					/** 新用户 **/
					user = new UserBean();
					user.setNickname(userInfo.getNickname().equals("") ? user
							.getUid() + "" : userInfo.getNickname());
					user.setPuid(puid);
					user.setSex(userInfo.getSex());
					user.setRegisterTime(new Timestamp(System
							.currentTimeMillis()));
					user.setDeviceId(deviceId);
					user.setPlatId(enm.getType());
					user.setDiamond(BusConfig.getInitDiamond());
					user.setHead(userInfo.getHead());
					user.insert();
					userInfo.downloadImage(user.getUid());
				} else {
					if (!user.getNickname().equals(userInfo.getNickname())) {
						user.setNickname(userInfo.getNickname());
					}
					userInfo.downloadImage(user.getUid());
				}
				user.setAccessTime(new Date());
				user.setRefreshTime(new Date());
			}
			if (user.getExpires_in() == ExpiresInType.feng.getType()) {
				result.put("code", NTxtAbs.YOUR_ACCOUNT_COLD);
				return;
			}
			if (System.currentTimeMillis() - user.getRefreshTime().getTime() > SystemConstantsAbs.days_29_timeInMillis) {
				LoggerService.getLogicLog().error(
						"微信accessTocken超时!{}",
						System.currentTimeMillis()
								- user.getRefreshTime().getTime());
				result.put("code", NTxtAbs.INVALID);
				return;
			}
			reLogin(req, resp, result, user);
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			result.put("code", NTxtAbs.EXCEPTION);
		}
	}

	/**
	 * 判断重登
	 * 
	 * 如果有重登，则踢下线
	 * 
	 * @param req
	 * @param resp
	 * @param result
	 * @param user
	 */
	private void reLogin(final HttpServletRequest req,
			HttpServletResponse resp, final JSONObject result,
			final UserBean user) {
		try {
			int serverId = user.getServerId();
			ServerTemplate lastServerT = serverId != SystemConstantsAbs.NoServerId ? ServerTemplate
					.getServerTemplate(serverId) : null;
			if (lastServerT != null && user.cannotChangeServerId()
					&& lastServerT.isConnected()) {// 不能变换服务器
			} else {
				lastServerT = lastServerT == null ? null : (lastServerT
						.isCanLogin() ? lastServerT : null);
			}
			if (lastServerT != null && user.isOnline()) {// 玩家已经在线？
				Continuation continuation = ContinuationSupport
						.getContinuation(req);
				if (!continuation.isInitial()) {
					result.put("code", NTxtAbs.CONTINUATION_NOT_INITIAL);
					return;
				}
				KickContinuationListener listener = new KickContinuationListener(
						req, resp, result, continuation, user.getUid(),
						lastServerT, this);
				int retCode = KickProcessor.getInstance().putKickUser(
						lastServerT, user.getUid(), listener);
				if (retCode == NTxtAbs.SUCCESS) {
					lastServerT.sendMessage(InnerMsgId.KickBusToGs, KickBusToGs
							.newBuilder().setUid(user.getUid()));
				} else {
					LoggerService.getLogicLog().error("重登，无法踢人!code：{}，uid:{}",
							retCode, user.getUid());
					result.put("code", retCode);
				}
			} else {
				handle(req, lastServerT, result, user);
			}
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			result.put("code", NTxtAbs.EXCEPTION);
		}
	}

	/**
	 * 获取服务器列表
	 * 
	 * @param req
	 * @param lastServerT
	 *            之前登陆的服务器
	 * @param result
	 * @param districtType
	 * @param user
	 * @throws SQLException
	 */
	public void handle(HttpServletRequest req, ServerTemplate lastServerT,
			JSONObject result, UserBean user) throws SQLException {
		/**
		 * 之前有个房间||走魔窗房间
		 * 
		 * 之前有个房间 ，则走这个，否则走魔窗房间，最后才走服务器列表
		 *
		 */

		ServerTemplate targetServerTemplate = null;
		if (lastServerT == null) {
			String roomIdStr = req.getParameter("roomId");
			int roomId = StringUtils.isBlank(roomIdStr) ? SystemConstantsAbs.NoRoomId
					: Integer.parseInt(roomIdStr);
			if (roomId != SystemConstantsAbs.NoRoomId) {
				int serverIdByRoomId = RoomDao.serverIdByRoomId(roomId);
				targetServerTemplate = serverIdByRoomId != SystemConstantsAbs.NoServerId ? ServerTemplate
						.getCanLoginServerTemplate(serverIdByRoomId) : null;
			} else {
				targetServerTemplate = ServerTemplate
						.getCanLoginServerTemplate(SystemConstantsAbs.NoRoomId);
			}
		} else {
			targetServerTemplate = lastServerT;
		}
		if (targetServerTemplate != null) {
			user.setServerListSessionCode(SecureUtil
					.genServerListSessionCode(user));
			JSONObject target = new JSONObject();
			target(target, user, targetServerTemplate);
			result.put("code", NTxtAbs.SUCCESS);
			result.put("uid", user.getUid());
			result.put("sessionCode", user.getServerListSessionCode());
			result.put("target", target);
		} else {
			result.put("code", NTxtAbs.BEST_SERVER_LIST_EMPTY);
		}
	}

	public static void codeGetAccessToken(String code) throws Exception {
	}

	public static void userinfo(String accessToken, String openId)
			throws Exception {
	}

	public static void refreshAccessToken(String refreshToken) throws Exception {
		// JSONObject sendHttp = HttpUtilTools.httpsGetJson(
		// "https://api.weixin.qq.com/sns/oauth2/refresh_token",
		// String.format(
		// "appid=%s&grant_type=refresh_token&refresh_token=%s",
		// BusConfig.AppID, refreshToken).getBytes(), true);
	}
}