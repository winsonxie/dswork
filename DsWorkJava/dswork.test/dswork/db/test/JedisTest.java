package dswork.db.test;

import java.io.IOException;
import java.util.Set;

import dswork.db.redis.RedisUtil;
import dswork.db.redis.config.RedisConfig;

public class JedisTest
{
	public static void main(String[] args) throws IOException
	{
		RedisConfig c = new RedisConfig();
		c.setHost("192.168.101.3");
		c.setPort(6379);
		
		// 初始化对象
		RedisUtil jedis = new RedisUtil(c);
		
		// 向redis中放入(k,v)
		jedis.getJedis().set("qaq", "qaq");
		System.out.println(jedis.getReadJedis().get("qaq"));
		
		jedis.getJedis().setex("kv1", 3600, "kv1");
		System.out.println(jedis.getReadJedis().get("kv1"));
		
		
		// 对某一个key值设置过期时间 单位:s
		jedis.getJedis().expire("aaa", 6666);
		
		//  (key  (key ,val))  map操作
		jedis.getJedis().hset("map", "a", "3");
		jedis.getJedis().hset("map", "b", "3");
		jedis.getJedis().hset("map", "c", "3");
		jedis.getJedis().hgetAll("map");
		jedis.getJedis().hget("map","a");

		//queue操作
		jedis.getJedis().lpush("que1","aaa","bbb");
		jedis.getJedis().rpush("que1","aaa1","bbb1");
		jedis.getJedis().llen("que1");
//		jedis.lrange(key, start, end);
//		jedis.lrem(key, count, value);
//		jedis.ltrim(key, start, end);
		
		//set if not exists，

		
		//list集合操作  (key,list)
//		jedis.getReadJedis().getJedis().setList(key, dataList);
//		jedis.getReadJedis().getList(key);
		
		
		//keys操作
		Set<String> set = jedis.getJedis().keys("lock*");
		for(String key : set)
		{
			System.out.println(key);
		}
		jedis.getJedis().hkeys("lock*");
		jedis.getJedis().hvals("lock*");
		
	}
}


	// 是否在从池中取出连接前进行检验，如果检验失败，则从池中去除连接并尝试取出另一个
	// config.setTestOnBorrow(true);
	//// 在return给pool时，是否提前进行validate操作
	// config.setTestOnReturn(true);
	//// 在空闲时检查有效性，默认false
	// config.setTestWhileIdle(true);
	//// 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；
	//// 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
	// config.setMinEvictableIdleTimeMillis(30000);
	//// 表示idle object evitor两次扫描之间要sleep的毫秒数
	// config.setTimeBetweenEvictionRunsMillis(60000);
	//// 表示idle object evitor每次扫描的最多的对象数
	// config.setNumTestsPerEvictionRun(1000);
