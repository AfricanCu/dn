package com.wk.gm.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.ibatis.common.beans.ClassInfo;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.SystemConstantsAbs;
import com.wk.db.IbatisDbServer;
import com.wk.engine.util.KeyValueDbCache;
import com.wk.gm.db.dao.ShopDao;

public class ShopTemplate {

	private static HashMap<Integer, ShopTemplate> shopTemplateMap;

	public static Collection<ShopTemplate> getTemplates() {
		return getShopTemplateMap().values();
	}

	public static ShopTemplate getTemplate(int shopId) {
		return getShopTemplateMap().get(shopId);
	}

	public static HashMap<Integer, ShopTemplate> getShopTemplateMap() {
		if (shopTemplateMap == null) {
			shopTemplateMap = new HashMap<Integer, ShopTemplate>();
			try {
				List<ShopTemplate> queryShopList = ShopDao.queryShopList();
				for (ShopTemplate t : queryShopList) {
					shopTemplateMap.put(t.getShopId(), t);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return shopTemplateMap;
	}

	public static class JSONObjectEx extends JSONObject {
		private ShopTemplate userBean;
		private final int db_key;

		public JSONObjectEx(ShopTemplate userBean, int db_key) {
			super();
			this.userBean = userBean;
			this.db_key = db_key;
		}

		public JSONObjectEx(ShopTemplate userBean, int db_key, String dbca) {
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

	public static final KeyValueDbCache<Integer, ShopTemplate> emptyShopDbCache = new KeyValueDbCache<Integer, ShopTemplate>() {
		private static final long serialVersionUID = 1L;

		public ShopTemplate put(Integer key, ShopTemplate value) {
			return null;
		};

		public ShopTemplate get(Object key) {
			return null;
		};

		public ShopTemplate remove(Object key) {
			return null;
		};
	};

	public static class BitSetEx extends BitSet {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ShopTemplate proxyBean;

		public BitSetEx(int i, ShopTemplate userBean) {
			super(i);
			this.proxyBean = userBean;
		}

		public void set(int bitIndex) {
			super.set(bitIndex);
			if (this.proxyBean.dbCache != emptyShopDbCache) {
				this.proxyBean.dbCache.put(this.proxyBean.shopId, proxyBean);
			}
		};

		@Override
		public void clear() {
			// LoggerService.getLogicLog().error("clear!!!!!");
			super.clear();
		}
	};

	private int shopId;
	private String rmb;
	private String pics;
	private String name;
	private String place;
	private String kind;
	private String feture;
	private int startSeal;
	private int maxSeal;
	private String detail;
	private String evaluate;
	protected JSONObjectEx dbca = new JSONObjectEx(this, dbca_key);
	private int star;
	private Date beginTime = SystemConstantsAbs.nullDate;
	private Date endTime = SystemConstantsAbs.nullDate;
	// tmp
	/** 标记需要更新的字段 **/
	protected BitSetEx bitSet = new BitSetEx(64, this);
	private final ArrayList<String> picList = new ArrayList<String>();
	// final 不会覆盖
	/** 玩家数据缓存定时回写器 **/
	protected final KeyValueDbCache<Integer, ShopTemplate> dbCache;
	// dbca JSON标签
	/** 在线状态 */
	protected static final String state_index = "s";
	// bitSet key
	protected static final int shopTemplateMap_key = 0;
	protected static final int emptyShopDbCache_key = 1;
	protected static final int shopId_key = 2;
	protected static final int rmb_key = 3;
	protected static final int pics_key = 4;
	protected static final int name_key = 5;
	protected static final int place_key = 6;
	protected static final int kind_key = 7;
	protected static final int feture_key = 8;
	protected static final int startSeal_key = 9;
	protected static final int maxSeal_key = 10;
	protected static final int detail_key = 11;
	protected static final int evaluate_key = 12;
	protected static final int dbca_key = 13;
	protected static final int star_key = 14;
	protected static final int beginTime_key = 15;
	protected static final int endTime_key = 16;

	public static void main(String[] args) {
		Field[] fields = ShopTemplate.class.getDeclaredFields();
		int index = 0;
		for (Field field : fields) {
			if (field.getName().equals("bitSet")) {
				break;
			}
			System.err.println("protected static final int " + field.getName()
					+ "_key = " + index++ + ";");
		}
	}

	public ShopTemplate() {
		this(emptyShopDbCache);
	}

	/**
	 * 
	 * @param dbCache
	 *            数据缓存定时回写器
	 */
	public ShopTemplate(KeyValueDbCache<Integer, ShopTemplate> dbCache) {
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
	public void overWrite(ShopTemplate bean) throws Exception {
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

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
		this.bitSet.set(shopId_key);
	}

	public String getRmb() {
		return rmb;
	}

	public float getMoney() {
		return Float.parseFloat(rmb);
	}

	public void setRmb(String rmb) {
		this.rmb = rmb;
		this.bitSet.set(rmb_key);
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
		picList.clear();
		String[] split = pics.split(";");
		for (String sp : split) {
			if (!sp.contains(".")) {
				sp = sp + ".jpg";
			}
			sp = "http://download.hunanshikecao.com/tree/" + sp;
			picList.add(sp);
		}
		this.bitSet.set(pics_key);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.bitSet.set(name_key);
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
		this.bitSet.set(place_key);
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
		this.bitSet.set(kind_key);
	}

	public String getFeture() {
		return feture;
	}

	public void setFeture(String feture) {
		this.feture = feture;
		this.bitSet.set(feture_key);
	}

	public int getStartSeal() {
		return startSeal;
	}

	public void setStartSeal(int startSeal) {
		this.startSeal = startSeal;
		this.bitSet.set(startSeal_key);
	}

	public int getMaxSeal() {
		return maxSeal;
	}

	public void setMaxSeal(int maxSeal) {
		this.maxSeal = maxSeal;
		this.bitSet.set(maxSeal_key);
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
		this.bitSet.set(detail_key);
	}

	public ArrayList<String> getPicList() {

		return picList;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
		this.bitSet.set(star_key);
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		if (beginTime == null) {
			return;
		}
		this.beginTime = beginTime;
		this.bitSet.set(beginTime_key);
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		if (endTime == null) {
			return;
		}
		this.endTime = endTime;
		this.bitSet.set(endTime_key);
	}

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
		this.bitSet.set(evaluate_key);
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
		ClassInfo info = ClassInfo.getInstance(ShopTemplate.class);
		ArrayList<Object> parameters = new ArrayList<>();
		Field[] fields = ShopTemplate.class.getDeclaredFields();
		StringBuilder builder = new StringBuilder("update shop set");
		int index = 0;
		for (Field field : fields) {
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
		builder.append(" where `shopId` = ? ;");
		parameters.add(this.shopId);
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
		int update = ShopDao.update(this);
		LoggerService.getLogicLog().warn("保存单个proxy数据：{}", update);
		this.bitSet.clear();
		if (this.dbCache != emptyShopDbCache) {
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
	public int insert() throws SQLException {
		int insertShop = (int) ShopDao.insertShop(this);
		this.setShopId(insertShop);
		LoggerService.getLogicLog().warn("插入单个shop数据,shopId:{}",
				this.getShopId());
		this.bitSet.clear();
		if (this.dbCache != emptyShopDbCache) {
			this.dbCache.remove(this);
		}
		return insertShop;
	}

	public void bitSetClear() {
		bitSet.clear();
	}

}
