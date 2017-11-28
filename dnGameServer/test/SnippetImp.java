import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import msg.LoginMessage.LoginSm;
import msg.RoomMessage.CreateRoomCm;
import msg.RoomMessage.JoinRoomCast;

import org.xml.sax.SAXException;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.jery.ngsp.server.InterfaceFactoryManager;
import com.jery.ngsp.server.dirtyword.DirtyWordsManager;
import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;
import com.wk.engine.util.ProtobufUtils;
import com.wk.http.HttpUtilTools;
import com.wk.logic.enm.MsgId;
import com.wk.server.ibatis.select.User;
import com.wk.user.bean.UserBean;
import com.wk.util.ReadUtil;
import com.wk.util.SocketUtil;

public class SnippetImp {

	public static void main(String[] args) throws Exception {
		// String post2 = HttpUtilTools
		// .post("http://abc.hunanshikecao.com:8115/Login/iosSandboxChargeServlet",
		// "order_id=1&apple_receipt="
		// + URLEncoder.encode("ss", "utf-8"));
		// System.out.println(post2);
		String post = HttpUtilTools.post(
				"http://192.168.10.145:8115/Login/iosSandboxChargeServlet",
				"order_id=1&apple_receipt=" + URLEncoder.encode("ss", "utf-8"));
		System.err.println(post);
		String order = "http://pay2.ylwqgame.com/xygpay/appstore/ldyh_trade1.php";
		// String post = HttpUtilTools.(order, "order_id=1&apple_receipt="
		// + URLEncoder.encode("ss", "utf-8"));
		// System.err.println(post);

		String ss = "http://pay2.ylwqgame.com/xygpay/appstore/ldyh_notify1.php";
		String post3 = HttpUtilTools.post(ss,
				"order_id=ldyh100114978857262054217698&apple_receipt="
						+ URLEncoder.encode("ss", "utf-8"));
		System.err.println(post3);
		// JSONObject jsonObject = new JSONObject();
		// jsonObject.put("code", 1);
		// jsonObject.put("code", 2);
		// System.err.println(jsonObject.toString());
		// String
		// str="https://itunes.apple.com/us/app/"+URLEncoder.encode("狼的诱惑")+"/id1222290743?mt=8";
		// System.err.println(str);

		// LRUMap<Integer, Object> lru = new LRUMap<Integer, Object>(10);
		// for (int i = 0; i < 10; i++) {
		// lru.put(i, Boolean.FALSE);
		// }
		// lru.get(2);
		// for (int i = 20; i < 25; i++) {
		// lru.put(i, Boolean.FALSE);
		// }
		// for (int i : lru.keySet()) {
		// System.err.println(i);
		// }
		// System.err.println(new JSONObject().put("code", 1).toString()
		// .getBytes().length);
		// System.err.println(new JSONObject().put("code", 1).toString()
		// .getBytes("utf-8").length);
		//
		// byte[] aa = new JSONObject().put("code", 1).toString().getBytes();
		// for (int i = 0; i < aa.length; i++) {
		// System.err.println(aa[i]);
		// }
		//
		// aa = new JSONObject().put("code", 1).toString().getBytes("utf-8");
		// for (int i = 0; i < aa.length; i++) {
		// System.out.println(aa[i]);
		// }
	}

	public static class Temple {
		String firstname;

	}

	public static class Temple2 {
		String lastname;

	}

	static String dir = "./resource/csv/";

	public static void main7(String[] args) throws SAXException, Exception {
		String str = ServerConfig.ADDR;
		DirtyWordsManager dirtyWordsManager = InterfaceFactoryManager
				.getInterfaceFactory().getDirtyWordsManager();
		// 脏词管理启动
		dirtyWordsManager.loadData(null);
		List<Temple> playerStageTemplateList = ReadUtil.explainCsvData(dir
				+ "Man_First_Name.csv", Temple.class, true);
		for (Temple temple : playerStageTemplateList) {
			String checkDirtyWord = dirtyWordsManager.checkDirtyWord(
					temple.firstname, true);
			if (checkDirtyWord != null) {
				System.err.println(checkDirtyWord);
			}
		}
		List<Temple2> playerStageTemplateList2 = ReadUtil.explainCsvData(dir
				+ "Last_Name.csv", Temple2.class, true);
		for (Temple2 temple : playerStageTemplateList2) {
			String checkDirtyWord = dirtyWordsManager.checkDirtyWord(
					temple.lastname, true);
			if (checkDirtyWord != null) {
				System.err.println(checkDirtyWord);
			}
		}

	}

	public static void main2(String[] args)
			throws InvalidProtocolBufferException {
		msg.RoomMessage.JoinRoomCast.Builder newBuilder = JoinRoomCast
				.newBuilder();
		newBuilder.setDelSeatIndex(1);
		String printToString = JsonFormat.printToString(newBuilder.build());
		System.out.println(printToString);
		JoinRoomCast build = newBuilder.build();
		System.err.println(build.getAddUser());
		// Builder newBuilder = ChatMessage.TestCast.newBuilder();
		// newBuilder.setContent("ddsfadfas");
		// newBuilder.setFromTime(System.currentTimeMillis());
		// newBuilder.setFromUserName("aaaaa");
		// TestCastxxx parseFrom = TestCastxxx.parseFrom(newBuilder.build()
		// .toByteArray());
		// System.err.println(parseFrom.getContent());
		// System.err.println(parseFrom.getFromTime());
		// System.err.println(parseFrom.getFromUserName());
		// RunSeedDeathCast runSeedDeathCast = new RunSeedDeathCast(null);
		// System.err.println(runSeedDeathCast instanceof RunSeed);
		// System.err.println(runSeedDeathCast instanceof RunSeedDeathCast);
		// System.err.println(runSeedDeathCast instanceof RunSeedSpecial);
		// HashSet hashSet = new HashSet();
		// DarkCast.Builder darkCast = DarkCast.newBuilder();
		// darkCast.setDarkCount(1);
		// darkCast.setTimeInSecond(RunSeed.genTimeInSecond(DarkConfig.timeInSecond));
		// DarkCast build1 = darkCast.build();
		// darkCast.setDarkCount(2);
		// DarkCast build2 = darkCast.build();
		// System.out.println(build1.toString());
		// LoggerService.initDef();
		// Stack st=new Stack();
		// st.addAll(Arrays.asList(1,2,3));
		// while(!st.isEmpty()){
		// LoggerService.getLogicLog().warn(st.pop());
		// }
		//
		// System.err.println((A)null);
	}

	public static class A {

		A() {

		}

		public String show(D obj) {
			return ("A and D");
		}

		public String show(A obj) {
			return ("A and A");
		}
	}

	public static class B extends A {
		public String show(B obj) {
			return ("B and B");
		}

		public String show(A obj) {
			return ("B and A");
		}
	}

	public static class C extends B {
	}

	public static class D extends B {
	}

	public static void main5(String[] args) {
		A a1 = new A();
		A a2 = new B();
		B b = new B();
		C c = new C();
		D d = new D();
		System.out.println(a1.show(b));
		System.out.println(a1.show(c));
		System.out.println(a1.show(d));
		System.out.println(a2.show(b));
		System.out.println(a2.show(c));
		System.out.println(a2.show(d));
		System.out.println(b.show(b));
		System.out.println(b.show(c));
		System.out.println(b.show(d));
	}

	public static void main3(String[] args) {
		System.out.println(int.class instanceof Object);
	}

	public static void main4(String[] args) throws SAXException, Exception {
		LoggerService.initDef();
		CreateRoomCm.Builder newBuilder = CreateRoomCm.newBuilder();
		byte[] byteArray = newBuilder.build().toByteArray();
		ByteBuf buf = Unpooled.buffer(2 + 2 + byteArray.length);
		// buf.ensureWritable();
		buf.writeShort(2 + byteArray.length);
		buf.writeShort(MsgId.CreateRoomCm.getType());
		buf.writeBytes(byteArray);
		byte[] readBytes = SocketUtil.readBytes("localhost", 8020, buf.array());
		ByteBuf buffer = Unpooled.buffer(readBytes.length);
		buffer.writeBytes(readBytes);
		short msgType = buffer.readShort();
		MsgId msgId = MsgId.getEnum(msgType);
		byte[] bb = new byte[readBytes.length - 2];
		System.arraycopy(readBytes, 2, bb, 0, bb.length);
		LoginSm protobuf = (LoginSm) ProtobufUtils.getProtobuf(bb,
				msgId.getDefaultInst());
	}

	public static void main1(String[] args) throws FileNotFoundException,
			SAXException, IOException, Exception {
		ServerConfig.init();
		List<UserBean> needUpdateDataList = new ArrayList<>();
		UserBean userBean = new UserBean();
		userBean.setPassword("pwd");
		needUpdateDataList.add(new User(userBean));
	}
}
