package com.wk.engine.db;

import com.jery.ngsp.server.scheduletask.type.SecondTask;
import com.wk.engine.ModuleAbs;
import com.wk.engine.ModuleManager;
import com.wk.util.TimeTaskUtil;

/**
 * 数据缓存管理
 * 
 * @author ems
 *
 */
public class DbCacheManger {

	/**
	 * 创建数据缓存回写时效
	 * 
	 * 1.sql db
	 * 
	 * 2.redis db
	 */
	public static void createAndSubmit() {
		for (final ModuleAbs<?, ?> module : ModuleManager.getModuleList()) {
			if (module.isBackDb()) {
				TimeTaskUtil.getTaskmanager().submitTask(
						new SecondTask(module.getBackDbTimeInSecond()) {
							@Override
							public void run() {
								try {
									module.backDb();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
			}
		}
	}

	public static void shutdown() {
		for (final ModuleAbs<?, ?> module : ModuleManager.getModuleList()) {
			if (module.isBackDb()) {
				try {
					module.backDb();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
