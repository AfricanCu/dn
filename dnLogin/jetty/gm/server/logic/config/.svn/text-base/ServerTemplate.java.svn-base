package gm.server.logic.config;

import gm.server.logic.continuation.ChargeProcessor;
import gm.server.logic.continuation.GmProcessor;
import gm.server.logic.continuation.KickProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import msg.InnerMessage.ChargeBusToGsbk;
import msg.InnerMessage.GmBusToGsbk;
import msg.InnerMessage.KickBusToGsbk;
import msg.InnerMessage.Server;
import msg.InnerMessage.ServerListCastBusToGs;
import msg.InnerMessage.ServerListCastBusToGsbk;
import msg.InnerMessage.ServerStatusBusToGs;
import msg.InnerMessage.ServerStatusBusToGsbk;

import org.json.JSONObject;

import com.googlecode.protobuf.format.JsonFormat;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.bean.SystemConstantsAbs;
import com.wk.engine.config.ServerConfigAbs;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.net.I.InnerCsConnectAbs;
import com.wk.enun.ServerStatus;
import com.wk.util.ReadUtil;
import com.wk.util.SocketUtil;

/**
 * 游戏服配置
 * 
 * 需要用到@Sharable，，因为涉及到ServerTemplate的重复开关操作
 * 
 * @author ems
 *
 */
@Sharable
public class ServerTemplate extends InnerCsConnectAbs {

	/**
	 * 游戏服务器map
	 * 
	 * <服务器id,配置>
	 */
	private static HashMap<Integer, ServerTemplate> serverMap;
	/**
	 * 服务器列表广播
	 */
	private static ServerListCastBusToGs serverListCastBG;

	/**
	 * 解析静态数据
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void init(String csvDir) throws Exception {
		List<ServerTemplate> serverList = ReadUtil.explainCsvData(csvDir,
				ServerTemplate.class, true);
		HashMap<Integer, ServerTemplate> map = new HashMap<>();
		HashMap<Integer, ArrayList<ServerTemplate>> mapByType = new HashMap<>();
		ServerListCastBusToGs.Builder serverListCastCmBuilder = ServerListCastBusToGs
				.newBuilder();
		for (ServerTemplate serverTemplate : serverList) {
			Server.Builder newBuilder = Server.newBuilder();
			newBuilder.setHost(serverTemplate.getHost());
			newBuilder.setInnerHost(serverTemplate.getInnerHost());
			newBuilder.setIsOpen(serverTemplate.isOpen());
			newBuilder.setName(serverTemplate.getName());
			newBuilder.setPort(serverTemplate.getPort());
			newBuilder.setServerId(serverTemplate.getServerId());
			newBuilder.setStatus(serverTemplate.getStatus().getType());
			newBuilder.setStrengthNeed(serverTemplate.getStrengthNeed());
			serverListCastCmBuilder.addServer(newBuilder);
			map.put(serverTemplate.getServerId(), serverTemplate);
			ArrayList<ServerTemplate> arrayList = mapByType.get(serverTemplate
					.getType());
			if (arrayList == null) {
				arrayList = new ArrayList<>();
				mapByType.put(serverTemplate.getType(), arrayList);
			}
			arrayList.add(serverTemplate);
		}
		if (serverMap != null) {

		}
		serverMap = map;
		serverListCastBG = serverListCastCmBuilder.build();
	}

	public static void getAllServerInfo(HttpServletRequest req,
			HttpServletResponse resp, JSONObject result) {
		for (ServerTemplate serverTemplate : serverMap.values()) {
			JSONObject server = new JSONObject();
			server.put("serverId", serverTemplate.getServerId());
			server.put("name", serverTemplate.getName());
			server.put("innerHost", serverTemplate.getInnerHost());
			server.put("host", serverTemplate.getHost());
			server.put("port", serverTemplate.getPort());
			server.put("isOpen", serverTemplate.isOpen());
			server.put("isCanLogin", serverTemplate.isCanLogin());
			server.put("status", serverTemplate.getStatus().getName());
			ServerStatusBusToGsbk prototmp = serverTemplate.proto;
			if (prototmp != null) {
				server.put("onlineNum", prototmp.getOnlineNum());
				server.put("roomUserNum", prototmp.getRoomUserNum());
				server.put("cacheSize", prototmp.getCacheSize());
				server.put("sessionCount", prototmp.getSessionCount());
				server.put("maxMemory", prototmp.getMaxMemory());
				server.put("totalMemory", prototmp.getTotalMemory());
				server.put("freeMemory", prototmp.getFreeMemory());
			} else {
				server.put("onlineNum", "0");
				server.put("roomUserNum", "0");
				server.put("cacheSize", "0");
				server.put("sessionCount", "0");
				server.put("maxMemory", "0");
				server.put("totalMemory", "0");
				server.put("freeMemory", "0");
			}
			result.put(serverTemplate.getServerId() + "", server);
		}
	}

	public static String log() {
		StringBuilder builder = new StringBuilder();
		for (ServerTemplate serverTemplate : serverMap.values()) {
			if (!serverTemplate.isOpen()) {
				continue;
			}
			builder.append(String.format(
					"\n服务器id：%s,服务器名：%s,host:%s,port:%s,isUpdate:%s,",
					serverTemplate.getServerId(), serverTemplate.getName(),
					serverTemplate.getHost(), serverTemplate.getPort(),
					serverTemplate.isUpdate.get()));
			ServerStatusBusToGsbk prototmp = serverTemplate.proto;
			if (prototmp != null) {
				builder.append(String.format(
						"\t在线人数:%s,在房间人数:%s,缓存数:%s,会话数:%s,状态:%s",
						prototmp.getOnlineNum(), prototmp.getRoomUserNum(),
						prototmp.getCacheSize(), prototmp.getSessionCount(),
						serverTemplate.getStatus().getName()));
			}
		}
		return builder.toString();
	}

	public static ServerTemplate getServerTemplate(int sId) {
		return serverMap.get(sId);
	}

	/**
	 * 获取可以登陆的服务器
	 * 
	 * @param sId
	 *            推荐
	 * @return
	 */
	public static ServerTemplate getCanLoginServerTemplate(int sId) {
		if (sId != SystemConstantsAbs.NoServerId) {
			ServerTemplate serverTemplate2 = getServerTemplate(sId);
			if (serverTemplate2 != null && serverTemplate2.isCanLogin()) {
				return serverTemplate2;
			}
		}
		for (ServerTemplate serverTemplate : serverMap.values()) {
			if (serverTemplate.isCanLogin()) {
				return serverTemplate;
			}
		}
		return null;
	}

	/**
	 * 获取某服可以切换的服务器
	 * 
	 * @param exceptServerT
	 *            除去这个
	 * @return
	 */
	private static ServerTemplate getBestServer(ServerTemplate exceptServerT) {
		for (ServerTemplate serverTemplate : serverMap.values()) {
			if (exceptServerT.getServerId() != serverTemplate.getServerId()
					&& serverTemplate.isCanLogin()) {
				return serverTemplate;
			}
		}
		return null;
	}

	public static HashMap<Integer, ServerTemplate> getServerMap() {
		return serverMap;
	}

	private int serverId;
	private String name;
	/** 是否对外开放 **/
	private boolean isOpen;
	/** 服务器状态 */
	private ServerStatus status;
	private String host;
	private String innerHost;
	private int port;
	/** 需要多少实力 才能进入 **/
	private int strengthNeed;
	private int type;
	// 可修改的
	private ServerStatusBusToGsbk proto;
	/**
	 * 是否已经发送了服务器列表
	 */
	public final AtomicBoolean isUpdate = new AtomicBoolean(false);

	/**
	 * 如果服务器不开放 就关闭这个连接，否则就建立连接
	 */
	public Channel checkClient() {
		if (!this.isOpen()) {
			if (client != null) {
				client.close();
			}
			return null;
		}
		if (client == null) {
			synchronized (this) {
				SocketUtil.createInnerSocketClientSync(getInnerHost(),
						getPort(), this);
			}
		}
		if (client != null && !(client.isOpen() && client.isActive())) {
			unregisterClient();
		}
		if (this.client != null && client.isOpen() && client.isActive()
				&& !this.isUpdate.get()) {
			this.sendMessage(InnerMsgId.ServerListCastBusToGs, serverListCastBG);
		}
		return client;
	}

	@Override
	public void registerClient(Channel channel) throws Exception {
		if (client != null) {
			throw new Exception(String.format(
					"严重错误！重复注册channel bus <=>serverId:%s", this.serverId));
		}
		this.client = channel;
		LoggerService.getLogicLog().error("bus <=> serverId:{} 注册连接！",
				this.serverId);
	}

	public void unregisterClient() {
		this.isUpdate.set(false);
		this.client = null;
		LoggerService.getLogicLog().error("bus <=> serverId:{} 注销连接！",
				this.serverId);
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, InnerIoMessage msg)
			throws Exception {
		if (msg != null && ctx.channel().isActive()) {
			switch (msg.getMsgId()) {
			case ServerStatusBusToGsbk:
				serverStatus((ServerStatusBusToGsbk) msg.genMessageLite());
				LoggerService.getLogicLog().error(ServerTemplate.log());
				break;
			case ServerListCastBusToGsbk:
				ServerListCastBusToGsbk serverListCastBGbk = (ServerListCastBusToGsbk) msg
						.genMessageLite();
				if (serverListCastBGbk.getCode() == NTxtAbs.SUCCESS) {
					this.isUpdate.set(true);
				} else {
					LoggerService.getLogicLog().error("发送服务器列表返回:{}",
							JsonFormat.printToString(serverListCastBGbk));
				}
				break;
			case KickBusToGsbk:
				KickProcessor.getInstance().kickbk(
						(KickBusToGsbk) msg.genMessageLite());
				break;
			case ChargeBusToGsbk:
				ChargeProcessor.getInstance().chargebk(
						(ChargeBusToGsbk) msg.genMessageLite());
				break;
			case GmBusToGsbk:
				GmProcessor.getInstance().gmbk(
						(GmBusToGsbk) msg.genMessageLite());
				break;
			default:
				break;
			}
		}
	}

	private void serverStatus(ServerStatusBusToGsbk proto) {
		if (proto.getCode() != NTxtAbs.SUCCESS) {
			LoggerService.getLogicLog().error("严重错误！serverId:{},code:{}",
					this.serverId, proto.getCode());
			return;
		}
		this.setStatus(proto.getStatus());
		this.proto = proto;
	}

	/**
	 * 请求服务器状态
	 */
	public void serverStatusReq() {
		Channel checkClient = checkClient();
		if (checkClient == null) {
			return;
		}
		ServerStatusBusToGs.Builder newBuilder = ServerStatusBusToGs
				.newBuilder();
		ServerTemplate bestServer = ServerTemplate.getBestServer(this);
		if (bestServer != null) {
			newBuilder.setOserverId(bestServer.getServerId());
			newBuilder.setOthost(bestServer.getHost());
			newBuilder.setOtport(bestServer.getPort());
			newBuilder.setOtswCode(bestServer.proto == null ? "{}"
					: bestServer.proto.getSwCode());
		} else {
			newBuilder.setOserverId(SystemConstantsAbs.NoServerId);
		}
		this.sendMessage(InnerMsgId.ServerStatusBusToGs, newBuilder);
	}

	public int getStrengthNeed() {
		return strengthNeed;
	}

	public int getServerId() {
		return serverId;
	}

	public String getName() {
		return name;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setStatus(int status) {
		ServerStatus enm = ServerStatus.getEnum(status);
		if (enm != null)
			this.status = enm;
	}

	public ServerStatus getStatus() {
		return status;
	}

	/**
	 * 是否能够登陆
	 * 
	 * 打开了，未爆满
	 * 
	 * @return
	 */
	public boolean isCanLogin() {
		return this.isOpen() && !this.status.isFull() && isUpdate.get();
	}

	/**
	 * 更新完服务器列表说明服务器正常
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return isUpdate.get();
	}

	public String getHost() {
		return host;
	}

	public String getInnerHost() {
		return innerHost;
	}

	public int getPort() {
		return port;
	}

	public int getType() {
		return type;
	}

	public String getTypeName() {
		if (ServerConfigAbs.isMonitorMessage()) {
			return this.host + ":" + this.port;
		} else
			switch (this.type) {
			case 0:
				return "新手区";
			case 1:
				return "高手区";
			default:
				return "新手区";
			}

	}

	@Override
	public String toString() {
		return this.host + " " + this.innerHost + " " + this.port;
	}

}
