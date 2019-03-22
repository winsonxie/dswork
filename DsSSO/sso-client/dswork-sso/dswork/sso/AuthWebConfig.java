/**
 * 调用webapi的工厂类
 */
package dswork.sso;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AuthWebConfig
{
	private static String str(java.util.Properties C, String key, String bakkey)
	{
		String v = C.getProperty(key);
		if(v == null)
		{
			if(bakkey != null)
			{
				return str(C, bakkey, null);
			}
			else
			{
				return null;
			}
		}
		return v.trim();
	}

	private static String Md5(String v)
	{
		if(v != null)
		{
			StringBuilder sb = new StringBuilder();
			try
			{
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] digest = md.digest(v.getBytes("UTF-8"));
				String stmp = "";
				for(int n = 0; n < digest.length; n++)
				{
					stmp = (Integer.toHexString(digest[n] & 0XFF));
					sb.append((stmp.length() == 1) ? "0" : "").append(stmp);
				}
				return sb.toString().toUpperCase(Locale.ENGLISH);
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
		return "";
	}

	private static String SSO_WEB_URL = "";
	private static String SYSTEM_LOGIN_URL = "";
	private static String SYSTEM_REDIRECT_URI = "";
	private static String SYSTEM_WEB_URL = "";
	private static String SYSTEM_ALIAS = "";
	private static String SYSTEM_PASSWORD = "";
	private static Set<String> ignoreURLSet = new HashSet<String>();// 无需验证页面
	private static boolean isload = false;

	private AuthWebConfig()
	{
	}
	
	public static void initSystemConfig(String webURL, String sLogin, String sRedirect, String sWeb, String sAlias, String sPassword)
	{
		SSO_WEB_URL = (webURL == null) ? "/sso" : webURL;
		SYSTEM_LOGIN_URL = (sLogin == null) ? "" : sLogin;
		if(SYSTEM_LOGIN_URL.length() > 0)
		{
			SYSTEM_LOGIN_URL = SYSTEM_LOGIN_URL + (SYSTEM_LOGIN_URL.contains("?") ? "&" : "?");
		}
		else
		{
			SYSTEM_LOGIN_URL = SSO_WEB_URL + "/user/authorize?response_type=code&";
		}
		SYSTEM_REDIRECT_URI = (sRedirect == null) ? "" : sRedirect;
		SYSTEM_WEB_URL = (sWeb == null) ? "" : sWeb;
		SYSTEM_ALIAS = sAlias == null ? "" : sAlias;
		SYSTEM_PASSWORD = sPassword == null ? "" : Md5(sPassword);
	}
	
	public static synchronized boolean loadConfig(javax.servlet.ServletContext context)
	{
		if(isload)
		{
			return true;
		}
		String configFile = String.valueOf(context.getInitParameter("dsworkSSOConfiguration")).trim();
		java.util.Properties C = new java.util.Properties();
		java.io.InputStream stream = null;
		try
		{
			stream = WebFilter.class.getResourceAsStream(configFile);
			if(stream != null)
			{
				C.load(stream);
				isload = true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try{if(stream != null) {stream.close();}}catch(Exception ioe){}
		}
		
		AuthGlobal.initConfig(str(C, "sso.appid", null), str(C, "sso.appsecret", null), str(C, "sso.apiURL", null));// 初始化全局设置

		String webURL = str(C, "sso.webURL", null);
		String sLogin = str(C, "sso.system.loginURL", null);
		String sRedirect = str(C, "sso.system.redirectURI", null);
		String sWeb = str(C, "sso.system.webURL", null);
		if(sWeb == null)
		{
			sWeb = context.getContextPath();
		}
		String sAlias = str(C, "sso.systemAlias", "sso.ssoName");
		String sPassword = str(C, "sso.systemPassword", "sso.ssoPassword");
		initSystemConfig(webURL, sLogin, sRedirect, sWeb, sAlias, sPassword);
		
		String ignoreURL = str(C, "sso.system.ignoreURL", null);
		ignoreURLSet.clear();
		ignoreURLSet.add("/sso/");
		if(ignoreURL != null && ignoreURL.length() > 0)
		{
			String[] values = ignoreURL.split(",");
			for(String value : values)
			{
				if(value.trim().length() > 0)
				{
					ignoreURLSet.add(value.trim());
				}
			}
		}
		return true;
	}

	public static String getSsoWebURL()
	{
		return SSO_WEB_URL;
	}

	public static String getSystemLoginURL()
	{
		return SYSTEM_LOGIN_URL;
	}

	public static String getSystemRedirectURI()
	{
		return SYSTEM_REDIRECT_URI;
	}

	public static String getSystemWebURL()
	{
		return SYSTEM_WEB_URL;
	}

	public static String getSystemAlias()
	{
		return SYSTEM_ALIAS;
	}

	public static String getSystemPassword()
	{
		return SYSTEM_PASSWORD;
	}

	public static boolean containsIgnoreURL(String uri)
	{
		java.util.Iterator<String> it = ignoreURLSet.iterator();
		while(it.hasNext())
		{
			String str = it.next();
			if(uri.startsWith(str))
			{
				return true;
			}
		}
		return false;
	}
}
