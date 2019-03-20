package dswork.sso;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dswork.sso.http.HttpUtil;
import dswork.sso.model.IUser;
import dswork.sso.model.JsonResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebFilter implements Filter
{
	static com.google.gson.Gson gson = AuthGlobal.gson;
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	
	private static boolean use = false;// 用于判断是否加载sso模块

	public final static String OPENID = "openid";
	public final static String ACCESS_TOKEN = "access_token";
	public final static String LOGINER = "sso.web.loginer";
	public final static String TICKET = "sso.web.ticket";
	
	private static Set<String> ignoreURLSet = new HashSet<String>();// 无需验证页面

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		HttpSession session = request.getSession();
		
		@SuppressWarnings("unchecked")
		Map<String, String> ouser = (Map<String, String>)session.getAttribute(LOGINER);
		
		String relativeURI = request.getServletPath();// 相对地址
		if(log.isInfoEnabled())
		{
			log.info("当前访问地址：" + request.getContextPath() + relativeURI);
			log.info(ouser != null ? "当前登录用户是：" + ouser.get("account") : "当前没有登录用户");
		}
		
		// 判断是否有ticket
		String openid = request.getParameter(OPENID);
		String access_token = request.getParameter(ACCESS_TOKEN);
		
		// 判断是否有ticket，当且仅当两者都有值时才当是
		if(openid != null && access_token != null && openid.length() > 0 && access_token.length() > 0)// ticket非空,由链接传来ticket
		{
			// 登录只是从get上传过来的，非get直接忽略
			String qstr = request.getQueryString();
			if(qstr.contains("openid="+openid) && qstr.contains("access_token="+access_token))
			{
				if(!validateTicket(session, openid, access_token))// 一样的ticket，这里的session放的ticket不是ssoticket
				{
					if(log.isDebugEnabled())
					{
						log.debug("ticket不相等，需要更新用户");
					}
					if(refreshUser(session, openid, access_token))
					{
						chain.doFilter(request, response);
						return;
					}
					ouser = null;
				}
			}
		}
		if(ouser != null)// 已登录
		{
			chain.doFilter(request, response);
			return;
		}
		
		// String relativeURI = request.getRequestURI();// 相对地址
		// if(request.getContextPath().length() > 0){relativeURI = relativeURI.replaceFirst(request.getContextPath(), "");}
		if(ignoreURLSet.contains(relativeURI))// 判断是否为无需验证页面
		{
			chain.doFilter(request, response);// 是无需验证页面
			return;
		}
		
		if("XMLHttpRequest".equals(String.valueOf(request.getHeader("X-Requested-With"))))
		{
			
			response.setHeader("P3P", "CP=CAO PSA OUR");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().print("{\"code\":401}");// 401未登录
			return;
		}
		
		response.sendRedirect(AuthFactory.getUserAuthorizeURL(request.getRequestURI()));// 这里缺少判断是否有自己的登录页面
		return;
	}

	public void init(FilterConfig config) throws ServletException
	{
		String ssoURL, ssoName, ssoPassword, ignoreURL, hasSameDoamin;
		String configFile = String.valueOf(config.getServletContext().getInitParameter("dsworkSSOConfiguration")).trim();
		java.util.Properties CONFIG = new java.util.Properties();
		java.io.InputStream stream = WebFilter.class.getResourceAsStream(configFile);
		if (stream != null)
		{
			try{CONFIG.load(stream);}
			catch (Exception e){}
			finally{try{stream.close();}catch (IOException ioe){}}
		}

		String appid,appsecret,apiURL;
		appid = str(CONFIG, "sso.appid", null, "portal");
		appsecret = str(CONFIG, "sso.appsecret", null, "portal");
		apiURL = str(CONFIG, "sso.apiURL", null, null);
		AuthGlobal.runAppConfig(appid, appsecret, apiURL);// 初始化全局设置
		
		String SYSTEM_ALIAS,SYSTEM_PASSWORD,REDIRECT_URI,WEB_URI;
		
		ssoURL = String.valueOf(CONFIG.getProperty("sso.ssoURL")).trim();
		ssoName = String.valueOf(CONFIG.getProperty("sso.ssoName")).trim();
		ssoPassword = String.valueOf(CONFIG.getProperty("sso.ssoPassword")).trim();
		webURL = String.valueOf(CONFIG.getProperty("sso.webURL")).trim();
		loginURL = String.valueOf(CONFIG.getProperty("sso.loginURL")).trim();
		logoutURL = String.valueOf(CONFIG.getProperty("sso.logoutURL")).trim();
		if(!"null".equals(webURL))
		{
			if("null".equals(loginURL))
			{
				loginURL  = webURL + "/login";
			}
			else if(!loginURL.equals(webURL + "/login"))
			{
				thirdLoginURL = loginURL;
			}
			if("null".equals(logoutURL))
			{
				logoutURL = webURL + "/logout";
			}
			passwordURL = webURL + "/password";
		}
		systemURL = String.valueOf(CONFIG.getProperty("sso.systemURL")).trim();
		hasSameDoamin = String.valueOf(CONFIG.getProperty("sso.sameDomain")).trim();
		ignoreURL = String.valueOf(CONFIG.getProperty("sso.ignoreURL")).trim();
		AuthGlobalSystem.init(ssoURL, ssoName, ssoPassword);
		WebFilter.use = true;
		if("null".equals(systemURL)){systemURL = "";}
		ignoreURLSet.clear();
		if(ignoreURL.length() > 0)
		{
			String[] values = ignoreURL.trim().split(",");
			for(String value : values){if(value.trim().length() > 0){ignoreURLSet.add(value.trim());}}
		}
		if(hasSameDoamin.equals("true")){sameDomain = true;}// 和sso在同一域名下时，可跳过ticket远程访问，直接读取cookie
	}

	public void destroy()
	{
	}

	public static String getAccount(HttpSession session)
	{
		Map<String, String> m = getLoginer(session);
		String account = String.valueOf(m.get("account"));
		return (account.length() > 0 && !account.equals("null")) ? account : "";
	}
	
	/**
	 * @note 获取指定用户的基本信息
	 * @param ticket 登录凭证
	 * @return Loginer
	 */
	@SuppressWarnings("all")
	public static Map<String, String> getLoginer(HttpSession session)
	{
		Map<String, String> m = null;
		try
		{
			m = (Map<String, String>) session.getAttribute(LOGINER);
		}
		catch(Exception e)
		{
		}
		if(m == null)
		{
			m = new java.util.HashMap<String, String>();
		}
		return m;
	}

	public static void logout(HttpSession session)
	{
		session.removeAttribute(LOGINER);
		session.removeAttribute(TICKET);
	}
	
	public static boolean isUse()
	{
		return WebFilter.use;
	}

	private static String getValueByParameter(HttpServletRequest request, String parameter)
	{
		String[] values = request.getParameterValues(parameter);
		String value = "";
		if(values.length == 1)
		{
			value = request.getParameter(parameter);
			if(value == null)
			{
				return "";
			}
			value = value.trim();
		}
		else
		{
			int k = values.length;
			for(int i = 0; i < values.length; i++)
			{
				if(i == k - 1)
				{
					value += values[i].trim();
				}
				else
				{
					value += values[i].trim() + ",";
				}
			}
		}
		return value;
	}
	private static boolean validateTicket(HttpSession session, String openid, String access_token)
	{
		return (openid + "-" + access_token).equals(session.getAttribute(TICKET));
	}
	private static boolean refreshUser(HttpSession session, String openid, String access_token)
	{
		try
		{
			IUser user = null;
			try
			{
				JsonResult<IUser> result = AuthFactory.getUserUserinfo(openid, access_token);
				if(result.getCode() == AuthGlobal.CODE_001)
				{
					user = result.getData();
				}
			}
			catch(Exception e)
			{
				log.error(e.getMessage());
			}
			if(user != null && user.getAccount().length() > 0 && !"null".equals(user.getAccount()))
			{
				Map<String, String> m = new java.util.HashMap<String, String>();
				m.put("id", user.getId() + "");
				m.put("sid", user.getSid() + "");
				m.put("account", user.getAccount());
				m.put("name", user.getName());
				m.put("idcard", user.getIdcard());
				m.put("workcard", user.getWorkcard());
				m.put("sex", user.getSex() + "");
				m.put("orgid", user.getOrgid() + "");
				m.put("orgpid", user.getOrgpid() + "");
				m.put("type", user.getType() + "");
				m.put("typename", user.getTypename() + "");
				m.put("exalias", user.getExalias() + "");
				m.put("exname", user.getExname() + "");
				m.put("openid", openid);
				m.put("access_token", access_token);
				session.setAttribute(LOGINER, m);
				session.setAttribute(TICKET, openid + "-" + access_token);
				return true;
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		logout(session);
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
