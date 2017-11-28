package com.wk.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibatis.common.beans.ClassInfo;
import com.ibatis.common.beans.Invoker;
import com.jery.ngsp.server.log.LoggerService;

public class IbatisUtil {

	/**
	 * 给class的静态变量赋值
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param args
	 * @throws Exception
	 */
	public static void invoke(Class<?> clazz, String fieldName, Object... args)
			throws Exception {
		ClassInfo info = ClassInfo.getInstance(clazz);
		Invoker setInvoker = info.getSetInvoker(fieldName);
		setInvoker.invoke(clazz, args);
	}

	/**
	 * 用obj2覆盖obj1的数据
	 * 
	 * @param obj1
	 * @param obj2
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void overBean(Object obj1, Object obj2)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = obj2.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.set(obj1, field.get(obj2));
		}
	}

	private static class Test {
		private int a;
		public int b;
		public final int c;

		public Test(int a, int b, int c) {
			super();
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException {
		overBean(new Test(1, 2, 3), new Test(3, 4, 5));
	}

	public static int executePreparedUpdate(Connection conn, String sql,
			Object[] parameters) {
		PreparedStatement pstate = null;
		try {
			pstate = conn.prepareStatement(sql);
			for (int index = 0; index < parameters.length; index++)
				pstate.setObject(index + 1, parameters[index]);
			return pstate.executeUpdate();
		} catch (SQLException e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			return 0;
		} finally {
			try {
				if (pstate != null && !pstate.isClosed()) {
					pstate.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				LoggerService.getLogicLog().error(e.getMessage(), e);
			}
		}
	}
}
