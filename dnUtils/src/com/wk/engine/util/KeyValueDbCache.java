package com.wk.engine.util;

import java.util.concurrent.ConcurrentHashMap;

import com.wk.gm.bean.ProxyBean;
import com.wk.guild.bean.GuildBean;
import com.wk.user.bean.UserBean;

/**
 * 数据回写缓存
 * 
 * 现在就是一个ConcurrentHashMap
 * 
 * @author Administrator
 *
 * @param <K>
 * @param <V>
 */
public class KeyValueDbCache<K, V> extends ConcurrentHashMap<K, V> {
	private static final long serialVersionUID = 1L;

	public static final KeyValueDbCache<Long, UserBean> emptyUserDbCache = new KeyValueDbCache<Long, UserBean>() {
		private static final long serialVersionUID = 1L;

		public UserBean put(Long key, UserBean value) {
			return null;
		};

		public UserBean get(Object key) {
			return null;
		};

		public UserBean remove(Object key) {
			return null;
		};
	};

	public static final KeyValueDbCache<String, ProxyBean> emptyProxyDbCache = new KeyValueDbCache<String, ProxyBean>() {
		private static final long serialVersionUID = 1L;

		public ProxyBean put(String key, ProxyBean value) {
			return null;
		};

		public ProxyBean get(Object key) {
			return null;
		};

		public ProxyBean remove(Object key) {
			return null;
		};
	};

	public static final KeyValueDbCache<Integer, GuildBean> emptyGuildDbCache = new KeyValueDbCache<Integer, GuildBean>() {
		private static final long serialVersionUID = 1L;

		public GuildBean put(Integer key, GuildBean value) {
			return null;
		};

		public GuildBean get(Object key) {
			return null;
		};

		public GuildBean remove(Object key) {
			return null;
		};
	};

	@Override
	public V put(K key, V value) {
		if (key == null || value == null) {
			return null;
		}
		return super.put(key, value);
	}

	@Override
	public V get(Object key) {
		return super.get(key);
	}
}