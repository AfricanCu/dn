package com.wk.server;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;

import java.util.List;

import redis.clients.jedis.JedisPool;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.RoomBean;
import com.wk.db.dao.RoomDao;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.db.DbCacheManger;
import com.wk.engine.event.EventManager;
import com.wk.engine.event.EventType;
import com.wk.engine.inner.BusConnect;
import com.wk.engine.inner.BusSysModule;
import com.wk.engine.inner.GsSysModule;
import com.wk.engine.net.handler.ServerChannelInitializer;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.MsgId;
import com.wk.server.logic.rank.RedisUtil;
import com.wk.server.logic.room.RoomManager;
import com.wk.server.logic.task.GameTask;
import com.wk.util.GameDayTask;
import com.wk.util.TimeTaskUtil;

/**
 * 服务器
 */
public class DnServer {

	private static EventLoopGroup bossGroup = null;
	private static EventLoopGroup workerGroup = null;
	private static ChannelFuture sync = null;
	private static boolean ok;
	private static final Thread thread = new Thread("钩子") {
		public void run() {
			hook.run();
		};
	};

	public static void main(String[] args) {
		try {
			ServerConfig.init();
			MsgId.CreateRoomCm.gRErrMsg(NTxt.ALREADY_IN_ROOM);
			netRun();
			TimeTaskUtil.getTaskmanager().start("时效线程池", 20, true);
			TimeTaskUtil.getTaskmanager().submitTask(GameTask.instance);
			TimeTaskUtil.getTaskmanager().submitTask(GameDayTask.gameDayTask);
			DbCacheManger.createAndSubmit();
			Runtime.getRuntime().addShutdownHook(thread);
			List<RoomBean> roomList = RoomDao
					.roomsByServerId(ServerConfig.serverId);
			LoggerService.getLogicLog()
					.warn("由于上次宕机备份的房间数:{}", roomList.size());
			for (RoomBean room : roomList) {
				RoomManager.reloadRoom(room);
			}
			ok = true;
			BusConnect.getInstance().checkClient();
		} catch (Exception e) {
			e.printStackTrace();
			hook.run();
		}
	}

	public static final Runnable hook = new Runnable() {
		@Override
		public void run() {
			try {
				DnServer.ok = false;
				Runtime.getRuntime().removeShutdownHook(thread);
				NTxt.println("移除hook完成！");
				if (BusSysModule.getInstance() != null)
					BusSysModule.getInstance().shutdown();
				if (GsSysModule.getInstance() != null)
					GsSysModule.getInstance().shutdown();
				if (EventManager.getInstance() != null) {
					EventManager.getInstance().processEvent(EventType.ShutDown,
							null);
					NTxt.println("执行ShutDown时间完成！");
				}
				DbCacheManger.shutdown();
				NTxt.println("关闭数据库回写缓存完成！");
				if (DnServer.sync != null) {
					ChannelFuture syncUninterruptibly = DnServer.sync.channel()
							.close().syncUninterruptibly();
					LoggerService.getLogicLog().error("关闭netty完成：{} {}",
							syncUninterruptibly.isSuccess(),
							syncUninterruptibly.isDone());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					TimeTaskUtil.getTaskmanager().shutdown(10);
					JedisPool pool = RedisUtil.getJedisPool();
					if (pool != null) {
						pool.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		}
	};

	public static void netRun() throws Exception {
		bossGroup = new NioEventLoopGroup(ServerConfig.bossGroupNThreads,
				new DefaultThreadFactory("网络层 boss"));// (1)
		workerGroup = new NioEventLoopGroup(ServerConfig.workerGroupNThreads,
				new DefaultThreadFactory("网络层 worker"));
		try {
			ServerBootstrap b = new ServerBootstrap();// (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					// 处理请求
					.handler(new LoggingHandler(LogLevel.INFO))
					// 处理通道
					.childHandler(new ServerChannelInitializer())
					.option(ChannelOption.SO_BACKLOG, 1024)// (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
			sync = b.bind(ServerConfig.serverIp, ServerConfig.serverPort)
					.sync();// (7)
			LoggerService
					.getLogicLog()
					.warn("{}:{} netty启动ok！bossGroupNThreads：{},workerGroupNThreads:{}",
							new Object[] { ServerConfig.serverIp,
									ServerConfig.serverPort,
									ServerConfig.bossGroupNThreads,
									ServerConfig.workerGroupNThreads });
		} catch (Exception e) {
			Future<?> bossGroupSync = bossGroup.shutdownGracefully().sync();
			LoggerService.getLogicLog().error("bossGroup关闭！成功：{} 完成：{}",
					bossGroupSync.isSuccess(), bossGroupSync.isDone());
			Future<?> workerGroupSync = workerGroup.shutdownGracefully().sync();
			LoggerService.getLogicLog().error("workerGroup关闭！成功：{} 完成：{}",
					workerGroupSync.isSuccess(), workerGroupSync.isDone());
			throw e;
		}
	}

	public static boolean isOk() {
		return ok;
	}

}
