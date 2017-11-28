package com.wk.server.logic.room;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import com.wk.db.dao.RoomDao;
import com.wk.engine.config.ServerConfig;
import com.wk.logic.config.NTxt;

/**
 * 房间工具类
 * 
 * @author ems
 *
 */
public class RoomUtils {

	/**
	 * 生成一个随机语音房间id
	 * 
	 * @return
	 */
	public static String genImId() {
		return String.valueOf(Math.abs(UUID.randomUUID()
				.getMostSignificantBits()));
	}

	/**
	 * 生成一个随机房间id
	 * 
	 * @param masterId
	 *            房主id
	 * @return 房间号字符串表示
	 * @throws SQLException
	 */
	public static int genRoomId(long masterId) throws SQLException {
		if (ServerConfig.isGetroomidaccorduid()) {
			return RoomDao.createFixRoom(ServerConfig.serverId, masterId);
		} else {
			return RoomDao.createRanRoom(ServerConfig.serverId, masterId);
		}
	}

	/***
	 * 创建房间
	 * 
	 * @param pType
	 * @return
	 */
	public static RoomAbs createRoom() {
		return new Room();
	}

	/** 检测空时效任务 **/
	public static void checkNullTask(ScheduledFuture<?> task, String desc) {
		if (task != null) {
			task.cancel(true);
			if (desc != null)
				NTxt.warn(desc, 2);
		}
	}
}
