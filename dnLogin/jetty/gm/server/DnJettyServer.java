package gm.server;

import gm.server.logic.task.ServerTask;

import java.io.FileInputStream;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;

import com.jery.ngsp.server.InterfaceFactoryManager;
import com.jery.ngsp.server.log.LoggerService;
import com.jery.ngsp.server.scheduletask.ScheduleTaskManager;

public class DnJettyServer {
	private static Server server;
	//获取一个时效任务管理器
	public static final ScheduleTaskManager taskManager = InterfaceFactoryManager
			.getInterfaceFactory().getTimeTaskManager();

	public static void main(String[] args) {
		try {
			BusConfig.initLog4J();
			//这里加载了ibatis的配置
			BusConfig.init();
			//启动一个时效任务管理器
			taskManager.start("时效线程池", 4, true);
			//将一个服务器线程放入时效任务池中
			taskManager.submitTask(new ServerTask());
			Runtime.getRuntime().addShutdownHook(hook);
			/* 创建一个jetty Server*/
			server = new Server();
			/* 配置jetty */
			new XmlConfiguration(new FileInputStream(BusConfig.jettyXml))
					.configure(server);
			server.start();
			LoggerService.getLogicLog().warn("服务器启动完毕！");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static final Thread hook = new Thread(new Runnable() {
		@Override
		public void run() {
			if (server != null) {
				try {
					server.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	});
}