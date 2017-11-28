package com.jery.ngsp.server.impl;

import com.jery.ngsp.server.InterfaceFactory;
import com.jery.ngsp.server.dirtyword.DirtyWordsManager;
import com.jery.ngsp.server.dirtyword.impl.DirtyWordsManagerImpl;
import com.jery.ngsp.server.scheduletask.ScheduleTaskManager;
import com.jery.ngsp.server.scheduletask.impl.JavaScheduleTaskManager;

public class InterfaceFactoryImpl implements InterfaceFactory {

	public ScheduleTaskManager getTimeTaskManager() {
		return JavaScheduleTaskManager.getInstance();
	}

	@Override
	public DirtyWordsManager getDirtyWordsManager() {
		return DirtyWordsManagerImpl.getInstance();
	}

}
