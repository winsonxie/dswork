package dswork.common.model;

import dswork.core.util.EncryptUtil;

public class ZAuthcode
{
	private String code = "";
	private long expires_in;// 单位毫秒

	public ZAuthcode(String code, long expires_in)
	{
		this.code = code;
		this.expires_in = expires_in;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public long getExpires_in()
	{
		return expires_in;
	}

	public void setExpires_in(long expires_in)
	{
		this.expires_in = expires_in;
	}
	

	// 1000(1秒)|60000(1分钟)|3600000(1小时)|86400000(1天)
	private static final int auth_timeout = 24 * 3600000;
	/**
	 * 获取访问码
	 * @param secret 密钥
	 * @return 访问码
	 */
	public static ZAuthcode code_create(String secret)
	{
		long authtime = System.currentTimeMillis() + auth_timeout;
		if(secret == null || secret.length() == 0)
		{
			secret = " ";
		}
		String authcode = EncryptUtil.encryptMd5(authtime + EncryptUtil.encryptMd5(secret));
		return new ZAuthcode(authcode, authtime);
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
			return authcode.equals(EncryptUtil.encryptMd5(authtime + EncryptUtil.encryptMd5(secret)));
		}
		catch(Exception e)
		{
		}
		return false;
	}
}
