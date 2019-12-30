package dswork.db.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class MyRedis
{
	private Jedis redis;
	public MyRedisMap map;
	public MyRedisList list;
	
	public MyRedis(Jedis redis)
	{
		this.redis= redis;
		this.map = new MyRedisMap(redis);
		this.list = new MyRedisList(redis);
	}

	/**
	 * 删除key
	 * @param keys 多个键
	 * @return long
	 */
	public long del(String... keys)
	{
		return redis.del(keys);
	}

	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return boolean
	 */
	public boolean exists(String key)
	{
		return redis.exists(key);
	}

	/**
	 * 设置key的过期时间
	 * @param key 键
	 * @param seconds 过期时间，单位：秒
	 * @return long
	 */
	public long expire(String key, int seconds)
	{
		return redis.expire(key, seconds);
	}

	/**
	 * 获取值
	 * @param key 键
	 * @return 值
	 */
	public String get(String key)
	{
		return redis.get(key);
	}

	/**
	 * 可模糊获取redis中的key值 eg:lock*得到 所有带lock的key的set集合
	 * @param pattern 匹配值
	 * @return 集合
	 */
	public Set<String> keys(String pattern)
	{
		return redis.keys(pattern);
	}

	/**
	 * 设置值
	 * @param key 键
	 * @param value 值
	 * @return 值
	 */
	public String set(String key, String value)
	{
		return redis.set(key, value);
	}

	/**
	 * set if not exists，若key已存在，则setnx不做任何操作
	 * @param key 键
	 * @param value 值
	 * @return long
	 */
	public long setnx(String key, String value)
	{
		return redis.setnx(key, value);
	}

	/**
	 * 设置key的过期时间和值
	 * @param key 键
	 * @param expire 过期时间，单位：秒
	 * @param value 值
	 * @return 值
	 */
	public String setex(String key, int expire, String value)
	{
		return redis.setex(key, expire, value);
	}
	
	
	class MyRedisMap
	{
		private Jedis redis;
		
		public MyRedisMap(Jedis redis)
		{
			this.redis = redis;
		}

		/**
		 * 从哈希表 key 中删除指定的field
		 * @param key 键
		 * @param fields 多个集合键
		 * @return long
		 */
		public long hdel(String key, String... fields)
		{
			return redis.hdel(key, fields);
		}

		/**
		 * 查看哈希表 key 中，指定的field字段是否存在。
		 * @param key 键
		 * @param field 集合键
		 * @return boolean
		 */
		public boolean hexists(String key, String field)
		{
			return redis.hexists(key, field);
		}

		/**
		 * 获取缓存的Map值
		 * @param key 键
		 * @param field 集合键
		 * @return 集合值
		 */
		public String hget(String key, String field)
		{
			return redis.hget(key, field);
		}

		/**
		 * 获取map所有的字段和值
		 * @param key 键
		 * @return 集合
		 */
		public Map<String, String> hgetAll(String key)
		{
			return redis.hgetAll(key);
		}

		/**
		 * 获取所有哈希表中的字段
		 * @param key 键
		 * @return 集合
		 */
		public Set<String> hkeys(String key)
		{
			return redis.hkeys(key);
		}
		
		/**
		 * 缓存Map赋值
		 * @param key 键
		 * @param field 集合键
		 * @param value 集合
		 * @return long
		 */
		public long hset(String key, String field, String value)
		{
			return redis.hset(key, field, value);
		}

		/**
		 * 获取所有哈希表中的值
		 * @param key 键
		 * @return 集合
		 */
		public List<String> hvals(String key)
		{
			return redis.hvals(key);
		}
	}
	
	class MyRedisList
	{
		private Jedis redis;
		
		public MyRedisList(Jedis redis)
		{
			this.redis = redis;
		}
	}
	
	/**
	 * 放回连接池
	 */
	public void close()
	{
		redis.close();
	}
}
