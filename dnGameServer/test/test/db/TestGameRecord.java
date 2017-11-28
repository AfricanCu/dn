package test.db;

import msg.LoginMessage.GameRecord;
import msg.LoginMessage.GameRecord.Builder;
import msg.LoginMessage.PlayerRecordCast;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.server.ibatis.select.User;
import com.wk.server.logic.login.LoginModule;
import com.wk.server.logic.login.UserManager;
import com.wk.user.dao.UserDao;
import com.wk.util.FileProcessor;

public class TestGameRecord {
	public static void main(String[] args) throws Exception {
		ServerConfig.init();
		User reLoadUser = UserManager.getInstance().reLoadUser(200037);
		msg.LoginMessage.PlayerRecordCast.Builder mergeFrom = PlayerRecordCast
				.newBuilder().mergeFrom(reLoadUser.getGameRecord());
		System.err.println(mergeFrom.build());
	}
}
