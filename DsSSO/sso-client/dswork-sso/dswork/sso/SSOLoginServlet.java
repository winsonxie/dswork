package dswork.sso;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dswork.sso.model.AccessToken;
import dswork.sso.model.IUser;
import dswork.sso.model.JsonResult;

@WebServlet(name = "SSOLoginServlet", loadOnStartup = 1, urlPatterns =
{
		"/sso/login"
})
public class SSOLoginServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	public static final String LOGINER = "sso.web.loginer";
	static com.google.gson.Gson gson = AuthGlobal.gson;
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	private static Set<String> ignoreURLSet = new HashSet<String>();// 无需验证页面

	public SSOLoginServlet()
	{
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		String configFile = String.valueOf(this.getServletContext().getInitParameter("dsworkSSOConfiguration")).trim();
		java.util.Properties CONFIG = new java.util.Properties();
		java.io.InputStream stream = WebFilter.class.getResourceAsStream(configFile);
		if(stream != null)
		{
			try
			{
				CONFIG.load(stream);
			}
			catch(Exception e)
			{
			}
			finally
			{
				try
				{
					stream.close();
				}
				catch(IOException ioe)
				{
				}
			}
		}
		String appid, appsecret, apiURL;
		appid = str(CONFIG, "sso.appid", null, "portal");
		appsecret = str(CONFIG, "sso.appsecret", null, "portal");
		apiURL = str(CONFIG, "sso.apiURL", null, null);
		AuthGlobal.runAppConfig(appid, appsecret, apiURL);// 初始化全局设置
		String systemAlias, systemPassword, redirect_uri, web_url, login_url;
		systemAlias = str(CONFIG, "sso.systemAlias", "sso.ssoName", "");
		systemPassword = str(CONFIG, "sso.systemPassword", "sso.ssoPassword", "");
		redirect_uri = str(CONFIG, "sso.redirectURI", "sso.systemURL", "");
		web_url = str(CONFIG, "sso.webURL", null, "/sso");
		login_url = str(CONFIG, "sso.loginURL", null, "");
		AuthFactory.initConfig(systemAlias, systemPassword, redirect_uri, web_url, login_url);
		String ignoreURL = str(CONFIG, "sso.ignoreURL", null, "");
		// ignoreURLSet.clear();
		ignoreURLSet.add("/sso/login");
		ignoreURLSet.add("/sso/logout");
		ignoreURLSet.add("/sso/menu");
		if(ignoreURL.length() > 0)
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
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String code = request.getParameter("code");
			String redirect_uri = request.getParameter("redirect_uri");// 需要解码，通常是本项目地址，否则拿不到用户
			if(redirect_uri == null)
			{
				redirect_uri = request.getParameter("service");// 需要解码
			}
			if(redirect_uri == null)
			{
				redirect_uri = AuthFactory.getRedirectUri();
			}
			else
			{
				redirect_uri = java.net.URLDecoder.decode(redirect_uri, "UTF-8");
			}
			JsonResult<AccessToken> token = AuthFactory.getUserAccessToken(code);
			if(token.getCode() == AuthGlobal.CODE_001)
			{
				AccessToken t = token.getData();
				JsonResult<IUser> u = AuthFactory.getUserUserinfo(t.getOpenid(), t.getAccess_token());
				if(u.getCode() == AuthGlobal.CODE_001)
				{
					refreshUser(request.getSession(), u.getData(), t.getOpenid(), t.getAccess_token());
				}
			}
			response.sendRedirect(redirect_uri);// 成功失败都跳转
		}
		catch(Exception ex)
		{
			log.error(ex.getMessage());
			ex.printStackTrace();
		}
		return;
	}

	public static boolean refreshUser(HttpSession session, IUser user, String openid, String access_token)
	{
		if(user != null)
		{
			user.setSsoticket(openid, access_token);
			session.setAttribute(LOGINER, gson.toJson(user));
			return true;
		}
		else
		{
			// 登录不成则退出
			session.removeAttribute(LOGINER);
			return false;
		}
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

	private static String str(java.util.Properties CONFIG, String key, String bakkey, String defaultValue)
	{
		String v = CONFIG.getProperty(key);
		if(v == null)
		{
			if(bakkey != null)
			{
				return str(CONFIG, bakkey, null, defaultValue);
			}
			else
			{
				return defaultValue;
			}
		}
		return v.trim();
	}
}
