package dswork.sso;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	
	public final static String LOGINER = SSOLoginServlet.LOGINER;
	public final static String TICKET = SSOLoginServlet.TICKET;

	public void init(FilterConfig config) throws ServletException
	{
		WebFilter.use = true;
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		HttpSession session = request.getSession();
		
		Object ouser = session.getAttribute(LOGINER);
		
		String relativeURI = request.getServletPath();// 相对地址
		if(log.isDebugEnabled())
		{
			log.debug("当前访问地址：" + request.getContextPath() + relativeURI);
			log.debug(ouser != null ? "当前已登录" + gson.toJson(ouser) : "当前未登录");
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
					try
					{
						IUser user = null;
						JsonResult<IUser> result = AuthFactory.getUserUserinfo(openid, access_token);
						if(result.getCode() == AuthGlobal.CODE_001)
						{
							user = result.getData();
						}
						if(SSOLoginServlet.refreshUser(session, user, openid, access_token))
						{
							chain.doFilter(request, response);
							return;
						}
					}
					catch(Exception e)
					{
						log.error(e.getMessage());
					}
					ouser = null;// 失败清空
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
		if(SSOLoginServlet.containsIgnoreURL(relativeURI))// 判断是否为无需验证页面
		{
			chain.doFilter(request, response);// 是无需验证页面
			return;
		}
		
		if("XMLHttpRequest".equals(String.valueOf(request.getHeader("X-Requested-With"))))
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("P3P", "CP=CAO PSA OUR");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().print("{\"code\":401}");// 401未登录
			return;
		}
		response.sendRedirect(getLoginURL(request));
		return;
	}

	public void destroy()
	{
	}

	public static String getLoginURL(HttpServletRequest request)
	{
		return AuthFactory.getUserAuthorizeURL(request.getRequestURI());
	}
	
	public static String getLoginActionURL(boolean isCode, String redirect_uri)
	{
		return AuthFactory.getUserLoginURL(isCode, redirect_uri);
	}

	public static String getAccount(HttpSession session)
	{
		IUser m = getLoginer(session);
		return m.getAccount();
	}
	
	/**
	 * @note 获取指定用户的基本信息
	 * @param session session
	 * @return IUser 失败返回空白的Iuser实例，即id为0
	 */
	@SuppressWarnings("all")
	public static IUser getLoginer(HttpSession session)
	{
		IUser m = null;
		try
		{
			m = gson.fromJson(String.valueOf(session.getAttribute(LOGINER)), IUser.class);
		}
		catch(Exception e)
		{
		}
		if(m == null)
		{
			m = new IUser();
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

	private static boolean validateTicket(HttpSession session, String openid, String access_token)
	{
		return (openid + "-" + access_token).equals(session.getAttribute(TICKET));
	}
}
