package com.jery.ngsp.server;

import com.jery.ngsp.server.dirtyword.DirtyWordsManager;
import com.jery.ngsp.server.scheduletask.ScheduleTaskManager;

public interface InterfaceFactory {

	public ScheduleTaskManager getTimeTaskManager();

	public DirtyWordsManager getDirtyWordsManager();
}