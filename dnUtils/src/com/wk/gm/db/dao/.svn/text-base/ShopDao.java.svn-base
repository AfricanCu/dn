package com.wk.gm.db.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.db.IbatisDbServer;
import com.wk.engine.util.LRUMap;
import com.wk.gm.bean.ShopTemplate;

public class ShopDao {

	private final static Map<Integer, ShopTemplate> userMap = Collections
			.synchronizedMap(new LRUMap<Integer, ShopTemplate>(1000) {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean removeEldestEntry(
						Map.Entry<Integer, ShopTemplate> eldest) {
					boolean removeEldestEntry = super.removeEldestEntry(eldest);
					if (removeEldestEntry) {
					}
					return removeEldestEntry;
				}
			});

	public static ShopTemplate queryShop(Integer shopId) throws SQLException {
		ShopTemplate proxyBean = userMap.get(shopId);
		if (proxyBean != null) {
			return proxyBean;
		}
		proxyBean = (ShopTemplate) IbatisDbServer.getGmSqlMapper()
				.queryForObject("shop.queryShop", shopId);
		if (proxyBean != null) {
			proxyBean.bitSetClear();
		}
		return proxyBean;
	}

	public static int updateBatch(
			ConcurrentHashMap<Integer, ? extends ShopTemplate> dbCache)
			throws SQLException {
		int count = 0;
		ArrayList<PreparedStatement> list = new ArrayList<>();
		Connection conn = IbatisDbServer.getGmSqlMapper().getDataSource()
				.getConnection();
		conn.setAutoCommit(false);
		try {
			for (Iterator<? extends ShopTemplate> iterator = dbCache.values()
					.iterator(); iterator.hasNext();) {
				ShopTemplate next = iterator.next();
				try {
					PreparedStatement prepareStatement = next
							.createPreparedStatement(conn);
					if (prepareStatement != null) {
						list.add(prepareStatement);
					}
					iterator.remove();
				} catch (Exception e) {
					LoggerService.getLogicLog().error(e.getMessage(), e);
				}
			}
			for (PreparedStatement a : list) {
				count += a.executeUpdate();
			}
			conn.commit();
			return count;
		} finally {
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				conn.close();
			}
			for (PreparedStatement a : list) {
				if (a != null && !a.isClosed()) {
					a.close();
				}
			}
		}
	}

	public static int update(ShopTemplate shop) throws SQLException {
		int update = 0;
		try {
			update = shop.update();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return update;
	}

	public static Object insertShop(ShopTemplate proxy) throws SQLException {
		return IbatisDbServer.getGmSqlMapper().insert("shop.insertShop", proxy);
	}

	public static List<ShopTemplate> queryShopList() throws SQLException {
		return IbatisDbServer.getGmSqlMapper().queryForList("shop.queryShops");
	}
}
