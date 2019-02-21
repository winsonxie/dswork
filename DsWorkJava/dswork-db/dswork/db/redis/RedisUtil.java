package dswork.db.redis;

import dswork.db.redis.config.RedisConfig;
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

	public Jedis getJedis()
	{
		return jedisPool.getResource();
	}

	public Jedis getReadJedis()
	{
		return jedisReadPool.getResource();
	}

	public MyRedis getRedis()
	{
		return new MyRedis(jedisPool.getResource());
	}

	public MyRedis getReadRedis()
	{
		return new MyRedis(jedisReadPool.getResource());
	}
}
