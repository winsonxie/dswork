package dswork.db.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dswork.core.util.EnvironmentUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 读写分离redis api
 * @author DinGYun
 *         两个连接池,一读一写
 *         该连接池是静态配置,用于在静态文件中配置好ip直接调用该类使用
 */
public class JedisSingleUtil
{
	private static final Logger log = LoggerFactory.getLogger(JedisSingleUtil.class);

	/**
	 * 设置值
	 * @param key
	 * @param value
	 * @return -5：Jedis实例获取失败<br/>
	 *         OK：操作成功<br/>
	 *         null：操作失败 写操作
	 */
	public static String set(String key, String value)
	{
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return JedisStatus.FAIL_STRING;
		}
		String result = null;
		try
		{
			result = jedis.set(key, value);
		}
		catch(Exception e)
		{
			log.error("设置值失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置值
	 * @param key
	 * @param value
	 * @param expire 过期时间，单位：秒
	 * @return -5：Jedis实例获取失败<br/>
	 *         OK：操作成功<br/>
	 *         null：操作失败 操作
	 * @author DinGYun
	 */
	public static String set(String key, String value, int expire)
	{
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return JedisStatus.FAIL_STRING;
		}
		String result = null;
		try
		{
			result = jedis.set(key, value);
			jedis.expire(key, expire);
		}
		catch(Exception e)
		{
			log.error("设置值失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取值
	 * @param key
	 * @return 读操作
	 * @author DinGYun
	 */
	public static String get(String key)
	{
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			return JedisStatus.FAIL_STRING;
		}
		String result = null;
		try
		{
			result = jedis.get(key);
		}
		catch(Exception e)
		{
			log.error("获取值失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置key的过期时间
	 * @param key
	 * @param value -5：Jedis实例获取失败，1：成功，0：失败
	 * @return 写操作
	 */
	public static long expire(String key, int seconds)
	{
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return JedisStatus.FAIL_LONG;
		}
		long result = 0L;
		try
		{
			result = jedis.expire(key, seconds);
		}
		catch(Exception e)
		{
			log.error(String.format("设置key=%s的过期时间失败：" + e.getMessage(), key), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 判断key是否存在
	 * @param key
	 * @return 读操作
	 */
	public static boolean exists(String key)
	{
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			log.warn("Jedis实例获取为空");
			return false;
		}
		boolean result = false;
		try
		{
			result = jedis.exists(key);
		}
		catch(Exception e)
		{
			log.error(String.format("判断key=%s是否存在失败：" + e.getMessage(), key), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 删除key
	 * @param keys
	 * @return -5：Jedis实例获取失败，1：成功，0：失败
	 * @author DinGYun 写操作
	 */
	public static long del(String... keys)
	{
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return JedisStatus.FAIL_LONG;
		}
		long result = JedisStatus.FAIL_LONG;
		try
		{
			result = jedis.del(keys);
		}
		catch(Exception e)
		{
			log.error(String.format("删除key=%s失败：" + e.getMessage(), keys), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * set if not exists，若key已存在，则setnx不做任何操作
	 * @param key
	 * @param value key已存在，1：key赋值成功
	 * @return 写操作
	 * @author DinGYun
	 */
	public static long setnx(String key, String value)
	{
		long result = JedisStatus.FAIL_LONG;
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return result;
		}
		try
		{
			result = jedis.setnx(key, value);
		}
		catch(Exception e)
		{
			log.error("设置值失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * set if not exists，若key已存在，则setnx不做任何操作
	 * @param key
	 * @param value key已存在，1：key赋值成功
	 * @param expire 过期时间，单位：秒
	 * @return
	 * @author DinGYun
	 *         写操作
	 */
	public static long setnx(String key, String value, int expire)
	{
		long result = JedisStatus.FAIL_LONG;
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return result;
		}
		try
		{
			result = jedis.setnx(key, value);
			jedis.expire(key, expire);
		}
		catch(Exception e)
		{
			log.error("设置值失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 在列表key的头部插入元素
	 * @param key
	 * @param values -5：Jedis实例获取失败，>0：返回操作成功的条数，0：失败
	 * @return
	 * 		写操作
	 */
	public static long lpush(String key, String... values)
	{
		long result = JedisStatus.FAIL_LONG;
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return result;
		}
		try
		{
			result = jedis.lpush(key, values);
		}
		catch(Exception e)
		{
			log.error("在列表key的头部插入元素失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 在列表key的尾部插入元素
	 * @param key
	 * @param values -5：Jedis实例获取失败，>0：返回操作成功的条数，0：失败
	 * @return
	 * 		写操作
	 */
	public static long rpush(String key, String... values)
	{
		long result = JedisStatus.FAIL_LONG;
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return result;
		}
		try
		{
			result = jedis.rpush(key, values);
		}
		catch(Exception e)
		{
			log.error("在列表key的尾部插入元素失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 返回存储在key列表的特定元素
	 * @param key
	 * @param start 开始索引，索引从0开始，0表示第一个元素，1表示第二个元素
	 * @param end 结束索引，-1表示最后一个元素，-2表示倒数第二个元素
	 * @return redis client获取失败返回null
	 * @author DinGYun
	 *         读操作
	 */
	public static List<String> lrange(String key, long start, long end)
	{
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			return null;
		}
		List<String> result = null;
		try
		{
			result = jedis.lrange(key, start, end);
		}
		catch(Exception e)
		{
			log.error("查询列表元素失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取列表长度
	 * @param key -5：Jedis实例获取失败
	 * @return
	 * 		读操作
	 */
	public static long llen(String key)
	{
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			return JedisStatus.FAIL_LONG;
		}
		long result = 0;
		try
		{
			result = jedis.llen(key);
		}
		catch(Exception e)
		{
			log.error("获取列表长度失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 移除等于value的元素<br/>
	 * <br/>
	 * 当count>0时，从表头开始查找，移除count个；<br/>
	 * 当count=0时，从表头开始查找，移除所有等于value的；<br/>
	 * 当count<0时，从表尾开始查找，移除count个
	 * @param key
	 * @param count
	 * @param value
	 * @return -5:Jedis实例获取失败
	 * @author DinGYun
	 *         写操作
	 */
	public static long lrem(String key, long count, String value)
	{
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return JedisStatus.FAIL_LONG;
		}
		long result = 0;
		try
		{
			result = jedis.lrem(key, count, value);
		}
		catch(Exception e)
		{
			log.error("获取列表长度失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 对列表进行修剪
	 * @param key
	 * @param start
	 * @param end
	 * @return -5：Jedis实例获取失败，OK：命令执行成功
	 *         写操作
	 */
	public static String ltrim(String key, long start, long end)
	{
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return JedisStatus.FAIL_STRING;
		}
		String result = "";
		try
		{
			result = jedis.ltrim(key, start, end);
		}
		catch(Exception e)
		{
			log.error("获取列表长度失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 缓存Map赋值
	 * @param key
	 * @param field
	 * @param value
	 * @return -5：Jedis实例获取失败
	 *         写操作
	 */
	public static long hset(String key, String field, String value)
	{
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			return JedisStatus.FAIL_LONG;
		}
		long result = 0L;
		try
		{
			result = jedis.hset(key, field, value);
		}
		catch(Exception e)
		{
			log.error("缓存Map赋值失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取缓存的Map值
	 * @param key
	 * @return
	 * 		读操作
	 */
	public static String hget(String key, String field)
	{
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			return null;
		}
		String result = null;
		try
		{
			result = jedis.hget(key, field);
		}
		catch(Exception e)
		{
			log.error("获取缓存的Map值失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取map所有的字段和值
	 * @param key
	 * @return
	 * 		读操作
	 */
	public static Map<String, String> hgetAll(String key)
	{
		Map<String, String> map = new HashMap<String, String>();
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			log.warn("Jedis实例获取为空");
			return map;
		}
		try
		{
			map = jedis.hgetAll(key);
		}
		catch(Exception e)
		{
			log.error("获取map所有的字段和值失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return map;
	}

	/**
	 * 查看哈希表 key 中，指定的field字段是否存在。
	 * @param key
	 * @param field
	 * @return
	 * 		读操作
	 */
	public static Boolean hexists(String key, String field)
	{
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			log.warn("Jedis实例获取为空");
			return null;
		}
		try
		{
			return jedis.hexists(key, field);
		}
		catch(Exception e)
		{
			log.error("查看哈希表field字段是否存在失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return null;
	}

	/**
	 * 获取所有哈希表中的字段
	 * @param key
	 * @return
	 * 		读操作
	 */
	public static Set<String> hkeys(String key)
	{
		Set<String> set = new HashSet<String>();
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			log.warn("Jedis实例获取为空");
			return set;
		}
		try
		{
			return jedis.hkeys(key);
		}
		catch(Exception e)
		{
			log.error("获取所有哈希表中的字段失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return null;
	}

	/**
	 * 获取所有哈希表中的值
	 * @param key
	 * @return
	 */
	public static List<String> hvals(String key)
	{
		List<String> list = new ArrayList<String>();
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			log.warn("Jedis实例获取为空");
			return list;
		}
		try
		{
			return jedis.hvals(key);
		}
		catch(Exception e)
		{
			log.error("获取所有哈希表中的值失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return null;
	}

	/**
	 * 从哈希表 key 中删除指定的field
	 * @param key
	 * @param field
	 * @return
	 * 		写操作
	 */
	public static long hdel(String key, String... fields)
	{
		Jedis jedis = RedisSingleConfig.getJedis();
		if(jedis == null)
		{
			log.warn("Jedis实例获取为空");
			return JedisStatus.FAIL_LONG;
		}
		try
		{
			return jedis.hdel(key, fields);
		}
		catch(Exception e)
		{
			log.error("map删除指定的field失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return 0;
	}

	/**
	 * 可模糊获取redis中的key值 eg:lock*得到 所有带lock的key的set集合
	 * @param pattern
	 * @return
	 */
	public static Set<String> keys(String pattern)
	{
		Set<String> keyList = new HashSet<String>();
		Jedis jedis = RedisSingleConfig.getReadJedis();
		if(jedis == null)
		{
			log.warn("Jedis实例获取为空");
			return keyList;
		}
		try
		{
			keyList = jedis.keys(pattern);
		}
		catch(Exception e)
		{
			log.error("操作keys失败：" + e.getMessage(), e);
			RedisSingleConfig.returnBrokenResource(jedis);
		}
		finally
		{
			RedisSingleConfig.returnResource(jedis);
		}
		return keyList;
	}

	/**
	 * Jedis实例获取返回码
	 */
	public static class JedisStatus
	{
		/** Jedis实例获取失败 */
		public static final long FAIL_LONG = -5L;
		/** Jedis实例获取失败 */
		public static final int FAIL_INT = -5;
		/** Jedis实例获取失败 */
		public static final String FAIL_STRING = "-5";
	}
}

class RedisSingleConfig
{
	private static final Logger log = LoggerFactory.getLogger(RedisSingleConfig.class);
	private static String REDIS_NATIVE_HOST = EnvironmentUtil.getStringProperty("redis.native.host");
	private static String REDIS_NATIVE_PORT = EnvironmentUtil.getStringProperty("redis.native.port");
	private static String REDIS_NATIVE_PASSWORD = EnvironmentUtil.getStringProperty("redis.native.password");
	private static String REDIS_NATIVE_RHOST = EnvironmentUtil.getStringProperty("redis.native.rhost");
	private static String REDIS_NATIVE_RPORT = EnvironmentUtil.getStringProperty("redis.native.rport");
	private static String REDIS_NATIVE_RPASSWORD = EnvironmentUtil.getStringProperty("redis.native.rpassword");
	private static String REDIS_NATIVE_MAXTOTAL = EnvironmentUtil.getStringProperty("redis.native.maxTotal");
	private static String REDIS_NATIVE_MAXIDLE = EnvironmentUtil.getStringProperty("redis.native.maxIdle");
	private static String REDIS_NATIVE_MINIDLE = EnvironmentUtil.getStringProperty("redis.native.minIdle");
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 15 * 1000;
	// 超时时间
	private static int TIMEOUT = 10 * 1000;
	private static JedisPool jedisPool = null;
	private static JedisPool jedisReadPool = null;

	private static void initialPool()
	{
		String HOST = REDIS_NATIVE_HOST;
		int PORT = NumberUtils.toInt(REDIS_NATIVE_PORT, 6379);
		String AUTH = REDIS_NATIVE_PASSWORD;
		System.out.println(REDIS_NATIVE_PASSWORD);
		String RHOST = REDIS_NATIVE_RHOST;
		int RPORT = NumberUtils.toInt(REDIS_NATIVE_RPORT, 6379);
		String RAUTH = REDIS_NATIVE_RPASSWORD;
		try
		{
			JedisPoolConfig config = new JedisPoolConfig();
			// 最大连接数，如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxTotal(NumberUtils.toInt(REDIS_NATIVE_MAXTOTAL, 400));
			// 最大空闲数，控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
			config.setMaxIdle(NumberUtils.toInt(REDIS_NATIVE_MAXIDLE, 50));
			// 最小空闲数
			config.setMinIdle(NumberUtils.toInt(REDIS_NATIVE_MINIDLE, 10));
			// 是否在从池中取出连接前进行检验，如果检验失败，则从池中去除连接并尝试取出另一个
			config.setTestOnBorrow(true);
			// 在return给pool时，是否提前进行validate操作
			config.setTestOnReturn(true);
			// 在空闲时检查有效性，默认false
			config.setTestWhileIdle(true);
			// 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；
			// 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
			config.setMinEvictableIdleTimeMillis(30000);
			// 表示idle object evitor两次扫描之间要sleep的毫秒数
			config.setTimeBetweenEvictionRunsMillis(60000);
			// 表示idle object evitor每次扫描的最多的对象数
			config.setNumTestsPerEvictionRun(1000);
			// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(MAX_WAIT);
			// 密码为空.直接访问, 增加读连接池的扩展
			if(StringUtils.isNotBlank(AUTH))
			{
				jedisPool = new JedisPool(config, HOST, PORT, TIMEOUT, AUTH);
				if(RHOST == null)
				{
					jedisReadPool = jedisPool;
				}
				else
				{
					jedisReadPool = new JedisPool(config, RHOST, RPORT, TIMEOUT, RAUTH);
				}
			}
			else
			{
				jedisPool = new JedisPool(config, HOST, PORT, TIMEOUT);
				if(RHOST == null)
				{
					jedisReadPool = jedisPool;
				}
				else
				{
					jedisReadPool = new JedisPool(config, RHOST, RPORT, TIMEOUT);
				}
			}
		}
		catch(Exception e)
		{
			if(jedisPool != null)
			{
				jedisPool.close();
			}
			if(jedisReadPool != null)
			{
				jedisReadPool.close();
			}
			log.error("初始化Redis连接池失败", e);
		}
	}
	/**
	 * 初始化Redis连接池
	 */
	static
	{
		initialPool();
	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit()
	{
		if(jedisPool == null)
		{
			initialPool();
		}
	}

	/**
	 * 同步获取Jedis实例
	 * @return Jedis
	 */
	public static Jedis getJedis()
	{
		if(jedisPool == null)
		{
			poolInit();
		}
		Jedis jedis = null;
		try
		{
			if(jedisPool != null)
			{
				jedis = jedisPool.getResource();
			}
		}
		catch(Exception e)
		{
			log.error("同步获取Jedis实例失败" + e.getMessage(), e);
			returnBrokenResource(jedis);
		}
		return jedis;
	}

	/**
	 * 释放jedis资源
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis)
	{
		if(jedis != null && jedisPool != null)
		{
			// jedisPool.returnResource(jedis);
			jedis.close();
		}
	}

	public static void returnBrokenResource(final Jedis jedis)
	{
		if(jedis != null && jedisPool != null)
		{
			// jedisPool.returnBrokenResource(jedis);
			jedis.close();
		}
	}

	public static Jedis getReadJedis()
	{
		if(jedisReadPool == null)
		{
			poolInit();
		}
		Jedis jedis = null;
		try
		{
			if(jedisReadPool != null)
			{
				jedis = jedisReadPool.getResource();
			}
		}
		catch(Exception e)
		{
			log.error("同步获取Jedis实例失败" + e.getMessage(), e);
			returnBrokenResource(jedis);
		}
		return jedis;
	}

	/**
	 * 释放jedis资源
	 * @param jedis
	 */
	public static void returnReadResource(final Jedis jedis)
	{
		if(jedis != null && jedisReadPool != null)
		{
			// jedisReadPool.returnResource(jedis);
			jedis.close();
		}
	}

	public static void returnReadBrokenResource(final Jedis jedis)
	{
		if(jedis != null && jedisReadPool != null)
		{
			// jedisReadPool.returnBrokenResource(jedis);
			jedis.close();
		}
	}
}
