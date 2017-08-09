package pers.wuchao.daobase;

import java.util.Properties;

import org.apache.log4j.Logger;

import pers.wuchao.action.framework.ConfigureProperties;
import pers.wuchao.tools.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
	private static Logger log = Logger.getLogger(JedisUtil.class);

	private static Properties info = new Properties();

	private static String ADDR_ARRAY,AUTH;
	private static int PORT,MAX_ACTIVE,MAX_IDLE,MAX_WAIT,TIMEOUT;
	private static boolean TEST_ON_BORROW = false;

	private static boolean setValueFromProperties() {
		try {
			info.putAll(ConfigureProperties.properties); // 从ConfigureProperties
															// 中加载配置文件

			ADDR_ARRAY = info.getProperty("addr_array");
			PORT = Integer.parseInt(info.getProperty("port"));
			AUTH = info.getProperty("auth");
			MAX_ACTIVE = Integer.parseInt(info.getProperty("max_active"));
			MAX_IDLE = Integer.parseInt(info.getProperty("max_active"));
			MAX_WAIT = Integer.parseInt(info.getProperty("max_wait"));
			TIMEOUT = Integer.parseInt(info.getProperty("timeout"));
			TEST_ON_BORROW = Boolean.parseBoolean(info.getProperty("test_on_borrow"));
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	private static JedisPool jedisPool = null;

	/**
	 * redis过期时间,以秒为单位
	 */
	public final static int EXRP_HOUR = 60 * 60; // 一小时
	public final static int EXRP_DAY = 60 * 60 * 24; // 一天
	public final static int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月

	/**
	 * 初始化Redis连接池
	 */
	public static void initialPool() {
		boolean f = setValueFromProperties();
		if (!f) {
			log.info("未检测到redis 环境，redis 未初始化");
			return;
		}
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, ADDR_ARRAY.split(",")[0], PORT, TIMEOUT, AUTH);
		} catch (Exception e) {
			log.error("First create JedisPool error : " + e);
			try {
				// 如果第一个IP异常，则访问第二个IP
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(MAX_ACTIVE);
				config.setMaxIdle(MAX_IDLE);
				config.setMaxWaitMillis(MAX_WAIT);
				config.setTestOnBorrow(TEST_ON_BORROW);
				config.setMinIdle(5);
				jedisPool = new JedisPool(config, ADDR_ARRAY.split(",")[1], PORT, TIMEOUT, AUTH);
			} catch (Exception e2) {
				log.error(e2, e2.fillInStackTrace());
			}
		}
		
		try {
			jedisPool.getResource();
			log.info("redis 成功初始化！");
		} catch (Exception e) {
			log.error(e.getMessage() + " redis 初始化失败！");
		}
	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		if (jedisPool == null) {
			initialPool();
		}
	}

	/**
	 * 同步获取Jedis实例
	 * 
	 * @return Jedis
	 */
	public synchronized static Jedis getJedis() {
		if (jedisPool == null) {
			poolInit();
		}
		Jedis jedis = null;
		if (jedisPool != null) {
			jedis = jedisPool.getResource();
		}
		return jedis;
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null && jedisPool != null) {
			jedis.close();
		}
	}

	/**
	 * 设置 String
	 * 
	 * @param key
	 * @param value
	 */
	public synchronized static void setString(String key, String value) {
		try {
			value = StringUtils.isEmpty(value) ? "" : value;
			getJedis().set(key, value);
		} catch (Exception e) {
			log.error("Set key error : " + e);
		}
	}

	/**
	 * 设置 过期时间
	 * 
	 * @param key
	 * @param seconds
	 *            以秒为单位
	 * @param value
	 */
	public synchronized static void setString(String key, int seconds, String value) {
		try {
			value = StringUtils.isEmpty(value) ? "" : value;
			getJedis().setex(key, seconds, value);
		} catch (Exception e) {
			log.error("Set keyex error : " + e);
		}
	}

	/**
	 * 获取String值
	 * @param key
	 * @return value
	 */
	public synchronized static String getString(String key) {
		if (getJedis() == null || !getJedis().exists(key)) {
			return null;
		}
		return getJedis().get(key);
	}
}
