package com.wk.guild.bean;

import io.netty.util.internal.ThreadLocalRandom;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import msg.GuildMessage.JulebuRecordSm;
import msg.LoginMessage.GameRecord;
import msg.RoomMessage;
import msg.RoomMessage.PlayType;

import org.json.JSONArray;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ibatis.common.beans.ClassInfo;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.bean.SystemConstantsAbs;
import com.wk.db.IbatisDbServer;
import com.wk.engine.config.LogicI;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.engine.util.KeyValueDbCache;
import com.wk.enun.DistrictType;
import com.wk.guild.dao.GuildDao;
import com.wk.guild.enm.MemberJobType;
import com.wk.mj.MjTools;
import com.wk.user.bean.UserBean;
import com.wk.util.GameDayTask;
import com.wk.util.InsertSort;

/**
 * 公会bean
 * 
 * @author ems
 *
 */
public class GuildBean {
	/** 公会id **/
	protected int id;
	/** 公会名称 */
	protected String name;
	/** 会长信息 */
	protected GuildJSONArrayEx master = new GuildJSONArrayEx(this, master_key);
	/** 公告 */
	protected String notice;
	/** 玩法 */
	protected PlayType playType;
	/** 注册时间 */
	protected Date registerTime = SystemConstantsAbs.nullDate;
	/** 最近登录服务器id */
	protected int serverId;
	/** 成员数据Map **/
	protected HashMap<Long, JSONArray> member = new HashMap<>();
	/** 申请数据 **/
	protected HashMap<Long, JSONArray> apply = new HashMap<>();
	/** 游戏记录 **/
	protected byte[] gameRecord = SystemConstantsAbs.empltyBytes;
	/** 扩展 不要给get **/
	protected GuildJSONObjectEx dbca = new GuildJSONObjectEx(this, dbca_key);
	// tmp
	/** 标记需要更新的字段 **/
	protected GuildBitSetEx bitSet = new GuildBitSetEx(64, this);
	/** 大赢家排序列表 */
	protected ArrayList<JSONArray> winnerList = new ArrayList<JSONArray>();
	/** 活跃度排序列表 */
	protected ArrayList<JSONArray> activeList = new ArrayList<JSONArray>();
	/** 游戏记录返回消息 */
	private JulebuRecordSm.Builder gameRecordSm;
	/** 俱乐部房间缓存列表 <[房间id，人数,状态]> **/
	private List<int[]> roomCacheList;
	/** 玩法描述 */
	private String playTypeDesc;
	// final 不会覆盖
	/** 玩家数据缓存定时回写器 **/
	protected final KeyValueDbCache<Integer, GuildBean> dbCache;
	// master tag member tag
	/** idtag **/
	protected static final int uid_index = 0;
	/** 昵称tag **/
	protected static final int nickname_index = 1;
	/** 职位tag **/
	protected static final int job_index = 2;
	/** 大赢家tag **/
	protected static final int winnerNum_index = 3;
	/** 活跃度tag **/
	protected static final int activeNum_index = 4;
	/** 活跃度时间tag **/
	protected static final int activeTime_index = 5;
	// dbca tag
	protected static final String prohibitIp_index = "pI";
	private static final InsertSort<JSONArray> winnerInsertSort = new InsertSort<JSONArray>(
			new Comparator<JSONArray>() {
				@Override
				public int compare(JSONArray o1, JSONArray o2) {
					if (o1.optInt(winnerNum_index) < o2.optInt(winnerNum_index)) {
						return 1;
					} else
						return -1;
				}
			});
	private static final InsertSort<JSONArray> activeInsertSort = new InsertSort<JSONArray>(
			new Comparator<JSONArray>() {
				@Override
				public int compare(JSONArray o1, JSONArray o2) {
					if (o1.optInt(activeNum_index) < o2.optInt(activeNum_index)) {
						return 1;
					} else
						return -1;
				}
			});
	protected static final Class<?> clazz = GuildBean.class;
	// 自动生成开始
	// bitSet key
	private static final int id_key = 0;
	private static final int name_key = 1;
	private static final int master_key = 2;
	private static final int notice_key = 3;
	private static final int playType_key = 4;
	private static final int registerTime_key = 5;
	private static final int serverId_key = 6;
	private static final int member_key = 7;
	private static final int apply_key = 8;
	private static final int gameRecord_key = 9;
	private static final int dbca_key = 10;

	// 自动生成结束

	public GuildBean() {
		this(KeyValueDbCache.emptyGuildDbCache);
	}

	/**
	 * 
	 * @param dbCache
	 *            数据缓存定时回写器
	 */
	public GuildBean(KeyValueDbCache<Integer, GuildBean> dbCache) {
		this.dbCache = dbCache;
	}

	/***
	 * 重置数据
	 */
	public void reset() {
		// TODO Auto-generated method stub
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
	public void overWrite(GuildBean bean) throws Exception {
		if (bean == null || bean.getClass() != clazz) {
			throw new Exception(String.format("not %s : %s", clazz, bean));
		}
		// ！！！ 易主
		bean.master.guildBean = this;
		bean.dbca.guildBean = this;
		bean.bitSet.guildBean = this;
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())
					&& !Modifier.isFinal(field.getModifiers())) {
				field.set(this, field.get(bean));
			}
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.bitSet.set(name_key);
	}

	public String getMaster() {
		return master.toString();
	}

	public void setMaster(String master) {
		if (master != null) {
			this.master = new GuildJSONArrayEx(this, master_key, master);
		}
		this.bitSet.set(master_key);
	}

	public void initMaster(long uid, String name) {
		this.master.put(uid_index, uid).put(nickname_index, name);
		JSONArray initJSONArray = this.initJSONArray(uid, name,
				MemberJobType.huiZhang);
		this.putMember(uid, initJSONArray);
		this.bitSet.set(member_key);
	}

	public long getMasterUid() {
		return this.master.optLong(uid_index);
	}

	public String getMasterNickName() {
		return this.master.getString(nickname_index);
	}

	public boolean isMaster(long uid) {
		return uid == this.getMasterUid();
	}

	public String getNotice() {
		return this.notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
		this.bitSet.set(notice_key);
	}

	public byte[] getPlayType() {
		return playType.toByteArray();
	}

	public RoomMessage.PlayType gPlayType() {
		return playType;
	}

	public void setPlayType(byte[] playType) throws Exception {
		this.setPlayType(PlayType.newBuilder().mergeFrom(playType).build());
	}

	public void setPlayType(PlayType playType) throws Exception {
		this.playType = playType;
		this.playTypeDesc = LogicI.getInstance().getPlayTypeDesc(playType);
		this.bitSet.set(playType_key);
	}

	public String getPlayTypeDesc() {
		return this.playTypeDesc;
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

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
		this.bitSet.set(serverId_key);
	}

	public String getMember() {
		StringBuilder builder = new StringBuilder();
		for (Entry<Long, JSONArray> b : member.entrySet()) {
			builder.append(b.getKey()).append(SystemConstantsAbs.sharp_SEP)
					.append(b.getValue().toString())
					.append(SystemConstantsAbs.sharp_SEP);
		}
		return builder.toString();
	}

	public void setMember(String member) {
		if (member != null && !member.trim().equals("")) {
			String[] split = member.split(SystemConstantsAbs.sharp_SEP);
			for (int i = 0; i < split.length; i += 2) {
				long uid = Long.parseLong(split[i]);
				JSONArray jsonArray = new JSONArray(split[i + 1]);
				this.putMember(uid, jsonArray);
			}
		}
		this.bitSet.set(member_key);
	}

	private void putMember(long uid, JSONArray jsonArray) {
		this.member.put(uid, jsonArray);
		insertWinner(jsonArray);
		insertActive(jsonArray);
	}

	/**
	 * 成为成员
	 * 
	 * @param uid
	 * @return 成功失败
	 */
	public boolean beenMember(long uid) {
		if (uid == this.getMasterUid()) {
			return false;
		}
		JSONArray object = this.removeApply(uid);
		if (object == null) {
			return false;
		}
		object.put(job_index, MemberJobType.chengYuan.getType());
		object.put(winnerNum_index, 0);
		this.putMember(uid, object);
		this.bitSet.set(member_key);
		return true;
	}

	/**
	 * 是否会长或副会长或成员
	 * 
	 * @param uid
	 * @return
	 */
	public boolean isMasterOrAssistOrMember(long uid) {
		return isMaster(uid) || this.member.containsKey(uid);
	}

	/**
	 * 是否会长或副会长
	 * 
	 * @param uid
	 * @return
	 */
	public boolean isMasterOrAssist(long uid) {
		return isMaster(uid) || isAssist(uid);
	}

	/**
	 * 是否副会长
	 * 
	 * @param uid
	 * @return
	 */
	public boolean isAssist(long uid) {
		JSONArray array = getMember(uid);
		return array != null
				&& array.optInt(job_index) == MemberJobType.fuhuiZhang
						.getType();
	}

	/**
	 * 是否成员
	 * 
	 * @param uid
	 * @return
	 */
	public boolean isMember(long uid) {
		return this.member.containsKey(uid) && !this.isMasterOrAssist(uid);
	}

	/**
	 * 是否副会长或成员
	 * 
	 * @param uid
	 * @return
	 */
	public boolean isAssistOrMember(long uid) {
		return !this.isMaster(uid) && this.member.containsKey(uid);
	}

	/**
	 * 改变职业
	 * 
	 * @param uid
	 * @param jobType
	 * @return
	 */
	public boolean changeJob(long uid, MemberJobType jobType) {
		if (jobType == null)
			return false;
		JSONArray opt = getMember(uid);
		if (opt == null)
			return false;
		opt.put(job_index, jobType.getType());
		this.bitSet.set(member_key);
		return true;
	}

	/**
	 * 获取成员职位
	 * 
	 * @param uid
	 * @return
	 */
	public int getMemberJob(long uid) {
		if (isMaster(uid)) {
			return MemberJobType.huiZhang.getType();
		}
		JSONArray opt = getMember(uid);
		return opt == null ? SystemConstantsAbs.NoJob : opt.optInt(job_index);
	}

	public JSONArray getMember(long uid) {
		return this.member.get(uid);
	}

	public void changeMemberWinNum(long uid, int num) {
		JSONArray jsonArray = this.getMember(uid);
		if (jsonArray == null) {
			LoggerService.getPlatformLog().error("找不到成员！uid:{}", uid);
			return;
		}
		int winnerNum = jsonArray.optInt(winnerNum_index) + num;
		if (winnerNum < 0) {
			LoggerService.getPlatformLog()
					.error("数据错误！uid:{},num:{}", uid, num);
			return;
		}
		jsonArray.put(winnerNum_index, winnerNum);
		insertWinner(jsonArray);
	}

	/**
	 * 累加活跃度
	 * 
	 * @param uid
	 */
	public void addMemberActiveNum(long uid) {
		JSONArray jsonArray = this.getMember(uid);
		if (jsonArray == null) {
			LoggerService.getPlatformLog().error("找不到成员！uid:{}", uid);
			return;
		}
		if (GameDayTask.isWeekChange(jsonArray.optLong(activeTime_index))) {
			jsonArray.put(activeNum_index, 0);
			jsonArray.put(activeTime_index, System.currentTimeMillis());
		}
		int activeNum = jsonArray.optInt(activeNum_index) + 1;
		jsonArray.put(activeNum_index, activeNum);
		insertActive(jsonArray);
	}

	public void refreshMemberActiveNum() {
		boolean isNeedRefresh = false;
		for (JSONArray jsonArray : activeList) {
			if (GameDayTask.isWeekChange(jsonArray.optLong(activeTime_index))) {
				jsonArray.put(activeNum_index, 0);
				jsonArray.put(activeTime_index, System.currentTimeMillis());
				isNeedRefresh = true;
			}
		}
		if (isNeedRefresh)
			Collections.sort(activeList, activeInsertSort.getComparator());
	}

	private void insertWinner(JSONArray jsonArray) {
		winnerList.remove(jsonArray);
		if (jsonArray.optInt(winnerNum_index) > 0) {
			winnerInsertSort.insert(this.winnerList, jsonArray);
		}
	}

	private void insertActive(JSONArray jsonArray) {
		activeList.remove(jsonArray);
		activeInsertSort.insert(this.activeList, jsonArray);
	}

	public int getMemberSize() {
		return this.member.size();
	}

	public int getWinnerSize() {
		return this.winnerList.size();
	}

	public int getActiveSize() {
		return this.activeList.size();
	}

	/***
	 * 移除成员
	 * 
	 * @param uid
	 * @return
	 */
	public JSONArray removeMember(long uid) {
		if (uid == this.getMasterUid()) {
			return null;
		}
		JSONArray remove = this.member.remove(uid);
		this.winnerList.remove(remove);
		this.activeList.remove(remove);
		this.bitSet.set(member_key);
		return remove;
	}

	public boolean levelupMember(long uid, MemberJobType jobType) {
		if (jobType == MemberJobType.huiZhang) {
			return false;
		}
		if (uid == this.getMasterUid()) {
			return false;
		}
		JSONArray jsonArray = this.member.get(uid);
		jsonArray.put(job_index, jobType.getType());
		return true;
	}

	public String getApply() {
		StringBuilder builder = new StringBuilder();
		for (Entry<Long, JSONArray> b : apply.entrySet()) {
			builder.append(b.getKey()).append(SystemConstantsAbs.sharp_SEP)
					.append(b.getValue().toString())
					.append(SystemConstantsAbs.sharp_SEP);
		}
		return builder.toString();
	}

	public void setApply(String apply) {
		if (apply != null && !apply.trim().equals("")) {
			String[] split = apply.split(SystemConstantsAbs.sharp_SEP);
			for (int i = 0; i < split.length; i += 2) {
				this.apply.put(Long.parseLong(split[i]), new JSONArray(
						split[i + 1]));
			}
		}
		this.bitSet.set(apply_key);
	}

	/**
	 * 申请
	 * 
	 * @param uid
	 * @return
	 */
	public boolean apply(long uid, String name) {
		if (this.isMasterOrAssistOrMember(uid)) {
			return false;
		}
		if (isApply(uid)) {
			return false;
		}
		this.apply.put(uid, initJSONArray(uid, name, null));
		this.bitSet.set(apply_key);
		return true;
	}

	public DistrictType getDistrictType() {
		return DistrictType.yuanjiang;
	}

	private JSONArray initJSONArray(long uid, String name, MemberJobType job) {
		return new JSONArray()
				.put(uid_index, uid)
				.put(nickname_index, name)
				.put(job_index,
						job == null ? SystemConstantsAbs.NoJob : job.getType())
				.put(winnerNum_index, 0);
	}

	/**
	 * 是否申请
	 * 
	 * @param uid
	 * @return
	 */
	public boolean isApply(long uid) {
		return this.apply.containsKey(uid);
	}

	public JSONArray removeApply(long uid) {
		JSONArray remove = this.apply.remove(uid);
		this.bitSet.set(apply_key);
		return remove;
	}

	public byte[] getGameRecord() {
		return getGameRecordSm().build().toByteArray();
	}

	public void setGameRecord(byte[] gameRecord)
			throws InvalidProtocolBufferException {
		if (gameRecord == null) {
			return;
		}
		this.gameRecord = gameRecord;
		this.getGameRecordSm().mergeFrom(this.gameRecord);
		this.bitSet.set(gameRecord_key);
	}

	/**
	 * 记录游戏结果
	 * 
	 * @param record
	 * @param checkId
	 */
	public void recordGame(GameRecord record) {
		if (this.getGameRecordSm().getRecordCount() >= 1000) {
			this.getGameRecordSm().removeRecord(0);
		}
		this.getGameRecordSm().addRecord(record);
		this.bitSet.set(gameRecord_key);
	}

	private JulebuRecordSm.Builder getGameRecordSm() {
		if (this.gameRecordSm == null) {
			this.gameRecordSm = JulebuRecordSm.newBuilder()
					.setCode(NTxtAbs.SUCCESS).setId(this.getId());
		}
		return this.gameRecordSm;
	}

	public String getDbca() {
		return this.dbca.toString();
	}

	public void setDbca(String dbca) {
		if (dbca != null) {
			this.dbca = new GuildJSONObjectEx(this, dbca_key, dbca);
		}
		this.bitSet.set(dbca_key);
	}

	public void setProhibitIp(boolean prohibitIp) {
		this.dbca.put(prohibitIp_index, prohibitIp);
	}

	/**
	 * 是否禁止同ip加入房间
	 * 
	 * @return
	 */
	public boolean getProhibitIp() {
		return this.dbca.optBoolean(prohibitIp_index, false);
	}

	/**
	 * 俱乐部房间缓存列表 <[房间id，人数,状态]>
	 * 
	 * @return
	 */
	public List<int[]> getRoomCacheList() {
		if (roomCacheList == null) {
			ArrayList<int[]> list = new ArrayList<>();
			for (int i = 0; i < MjTools.getGuildCreateRoomMax(); i++) {
				list.add(new int[] { SystemConstantsAbs.NoRoomId, 0, 0 });
			}
			roomCacheList = Collections.synchronizedList(list);
		}
		return roomCacheList;
	}

	/**
	 * 
	 * @param num
	 *            房间编号
	 * @return 房间ID
	 */
	public int getRoomId(int num) {
		return getRoomCacheList().get(num - 1)[0];
	}

	/**
	 * 设置房间ID
	 * 
	 * @param num
	 *            房间编号
	 * @param roomId
	 *            房间ID
	 */
	public void setRoomId(int num, int roomId) {
		this.getRoomCacheList().get(num - 1)[0] = roomId;
	}

	/**
	 * 设置房间在线人数
	 * 
	 * @param num
	 *            房间编号
	 * @param roomNum
	 *            房间人数
	 * @param status
	 *            状态
	 */
	public void setRoomNum(int num, int roomNum, int status) {
		int[] arr = this.getRoomCacheList().get(num - 1);
		arr[1] = roomNum;
		arr[2] = status;
	}

	/**
	 * 不能移除公会
	 * 
	 * @return
	 */
	public boolean cannotRemove() {
		for (int[] roomId : this.roomCacheList) {
			if (roomId[0] != SystemConstantsAbs.NoRoomId) {
				return false;
			}
		}
		return true;
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
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PreparedStatement createPreparedStatement(Connection conn)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (this.bitSet.isEmpty()) {
			return null;
		}
		ClassInfo info = ClassInfo.getInstance(clazz);
		ArrayList<Object> parameters = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder builder = new StringBuilder("update guild set");
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
		builder.append(" where `id` = ? ;");
		parameters.add(this.id);
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
		int update = GuildDao.update(this);
		if (ServerConfigAbs.isMonitorPrint())
			LoggerService.getLogicLog().debug("保存单个公会数据：{}", update);
		this.bitSet.clear();
		if (this.dbCache != KeyValueDbCache.emptyGuildDbCache) {
			this.dbCache.remove(this);
		}
	}

	/**
	 * 插入玩家数据到数据库
	 * 
	 * @throws SQLException
	 */
	public void insert() throws SQLException {
		this.id = GuildDao.insertGuild(this);
		if (ServerConfigAbs.isMonitorPrint())
			LoggerService.getLogicLog().debug("插入单个公会数据,masterUid:{},自增ID:{}",
					this.getMasterUid(), this.getId());
		this.bitSet.clear();
		if (this.dbCache != KeyValueDbCache.emptyGuildDbCache) {
			this.dbCache.remove(this);
		}
	}

	public void bitSetClear() {
		bitSet.clear();
	}

	public int getApplySize() {
		return this.apply.size();
	}

}