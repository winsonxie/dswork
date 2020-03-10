package dswork.db.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class MyRedis
{
	private Jedis redis;
	public MyRedisString string;
	public MyRedisMap map;
	public MyRedisList list;

	public MyRedis(Jedis redis)
	{
		this.redis = redis;
		this.string = new MyRedisString(redis);
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
	 * 设置key的过期时间
	 * @param key 键
	 * @param unixTime 过期时间的UNIX时间戳(unix timestamp)，单位：毫秒
	 * @return long
	 */
	public long expireAt(String key, long unixTime)
	{
		return redis.expireAt(key, unixTime);
	}

	/**
	 * 设置key的过期时间
	 * @param key 键
	 * @param milliseconds 过期时间，单位：毫秒
	 * @return long
	 */
	public long pexpire(String key, long milliseconds)
	{
		return redis.pexpire(key, milliseconds);
	}

	/**
	 * 设置key的过期时间
	 * @param key 键
	 * @param milliseconds 过期时间的时间戳(unix timestamp)，单位：毫秒
	 * @return long
	 */
	public long pexpire(String key, long millisecondsTimestamp)
	{
		return redis.pexpireAt(key, millisecondsTimestamp;
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
	 * 将当前数据库的key移动到给定的数据库db当中
	 * @param key 键
	 * @param dbIndex 给定的数据库
	 * @return 移动成功返回 1 ，失败则返回 0 。
	 */
	public long move(String key, int dbIndex)
	{
		return redis.move(key, dbIndex);
	}

	/**
	 * 移除key的过期时间，key将持久保持
	 * @param key 键
	 * @return 当过期时间移除成功时，返回 1 。 如果 key 不存在或 key 没有设置过期时间，返回 0 。
	 */
	public long persist(String key)
	{
		return redis.persist(key);
	}

	/**
	 * 以毫秒为单位返回key的剩余过期时间
	 * @param key 键
	 * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以毫秒为单位，返回 key 的剩余生存时间。在 Redis 2.8 以前，当 key 不存在，或者 key
	 *         没有设置剩余生存时间时，命令都返回 -1 。
	 */
	public long pttl(String key)
	{
		return redis.pttl(key);
	}

	/**
	 * 以秒为单位返回key的剩余过期时间
	 * @param key 键
	 * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。在 Redis 2.8 以前，当 key 不存在，或者 key
	 *         没有设置剩余生存时间时，命令都返回 -1 。
	 */
	public long ttl(String key)
	{
		return redis.ttl(key);
	}

	/**
	 * 从当前数据库中随机返回一个key
	 * @return 当数据库不为空时，返回一个 key 。 当数据库为空时，返回 nil （windows 系统返回 null）。
	 */
	public String randomKey()
	{
		return redis.randomKey();
	}

	/**
	 * 修改oldkey的名称，改名成功时提示OK，失败时候返回一个错误(ERR no such key)。
	 * @param oldkey 旧键
	 * @param newkey 梳妆打扮值
	 * @return 结果
	 */
	public String rename(String oldkey, String newkey)
	{
		return redis.rename(oldkey, newkey);
	}

	/**
	 * 仅当 newkey不存在时，将oldkey改名为newkey
	 * @param oldkey 旧键
	 * @param newkey 梳妆打扮值
	 * @return 修改成功时，返回 1 。 如果 NEW_KEY_NAME 已经存在，返回 0 。
	 */
	public long rename(String oldkey, String newkey)
	{
		return redis.renamenx(oldkey, newkey);
	}

	/**
	 * 返回key所储存的值的类型
	 * @param key 键
	 * @return 结果。none(key不存在)、string(字符串)、list(列表)、set(集合)、zset(有序集)、hash(哈希表)
	 */
	public String rename(String key)
	{
		return redis.type(key);
	}

	class MyRedisString
	{
		private Jedis redis;

		public MyRedisString(Jedis redis)
		{
			this.redis = redis;
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
		 * 获取存储在指定 key 中字符串的子字符串。字符串的截取范围由startOffset和endOffset两个偏移量决定(包括startOffset和endOffset在内)
		 * @param key 键
		 * @param startOffset 开始偏移量
		 * @param endOffset 结束偏移量
		 * @return 值
		 */
		public String getrange(String key, long startOffset, long endOffset)
		{
			return redis.getrange(key, startOffset, endOffset);
		}
		/**
		 * 设置指定key的值，并返回key的旧值。key不存在时，返回nil。
		 * @param key 键
		 * @param value 值
		 * @return 旧值
		 */
		public String getSet(String key, String value)
		{
			return redis.getSet(key, value);
		}
		
		/**
		 * 返回所有给定的key的值
		 * @param keys 一个键或多个键
		 * @return 集合
		 */
		public List<String> getSet(String... keys)
		{
			return redis.mget(keys);
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
		 * @param seconds 过期时间，单位：秒
		 * @param value 值
		 * @return 值
		 */
		public String setex(String key, int seconds, String value)
		{
			return redis.setex(key, seconds, value);
		}
		
		//STRLEN key
		//返回 key 所储存的字符串值的长度
		
		//MSET key value [key value ...]
		//同时设置一个或多个 key-value 对。
		

		/**
		 * 同时设置一个或多个key-value对，当且仅当所有给定key都不存在
		 * @param keyvalues 一个或多个键值对 key1 value1 key2 value2 .. keyN valueN
		 * @return long 当所有key都成功设置，返回 1，如果所有给定key都设置失败(至少有一个key已经存在)，那么返回 0 
		 */
		public long msetnx(String... keyvalues)
		{
			return redis.msetnx(keyvalues);
		}

		/**
		 * 设置key的过期时间和值
		 * @param key 键
		 * @param milliseconds 过期时间，单位：毫秒
		 * @param value 值
		 * @return 值
		 */
		public String psetex(String key, long milliseconds, String value)
		{
			return redis.psetex(key, milliseconds, value);
		}

		/**
		 * 数字值自增一
		 * 如果key不存在，那么key的值会先被初始化为0，然后再执行操作。
		 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误(ERR value is not an integer or out of rang)。
		 * 操作的值限制在 64 位(bit)有符号数字表示之内。
		 * @param key 键
		 * @return 执行命令之后key的值
		 */
		public long incr(String key)
		{
			return redis.incr(key);
		}

		/**
		 * 数字值增加increment
		 * 如果key不存在，那么key的值会先被初始化为0，然后再执行操作。
		 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误(ERR value is not an integer or out of rang)。
		 * 操作的值限制在 64 位(bit)有符号数字表示之内。
		 * @param key 键
		 * @param increment 增量值
		 * @return 执行命令之后key的值
		 */
		public long incrBy(String key, long increment)
		{
			return redis.incrBy(key, increment);
		}

		/**
		 * 数字值增加浮点数increment
		 * 如果key不存在，那么key的值会先被初始化为0，然后再执行操作。
		 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
		 * @param key 键
		 * @param increment 浮点数增量值
		 * @return 执行命令之后key的值
		 */
		public long incrByFloat(String key, double increment)
		{
			return redis.incrByFloat(key, increment);
		}

		/**
		 * 数字值自减一
		 * 如果key不存在，那么key的值会先被初始化为0，然后再执行操作。
		 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误(ERR value is not an integer or out of rang)。
		 * 操作的值限制在 64 位(bit)有符号数字表示之内。
		 * @param key 键
		 * @return 执行命令之后key的值
		 */
		public long decr(String key)
		{
			return redis.decr(key);
		}

		/**
		 * 数字值减少increment
		 * 如果key不存在，那么key的值会先被初始化为0，然后再执行操作。
		 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误(ERR value is not an integer or out of rang)。
		 * 操作的值限制在 64 位(bit)有符号数字表示之内。
		 * @param key 键
		 * @param decrement 减量值
		 * @return 执行命令之后key的值
		 */
		public long decrBy(String key, long decrement)
		{
			return redis.decrBy(key, decrement);
		}
		

		/**
		 * 为指定的key追加值
		 * 如果key已经存在并且是一个字符串， APPEND命令将value追加到key原来的值的末尾。
		 * 如果key不存在，则相当于set
		 * @param key 键
		 * @param value 追加值
		 * @return 长度，追加指定值之后，key中字符串的长度
		 */
		public long append(String key, String value)
		{
			return redis.append(key, value);
		}
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

		public String lpop(String key)
		{
			return redis.lpop(key);
		}

		public String rpop(String key)
		{
			return redis.rpop(key);
		}

		public Long lpush(String key, String... strings)
		{
			return redis.lpush(key, strings);
		}

		public Long rpush(String key, String... strings)
		{
			return redis.rpush(key, strings);
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
