package com.wk.db.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.wk.bean.RoomBean;
import com.wk.bean.SystemConstantsAbs;
import com.wk.db.IbatisDbServer;
import com.wk.engine.config.ServerConfigAbs;

public class RoomDao {

	/**
	 * 随机创建一个房间
	 * 
	 * @param serverId
	 *            服务器id
	 * @param masterUid
	 *            创建者Id
	 * @return 房号
	 * @throws SQLException
	 */
	public static int createRanRoom(int serverId, long masterUid)
			throws SQLException {
		int roomId = (Integer) IbatisDbServer.getGmSqlMapper().queryForObject(
				"room.ranRoomId");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sId", serverId);
		paramMap.put("masterId", masterUid);
		paramMap.put("roomId", roomId);
		int update = IbatisDbServer.getGmSqlMapper().update("room.updateRoom",
				paramMap);
		if (update == 1) {
			return roomId;
		} else
			throw new SQLException("创建房间失败！！！！！！");
	}

	/**
	 * 优先房主UID作为房间id创建一个房间，否则随机房间
	 * 
	 * @param serverId
	 *            服务器id
	 * @param masterUid
	 *            创建者Id
	 * @return 房号
	 * @throws SQLException
	 */
	public static int createFixRoom(int serverId, long masterUid)
			throws SQLException {
		int roomId = (int) masterUid % ServerConfigAbs.getRoomidtoplimitation();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sId", serverId);
		paramMap.put("masterId", masterUid);
		paramMap.put("roomId", roomId);
		int update = IbatisDbServer.getGmSqlMapper().update("room.updateRoom",
				paramMap);
		if (update == 1) {
			return roomId;
		} else {
			return createRanRoom(serverId, masterUid);
		}

	}

	/**
	 * 归还房间
	 * 
	 * @param roomId
	 * @return
	 * @throws SQLException
	 */
	public static int resetRoom(int roomId) throws SQLException {
		return IbatisDbServer.getGmSqlMapper().update("room.resetRoom", roomId);
	}

	/**
	 * 通过房间id查服务器id
	 * 
	 * @return 找不到返回{@link Statics.<code>No_ServerId</code>}
	 * @throws SQLException
	 */
	public static int serverIdByRoomId(int roomId) throws SQLException {
		Integer serverId = (Integer) IbatisDbServer.getGmSqlMapper()
				.queryForObject("room.serverIdByRoomId", roomId);
		if (serverId == null) {
			return SystemConstantsAbs.NoServerId;
		}
		return serverId.intValue();
	}

	/**
	 * 使用房间备份
	 * 
	 * @param masterUid
	 * @return
	 * @throws SQLException
	 */
	public static int updateRoomBack(byte[] back, int roomId, int serverId)
			throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("back", back);
		paramMap.put("roomId", roomId);
		paramMap.put("sId", serverId);
		return IbatisDbServer.getGmSqlMapper().update("room.updateRoomBack",
				paramMap);
	}

	/**
	 * 服务器id查询房间列表
	 * 
	 * @param serverId
	 * @return
	 * @throws SQLException
	 */
	public static List<RoomBean> roomsByServerId(int serverId)
			throws SQLException {
		return IbatisDbServer.getGmSqlMapper().queryForList(
				"room.roomsByServerId", serverId);
	}

	/**
	 * 归还老房间
	 * 
	 * @param serverid
	 * 
	 * @param roomId
	 * @return
	 * @throws SQLException
	 */
	private static int resetOldRooms(int serverid) throws SQLException {
		return IbatisDbServer.getGmSqlMapper().update("room.resetOldRooms",
				serverid);
	}

	/**
	 * 更新房间所属房主
	 * 
	 * @param masterUid
	 * @return
	 * @throws SQLException
	 */
	private static int updateRoomMasterUid(long masterUid, int serverId,
			int roomId, long oldMasterUid) throws SQLException {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("masterId", masterUid);
		paramMap.put("roomId", roomId);
		paramMap.put("serverId", serverId);
		paramMap.put("oldMasterUid", oldMasterUid);
		return IbatisDbServer.getGmSqlMapper().update(
				"room.updateRoomMasterUid", paramMap);
	}

}
