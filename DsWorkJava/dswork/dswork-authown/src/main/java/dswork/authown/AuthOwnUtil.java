package dswork.authown;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthOwnUtil
{
	private static final String WEB_AUTH_COOKIE = "WEB_AUTH";
	private static final String WEB_AUTH_SESSION = "WEB_AUTH_SESSION";

	public static void login(HttpServletRequest request, HttpServletResponse response, String id, String account, String name, String own)
	{
		String value = id + "&" + account + "&" + name + "&" + own;
		addCookie(response, WEB_AUTH_COOKIE, AuthDesUtil.encodeDes(value, "own"), -1, "/", null, false, false);
	}
	public static void logout(HttpServletRequest request, HttpServletResponse response)
	{
		addCookie(response, WEB_AUTH_COOKIE, "", 0, "/", null, false, false);// 删除
	}

	public static AuthOwn getUser(HttpServletRequest request)
	{
		String s = (String) request.getSession().getAttribute(WEB_AUTH_SESSION);
		if(s == null)
		{
			return null;
		}
		String[] ss = s.split("&", -1);
		if(ss.length == 4)
		{
			return new AuthOwn(ss[0], ss[1], ss[2], ss[3]);
		}
		return null;
	}

	protected static AuthOwn getUserCookie(HttpServletRequest request, HttpServletResponse response)
	{
		String s = getValue(request, WEB_AUTH_COOKIE);
		if(s == null)
		{
			return null;
		}
		s = AuthDesUtil.decodeDes(s, "own");
		String[] ss = s.split("&", -1);
		if(ss.length == 4)
		{
			return new AuthOwn(ss[0], ss[1], ss[2], ss[3]);
		}
		return null;
	}
	protected static void setUser(HttpServletRequest request, String id, String account, String name, String own)
	{
		String value = id + "&" + account + "&" + name + "&" + own;
		request.getSession().setAttribute(WEB_AUTH_SESSION, value);
	}
	protected static void clearUser(HttpServletRequest request)
	{
		request.getSession().removeAttribute(WEB_AUTH_SESSION);
	}

	/**
	 * 往客户端写入Cookie
	 * @param name cookie参数名
	 * @param value cookie参数值
	 * @param maxAge 有效时间，int(单位秒)，0:删除Cookie，-1:页面关闭时删除cookie
	 * @param path 与cookie一起传输的虚拟路径
	 * @param domain 与cookie关联的域
	 * @param isSecure 是否在https请求时才进行传输
	 * @param isHttpOnly 是否只能通过http访问
	 */
	private static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String path, String domain, boolean isSecure, boolean isHttpOnly)
	{
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		if(domain != null)
		{
			cookie.setDomain(domain);
		}
		cookie.setSecure(isSecure);
		try
		{
			Cookie.class.getMethod("setHttpOnly", boolean.class);
			cookie.setHttpOnly(isHttpOnly);
		}
		catch(Exception e)
		{
			System.out.println("MyCookie ignore setHttpOnly Method");
		}
		response.addCookie(cookie);
	}

	/**
	 * 根据cookie名称取得参数值
	 * @param name cookie参数名
	 * @return 存在返回String，不存在返回null
	 */
	private static String getValue(HttpServletRequest request, String name)
	{
		Cookie cookies[] = request.getCookies();
		String value = null;
		if(cookies != null)
		{
			Cookie cookie = null;
			for(int i = 0; i < cookies.length; i++)
			{
				cookie = cookies[i];
				if(cookie.getName().equals(name))
				{
					value = cookie.getValue();
					break;
				}
			}
		}
		return value;
	}
}
