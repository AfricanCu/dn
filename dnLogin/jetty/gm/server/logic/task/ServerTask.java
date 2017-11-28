package gm.server.logic.task;

import gm.server.logic.config.ServerTemplate;

import com.jery.ngsp.server.log.LoggerService;
import com.jery.ngsp.server.scheduletask.type.SecondTask;

public class ServerTask extends SecondTask {

	public ServerTask() {
		super(2, 30);
	}

	@Override
	public void run() {
		try {
			for (ServerTemplate serverTemplate : ServerTemplate.getServerMap()
					.values()) {
				if (!serverTemplate.isOpen()) {
					continue;
				}
				serverTemplate.serverStatusReq();
			}
			LoggerService.getLogicLog().error(ServerTemplate.log());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
