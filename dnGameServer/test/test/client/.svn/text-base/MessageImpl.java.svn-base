package test.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msg.BullMessage.BetOnSm;
import msg.BullMessage.FinishPukeSm;
import msg.BullMessage.NextRoundSm;
import msg.BullMessage.QiangZhuangSm;
import msg.BullMessage.UserRoundInfo;
import msg.RoomMessage;
import msg.BullMessage.ReconnectSm;
import msg.LoginMessage.LoginSm;
import msg.RoomMessage.CreateRoomSm;
import msg.RoomMessage.JoinRoomBeforeSm;
import msg.RoomMessage.JoinRoomCm;
import msg.RoomMessage.JoinRoomSm;
import msg.RoomMessage.JulebuRoom;
import msg.RoomMessage.LeaveRoomSm;
import msg.RoomMessage.PlayType;
import msg.RoomMessage.PrepareRoomCm;
import msg.RoomMessage.ProxyCreateRoomCm;
import msg.RoomMessage.ProxyCreateRoomSm;
import msg.RoomMessage.UserInfo;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;
import com.google.protobuf.MessageLiteOrBuilder;
import com.ibatis.common.beans.ClassInfo;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.net.IoMessage;
import com.wk.engine.net.MessageManager;
import com.wk.logic.config.NTxt;
import com.wk.logic.enm.GameState;
import com.wk.logic.enm.MsgId;

public enum MessageImpl {
	ProxyCreateRoom(MsgId.ProxyCreateRoomCm) {
		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			ProxyCreateRoomSm createRoomSm = (ProxyCreateRoomSm) msg
					.genMessageLite();
			if (createRoomSm.getCode() == NTxt.SUCCESS) {
				LoggerService.getLogicLog().warn("代房间成功！！{}",
						createRoomSm.getRoomId());
			} else {
				LoggerService.getLogicLog().warn("创建房间失败！！{}",
						createRoomSm.getCode());
			}
		}

		@Override
		public Object getDefaultContent() {
			return xxx;
		}
	},
	ProxyRoomsDissovle(MsgId.ProxyRoomsDissovleCm) {
	},
	AgreeApply(MsgId.AgreeApplyCm) {
	},
	JulebuRecord(MsgId.JulebuRecordCm) {
	},
	JulebuMemberList(MsgId.JulebuMemberListCm) {
	},
	CreateJulebu(MsgId.CreateJulebuCm) {
	},
	ApplyJulebu(MsgId.ApplyJulebuCm) {
	},
	NextRound(MsgId.NextRoundCm) {
		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			NextRoundSm nextRoundSm = (NextRoundSm) msg.genMessageLite();
			if (nextRoundSm.getCode() == NTxt.SUCCESS) {
				putChannel(ctx, TestClient.gameStateIndex,
						GameState.nextRounded);
			}
		}
	},
	FinishPuke(MsgId.FinishPukeCm) {
		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			FinishPukeSm finishPukeSm = (FinishPukeSm) msg.genMessageLite();
			if (finishPukeSm.getCode() == NTxt.SUCCESS) {
				putChannel(ctx, TestClient.gameStateIndex, GameState.finished);
			}
		}
	},
	BetOn(MsgId.BetOnCm) {
		@Override
		public Object getDefaultContent() {
			return "1";
		}

		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			BetOnSm betOnSm = (BetOnSm) msg.genMessageLite();
			if (betOnSm.getCode() == NTxt.SUCCESS) {
				putChannel(ctx, TestClient.gameStateIndex, GameState.betOned);
			}
		}
	},
	QiangZhuang(MsgId.QiangZhuangCm) {
		@Override
		public Object getDefaultContent() {
			return "true";
		}

		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			QiangZhuangSm qiangZhuangSm = (QiangZhuangSm) msg.genMessageLite();
			if (qiangZhuangSm.getCode() == NTxt.SUCCESS) {
				putChannel(ctx, TestClient.gameStateIndex,
						GameState.qiangZhuanged);
			}
		}
	},
	CreateRoom(MsgId.CreateRoomCm) {
		@Override
		public MessageLiteOrBuilder getMessage(String content) {
			return super.getMessage(content);
		}

		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			CreateRoomSm createRoomSm = (CreateRoomSm) msg.genMessageLite();
			if (createRoomSm.getCode() == NTxt.SUCCESS) {
				putChannel(ctx, TestClient.roomIdIndex,
						createRoomSm.getRoomId());
				putChannel(ctx, TestClient.seatIndex, createRoomSm.getInfo()
						.getSeetIndex());
				if (ClientFrame.clientFrame.autoPreparedCheck.isSelected())
					sendMessage(ctx.channel(), MsgId.PrepareRoomCm,
							PrepareRoomCm.newBuilder());
			} else {
				LoggerService.getLogicLog().warn("创建房间失败！！{}",
						createRoomSm.getCode());
			}
		}

		@Override
		public Object getDefaultContent() {
			return xxx;
		}
	},
	GameRecord(MsgId.GameRecordCm) {

	},
	Reconnect(MsgId.ReconnectCm) {
		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			ReconnectSm reconnectSm = (ReconnectSm) msg.genMessageLite();
			if (reconnectSm.getCode() == NTxt.SUCCESS) {
				List<UserInfo> usersRoundList = reconnectSm.getUsersList();
				for (UserInfo xx : usersRoundList) {
					if (xx.getUid() == (long) getChannel(ctx,
							TestClient.uidIndex)) {
						putChannel(ctx, TestClient.seatIndex, xx.getSeetIndex());
						putChannel(ctx, TestClient.gameStateIndex,
								GameState.getEnum(xx.getGstate()));
						break;
					}
				}
			}
		}
	},
	MemberDissolveRoom(MsgId.MemberDissolveRoomCm) {
	},
	Prepare(MsgId.PrepareRoomCm) {
	},

	Login(MsgId.LoginCm) {
		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			LoginSm loginSm = (LoginSm) msg.genMessageLite();
			if (loginSm.getCode() == NTxt.SUCCESS) {
				if (loginSm.getRoomId() != null
						&& !loginSm.getRoomId().equals("")) {
				}
				putChannel(ctx, TestClient.nicknameIndex, loginSm.getNickname());
				putChannel(ctx, TestClient.roomIdIndex, loginSm.getRoomId());
			} else {
				LoggerService.getLogicLog().warn("登陆失败！！{}", loginSm.getCode());
				ctx.channel().close();
			}
		}
	},

	DissolveRoom(MsgId.DissolveRoomCm) {
	},
	LeaveRoom(MsgId.LeaveRoomCm) {
		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			LeaveRoomSm genMessageLite = (LeaveRoomSm) msg.genMessageLite();
			if (genMessageLite.getCode() == NTxt.SUCCESS) {
				putChannel(ctx, TestClient.roomIdIndex, "");
			}
		}
	},
	JoinRoom(MsgId.JoinRoomCm) {
		@Override
		public void record(Channel channel, MessageLiteOrBuilder message) {
			JoinRoomCm.Builder m = (JoinRoomCm.Builder) message;
			putChannel(channel, TestClient.tmpRoomIdIndex, m.getRoomId());
		}

		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			JoinRoomSm joinRoomSm = (JoinRoomSm) msg.genMessageLite();
			if (joinRoomSm.getCode() == NTxt.SUCCESS) {
				putChannel(ctx, TestClient.roomIdIndex, joinRoomSm.getRoomId());
				putChannel(ctx, TestClient.seatIndex, joinRoomSm.getSeatIndex());
				if (ClientFrame.clientFrame.autoPreparedCheck.isSelected())
					sendMessage(ctx.channel(), MsgId.PrepareRoomCm,
							PrepareRoomCm.newBuilder());
			} else {
				LoggerService.getLogicLog().warn(
						"加入房间失败！！" + joinRoomSm.getCode());
			}
		}

		@Override
		public Object getDefaultContent() {
			return joinRoomStr;
		}
	},
	JoinRoomBefore(MsgId.JoinRoomBeforeCm) {
		@Override
		public void resp(ChannelHandlerContext ctx, IoMessage msg)
				throws Exception {
			JoinRoomBeforeSm joinRoomBeforeSm = (JoinRoomBeforeSm) msg
					.genMessageLite();
			if (joinRoomBeforeSm.getCode() == 1) {
				MessageImpl.JoinRoom.sendMessage(ctx,
						joinRoomBeforeSm.getRoomId());
			} else if (joinRoomBeforeSm.getCode() == 2) {
				ctx.channel().attr(TestClient.MAP_Attr).get()
						.put(TestClient.swRoomIdIndex, joinRoomBeforeSm);
				ctx.channel().close();
			} else {
				LoggerService.getLogicLog().warn("加入房间预处理失败！！{}",
						joinRoomBeforeSm.getCode());
			}
		}
	},
	;
	public static final String joinRoomStr = "0#0,0";
	public static final String xxx = "1,1,1";
	public static final String REG = "#";
	// PlayType内容分割
	public static final String DOT = ",";
	private final MsgId msgId;
	private final ArrayList<Method> setterList = new ArrayList<>();
	private final ArrayList<Class<?>> paraClazzList = new ArrayList<>();
	private final String tips;

	private MessageImpl(MsgId msgId) {
		this.msgId = msgId;
		Class<? extends Builder> builderClazz = msgId.genMessageLiteBuilder()
				.getClass();
		tips = genTips(builderClazz, setterList, paraClazzList, REG)
		// playType
				.replace("PlayType:playType",
						genTips(PlayType.Builder.class, null, null, DOT))
				// julebuRoom
				.replace("JulebuRoom:julebuRoom",
						genTips(JulebuRoom.Builder.class, null, null, DOT));
	}

	public static String genTips(Class<?> builderClazz,
			ArrayList<Method> setterList, ArrayList<Class<?>> paraClazzList,
			String REG) {
		Field[] allFields = builderClazz.getDeclaredFields();
		ClassInfo classInfo = ClassInfo.getInstance(builderClazz);
		StringBuilder builder = new StringBuilder();
		for (Field f : allFields) {
			if (Modifier.isPrivate(f.getModifiers())
					&& !Modifier.isStatic(f.getModifiers())
					&& !Modifier.isFinal(f.getModifiers())
					&& !(f.getName().equals("bitField0_")
							|| f.getName().equals("memoizedIsInitialized") || f
							.getName().equals("memoizedSerializedSize"))) {
				String name2 = f.getName().substring(0,
						f.getName().length() - 1);
				if (name2.endsWith("Builder")) {
					LoggerService.getLogicLog().error(name2);
					continue;
				}
				if (name2.equals("pType")) {
					System.err.println(name2);
					continue;
				}
				Method setter = classInfo.getSetter(name2);
				Class<?> paraClazz = setter.getParameterTypes()[0];
				builder.append(paraClazz.getSimpleName()).append(":")
						.append(name2).append(REG);
				if (setterList != null)
					setterList.add(setter);
				if (paraClazzList != null)
					paraClazzList.add(paraClazz);
			}
		}
		return builder.toString();
	}

	public MsgId getMsgId() {
		return msgId;
	}

	public void resp(ChannelHandlerContext ctx, IoMessage msg) throws Exception {
	}

	public MessageLiteOrBuilder sendMessage(ChannelHandlerContext ctx,
			String content) {
		return sendMessage(1, ctx.channel(), content);
	}

	public MessageLiteOrBuilder sendMessage(int num, Channel channel,
			String content) {
		MessageLiteOrBuilder message = getMessage(content);
		record(channel, message);
		MessageManager.sendMessage(num, channel, msgId, message);
		return message;
	}

	public void record(Channel channel, MessageLiteOrBuilder message) {
	}

	public static void sendMessage(Channel channel, MsgId msgId,
			MessageLiteOrBuilder messageLiteOrBuilder) {
		MessageManager.sendMessage(channel, msgId, messageLiteOrBuilder);
	}

	@Override
	public String toString() {
		return msgId.getName() + "  " + msgId.getType();
	}

	private final static Map<MsgId, MessageImpl> typeMap = new HashMap<>();
	static {
		MessageImpl[] missionStateArr = MessageImpl.values();
		for (int i = 0; i < missionStateArr.length; i++) {
			MessageImpl missionState = missionStateArr[i];
			typeMap.put(missionState.msgId, missionState);
			if (missionState.msgId.getResMsgId() != null)
				typeMap.put(missionState.msgId.getResMsgId(), missionState);
		}
	}

	/**
	 *
	 * @param type
	 * @return
	 */
	public static MessageImpl getEnum(MsgId type) {
		return typeMap.get(type);
	}

	public MessageLiteOrBuilder getMessage(String content) {
		String[] split;
		if (content == null || content.equals("")) {
			split = new String[0];
		} else {
			split = content.split(REG);
		}
		if (split.length != this.paraClazzList.size()) {
			LoggerService.getLogicLog().error("消息格式填写错误！！！！！！！！！！！！！！！！{}",
					this.name());
			return null;
		}
		MessageLite.Builder retBuilder = msgId.genMessageLiteBuilder();
		for (int index = 0; index < this.paraClazzList.size(); index++) {
			Class<?> clazz = this.paraClazzList.get(index);
			Method method = this.setterList.get(index);
			String string = split[index];
			try {
				if (clazz == String.class) {
					method.invoke(retBuilder, string);
				} else if (clazz == int.class || clazz == Integer.class) {
					method.invoke(retBuilder, Integer.parseInt(string));
				} else if (clazz == boolean.class || clazz == Boolean.class) {
					method.invoke(retBuilder, Boolean.parseBoolean(string));
				} else if (clazz == PlayType.class) {
					method.invoke(retBuilder,
							ClientFrame.clientFrame.getPlayType());
				} else if (clazz == long.class || clazz == Long.class) {
					method.invoke(retBuilder, Long.parseLong(string));
				} else if (clazz == JulebuRoom.class) {
					String[] sps = string.split(DOT);
					JulebuRoom.Builder mjBuilder = JulebuRoom.newBuilder()
							.setId(Integer.parseInt(sps[0]))
							.setNum(Integer.parseInt(sps[1]));
					method.invoke(retBuilder, mjBuilder.build());
				} else {
					LoggerService.getLogicLog().error(clazz.getName() + "未实现！");
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LoggerService.getLogicLog().error(e.getMessage(), e);
			}
		}
		return retBuilder;
	}

	public static void putChannel(ChannelHandlerContext ctx, String key,
			Object value) {
		putChannel(ctx.channel(), key, value);
	}

	public static void putChannel(Channel channel, String key, Object value) {
		try {
			channel.attr(TestClient.MAP_Attr).get().put(key, value);
			if (channel == TestClient.getCurrentChannel()
					&& key.equals(TestClient.roomIdIndex)) {
				ClientFrame.clientFrame.roomIdLb.setText((String) value);
			}
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
		}
	}

	public static Object getChannel(ChannelHandlerContext ctx, String key) {
		return getChannel(ctx.channel(), key);
	}

	public Object getDefaultContent() {
		return this.getTips();
	}

	public String getTips() {
		return tips;
	}

	public static Object getChannel(Channel channel, String key) {
		HashMap<String, Object> hashMap = channel.attr(TestClient.MAP_Attr)
				.get();
		return hashMap.get(key);
	}

}
