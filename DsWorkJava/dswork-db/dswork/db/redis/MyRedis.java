package dswork.db.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class MyRedis
{
	private Jedis redis;
	public MyRedis(Jedis redis)
	{
		this.redis= redis;
	}

	public String set(String key, String value)
	{
		return redis.set(key, value);
	}

	/**
	 * 获取值
	 * @param key
	 * @return 读操作
	 */
	public String get(String key)
	{
		return redis.get(key);
	}

	/**
	 * 设置key的过期时间
	 * @param key
	 * @param value -5：Jedis实例获取失败，1：成功，0：失败
	 * @return 写操作
	 */
	public long expire(String key, int seconds)
	{
		return redis.expire(key, seconds);
	}

	/**
	 * 判断key是否存在
	 * @param key
	 * @return 读操作
	 */
	public boolean exists(String key)
	{
		return redis.exists(key);
	}

	/**
	 * 删除key
	 * @param keys
	 * @return -5：Jedis实例获取失败，1：成功，0：失败
	 */
	public long del(String... keys)
	{
		return redis.del(keys);
	}

	/**
	 * set if not exists，若key已存在，则setnx不做任何操作
	 * @param key
	 * @param value key已存在，1：key赋值成功
	 * @return 写操作
	 */
	public long setnx(String key, String value)
	{
		return redis.setnx(key, value);
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
		Jedis jedis = redis;
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
		return redis.hset(key, field, value);
	}

	/**
	 * 获取缓存的Map值
	 * @param key
	 * @return
	 */
	public String hget(String key, String field)
	{
		return redis.hget(key, field);
	}

	/**
	 * 获取map所有的字段和值
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key)
	{
		return redis.hgetAll(key);
	}

	/**
	 * 查看哈希表 key 中，指定的field字段是否存在。
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean hexists(String key, String field)
	{
		return redis.hexists(key, field);
	}

	/**
	 * 获取所有哈希表中的字段
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key)
	{
		return redis.hkeys(key);
	}

	/**
	 * 获取所有哈希表中的值
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key)
	{
		return redis.hvals(key);
	}

	/**
	 * 从哈希表 key 中删除指定的field
	 * @param key
	 * @param field
	 * @return
	 */
	public long hdel(String key, String... fields)
	{
		return redis.hdel(key, fields);
	}

	/**
	 * 可模糊获取redis中的key值 eg:lock*得到 所有带lock的key的set集合
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(String pattern)
	{
		return redis.keys(pattern);
	}
	
	/**
	 * 放回连接池
	 */
	public void close()
	{
		redis.close();
	}
}
