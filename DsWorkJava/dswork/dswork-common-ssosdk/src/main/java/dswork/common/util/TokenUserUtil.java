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
	public static final long token_timeout = 30 * 86400000L;
	public static final int token_timeout_second = 30 * 86400;
	public static final long user_timeout = 365 * 86400000L;
	private static final String secret = "TokenUserUtil";
	private static final String SSO = "SSO";// 二级key
	private static final String UT = "UT";

	private static String getKey(long appkey, String openid)
	{
		StringBuilder sb = new StringBuilder(20);
		return sb.append(UT).append(appkey).append("-").append(openid).toString();
	}

	/**
	 * 存入值
	 * @param redirect_uri 重定向地址
	 * @param value code
	 * @return ZAuthorizecode
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
			db.psetex(key, code_timeout, ResponseUtil.toJson(code));
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
	 * @param authorizecode authorizecode
	 * @param del 如果为true，则获取后删除
	 * @return ZAuthorizecode
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
	 * 存入值
	 * @param appkey 即unit的type
	 * @param openid 即userid
	 * @param userinfoJSON json
	 * @return ZAuthtoken
	 */
	public static ZAuthtoken tokenCreate(long appkey, String openid, String userinfoJSON)
	{
		long time = System.currentTimeMillis() + token_timeout;
		String access_token = dswork.core.util.EncryptUtil.encryptDes(time + "", secret);
		ZAuthtoken token = new ZAuthtoken(access_token, token_timeout_second, "", openid);
		String key = getKey(appkey, openid);
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			db.psetex(key, token_timeout, access_token);
			// db.psetex(openid, user_timeout, userinfoJSON == null ? "" : userinfoJSON);
			db.hset(openid, SSO, userinfoJSON == null ? "" : userinfoJSON);
			db.pexpire(openid, user_timeout);
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
	 * @param openid openid
	 * @param access_token access_token
	 * @return boolean
	 */
	public static boolean tokenDel(long appkey, String openid, String access_token)
	{
		boolean result = false;
		String key = getKey(appkey, openid);
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

	/**
	 * 获取值
	 * @param appkey 即unit的type
	 * @param openid openid
	 * @param access_token access_token
	 * @return userinfoJSON
	 */
	public static String tokenGet(long appkey, String openid, String access_token)
	{
		String info = null;
		String key = getKey(appkey, openid);
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getReadJedis();
			String token = db.get(key);
			if(token != null && token.equals(access_token))
			{
				// info = db.get(openid);
				info = db.hget(openid, SSO);
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
	 * 更新user值
	 * @param openid 即userid
	 * @param userinfoJSON json
	 */
	public static void userUpdate(String openid, String userinfoJSON)
	{
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			// db.psetex(openid, user_timeout, userinfoJSON == null ? "" : userinfoJSON);
			db.hset(openid, SSO, userinfoJSON == null ? "" : userinfoJSON);
			db.pexpire(openid, user_timeout);
			db.close();
		}
		else
		{
			user.put(openid, userinfoJSON == null ? "" : userinfoJSON);
		}
	}
}
