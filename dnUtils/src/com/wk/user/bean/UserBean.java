package com.wk.user.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import msg.LoginMessage.GameRecord;
import msg.LoginMessage.PlayerRecordCast;
import msg.LoginMessage.ProxyRecordCast;
import msg.RoomMessage.ProxyRoom;

import org.json.JSONObject;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ibatis.common.beans.ClassInfo;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.bean.SystemConstantsAbs;
import com.wk.db.IbatisDbServer;
import com.wk.db.dao.GamerecordDao;
import com.wk.engine.config.LogicI;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.engine.util.KeyValueDbCache;
import com.wk.enun.PlatformType;
import com.wk.enun.UserState;
import com.wk.guild.bean.GuildBean;
import com.wk.guild.enm.JulebuState;
import com.wk.user.dao.UserDao;
import com.wk.util.GameDayTask;

public class UserBean {
	/** 玩家ID */
	protected long uid;
	/** 昵称 */
	protected String nickname;
	/** 平台提供的id */
	protected String puid;
	/** 设备id **/
	protected String deviceId;
	/** 平台id */
	protected int platId;
	/** 密码 */
	protected String password;
	/** 登陆游戏session */
	protected String sessionCode;
	/** 头像 **/
	protected String head;
	/** 普通用户性别，1为男性，2为女性 **/
	protected int sex;
	/** 注册时间 */
	protected Date registerTime = SystemConstantsAbs.nullDate;
	/** 登陆时间 */
	protected Date loginTime = SystemConstantsAbs.nullDate;
	/** 登出时间 */
	protected Date logoutTime = SystemConstantsAbs.nullDate;
	/** 最近登录服务器id */
	protected int serverId;
	/** 最近进入房间id */
	protected int roomId;
	/** 我的俱乐部列表 */
	protected HashMap<Integer, JSONObject> myGuild = new HashMap<>(0);
	/** 加入的俱乐部列表 */
	protected HashMap<Integer, JSONObject> joinGuild = new HashMap<>(0);
	/*** 代理开的房间列表 <房间id,代理开房数据> **/
	protected HashMap<Integer, ProxyRoom> proxy = new HashMap<>();
	/** 钻石数 */
	protected int diamond;
	/** 封号状态 */
	protected int expires_in;
	/** 微信access时间 */
	protected Date accessTime = SystemConstantsAbs.nullDate;
	/** 微信登录刷新时间 */
	protected Date refreshTime = SystemConstantsAbs.nullDate;
	/** 游戏记录 **/
	protected byte[] gameRecord = SystemConstantsAbs.empltyBytes;
	/** 代开房游戏记录 **/
	protected byte[] proxyRecord = SystemConstantsAbs.empltyBytes;
	/** 扩展 不要给get **/
	protected UserJSONObjectEx dbca = new UserJSONObjectEx(this, dbca_key);
	/** 我的代理 **/
	protected int myAgency;
	// tmp
	/** 标记需要更新的字段 **/
	protected UserBitSetEx bitSet = new UserBitSetEx(64, this);
	// final 不会覆盖
	/** 玩家数据缓存定时回写器 **/
	protected final KeyValueDbCache<Long, UserBean> dbCache;
	/** 游戏记录推送消息Builder */
	private PlayerRecordCast.Builder gameRecordCast = null;
	/** PROXY游戏记录推送消息Builder */
	private ProxyRecordCast.Builder proxyRecordCast = null;
	// 俱乐部 JSON标签
	/** 名称 */
	protected static final String julebuName_index = "jN";
	/** 会长ID */
	protected static final String julebuMasterUid_index = "jMU";
	/** 会长昵称 */
	protected static final String julebuMasterName_index = "jMN";
	/** 状态 */
	protected static final String julebuState_index = "jS";
	/** 玩法描述 */
	protected static final String julebuPlayType_index = "jPT";
	// dbca JSON标签
	/** 在线状态 */
	protected static final String state_index = "s";
	/** 服务器列表session */
	public static final String serverListSessionCode_index = "sLSC";
	/** 创建公会所在服务器ID */
	public static final String createGuildServerId_index = "cGSI";
	protected static final Class<?> clazz = UserBean.class;
	// 自动生成开始
	// bitSet key
	private static final int uid_key = 0;
	private static final int nickname_key = 1;
	private static final int puid_key = 2;
	private static final int deviceId_key = 3;
	private static final int platId_key = 4;
	private static final int password_key = 5;
	private static final int sessionCode_key = 6;
	private static final int head_key = 7;
	private static final int sex_key = 8;
	private static final int registerTime_key = 9;
	private static final int loginTime_key = 10;
	private static final int logoutTime_key = 11;
	private static final int serverId_key = 12;
	private static final int roomId_key = 13;
	private static final int myGuild_key = 14;
	private static final int joinGuild_key = 15;
	private static final int proxy_key = 16;
	private static final int diamond_key = 17;
	private static final int expires_in_key = 18;
	private static final int accessTime_key = 19;
	private static final int refreshTime_key = 20;
	private static final int gameRecord_key = 21;
	private static final int proxyRecord_key = 22;
	private static final int dbca_key = 23;
	private static final int myAgency_key = 24;

	// 自动生成结束
	public UserBean() {
		this(KeyValueDbCache.emptyUserDbCache);
	}

	/**
	 * 
	 * @param dbCache
	 *            数据缓存定时回写器
	 */
	public UserBean(KeyValueDbCache<Long, UserBean> dbCache) {
		this.dbCache = dbCache;
	}

	/** 重置临时数据 */
	public void reset() {
		// TODO
		this.gameRecordCast = null;
		this.proxyRecordCast = null;
	}

	/**
	 * <pre>
	 * 覆盖对象
	 * 用于重写User
	 * 不覆盖final或static变量
	 * 注意：有引用this的要易主
	 * </pre>
	 * 
	 * @param bean
	 * @throws Exception
	 */
	public void overWrite(UserBean bean) throws Exception {
		if (bean == null || bean.getClass() != clazz) {
			throw new Exception(String.format("not %s : %s", clazz, bean));
		}
		// ！！！ 易主
		bean.dbca.userBean = this;
		bean.bitSet.userBean = this;
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())
					&& !Modifier.isFinal(field.getModifiers())) {
				field.set(this, field.get(bean));
			}
		}
	}

	public long getUid() {
		return uid;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
		this.bitSet.set(nickname_key);
	}

	public String getNickname() {
		return nickname;
	}

	public String getPuid() {
		return puid;
	}

	public void setPuid(String puid) {
		this.puid = puid;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		this.bitSet.set(deviceId_key);
	}

	public int getPlatId() {
		return platId;
	}

	public void setPlatId(int platId) {
		this.platId = platId;
		this.bitSet.set(platId_key);
	}

	public PlatformType getPlatFormType() {
		return PlatformType.getEnum(this.platId);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.bitSet.set(password_key);
	}

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
		this.bitSet.set(sessionCode_key);
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
		this.bitSet.set(head_key);
	}

	/** 普通用户性别，1为男性，2为女性 **/
	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		if (sex == 1 || sex == 2) {
			this.sex = sex;
		} else {
			this.sex = 1;
		}
		this.bitSet.set(sex_key);
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		if (registerTime == null) {
			return;
		}
		this.registerTime = registerTime;
		this.bitSet.set(registerTime_key);
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		if (loginTime == null) {
			return;
		}
		this.loginTime = loginTime;
		this.bitSet.set(loginTime_key);
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		if (logoutTime == null) {
			return;
		}
		this.logoutTime = logoutTime;
		this.bitSet.set(logoutTime_key);
	}

	/**
	 * 是否在线
	 * 
	 * @return
	 */
	public boolean isOnline() {
		return this.loginTime.getTime() >= this.logoutTime.getTime();
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
		this.bitSet.set(serverId_key);
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
		this.bitSet.set(roomId_key);
	}

	/**
	 * 代理开房和自己有房间都不能变换服务器id
	 * 
	 * @return
	 */
	public boolean cannotChangeServerId() {
		return this.getRoomId() != SystemConstantsAbs.NoRoomId;
	}

	public String getMyGuild() {
		StringBuilder builder = new StringBuilder();
		for (Entry<Integer, JSONObject> b : myGuild.entrySet()) {
			builder.append(b.getKey()).append(SystemConstantsAbs.sharp_SEP)
					.append(b.getValue().toString())
					.append(SystemConstantsAbs.sharp_SEP);
		}
		return builder.toString();
	}

	public void setMyGuild(String myGuild) {
		if (myGuild != null && !myGuild.trim().equals("")) {
			String[] split = myGuild.split(SystemConstantsAbs.sharp_SEP);
			for (int i = 0; i < split.length; i += 2) {
				this.myGuild.put(Integer.parseInt(split[i]), new JSONObject(
						split[i + 1]));
			}
		}
		this.bitSet.set(myGuild_key);
	}

	public HashMap<Integer, JSONObject> getMyJulebuMap() {
		return this.myGuild;
	}

	/**
	 * 创建俱乐部
	 * 
	 * @param id
	 */
	public JSONObject createMyJulebu(GuildBean guild) {
		int id = guild.getId();
		if (this.myGuild.containsKey(id)) {
			return null;
		}
		JSONObject put = createJulebuJson(guild, JulebuState.created);
		this.myGuild.put(id, put);
		this.bitSet.set(myGuild_key);
		return put;
	}

	private JSONObject createJulebuJson(GuildBean guild, JulebuState state) {
		return new JSONObject().put(julebuName_index, guild.getName())
				.put(julebuMasterUid_index, guild.getMasterUid())
				.put(julebuMasterName_index, guild.getMasterNickName())
				.put(julebuState_index, state.getType())
				.put(julebuPlayType_index, guild.getPlayTypeDesc());
	}

	/** 更新俱乐部信息 **/
	public JSONObject updateJulebuInfo(int guildId, String name,
			String playTypeDesc) {
		JSONObject jsonObject = this.myGuild.get(guildId);
		if (jsonObject == null) {
			jsonObject = this.joinGuild.get(guildId);
		}
		jsonObject.put(julebuName_index, name);
		jsonObject.put(julebuPlayType_index, playTypeDesc);
		this.bitSet.set(myGuild_key);
		return jsonObject;
	}

	public JSONObject removeMyJulebu(int guildId) {
		JSONObject remove = this.myGuild.remove(guildId);
		this.bitSet.set(myGuild_key);
		return remove;
	}

	/**
	 * 自己建的俱乐部数
	 * 
	 * @return
	 */
	public int getMyGuildSize() {
		return this.myGuild.size();
	}

	public String getJoinGuild() {
		StringBuilder builder = new StringBuilder();
		for (Entry<Integer, JSONObject> b : joinGuild.entrySet()) {
			builder.append(b.getKey()).append(SystemConstantsAbs.sharp_SEP)
					.append(b.getValue().toString())
					.append(SystemConstantsAbs.sharp_SEP);
		}
		return builder.toString();
	}

	public void setJoinGuild(String joinGuild) {
		if (joinGuild != null && !joinGuild.trim().equals("")) {
			String[] split = joinGuild.split(SystemConstantsAbs.sharp_SEP);
			for (int i = 0; i < split.length; i += 2) {
				this.joinGuild.put(Integer.parseInt(split[i]), new JSONObject(
						split[i + 1]));
			}
		}
		this.bitSet.set(joinGuild_key);
	}

	public HashMap<Integer, JSONObject> getJoinJulebuMap() {
		return this.joinGuild;
	}

	/**
	 * 申请俱乐部
	 * 
	 * @param id
	 * @return
	 */
	public JSONObject applyJulebu(GuildBean guild) {
		int id = guild.getId();
		if (this.joinGuild.containsKey(id)) {
			return null;
		}
		JSONObject put = createJulebuJson(guild, JulebuState.apply);
		this.joinGuild.put(id, put);
		this.bitSet.set(joinGuild_key);
		return put;
	}

	public JSONObject removeJoinJulebu(int guildId) {
		JSONObject remove = this.joinGuild.remove(guildId);
		this.bitSet.set(joinGuild_key);
		return remove;
	}

	/**
	 * 加入俱乐部
	 * 
	 * @param id
	 * @return
	 */
	public JSONObject joinJulebu(int guildId) {
		JSONObject jsonObject = this.joinGuild.get(guildId);
		if (jsonObject == null) {
			return null;
		}
		jsonObject.put(julebuState_index, JulebuState.joined.getType());
		this.bitSet.set(joinGuild_key);
		return jsonObject;
	}

	/**
	 * 加入的俱乐部数
	 * 
	 * @return
	 */
	public int getJoinGuildSize() {
		return this.joinGuild.size();
	}

	public String getProxy() {
		StringBuilder builder = new StringBuilder();
		for (Integer b : proxy.keySet()) {
			builder.append(b).append(SystemConstantsAbs.sharp_SEP);
		}
		return builder.toString();
	}

	public void setProxy(String proxy) {
		if (proxy != null && !proxy.trim().equals("")) {
			String[] split = proxy.split(SystemConstantsAbs.sharp_SEP);
			for (int i = 0; i < split.length; i++) {
				this.proxy.put(Integer.parseInt(split[i]), null);
			}
		}
		this.bitSet.set(proxy_key);
	}

	protected void setProxyKey() {
		this.bitSet.set(proxy_key);
	}

	/**
	 * 加入代理房间
	 * 
	 * @param id
	 * @param proxyRoom
	 */
	public void addProxy(int id, ProxyRoom proxyRoom) {
		this.proxy.put(id, proxyRoom);
		this.bitSet.set(proxy_key);
	}

	/**
	 * 移除代理房间
	 * 
	 * @param id
	 */
	public void removeProxy(int id) {
		this.proxy.remove(id);
		this.bitSet.set(proxy_key);
	}

	/**
	 * 代理房间数目
	 * 
	 * @return
	 */
	public int getProxyRoomNumber() {
		return this.proxy.size();
	}

	public int getDiamond() {
		return this.diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
		this.bitSet.set(diamond_key);
	}

	/**
	 * 加钻
	 * 
	 * @param districtType
	 * @param dia
	 */
	public void changeDiamond(int dia) {
		if (dia == 0) {
			return;
		}
		if (dia > 0) {
		} else if (dia < 0) {
		}
		int tmpdiamond = this.getDiamond() + dia;
		if (tmpdiamond < 0) {
			LoggerService.getLogicLog().error("钻石不足！diamond:{},dia:{}",
					this.diamond, dia);
			tmpdiamond = 0;
		}
		this.setDiamond(tmpdiamond);
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
		this.bitSet.set(expires_in_key);
	}

	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		if (accessTime == null) {
			return;
		}
		this.accessTime = accessTime;
		this.bitSet.set(accessTime_key);
	}

	public Date getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(Date refreshTime) {
		if (refreshTime == null) {
			return;
		}
		this.refreshTime = refreshTime;
		this.bitSet.set(refreshTime_key);
	}

	public byte[] getGameRecord() {
		return this.getGameRecordCast().build().toByteArray();
	}

	public void setGameRecord(byte[] gameRecord) {
		if (gameRecord == null) {
			return;
		}
		this.gameRecord = gameRecord;
		this.bitSet.set(gameRecord_key);
	}

	public byte[] getProxyRecord() {
		return this.getProxyRecordCast().build().toByteArray();
	}

	public void setProxyRecord(byte[] proxyRecord) {
		if (proxyRecord == null) {
			return;
		}
		this.proxyRecord = proxyRecord;
		this.bitSet.set(proxyRecord_key);
	}

	/**
	 * 记录游戏结果
	 * 
	 * @param record
	 * @param checkId
	 */
	public void recordGame(GameRecord record) {
		this.getGameRecordCast().addRecord(record);
		this.bitSet.set(gameRecord_key);
	}

	/**
	 * 记录代理开房游戏结果
	 * 
	 * @param record
	 */
	public void recordProxyGame(GameRecord record) {
		this.getProxyRecordCast().addRecord(record);
		this.bitSet.set(proxyRecord_key);

	}

	/**
	 * 非常小心，，不能在一个地方获取两次，，获取一次就缓存，，否则数组越界
	 * 
	 * @return
	 */
	public PlayerRecordCast.Builder getGameRecordCast() {
		if (gameRecordCast == null) {
			try {
				gameRecordCast = PlayerRecordCast.newBuilder().mergeFrom(
						this.gameRecord);
			} catch (InvalidProtocolBufferException e) {
				LoggerService.getPlatformLog().error(e.getMessage(), e);
			}
		}
		if (!this.gameRecordCast.getRecordList().isEmpty()
				&& (GameDayTask.isWeekChange(this.gameRecordCast.getRecord(0)
						.getTime()) || this.gameRecordCast.getRecordCount() > LogicI
						.getInstance().getGameRecordMax())) {
			GameRecord record = this.gameRecordCast.getRecord(0);
			this.gameRecordCast.removeRecord(0);
			try {
				GamerecordDao.deleteGamerecord(record.getIndex());
			} catch (SQLException e) {
				LoggerService.getLogicLog().error(e.getMessage(), e);
			}
			NTxtAbs.println(String.format("移除记录--%s--%s--%s",
					record.getIndex(), new SimpleDateFormat(
							"YYYY-MM-DD HH:mm:ss").format(new Date(record
							.getTime())), this.gameRecordCast.getRecordCount()));
			this.bitSet.set(gameRecord_key);
		}
		return gameRecordCast;
	}

	protected ProxyRecordCast.Builder getProxyRecordCast() {
		if (proxyRecordCast == null) {
			try {
				proxyRecordCast = ProxyRecordCast.newBuilder().mergeFrom(
						this.proxyRecord);
			} catch (InvalidProtocolBufferException e) {
				LoggerService.getPlatformLog().error(e.getMessage(), e);
			}
		}
		if (!this.proxyRecordCast.getRecordList().isEmpty()
				&& (GameDayTask.isWeekChange(this.proxyRecordCast.getRecord(0)
						.getTime()) || this.proxyRecordCast.getRecordCount() > LogicI
						.getInstance().getGameRecordMax())) {
			GameRecord record = this.proxyRecordCast.getRecord(0);
			this.proxyRecordCast.removeRecord(0);
			try {
				GamerecordDao.deleteGamerecord(record.getIndex());
			} catch (SQLException e) {
				LoggerService.getLogicLog().error(e.getMessage(), e);
			}
			NTxtAbs.println(String.format("移除proxy记录--%s--%s--%s", record
					.getIndex(), new SimpleDateFormat("YYYY-MM-DD HH:mm:ss")
					.format(new Date(record.getTime())), this.proxyRecordCast
					.getRecordCount()));
			this.bitSet.set(proxyRecord_key);
		}
		return proxyRecordCast;
	}

	public String getDbca() {
		return this.dbca.toString();
	}

	public void setDbca(String dbca) {
		if (dbca != null) {
			this.dbca = new UserJSONObjectEx(this, dbca_key, dbca);
		}
		this.bitSet.set(dbca_key);
	}

	public void setState(UserState state) {
		this.dbca.put(state_index, state.getType());
	}

	/**
	 * 玩家状态
	 * 
	 * @return
	 */
	public UserState getState() {
		return UserState.getEnum(this.dbca.optInt(state_index,
				UserState.offline.getType()));
	}

	public void setServerListSessionCode(String serverListSessionCode) {
		this.dbca.put(serverListSessionCode_index, serverListSessionCode);
	}

	public String getServerListSessionCode() {
		return this.dbca.optString(serverListSessionCode_index, "");
	}

	public void setCreateGuildServerId(int createGuildServerId) {
		this.dbca.put(createGuildServerId_index, createGuildServerId);
	}

	/**
	 * 创建公会所在服务器ID
	 * 
	 * @return
	 */
	public int getCreateGuildServerId() {
		return this.dbca.optInt(createGuildServerId_index,
				SystemConstantsAbs.NoServerId);
	}

	public int getMyAgency() {
		return myAgency;
	}

	public void setMyAgency(int myAgency) {
		this.myAgency = myAgency;
		this.bitSet.set(myAgency_key);
	}

	/***
	 * 是否能创俱乐部
	 * 
	 * @return
	 */
	public boolean isCreateJ() {
		return true;
		// return this.myAgency != SystemConstantsAbs.NoAgency
		// || this.getPlatFormType() == PlatformType.ios_visitor
		// || this.getPlatFormType() == PlatformType.pc;
	}

	/**
	 * 更新数据到数据库
	 * 
	 * @return 更新结果
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public int update() throws SQLException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (this.bitSet.isEmpty()) {
			return 0;
		}
		Connection conn = IbatisDbServer.getGmSqlMapper().getDataSource()
				.getConnection();
		if (conn == null) {
			return 0;
		}
		conn.setAutoCommit(true);
		PreparedStatement pstate = null;
		try {
			pstate = this.createPreparedStatement(conn);
			if (pstate == null) {
				return 0;
			}
			return pstate.executeUpdate();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (Throwable e) {
				LoggerService.getLogicLog().error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 创建更新数据 PreparedStatement
	 * 
	 * 开启一个事务操作
	 * 
	 * @param conn
	 * 
	 * @param conn
	 * @return
	 * @return
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PreparedStatement createPreparedStatement(Connection conn)
			throws SQLException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (this.bitSet.isEmpty()) {
			return null;
		}
		ClassInfo info = ClassInfo.getInstance(clazz);
		ArrayList<Object> parameters = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder builder = new StringBuilder("update user set");
		int index = 0;
		for (Field field : fields) {
			if (field.getName().equals(SystemConstantsAbs.EndLoopStr)) {
				break;
			}
			if (this.bitSet.get(index)) {
				if (!parameters.isEmpty()) {
					builder.append(" , ");
				}
				Object invoke = info.getGetInvoker(field.getName()).invoke(
						this, null);
				parameters.add(invoke);
				builder.append(" ").append(field.getName()).append(" = ? ");
			}
			index++;
		}
		if (parameters.isEmpty()) {
			return null;
		}
		builder.append(" where `uid` = ? ;");
		parameters.add(this.uid);
		PreparedStatement pstate = null;
		try {
			if (ServerConfigAbs.isMonitorPrint())
				NTxtAbs.println(builder.toString());
			pstate = conn.prepareStatement(builder.toString());
			int i = 1;
			for (Object parameter : parameters) {
				// if (parameter == null) {
				// pstate.setNull(i++, Types.VARCHAR);
				// } else {
				pstate.setObject(i++, parameter);
				// }
			}
			this.bitSet.clear();
			return pstate;
		} catch (SQLException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 保存玩家数据到数据库
	 * 
	 * @throws SQLException
	 */
	public void save() throws SQLException {
		UserDao.update(this);
		this.bitSet.clear();
		if (this.dbCache != KeyValueDbCache.emptyUserDbCache) {
			this.dbCache.remove(this);
		}
	}

	/**
	 * 插入玩家数据到数据库
	 * 
	 * @return
	 * 
	 * @throws SQLException
	 */
	public long insert() throws SQLException {
		this.uid = UserDao.insertUser(this);
		if (ServerConfigAbs.isMonitorPrint())
			LoggerService.getLogicLog().debug("插入单个玩家数据,puid:{},自增uid:{}",
					this.getPuid(), this.getUid());
		this.bitSet.clear();
		if (this.dbCache != KeyValueDbCache.emptyUserDbCache) {
			this.dbCache.remove(this);
		}
		return this.uid;
	}

	public void bitSetClear() {
		bitSet.clear();
	}

}