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

	public final static String LOGINER = SSOLoginServlet.LOGINER;

	public void init(FilterConfig config) throws ServletException
	{
		WebFilter.use = true;
		AuthWebConfig.loadConfig(config.getServletContext());
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
			log.debug(ouser != null ? "当前已登录" + String.valueOf(ouser) : "当前未登录");
		}
		
		String[] arr = AuthWebConfig.getSSOTicket(request);// 获取是否存在ticket信息
		if(arr != null)
		{
			String openid = arr[0];
			String access_token = arr[1];
			IUser user = null;
			if(ouser != null)
			{
				try
				{
					user = gson.fromJson(String.valueOf(ouser), IUser.class);
					if(!arr[2].equals(user.getSsoticket()))
					{
						ouser = null;// 相等不需要 更新用户
						if(log.isDebugEnabled())
						{
							log.debug("ticket不相等，需要更新用户");
						}
					}
				}
				catch(Exception e)
				{
					log.error(e.getMessage());
					ouser = null;// 用户数据异常，重新获取
				}
			}
			if(ouser == null)
			{
				try
				{
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
		if(ouser != null)// 已登录
		{
			chain.doFilter(request, response);
			return;
		}
		// String relativeURI = request.getRequestURI();// 相对地址
		// if(request.getContextPath().length() > 0){relativeURI = relativeURI.replaceFirst(request.getContextPath(), "");}
		if(AuthWebConfig.containsIgnoreURL(relativeURI))// 判断是否为无需验证页面
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
	 * @return IUser 失败返回空白的Iuser实例，即id为-1
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
			m.setId(-1L);
		}
		return m;
	}

	public static void logout(HttpSession session)
	{
		session.removeAttribute(LOGINER);
	}
	
	public static boolean isUse()
	{
		return WebFilter.use;
	}
}
