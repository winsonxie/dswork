package dswork.common.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

@Service
public class TokenSmsUtil
{
	private static ConcurrentMap<String, String> smsMap = new ConcurrentHashMap<String, String>();
	// 1000(1秒)|60000(1分钟)|3600000(1小时)|86400000(1天)
	public static final int sms_timeout = 20 * 60000;

	/**
	 * 存入值
	 * @param key appid + mobile
	 * @param code 短信
	 * @param timeout 超时时间TokenSmsUtil.sms_timeout，默认20分钟
	 */
	public static void smscodeSet(String key, String code, int timeout)
	{
		if(key == null)
		{
			key = "";
		}
		if(code == null)
		{
			code = "";
		}
		long time = System.currentTimeMillis() + timeout;
		if(ResponseUtil.USE_REDIS)
		{
			String mkey = "u0" + key;
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			db.psetex(mkey, timeout, code);
			db.close();
		}
		else
		{
			smsMap.put(key, code + time);
		}
	}

	/**
	 * 获取值，如果timeout小于等于0取完删掉
	 * @param key appid + mobile
	 * @return 短信验证码，不存在返回空
	 */
	public static String smscodeGet(String key)
	{
		String value = "";
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getReadJedis();
			value = db.get("u0" + key);
			db.close();
			return value == null ? "" : value;
		}
		else
		{
			value = smsMap.remove(key);
			long time = 0;
			if(value != null)
			{
				try
				{
					time = Long.parseLong(value.substring(6, value.length()));
					value = value.substring(0, 6);
				}
				catch(Exception e)
				{
					time = 0;
				}
			}
			boolean out = time < System.currentTimeMillis();// 时间超时了没
			return (value == null || out) ? "" : value;
		}
	}
}
