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

public class WebFilter implements Filter
{
	static com.google.gson.Gson gson = AuthGlobal.gson;
	static org.slf4j.Logger log = AuthGlobal.log;
	
	private static boolean use = false;// 用于判断是否加载sso模块

	public static final String LOGINER = SSOLoginServlet.LOGINER;
	public static final String SSOTICKET = AuthWebConfig.SSOTICKET;

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
		
		Object o = session.getAttribute(LOGINER);// 当前登录用户
		String ouser = o == null ? null : String.valueOf(o);
		String relativeURI = request.getServletPath();// 相对地址
		String[] arr = AuthWebConfig.getSSOTicket(request, ouser);// 获取是否存在需要登录的ticket
		if(log.isDebugEnabled())
		{
			StringBuilder sb = new StringBuilder(126);
			sb.append("当前访问地址：" + request.getContextPath() + relativeURI);
			sb.append(ouser != null ? "，已登录" + String.valueOf(ouser) : "，未登录");
			if(arr != null)
			{
				sb.append("，sso-ticket：" + arr[2]);
			}
			else
			{
				sb.append("，取不到sso-ticket");
			}
			log.debug(sb.toString());
		}
		if(arr != null)
		{
			if(ouser != null)
			{
				ouser = null;// 需要登录
				session.removeAttribute(LOGINER);// 清除掉原登录信息
			}
			
			String openid = arr[0];
			String access_token = arr[1];
			IUser user = null;
			try
			{
				JsonResult<IUser> result = AuthFactory.getUserUserinfo(openid, access_token);
				if(result.getCode() == AuthGlobal.CODE_001)
				{
					user = result.getData();
				}
				if(SSOLoginServlet.refreshUser(session, user, openid, access_token))
				{
					try
					{
						chain.doFilter(request, response);
					}
					catch(Exception e)
					{
					}
					return;
				}
			}
			catch(Exception e)
			{
				log.error(e.getMessage());
				// 发生在result为null的情况
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=UTF-8");
				response.setHeader("P3P", "CP=CAO PSA OUR");
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.getWriter().print("{\"code\":500,\"msg\":\"未连接上认证接口，请稍候再试\"}");// 401未登录
				return;// 应该提示稍候再试
				// 拿不到用户信息，但有可能不是没有登录
			}
		}
		if(ouser != null)// 已登录
		{
			try
			{
				chain.doFilter(request, response);
			}
			catch(Exception e)
			{
			}
			return;
		}
		else
		{
			session.removeAttribute(LOGINER);// 没有用户，清一下session
		}
		if(AuthWebConfig.containsIgnoreURL(relativeURI))// 判断是否为无需验证页面
		{
			try
			{
				chain.doFilter(request, response);// 是无需验证页面
			}
			catch(Exception e)
			{
			}
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
	 * 获取指定用户的基本信息
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
			m.setId(Long.MIN_VALUE);
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
