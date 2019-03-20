package dswork.sso.model;

import java.security.MessageDigest;
import java.util.Locale;

public class Authcode
{
	private String authcode = "";
	private long authtime;// 单位毫秒

	public Authcode(String authcode, long authtime)
	{
		this.authcode = authcode;
		this.authtime = authtime;
	}

	public String getAuthcode()
	{
		return authcode;
	}

	public void setAuthcode(String authcode)
	{
		this.authcode = authcode;
	}

	public long getAuthtime()
	{
		return authtime;
	}

	public void setAuthtime(long authtime)
	{
		this.authtime = authtime;
	}
	// 1000(1秒)|60000(1分钟)|3600000(1小时)|86400000(1天)
	private static final int auth_timeout = 2 * 3600000;

	/**
	 * 获取访问码
	 * @param authtime 有效时间
	 * @param secret 密钥
	 * @return 访问码
	 */
	public static Authcode code_create(String secret)
	{
		long authtime = System.currentTimeMillis() + auth_timeout;
		if(secret == null || secret.length() == 0)
		{
			secret = " ";
		}
		String authcode = encryptMd5(authtime + encryptMd5(secret));
		return new Authcode(authcode, authtime);
	}

	/**
	 * 校验authcode是否有效
	 * @param authcode 访问码
	 * @param authtime 有效时间
	 * @param secret 密钥
	 * @return 访问码是否有效
	 */
	public static boolean code_check(String authcode, long authtime, String secret)
	{
		if(secret == null || secret.length() == 0)
		{
			secret = " ";
		}
		if(authcode == null || authcode.length() == 0)
		{
			return false;
		}
		if(authtime < (System.currentTimeMillis() - auth_timeout))
		{
			return false;
		}
		try
		{
			return authcode.equals(encryptMd5(authtime + encryptMd5(secret)));
		}
		catch(Exception e)
		{
		}
		return false;
	}

	/**
	 * 处理加密码内置方法
	 * @param str 加密字符串
	 * @param type 加密类型
	 * @return String
	 */
	private static String encrypts(String str, String type)
	{
		if(str != null)
		{
			StringBuilder sb = new StringBuilder();
			try
			{
				MessageDigest md = MessageDigest.getInstance(type);
				byte[] digest = md.digest(str.getBytes("UTF-8"));
				String stmp = "";
				for(int n = 0; n < digest.length; n++)
				{
					stmp = (Integer.toHexString(digest[n] & 0XFF));
					sb.append((stmp.length() == 1) ? "0" : "").append(stmp);
				}
				return sb.toString();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				sb = null;
			}
		}
		return null;
	}

	/**
	 * MD5加密
	 * @param str 需要加密的String
	 * @return 32位MD5的String，失败返回null
	 */
	public static String encryptMd5(String str)
	{
		String code = encrypts(str, "MD5");
		if(code != null)
		{
			return code.toUpperCase(Locale.ENGLISH);
		}
		return null;
	}
}
