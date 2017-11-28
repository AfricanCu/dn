package com.wk.server.logic.task;

import com.jery.ngsp.server.scheduletask.type.SecondTask;
import com.wk.server.logic.rank.RedisUtil;
import com.wk.util.TimeTaskUtil;

/**
 * 游戏任务
 * 
 * @author ems
 *
 */
public class GameTask extends SecondTask {
	public static final GameTask instance = new GameTask();

	private GameTask() {
		super(30);
	}

	@Override
	public void run() {
		try {
			RedisUtil.log();
		} catch (Exception e) {
			e.printStackTrace();
		}
		TimeTaskUtil.getTaskmanager().log();

	}
}
