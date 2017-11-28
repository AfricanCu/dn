package com.wk.gm.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ibatis.common.beans.ClassInfo;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.SystemConstantsAbs;
import com.wk.db.IbatisDbServer;
import com.wk.engine.util.KeyValueDbCache;
import com.wk.enun.DistrictType;
import com.wk.gm.db.dao.ProxyDao;

public class ProxyBean {

	public static class JSONObjectEx extends JSONObject {
		private ProxyBean userBean;
		private final int db_key;

		public JSONObjectEx(ProxyBean userBean, int db_key) {
			super();
			this.userBean = userBean;
			this.db_key = db_key;
		}

		public JSONObjectEx(ProxyBean userBean, int db_key, String dbca) {
			super(dbca);
			this.userBean = userBean;
			this.db_key = db_key;
		}

		public JSONObject put(String key, Object value)
				throws org.json.JSONException {
			if (this.userBean != null) {// 这里 加个判断才行不然 抛空
				this.userBean.bitSet.set(this.db_key);
			}
			return super.put(key, value);
		};
	}

	public static class BitSetEx extends BitSet {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ProxyBean proxyBean;

		public BitSetEx(int i, ProxyBean userBean) {
			super(i);
			this.proxyBean = userBean;
		}

		public void set(int bitIndex) {
			super.set(bitIndex);
			if (this.proxyBean.dbCache != KeyValueDbCache.emptyProxyDbCache) {
				this.proxyBean.dbCache.put(this.proxyBean.uid, proxyBean);
			}
		};

		@Override
		public void clear() {
			// LoggerService.getLogicLog().error("clear!!!!!");
			super.clear();
		}
	};

	/***/
	protected String uid;
	/** 密码 */
	protected String password;
	/** 邀请码 */
	protected int invitation;
	/** 介绍人邀请码 */
	protected int parent;
	/**授权码*/
	protected int authorization;
	/**代理模式*/
	protected int agencyMode;
	/** 姓名 */
	protected String nickname;
	/** 电话 */
	protected String phone;
	/** 微信 */
	protected String weChat;
	/** 银行 */
	protected int bankId;
	/** 银行卡号 */
	protected String bankCard;
	/** 登陆游戏session */
	protected String sessionCode;
	/** 头像 **/
	protected String head;
	/** 注册时间 */
	protected Date registerTime = SystemConstantsAbs.nullDate;
	/** 登陆时间 */
	protected Date loginTime = SystemConstantsAbs.nullDate;
	/** 登出时间 */
	protected Date logoutTime = SystemConstantsAbs.nullDate;
	/**累计买的房卡*/
	protected int buyCard;
	/**累计卖 的房卡*/
	protected int sellCard;
	/***/
	protected int diamond;
	/***/
	protected int credits;
	/**代理状态 0 申请审核 1通过审核需完成首充*/
	protected int flag;
	/** 扩展 不要给get **/
	protected JSONObjectEx dbca = new JSONObjectEx(this, dbca_key);
	/** 所属区域 */
	protected DistrictType district;
	// tmp
	/** 标记需要更新的字段 **/
	protected BitSetEx bitSet = new BitSetEx(64, this);
	// final 不会覆盖
	/** 玩家数据缓存定时回写器 **/
	protected final KeyValueDbCache<String, ProxyBean> dbCache;
	// dbca JSON标签
	/** 是否新人 */
	protected static final String isNewer_index = "iN";
	/** 输错密码次数 ***/
	protected static final String password_err_index = "pE";
	/** 代理列表 ***/
	protected static final String proxyList_index = "pL";
	/** 总充值额度 ***/
	protected static final String chargeSum_index = "cS";
	/** 介绍人 ***/
	protected static final String introduce_index = "i";
	/*** 特殊的菜单 **/
	protected static final String specialMenus_index = "sMs";
	private static final String EndLoopStr = "bitSet";
	// bitSet key
	protected static final int uid_key = 0;
	protected static final int password_key = 1;
	protected static final int invitation_key = 2;
	protected static final int parent_key = 3;
	protected static final int authorization_key = 4;
	protected static final int agencyMode_key = 5;
	protected static final int nickname_key = 6;
	protected static final int phone_key = 7;
	protected static final int weChat_key = 8;
	protected static final int bankId_key = 9;
	protected static final int bankCard_key = 10;
	protected static final int sessionCode_key = 11;
	protected static final int head_key = 12;
	protected static final int registerTime_key = 13;
	protected static final int loginTime_key = 14;
	protected static final int logoutTime_key = 15;
	protected static final int buyCard_key = 16;
	protected static final int sellCard_key = 17;
	protected static final int diamond_key = 18;
	protected static final int credits_key = 19;
	protected static final int flag_key = 20;
	protected static final int dbca_key = 21;
	protected static final int district_key = 22;



	public static void main(String[] args) {
		Field[] fields = ProxyBean.class.getDeclaredFields();
		int index = 0;
		for (Field field : fields) {
			if (field.getName().equals(EndLoopStr)) {
				break;
			}
			System.err.println("protected static final int " + field.getName()
					+ "_key = " + index++ + ";");
		}
	}

	public ProxyBean() {
		this(KeyValueDbCache.emptyProxyDbCache);
	}

	/**
	 * 
	 * @param dbCache
	 *            数据缓存定时回写器
	 */
	public ProxyBean(KeyValueDbCache<String, ProxyBean> dbCache) {
		this.dbCache = dbCache;
	}

	/**
	 * 覆盖对象
	 * 
	 * 用于reloadUser
	 * 
	 * final or static 变量 不覆盖
	 * 
	 * @param bean
	 * @throws Exception
	 */
	public void overWrite(ProxyBean bean) throws Exception {
		if (bean == null) {
			return;
		}
		// ！！！ 易主
		bean.dbca.userBean = this;
		bean.bitSet.proxyBean = this;
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())
					&& !Modifier.isFinal(field.getModifiers())) {
				field.set(this, field.get(bean));
			}
		}
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getInvitation() {
		return invitation;
	}

	public void setInvitation(int invitation) {
		this.invitation = invitation;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}
	
	public int getAuthorization() {
		return authorization;
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setAuthorization(int authorization) {
		this.authorization = authorization;
	}

	public int getAgencyMode() {
		return agencyMode;
	}
	
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setAgencyMode(int agencyMode) {
		this.agencyMode = agencyMode;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWeChat() {
		return weChat;
	}

	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
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
	
	public int getBuyCard() {
		return buyCard;
	}

	public void setBuyCard(int buyCard) {
		this.buyCard = buyCard;
	}

	public int getSellCard() {
		return sellCard;
	}

	public void setSellCard(int sellCard) {
		this.sellCard = sellCard;
		this.bitSet.set(sellCard_key);
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		this.diamond = diamond;
		this.bitSet.set(diamond_key);
	}

	public int getCredits() {
		return credits;
	}
	
	public void setCredits(int credits) {
		this.credits = credits;
		this.bitSet.set(credits_key);
	}

	public String getDbca() {
		return this.dbca.toString();
	}
	
	public void setDbca(String dbca) {
		if (dbca != null) {
			this.dbca = new JSONObjectEx(this, dbca_key, dbca);
		}
		this.bitSet.set(dbca_key);
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
		// LoggerService.getLogicLog().error("this.bitSet{}", this.bitSet);
		if (this.bitSet.isEmpty()) {
			return null;
		}
		ClassInfo info = ClassInfo.getInstance(ProxyBean.class);
		ArrayList<Object> parameters = new ArrayList<>();
		Field[] fields = ProxyBean.class.getDeclaredFields();
		StringBuilder builder = new StringBuilder("update proxy set");
		int index = 0;
		for (Field field : fields) {
			if (field.getName().equals(EndLoopStr)) {
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
		int update = ProxyDao.update(this);
		LoggerService.getLogicLog().warn("保存单个proxy数据：{}", update);
		this.bitSet.clear();
		if (this.dbCache != KeyValueDbCache.emptyProxyDbCache) {
			this.dbCache.remove(this);
		}
	}

	/**
	 * 插入玩家数据到数据库
	 * 
	 * @throws SQLException
	 */
	public void insert() throws SQLException {
		ProxyDao.insertProxy(this);
		LoggerService.getLogicLog().warn("插入单个proxy数据,uid:{}", this.getUid());
		this.bitSet.clear();
		if (this.dbCache != KeyValueDbCache.emptyProxyDbCache) {
			this.dbCache.remove(this);
		}
	}

	public void bitSetClear() {
		bitSet.clear();
	}

	/**
	 * 是否在线
	 * 
	 * @return
	 */
	public boolean isOnline() {
		return this.loginTime.getTime() > this.logoutTime.getTime();
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
		this.bitSet.set(nickname_key);
	}

	public String getNickname() {
		return nickname;
	}

	public boolean isNewer() {
		return this.dbca.optBoolean(isNewer_index, true);
	}

	public void setNotNewer() {
		this.dbca.put(isNewer_index, false);
	}

	public int getPasswordErrTimes() {
		return this.dbca.optInt(password_err_index, 0);
	}

	public void addPwdErrTimes() {
		int passwordErrTimes = getPasswordErrTimes();
		this.dbca.put(password_err_index, passwordErrTimes + 1);
	}

	public void resetPwdErrTimes() {
		this.dbca.put(password_err_index, 0);
	}

	public JSONArray getProxyList() {
		if (!this.dbca.has(proxyList_index)) {
			this.dbca.put(proxyList_index, new JSONArray());
		}
		return this.dbca.optJSONArray(proxyList_index);
	}

	public void addProxyList(String proxy) {
		JSONArray proxyList = getProxyList();
		for (int index = 0; index < proxyList.length(); index++) {
			if (proxyList.optString(index).equals(proxy)) {
				return;
			}
		}
		proxyList.put(proxy);
		this.bitSet.set(dbca_key);
	}

	public ArrayList<String> getProxys() {
		ArrayList<String> arrayList = new ArrayList<String>();
		JSONArray proxyList = getProxyList();
		for (int index = 0; index < proxyList.length(); index++) {
			arrayList.add(proxyList.getString(index));
		}
		return arrayList;
	}

	public int getChargeSum() {
		return this.dbca.optInt(chargeSum_index, 0);
	}

	public void addChargeSum(int charge) {
		this.dbca.put(chargeSum_index, getChargeSum() + charge);
	}

	public String getIntroduce() {
		return this.dbca.optString(introduce_index);
	}

	public void setIntroduce(String introduce) {
		this.dbca.put(introduce_index, introduce);
	}

	public JSONArray getSpecialMenuArr() {
		if (!this.dbca.has(specialMenus_index)) {
			this.dbca.put(specialMenus_index, new JSONArray());
		}
		return this.dbca.optJSONArray(specialMenus_index);
	}

	public void addSpecialMenu(int special) {
		JSONArray specialMenu = getSpecialMenuArr();
		for (int index = 0; index < specialMenu.length(); index++) {
			if (specialMenu.optInt(index) == special) {
				return;
			}
		}
		specialMenu.put(special);
		this.bitSet.set(dbca_key);
	}

	public ArrayList<Integer> getSpecialMenus() {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		JSONArray specialMenu = getSpecialMenuArr();
		for (int index = 0; index < specialMenu.length(); index++) {
			arrayList.add(specialMenu.getInt(index));
		}
		return arrayList;
	}

	public int getDistrict() {
		return district.getType();
	}

	public void setDistrict(int district) {
		this.district = DistrictType.getEnum(district);
	}
	
	public DistrictType getDistrictType() {
		return district;
	}
}
