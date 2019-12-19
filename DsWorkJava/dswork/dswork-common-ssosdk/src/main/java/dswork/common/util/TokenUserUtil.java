package dswork.common.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import dswork.common.model.ZAuthorizecode;
import dswork.common.model.ZAuthtoken;

@Service
public class TokenUserUtil
{
	private static ConcurrentMap<String, String> user_token = new ConcurrentHashMap<String, String>();
	private static ConcurrentMap<String, String> user_code = new ConcurrentHashMap<String, String>();
	private static ConcurrentMap<String, String> user = new ConcurrentHashMap<String, String>();
	// 1000(1秒)|60000(1分钟)|3600000(1小时)|86400000(1天)
	public static final int code_timeout = 10 * 60000;
	public static final int code_timeout_second = 10 * 60;
	public static final long token_timeout = 30 * 86400000;
	public static final int token_timeout_second = 30 * 86400;
	public static final int user_timeout_second = 50 * 365 * 86400;
	private static final String secret = "TokenUserUtil";

	/**
	 * 存入值
	 * @param key
	 * @param value
	 * @param timeout
	 * @return token
	 */
	public static ZAuthorizecode codeCreate(String redirect_uri, String value)
	{
		long time = System.currentTimeMillis() + code_timeout;
		String authorizecode = dswork.core.util.EncryptUtil.encryptDes(time + "", secret);
		ZAuthorizecode code = new ZAuthorizecode(authorizecode, time, redirect_uri, value);
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			String key = authorizecode;
			db.set(key, ResponseUtil.toJson(code));
			db.expire(key, code_timeout_second);
			db.close();
		}
		else
		{
			user_code.put(authorizecode, ResponseUtil.toJson(code));
		}
		return code;
	}

	/**
	 * 获取值
	 * @param authorizecode
	 * @param del 如果为true，则获取后删除
	 * @return
	 */
	public static ZAuthorizecode codeGet(String authorizecode, boolean del)
	{
		ZAuthorizecode code = null;
		long time = 0;
		if(authorizecode != null && authorizecode.length() > 0)
		{
			try
			{
				String key = authorizecode;
				String codejson = null;
				time = Long.parseLong(dswork.core.util.EncryptUtil.decryptDes(authorizecode, secret));// 正常情况code是可被解密的
				if(ResponseUtil.USE_REDIS)
				{
					if(del)
					{
						redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
						codejson = db.get(key);
						db.del(key);
						db.close();
					}
					else
					{
						redis.clients.jedis.Jedis db = RedisUtil.db.getReadJedis();
						codejson = db.get(key);
						db.close();
					}
				}
				else
				{
					codejson = del ? user_code.remove(authorizecode) : user_code.get(authorizecode);
				}
				if(codejson != null)
				{
					code = ResponseUtil.toBean(codejson, ZAuthorizecode.class);
				}
			}
			catch(Exception e)
			{
			}
		}
		boolean out = time < System.currentTimeMillis();// 时间超时了没
		return (code == null || out) ? null : code;
	}

	/**
	 * 更新user值
	 * @param openid 即userid
	 * @param userinfoJSON
	 */
	public static void userUpdate(String openid, String userinfoJSON)
	{
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			db.set(openid, userinfoJSON == null ? "" : userinfoJSON);
			db.expire(openid, user_timeout_second);
			db.close();
		}
		else
		{
			user.put(openid, userinfoJSON == null ? "" : userinfoJSON);
		}
	}

	/**
	 * 存入值
	 * @param appkey 即unit的type
	 * @param openid 即userid
	 * @param userinfoJSON
	 * @return access_token
	 */
	public static ZAuthtoken tokenCreate(long appkey, String openid, String userinfoJSON)
	{
		long time = System.currentTimeMillis() + token_timeout;
		String access_token = dswork.core.util.EncryptUtil.encryptDes(time + "", secret);
		ZAuthtoken token = new ZAuthtoken(access_token, token_timeout_second, "", openid);
		String key = appkey + "-" + openid;
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			db.set(key, access_token);
			db.expire(key, token_timeout_second);
			db.set(openid, userinfoJSON == null ? "" : userinfoJSON);
			db.expire(openid, user_timeout_second);
			db.close();
		}
		else
		{
			user_token.put(key, access_token);
			user.put(openid, userinfoJSON == null ? "" : userinfoJSON);
		}
		return token;
	}

	/**
	 * 获取值
	 * @param appkey 即unit的type
	 * @param openid
	 * @param access_token
	 * @return userinfoJSON
	 */
	public static String tokenGet(long appkey, String openid, String access_token)
	{
		String info = null;
		String key = appkey + "-" + openid;
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getReadJedis();
			String token = db.get(key);
			if(token != null && token.equals(access_token))
			{
				info = db.get(openid);
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
			boolean out = time > System.currentTimeMillis();// 时间超时了没
			String token = user_token.get(key);
			if(token != null && token.equals(access_token))
			{
				if(out)
				{
					user_token.remove(key);
				}
				else
				{
					info = user.get(openid);
				}
			}
		}
		return info == null ? "" : info;
	}

	/**
	 * 获取值
	 * @param appkey 即unit的type
	 * @param openid
	 * @param access_token
	 * @return info
	 */
	public static boolean tokenDel(long appkey, String openid, String access_token)
	{
		boolean result = false;
		String key = appkey + "-" + openid;
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			String token = db.get(key);
			if(token != null && token.equals(access_token))
			{
				db.del(key);
				result = true;
			}
			db.close();
		}
		else
		{
			String token = user_token.get(key);
			if(token != null && token.equals(access_token))
			{
				user_token.remove(key);
				result = true;
			}
		}
		return result;
	}
}
