package dswork.common.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import dswork.common.model.ZAuthtoken;

@Service
public class TokenUnitUtil
{
	private static ConcurrentMap<String, String> unitTokenMap = new ConcurrentHashMap<String, String>();
	private static ConcurrentMap<String, String> unitTokenTempMap = new ConcurrentHashMap<String, String>();
	// 3600000小时-60000分钟-1000秒
	public static final int token_timeout = 12 * 3600000;
	public static final int token_timeout_second = token_timeout/1000;
	private static final String secret = "TokenUnitUtil";
	
	/**
	 * 存入值
	 * @param key appid
	 * @param value
	 * @param timeout
	 * @return token
	 */
	public static ZAuthtoken setUnitToken(String appid)
	{
		long time = System.currentTimeMillis() + token_timeout;
		String access_token = dswork.core.util.EncryptUtil.encryptDes(time + "", secret);
		ZAuthtoken token = new ZAuthtoken(access_token, token_timeout_second, "", "");
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			String ukey1 = "u1" + appid;
			String ukey2 = "u2" + appid;
			String oldvalue = db.get(ukey1);
			if(oldvalue != null)
			{
				long out = 0L;
				try
				{
					out = System.currentTimeMillis() - Long.parseLong(dswork.core.util.EncryptUtil.decryptDes(oldvalue, secret));
				}
				catch(Exception e)
				{
				}
				if(out > 3000)
				{
					db.setex(ukey2, (int)(out/1000), oldvalue);
				}
			}
			db.psetex(ukey1, token_timeout, access_token);
			db.close();
		}
		else
		{
			String oldvalue = unitTokenMap.get(appid);
			if(oldvalue != null)
			{
				boolean out = false;
				try
				{
					out = System.currentTimeMillis() > Long.parseLong(dswork.core.util.EncryptUtil.decryptDes(oldvalue, secret));
				}
				catch(Exception e)
				{
				}
				if(out)
				{
					unitTokenTempMap.remove(appid);// 更旧的
				}
				else
				{
					unitTokenTempMap.put(appid, oldvalue);
				}
			}
			unitTokenMap.put(appid, access_token);// 更新
		}
		return token;
	}

	/**
	 * 检查access_token是否有效
	 * @param key
	 * @param access_token
	 * @return boolean
	 */
	public static boolean checkUnitToken(String appid, String access_token)
	{
		boolean check = false;
		if(appid != null)
		{
			if(ResponseUtil.USE_REDIS)
			{
				redis.clients.jedis.Jedis db = RedisUtil.db.getReadJedis();
				String ukey1 = "u1" + appid;
				String ukey2 = "u2" + appid;
				if(access_token.equals(db.get(ukey1)))
				{
					check = true;
				}
				if(!check)
				{
					if(access_token.equals(db.get(ukey2)))
					{
						check = true;
					}
				}
				db.close();
			}
			else
			{
				long time = 0;
				try
				{
					time = Long.parseLong(dswork.core.util.EncryptUtil.decryptDes(access_token, secret));
				}
				catch(Exception e)
				{
				}
				boolean out = time < System.currentTimeMillis();// 时间超时了没
				if(unitTokenMap.get(appid).equals(access_token))
				{
					if(out)
					{
						unitTokenMap.remove(appid);
					}
					else
					{
						check = true;
					}
				}
				if(!check)
				{
					if(unitTokenTempMap.get(appid).equals(access_token))
					{
						if(out)
						{
							unitTokenTempMap.remove(appid);
						}
						else
						{
							check = true;
						}
					}
				}
			}
		}
		return check;
	}
}
