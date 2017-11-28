package com.wk.server.logic.rank;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.jery.ngsp.server.log.LoggerService;
import com.wk.engine.config.ServerConfig;

public final class RedisUtil {

	public static final String HASH_NAME = "Name";

	// Redis的端口号
	private static int PORT = 6379;

	// 访问密码
	private static String AUTH = null;

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = 8;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 8;

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int maxWaitMillis = 10000;

	private static int TIMEOUT = 60000;

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;

	private static JedisPool jedisPool = null;

	/**
	 * 获取Jedis实例
	 * 
	 * !!!!!!!!一定要归还
	 * 
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		// if (!ServerConfig.isDebug()) {
		// return new Jedis(ServerConfig.ADDR, PORT);
		// }
		if (jedisPool == null) {
			try {
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(MAX_ACTIVE);
				config.setMaxIdle(MAX_IDLE);
				config.setMaxWaitMillis(maxWaitMillis);
				config.setTestOnBorrow(TEST_ON_BORROW);
				jedisPool = new JedisPool(config, ServerConfig.ADDR, PORT,
						TIMEOUT, AUTH);
			} catch (Exception e) {
				e.printStackTrace();
				jedisPool = null;
				return null;
			}
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis;
		} catch (Exception e) {
			LoggerService.getLogicLog().error(e.getMessage(), e);
			if (jedis != null) {
				jedis.close();
			}
			return null;
		}
	}

	public static void returnResource(Jedis jedis) {
		if (jedis != null)
			jedis.close();
	}

	public static void log() {
		if (jedisPool == null) {
			return;
		}
		int numActive = jedisPool != null ? jedisPool.getNumActive() : -1;
		int NumIdle = jedisPool != null ? jedisPool.getNumIdle() : -1;
		int NumWaiters = jedisPool != null ? jedisPool.getNumWaiters() : -1;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("活跃数:").append(numActive).append("闲置数：")
				.append(NumIdle).append("等待数：").append(NumWaiters);
		LoggerService.getLogicLog().error(stringBuilder.toString());
	}

	public static JedisPool getJedisPool() {
		return jedisPool;
	}

}