package test.gen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import com.wk.bean.SystemConstantsAbs;
import com.wk.enun.BusFuncType;
import com.wk.enun.ChargeStatus;
import com.wk.enun.DistrictType;
import com.wk.enun.FriendState;
import com.wk.enun.PlatformType;
import com.wk.enun.ServerStatus;
import com.wk.enun.UserState;
import com.wk.guild.bean.GuildBean;
import com.wk.guild.enm.JulebuState;
import com.wk.guild.enm.MemberJobType;
import com.wk.guild.enm.WinnerNumType;
import com.wk.play.enun.NumType;
import com.wk.play.enun.SeveralNiaoType;
import com.wk.play.enun.TimesLimitType;
import com.wk.play.enun.ZaNiaoType;
import com.wk.user.bean.UserBean;
import com.wk.user.enm.ExpiresInType;

public class GenInnerEnm {
	public static void main(String[] args) throws Exception {
		innerMsgId(ZaNiaoType.values());
		innerMsgId(SeveralNiaoType.values());
		innerMsgId(TimesLimitType.values());
		// /////////
		innerMsgId(BusFuncType.values(), "getName");
		innerMsgId(ChargeStatus.values(), "getType");
		innerMsgId(FriendState.values(), "getType");
		innerMsgId(DistrictType.values(), "getType");
		innerMsgId(PlatformType.values());
		innerMsgId(ServerStatus.values(), "getType");
		innerMsgId(UserState.values(), "getType");

		innerMsgId(NumType.values());

		innerMsgId(MemberJobType.values());
		innerMsgId(JulebuState.values());
		innerMsgId(ExpiresInType.values());
		innerMsgId(WinnerNumType.values());

		System.out.println("------------genBitSetKey---------------");
		genBitSetKey();
		System.out.println("------------GenInnerMsg---------------");
		GenInnerMsg.innerMsgId();
	}

	public static void genBitSetKey() {
		for (Class<?> clazz : new Class[] { UserBean.class, GuildBean.class }) {
			Field[] fields = clazz.getDeclaredFields();
			int index = 0;
			StringBuilder builder = new StringBuilder("// bitSet key\n");
			for (Field field : fields) {
				if (field.getName().equals(SystemConstantsAbs.EndLoopStr)) {
					break;
				}
				builder.append(String.format(
						"private static final int %s_key = %s;\n",
						field.getName(), index++));
			}
			JavaGenerate.gen(clazz, builder.toString());
		}
	}

	public static void innerMsgId(Enum<?>[] enms) throws Exception {
		innerMsgId(enms, "getType");
	}

	public static void innerMsgId(Enum<?>[] enms, String typeMethod)
			throws FileNotFoundException, IOException, URISyntaxException,
			NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		if (enms.length <= 0) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		Class<? extends Enum> clazz = enms[0].getClass();
		Method method = clazz.getMethod(typeMethod);
		Object invoke = method.invoke(enms[0]);
		int lastIndexOf = clazz.getName().lastIndexOf('.');
		int indexOf = clazz.getName().indexOf('$');
		String name = indexOf == -1 ? clazz.getSimpleName() : clazz.getName()
				.substring(lastIndexOf + 1, indexOf);
		builder.append("public static ").append(name).append(" ")
				.append("getEnum(");
		String typeClazzName = invoke.getClass().getSimpleName();
		switch (typeClazzName) {
		case "Integer":
			builder.append("int");
			break;
		default:
			builder.append(typeClazzName);
			break;
		}
		builder.append(" type").append("){\n");
		builder.append("switch(type) {\n");
		for (Enum<?> enm : enms) {
			Object obj = method.invoke(enm);
			switch (typeClazzName) {
			case "String":
				obj = "\"" + obj + "\"";
				break;
			default:
				break;
			}
			builder.append(String.format("case %s:\n  return %s;\n", obj,
					enm.name()));
		}
		builder.append("default:\n  return null;\n}\n}");
		JavaGenerate.gen(clazz, builder.toString());
	}
}
