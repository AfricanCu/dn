package test.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.internal.ThreadLocalRandom;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import msg.BullMessage.BankerCast;
import msg.BullMessage.BetOnCast;
import msg.BullMessage.BullResultCast;
import msg.LoginMessage.LoginCm;
import msg.LoginMessage.SwLoginCm;
import msg.RoomMessage.JoinRoomBeforeSm;
import msg.RoomMessage.SwServer;

import org.json.JSONObject;

import test.client.util.NoticeTextTemplate;

import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.googlecode.protobuf.format.JsonFormat;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.bean.NTxtAbs;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.config.SystemConstants;
import com.wk.engine.net.InnerIoMessage;
import com.wk.engine.net.InnerMsgId;
import com.wk.engine.net.IoMessage;
import com.wk.engine.net.I.ChannelAttachmentAbs;
import com.wk.engine.net.util.ChannelAttachment;
import com.wk.enun.PlatformType;
import com.wk.logic.config.NTxt;
import com.wk.logic.config.StaticDataManager;
import com.wk.logic.enm.GameState;
import com.wk.logic.enm.MsgId;
import com.wk.logic.enm.SwType;
import com.wk.util.HttpCommonTools;
import com.wk.util.NettyClient;
import com.wk.util.TimeTaskUtil;

/**
 *
 * @author Administrator
 */
public class TestClient {
	static {
		URL configURL = LoggerService.class.getClassLoader().getResource(
				"test/client/gameServerLog4j.properties");
		LoggerService.init(configURL);
		System.err.println(ServerConfig.ADDR);
		TimeTaskUtil.getTaskmanager().start("robot", 25, true);
		try {
			StaticDataManager.loadData();
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | IllegalAccessException
				| InstantiationException | UnsupportedLookAndFeelException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
		}
	}
	public static final Properties properties = new Properties();
	/** nickname */
	public static final String nicknameIndex = "nicknameIndex";
	/** 座位号 */
	public static final String seatIndex = "seatIndex";
	/** 玩家名 **/
	public static final String uidIndex = "nameIndex";
	/** 这是第几个连接下标 */
	public static final String item_index = "itemIndex";
	/** 切服缓存 **/
	public static final String swRoomIdIndex = "swRoomIdIndex";
	/** 客户端类型 */
	public static final String channelType = "channelType";
	/** puid **/
	public static final String puidIndex = "puid";
	/** roomId **/
	public static final String tmpRoomIdIndex = "tmpRoomId";
	/** roomId **/
	public static final String roomIdIndex = "roomId";
	/***/
	public static final String bankerSeetIndex = "bankerSeetIndex";
	/****/
	public static final String cutSeetIndex = "cutSeetIndex";
	/****/
	public static final String gameStateIndex = "gameStateIndex";
	/** channel缓存 */
	public static final AttributeKey<HashMap<String, Object>> MAP_Attr = new AttributeKey<HashMap<String, Object>>(
			HashMap.class.getName());
	/** 连接队列 **/
	public static final ConcurrentHashSetExChannel list = new ConcurrentHashSetExChannel();

	/** 当前选中的客户端 */
	private static Channel currentChannel;

	public static void main(String[] args) {
		try {
			URL resource = TestClient.class.getClassLoader().getResource(
					"test.properties");
			properties.load(resource.openStream());
			//调整此窗口的大小，以适合其子组件的首选大小和布局。
			ClientFrame.clientFrame.pack();
			//将指定的框放到屏幕中间
			RefineryUtilities.centerFrameOnScreen(ClientFrame.clientFrame);
			ClientFrame.clientFrame.setVisible(true);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Channel connect(String url, String puid, ChannelType chType) {
		try {
			JSONObject loginJson = TestClient.login(puid, url);
			if (loginJson != null) {
				int code = loginJson.optInt("code");
				if (code == NTxt.SUCCESS) {
					long uid = loginJson.optLong("uid");
					JSONObject targetJSON = loginJson.optJSONObject("target");
					String host = targetJSON.optString("serverIp");
					int port = targetJSON.optInt("port");
					LoginCm.Builder loginCm = LoginCm.newBuilder();
					loginCm.setUid(uid);
					loginCm.setSessionCode(targetJSON.optString("sessionCode"));
					loginCm.setLoginTime(targetJSON.optString("loginTime"));
					Channel channel = NettyClient.createOuterSocketClientSync(
							host, port, new MessageInboundHandler(uid, puid,
									chType));
					MessageImpl.sendMessage(channel, MsgId.LoginCm, loginCm);
					return channel;
				} else {
					LoggerService.getLogicLog().error(loginJson.toString());
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return null;
		}
	}
	// {"s":2,"sLSC":"{\"uid\":1318,\"accessTime\":1496588995000,\"refreshTime\":1496588995000}"}
	public static JSONObject login(String puid, String url) {
		try {
			String sendHttp = null;
			if (ClientFrame.clientFrame.sessionCodeCheck.isSelected()) {
				sendHttp = HttpCommonTools
						.sendHttp(
								url,
								String.format(
										"type=%s&sessionCode=%s&platformId=%s&deviceId=%s",
										"login", URLEncoder.encode(
												new JSONObject(puid)
														.getString("sLSC"),
												"utf-8"), PlatformType.pc                            
												.getType(), "xx"));
			} else {
				sendHttp = HttpCommonTools.sendHttp(url, String.format(
						"type=%s&code=%s&platformId=%s&deviceId=%s", "login",
						URLEncoder.encode(puid, "utf-8"),
						PlatformType.pc.getType(), "xx"));
			}
			JSONObject jsonObject = new JSONObject(sendHttp);
			LoggerService.getLogicLog().error(jsonObject.toString());
			int code = jsonObject.optInt("code");
			if (code != NTxt.SUCCESS) {
				LoggerService.getLogicLog().error("登录失败！！！{}", code);
			}
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Item showDialog(String title, List<Item> list, int index) {
		if (ClientFrame.clientFrame.ranAreaCheck.isSelected()) {
			return list.get(ThreadLocalRandom.current().nextInt(list.size()));
		} else {
			int se = (Integer) ClientFrame.clientFrame.areaComboBox
					.getSelectedItem();
			if (se > 0 && se <= list.size()) {
				return list.get(se - 1);
			}
		}
		list.add(0, Item.NullItem);
		final JDialog dialog = new JDialog(ClientFrame.clientFrame, title, true);// JOptionPane.getRootFrame()
		dialog.setLocationRelativeTo(ClientFrame.clientFrame);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JLabel label = new JLabel(title);
		dialog.add(label);
		JComboBox<Item> comboBox = new JComboBox<>(list.toArray(new Item[list
				.size()]));
		comboBox.setSelectedIndex(index);
		dialog.add(comboBox);
		ItemListener aListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					JComboBox<?> jc = ((JComboBox<?>) e.getSource());
					int index = jc.getSelectedIndex();
					if (index != 0) { // ==0表示选中的是第一个空项
						dialog.setVisible(false);
					}
				}
			}
		};
		comboBox.addItemListener(aListener);
		dialog.pack();
		dialog.setVisible(true);
		dialog.dispose();
		return (Item) comboBox.getSelectedItem();
	}

	public static class MessageInboundHandler extends
			SimpleChannelInboundHandler<IoMessage> {

		private boolean threadBreak = false;
		private long uName;
		private final ChannelType chType;
		private final String puid;

		public MessageInboundHandler(long uName, String puid, ChannelType chType) {
			this.uName = uName;
			this.chType = chType;
			this.puid = puid;
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			list.remove(ctx.channel());
			this.threadBreak = true;
			HashMap<String, Object> map = ctx.channel().attr(MAP_Attr).get();
			LoggerService.getLogicLog().error("断线{}！！！！！！！！！！！！{}",
					map.get(uidIndex), map.get(nicknameIndex));
			JoinRoomBeforeSm joinRoomBeforeSm = (JoinRoomBeforeSm) map
					.get(swRoomIdIndex);
			if (joinRoomBeforeSm != null) {
				SwServer sw = joinRoomBeforeSm.getSw();
				long userName = (long) map.get(uidIndex);
				SwLoginCm.Builder swLoginCm = SwLoginCm.newBuilder();
				swLoginCm.setUid(userName);
				swLoginCm.setSwCode(sw.getMyCode());
				swLoginCm.setSId(sw.getSId());
				swLoginCm.setType(SwType.joinRoom.getType());
				try {
					Channel channel = NettyClient.createOuterSocketClientSync(
							sw.getHost(), sw.getPort(),
							new MessageInboundHandler(userName, puid, chType));
					MessageImpl
							.sendMessage(channel, MsgId.SwLoginCm, swLoginCm);
				} catch (Exception e) {
					LoggerService.getLogicLog().error(e.getMessage(), e);
				}
			}
			super.channelInactive(ctx);
		}

		@Override
		public void channelActive(final ChannelHandlerContext ctx)
				throws Exception {
			HashMap<String, Object> hashMap = ctx.channel().attr(MAP_Attr)
					.get();
			if (hashMap == null) {
				HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
				hashMap2.put(item_index, new ChannelItem(ctx.channel()));
				hashMap2.put(nicknameIndex, "");
				hashMap2.put(seatIndex, 0);
				hashMap2.put(uidIndex, uName);
				hashMap2.put(channelType, chType);
				hashMap2.put(puidIndex, puid);
				hashMap2.put(roomIdIndex, 0);
				ctx.channel().attr(MAP_Attr).set(hashMap2);
			}
			ChannelAttachmentAbs channelAttachment = ctx.channel()
					.attr(SystemConstants.CHANNEL_ATTR_KEY).get();
			if (channelAttachment == null) {
				channelAttachment = new ChannelAttachment(ctx.channel());
				ctx.channel().attr(SystemConstants.CHANNEL_ATTR_KEY)
						.set(channelAttachment);
				if (ServerConfig.isHeartBeat())
					new Thread(new Runnable() {
						@Override
						public void run() {
							while (true) {
								if (threadBreak) {
									break;
								}
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								try {
									ctx.channel().writeAndFlush(
											new InnerIoMessage(
													InnerMsgId.HeartBeat,
													new byte[] {}));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						}
					}).start();
			}
			list.put(ctx.channel(), Boolean.TRUE);
			super.channelActive(ctx);
		}

		@Override
		public void channelRead0(final ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			MessageLite genMessageLite = msg.genMessageLite();
			MessageImpl getEnum;
			if (msg.getMsgId().name().endsWith("Cast")) {
				switch (msg.getMsgId()) {
				case BankerCast:
					BankerCast bankerCast = (BankerCast) genMessageLite;
					MessageImpl.putChannel(ctx, bankerSeetIndex,
							bankerCast.getSeetIndex());
					break;
				case BetOnBeginCast:
					MessageImpl.putChannel(ctx, gameStateIndex,
							GameState.noBetOn);
					break;
				case BullResultCast:
					MessageImpl.putChannel(ctx, gameStateIndex,
							GameState.noNextRound);
					break;
				case QzBeginCast:
					MessageImpl.putChannel(ctx, gameStateIndex,
							GameState.noQiangZhuang);
					break;
				default:

				}

			} else {
				getEnum = MessageImpl.getEnum(msg.getMsgId());
				if (getEnum != null)
					getEnum.resp(ctx, msg);
				int code = (int) genMessageLite.getClass()
						.getMethod("getCode", null)
						.invoke(genMessageLite, null);
				if (code != NTxtAbs.SUCCESS) {
					NTxt.println(String.format("%s,%s,%s",
							MessageImpl.getChannel(ctx, puidIndex), code,
							NoticeTextTemplate.getNoticeText(code)));
				}

			}
			String printToString = JsonFormat
					.printToString((Message) genMessageLite);

			LoggerService.getLogicLog().warn(
					"{}：{}",
					msg.getMsgId().name(),
					printToString.length() > 1000 ? new JSONObject(
							printToString).toString(1) : printToString);

		}
	}

	public static boolean isDebug() {
		return true;
	}

	public static List<String> getURLs() {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			String property = properties.getProperty("login" + i);
			if (property != null)
				arrayList.add(property);
		}
		return arrayList;
	}

	public static Channel getCurrentChannel() {
		return currentChannel;
	}

	public static void setCurrentChannel(Channel currentChannel) {
		TestClient.currentChannel = currentChannel;
	}

}
