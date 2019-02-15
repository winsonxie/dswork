package dswork.db.redis.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RedisConfig
{
	private String host = "0.0.0.0";
	private String password = null;
	private int dababase = 0;
	private int port = 6379;
	private int minIdle = 5;// 最小空闲数
	private int maxIdle = 64;// 最大空闲数
	private int maxTotal = 64;// 最大链接数
	private int maxWaitMillis = 10000;// 等待可用连接的最大时间
	private boolean testOnBorrow = true;// 在空闲时检查有效性，默认false
	private boolean testOnReturn = false;// 在return给pool时，是否提前进行validate操作
	private boolean testWhileIdle = true;// 在空闲时检查有效性
	private int minEvictableIdleTimeMillis = 60000; // 连接在池中最小生存的时间
	private int timeBetweenEvictionRunsMillis = 30000;// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接
	private int numTestsPerEvictionRun = -1;// 表示idle object evitor每次扫描的最多的对象数
	private int connectTimeout = 2000;// 连接超时时间
	private int soTimeout = 2000;// 读取数据超时时间
	private String clientName = null;// 默认直接为null即可
	boolean ssl = false;
	SSLSocketFactory sslSocketFactory = null;
	SSLParameters sslParameters = null;
	HostnameVerifier hostnameVerifier = null;

	public String getHost()
	{
		return host;
	}

	public RedisConfig setHost(String host)
	{
		this.host = host;
		return this;
	}

	public int getPort()
	{
		return port;
	}

	public RedisConfig setPort(int port)
	{
		this.port = port;
		return this;
	}

	public String getPassword()
	{
		return password;
	}

	public RedisConfig setPassword(String password)
	{
		this.password = password;
		return this;
	}

	public int getDababase()
	{
		return dababase;
	}

	public RedisConfig setDababase(int dababase)
	{
		this.dababase = dababase;
		return this;
	}

	public int getMinIdle()
	{
		return minIdle;
	}

	public RedisConfig setMinIdle(int minIdle)
	{
		this.minIdle = minIdle;
		return this;
	}

	public int getMaxIdle()
	{
		return maxIdle;
	}

	public RedisConfig setMaxIdle(int maxIdle)
	{
		this.maxIdle = maxIdle;
		return this;
	}

	public int getMaxTotal()
	{
		return maxTotal;
	}

	public RedisConfig setMaxTotal(int maxTotal)
	{
		this.maxTotal = maxTotal;
		return this;
	}

	public int getMaxWaitMillis()
	{
		return maxWaitMillis;
	}

	public RedisConfig setMaxWaitMillis(int maxWaitMillis)
	{
		this.maxWaitMillis = maxWaitMillis;
		return this;
	}

	public boolean isTestOnBorrow()
	{
		return testOnBorrow;
	}

	public RedisConfig setTestOnBorrow(boolean testOnBorrow)
	{
		this.testOnBorrow = testOnBorrow;
		return this;
	}

	public boolean isTestOnReturn()
	{
		return testOnReturn;
	}

	public RedisConfig setTestOnReturn(boolean testOnReturn)
	{
		this.testOnReturn = testOnReturn;
		return this;
	}

	public boolean isTestWhileIdle()
	{
		return testWhileIdle;
	}

	public RedisConfig setTestWhileIdle(boolean testWhileIdle)
	{
		this.testWhileIdle = testWhileIdle;
		return this;
	}

	public int getMinEvictableIdleTimeMillis()
	{
		return minEvictableIdleTimeMillis;
	}

	public RedisConfig setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis)
	{
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
		return this;
	}

	public int getTimeBetweenEvictionRunsMillis()
	{
		return timeBetweenEvictionRunsMillis;
	}

	public RedisConfig setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis)
	{
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
		return this;
	}

	public int getNumTestsPerEvictionRun()
	{
		return numTestsPerEvictionRun;
	}

	public RedisConfig setNumTestsPerEvictionRun(int numTestsPerEvictionRun)
	{
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
		return this;
	}

	public int getConnectTimeout()
	{
		return connectTimeout;
	}

	public RedisConfig setConnectTimeout(int connectTimeout)
	{
		this.connectTimeout = connectTimeout;
		return this;
	}

	public int getSoTimeout()
	{
		return soTimeout;
	}

	public RedisConfig setSoTimeout(int soTimeout)
	{
		this.soTimeout = soTimeout;
		return this;
	}

	/**
	 * 设置connectTimeout和soTimeout
	 * @param timeout
	 * @return
	 */
	public RedisConfig setTimeout(int timeout)
	{
		this.connectTimeout = timeout;
		this.soTimeout = timeout;
		return this;
	}

	public String getClientName()
	{
		return clientName;
	}

	public RedisConfig setClientName(String clientName)
	{
		this.clientName = clientName;
		return this;
	}

	public boolean isSsl()
	{
		return ssl;
	}

	public RedisConfig setSsl(boolean ssl)
	{
		this.ssl = ssl;
		return this;
	}

	public SSLSocketFactory getSslSocketFactory()
	{
		return sslSocketFactory;
	}

	public RedisConfig setSslSocketFactory(SSLSocketFactory sslSocketFactory)
	{
		this.sslSocketFactory = sslSocketFactory;
		return this;
	}

	public SSLParameters getSslParameters()
	{
		return sslParameters;
	}

	public RedisConfig setSslParameters(SSLParameters sslParameters)
	{
		this.sslParameters = sslParameters;
		return this;
	}

	public HostnameVerifier getHostnameVerifier()
	{
		return hostnameVerifier;
	}

	public RedisConfig setHostnameVerifier(HostnameVerifier hostnameVerifier)
	{
		this.hostnameVerifier = hostnameVerifier;
		return this;
	}

	public GenericObjectPoolConfig<Object> getConfig()
	{
		GenericObjectPoolConfig<Object> config = new GenericObjectPoolConfig<Object>();
		config.setMinIdle(getMinIdle());
		config.setMaxIdle(getMaxIdle());
		config.setMaxTotal(getMaxTotal());
		config.setTestOnBorrow(isTestOnBorrow());
		config.setTestOnReturn(isTestOnReturn());
		config.setTestWhileIdle(isTestWhileIdle());
		config.setMinEvictableIdleTimeMillis(getMinEvictableIdleTimeMillis());
		config.setTimeBetweenEvictionRunsMillis(getTimeBetweenEvictionRunsMillis());
		config.setNumTestsPerEvictionRun(getNumTestsPerEvictionRun());
		config.setMaxWaitMillis(getMaxWaitMillis());
		return config;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("redis://");
		if(getPassword() != null)
		{
			sb.append(getPassword()).append("@");
		}
		return sb.append(getHost()).append(":").append(getPort()).append("/").append(getDababase()).toString();
	}
}
