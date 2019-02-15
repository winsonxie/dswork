package dswork.db.redis;

import dswork.db.redis.config.RedisConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisUtil
{
	// protected Logger log = LoggerFactory.getLogger("db.redis");
	private JedisPool jedisPool = null;
	private JedisPool jedisReadPool = null;

	/**
	 * 在多线程环境同步初始化
	 */
	public RedisUtil(RedisConfig config)
	{
		this(config, null);
	}

	public RedisUtil(RedisConfig config, RedisConfig readConfig)
	{
		boolean notread = false;
		if(readConfig == null)
		{
			readConfig = config;
			notread = true;
		}
		this.jedisPool = new JedisPool(config.getConfig(), config.getHost(), config.getPort(), config.getConnectTimeout(), config.getSoTimeout(), config.getPassword(), config.getDababase(), config.getClientName(), config.isSsl(), config.getSslSocketFactory(), config.getSslParameters(),
				config.getHostnameVerifier());
		if(notread)
		{
			this.jedisReadPool = jedisPool;
			// log.debug(config.getConfig() + "主从连接池创建成功");
		}
		else
		{
			// log.debug(config.getConfig() + "主连接池创建成功");
			this.jedisReadPool = new JedisPool(readConfig.getConfig(), readConfig.getHost(), readConfig.getPort(), readConfig.getConnectTimeout(), readConfig.getSoTimeout(), readConfig.getPassword(), readConfig.getDababase(), readConfig.getClientName(), readConfig.isSsl(),
					readConfig.getSslSocketFactory(), readConfig.getSslParameters(), readConfig.getHostnameVerifier());
			// log.debug(readConfig.getConfig() + "从连接池创建成功");
		}
	}

	protected Jedis getJedis()
	{
		return jedisPool.getResource();
	}

	protected Jedis getReadJedis()
	{
		return jedisReadPool.getResource();
	}

	public String set(String key, String value)
	{
		return getJedis().set(key, value);
	}

	/**
	 * 设置值
	 * @param key
	 * @param value
	 * @param expire 过期时间，单位：秒
	 * @return -5：Jedis实例获取失败<br/>
	 *         OK：操作成功<br/>
	 *         null：操作失败 操作
	 */
	public String set(String key, String value, int expire)
	{
		Jedis jedis = getJedis();
		String result = jedis.set(key, value);
		jedis.expire(key, expire);
		return result;
	}

	/**
	 * 获取值
	 * @param key
	 * @return 读操作
	 */
	public String get(String key)
	{
		return getReadJedis().get(key);
	}

	/**
	 * 设置key的过期时间
	 * @param key
	 * @param value -5：Jedis实例获取失败，1：成功，0：失败
	 * @return 写操作
	 */
	public long expire(String key, int seconds)
	{
		return getJedis().expire(key, seconds);
	}

	/**
	 * 判断key是否存在
	 * @param key
	 * @return 读操作
	 */
	public boolean exists(String key)
	{
		return getReadJedis().exists(key);
	}

	/**
	 * 删除key
	 * @param keys
	 * @return -5：Jedis实例获取失败，1：成功，0：失败
	 */
	public long del(String... keys)
	{
		return getJedis().del(keys);
	}

	/**
	 * set if not exists，若key已存在，则setnx不做任何操作
	 * @param key
	 * @param value key已存在，1：key赋值成功
	 * @return 写操作
	 */
	public long setnx(String key, String value)
	{
		return getJedis().setnx(key, value);
	}

	/**
	 * set if not exists，若key已存在，则setnx不做任何操作
	 * @param key
	 * @param value key已存在，1：key赋值成功
	 * @param expire 过期时间，单位：秒
	 * @return
	 */
	public long setnx(String key, String value, int expire)
	{
		Jedis jedis = getJedis();
		long result = jedis.setnx(key, value);
		jedis.expire(key, expire);
		return result;
	}

	/**
	 * 缓存Map赋值
	 * @param key
	 * @param field
	 * @param value
	 * @return -5：Jedis实例获取失败
	 */
	public long hset(String key, String field, String value)
	{
		return getJedis().hset(key, field, value);
	}

	/**
	 * 获取缓存的Map值
	 * @param key
	 * @return
	 */
	public String hget(String key, String field)
	{
		return getReadJedis().hget(key, field);
	}

	/**
	 * 获取map所有的字段和值
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key)
	{
		return getReadJedis().hgetAll(key);
	}

	/**
	 * 查看哈希表 key 中，指定的field字段是否存在。
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean hexists(String key, String field)
	{
		return getReadJedis().hexists(key, field);
	}

	/**
	 * 获取所有哈希表中的字段
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key)
	{
		return getReadJedis().hkeys(key);
	}

	/**
	 * 获取所有哈希表中的值
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key)
	{
		return getReadJedis().hvals(key);
	}

	/**
	 * 从哈希表 key 中删除指定的field
	 * @param key
	 * @param field
	 * @return
	 */
	public long hdel(String key, String... fields)
	{
		return getJedis().hdel(key, fields);
	}

	/**
	 * 可模糊获取redis中的key值 eg:lock*得到 所有带lock的key的set集合
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(String pattern)
	{
		return getReadJedis().keys(pattern);
	}
}
