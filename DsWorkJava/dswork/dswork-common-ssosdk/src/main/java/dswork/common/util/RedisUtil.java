package dswork.common.util;

import dswork.core.util.EnvironmentUtil;
import dswork.db.redis.config.RedisConfig;

public class RedisUtil
{
	private static String host = EnvironmentUtil.getToString("redis.host", "127.0.0.1");
	private static String password = EnvironmentUtil.getToString("redis.password", null);
	private static int database = (new Long(EnvironmentUtil.getToLong("redis.database", 0))).intValue();
	private static int port = (new Long(EnvironmentUtil.getToLong("redis.port", 6379))).intValue();
	private static int minIdle = (new Long(EnvironmentUtil.getToLong("redis.minIdle", 5))).intValue();// 最小空闲数
	private static int maxIdle = (new Long(EnvironmentUtil.getToLong("redis.maxIdle", 64))).intValue();// 最大空闲数
	private static int maxTotal = (new Long(EnvironmentUtil.getToLong("redis.maxTotal", 64))).intValue();// 最大链接数
	private static int maxWaitMillis = (new Long(EnvironmentUtil.getToLong("redis.maxWaitMillis", 10000))).intValue();// 等待可用连接的最大时间
	private static boolean testOnBorrow = EnvironmentUtil.getToBoolean("redis.testOnBorrow", true);// 在空闲时检查有效性，默认true
	private static boolean testOnReturn = EnvironmentUtil.getToBoolean("redis.testOnReturn", false);// 在return给pool时，是否提前进行validate操作
	private static boolean testWhileIdle = EnvironmentUtil.getToBoolean("redis.testWhileIdle", true);// 在空闲时检查有效性
	private static int minEvictableIdleTimeMillis = (new Long(EnvironmentUtil.getToLong("redis.minEvictableIdleTimeMillis", 60000))).intValue();; // 连接在池中最小生存的时间
	private static int timeBetweenEvictionRunsMillis = (new Long(EnvironmentUtil.getToLong("redis.timeBetweenEvictionRunsMillis", 30000))).intValue();;// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接
	private static int numTestsPerEvictionRun = (new Long(EnvironmentUtil.getToLong("redis.numTestsPerEvictionRun", -1))).intValue();;// 表示idle object evitor每次扫描的最多的对象数
	private static int connectTimeout = (new Long(EnvironmentUtil.getToLong("redis.connectTimeout", 2000))).intValue();;// 连接超时时间
	private static int soTimeout = (new Long(EnvironmentUtil.getToLong("redis.soTimeout", 2000))).intValue();;// 读取数据超时时间
	private static String clientName = EnvironmentUtil.getToString("redis.clientName", null);;// 默认直接为null即可
	
	private static RedisConfig config = new RedisConfig()
			.setHost(host)
			.setPassword(password)
			.setDatabase(database)
			.setPort(port)
			.setMinIdle(minIdle)
			.setMaxIdle(maxIdle)
			.setMaxTotal(maxTotal)
			.setMaxWaitMillis(maxWaitMillis)
			.setTestOnBorrow(testOnBorrow)
			.setTestOnReturn(testOnReturn)
			.setTestWhileIdle(testWhileIdle)
			.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis)
			.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis)
			.setNumTestsPerEvictionRun(numTestsPerEvictionRun)
			.setConnectTimeout(connectTimeout)
			.setSoTimeout(soTimeout)
			.setClientName(clientName);
	
	public static dswork.db.redis.RedisUtil db = new dswork.db.redis.RedisUtil(config);
}
