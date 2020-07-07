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
	// 3600000/小时-60000/分钟-1000/秒
	private static final int token_timeout_must = 3600000;
	public static final int token_timeout = 12 * token_timeout_must;
	public static final int token_timeout_second = token_timeout/1000;
	private static final String secret = "TokenUnitUtil";
	
	/**
	 * 存入值
	 * @param appid appid
	 * @return token
	 */
	public static ZAuthtoken setUnitToken(String appid)
	{
		long time = System.currentTimeMillis() + token_timeout;
		String access_token = null;
		int timeout_second = token_timeout_second;
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = null;
			try
			{
				db = RedisUtil.db.getJedis();
				String ukey1 = "u1" + appid;
				String ukey2 = "u2" + appid;
				String oldvalue = db.get(ukey1);
				if(oldvalue != null)
				{
					long out = db.pttl(ukey1);// 以毫秒为单位返回key剩余生存时间
					if(out > token_timeout_must)// 未过期不需要重新生成
					{
						access_token = oldvalue;
						timeout_second = (int)(out / 1000);
					}
					else
					{
						if(out > 3000)// 大于3秒才保留
						{
							db.setex(ukey2, (int)(out / 1000), oldvalue);
						}
					}
				}
				if(access_token == null)
				{
					access_token = dswork.core.util.EncryptUtil.encryptDes(time + "", secret);
					db.psetex(ukey1, token_timeout, access_token);
				}
			}
			catch(Exception e)
			{
			}
			finally
			{
				db.close();
			}
		}
		else
		{
			String oldvalue = unitTokenMap.get(appid);
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
				if(out > token_timeout_must)// 未过期不需要重新生成
				{
					access_token = oldvalue;
					timeout_second = (int)(out / 1000);
				}
				else
				{
					// unitTokenTempMap.remove(appid);// 下面会设置新的值
					if(out > 3000)// 大于3秒才保留
					{
						unitTokenTempMap.put(appid, oldvalue);
					}
				}
			}
			if(access_token == null)
			{
				access_token = dswork.core.util.EncryptUtil.encryptDes(time + "", secret);
				unitTokenMap.put(appid, access_token);// 更新
			}
		}
		ZAuthtoken token = new ZAuthtoken(access_token, timeout_second, "", "");
		return token;
	}

	/**
	 * 检查access_token是否有效
	 * @param appid appid
	 * @param access_token access_token
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
