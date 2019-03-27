package dswork.sso;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dswork.sso.model.AccessToken;
import dswork.sso.model.IUser;
import dswork.sso.model.JsonResult;

@WebServlet(name = "SSOLoginServlet", loadOnStartup = 1, urlPatterns = {"/sso/login"})
public class SSOLoginServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	public static final String LOGINER = "sso.web.loginer";
	static org.slf4j.Logger log = AuthGlobal.log;

	public SSOLoginServlet()
	{
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		AuthWebConfig.loadConfig(config.getServletContext());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String code = request.getParameter("code");
			String redirect_uri = null;
			String ssoticket = "";
			if(code != null && code.length() > 0)
			{
				if(AuthWebConfig.getSystemRedirectURI().length() > 0)
				{
					redirect_uri = AuthWebConfig.getSystemRedirectURI();
				}
				else
				{
					redirect_uri = request.getParameter("url");// 需要解码，通常是本项目地址，否则拿不到用户
//					if(redirect_uri != null)
//					{
//						redirect_uri = java.net.URLDecoder.decode(redirect_uri, "UTF-8");
//					}
				}
				JsonResult<AccessToken> token = AuthFactory.getUserAccessToken(code);
				if(token.getCode() == AuthGlobal.CODE_001)
				{
					AccessToken t = token.getData();
					JsonResult<IUser> u = AuthFactory.getUserUserinfo(t.getOpenid(), t.getAccess_token());
					if(u.getCode() == AuthGlobal.CODE_001)
					{
						ssoticket = "ssoticket=" + t.getOpenid() + "-" + t.getAccess_token();
					}
				}
			}
			if(redirect_uri == null || redirect_uri.length() == 0)
			{
				redirect_uri = "about:blank";
			}
			else if(ssoticket.length() > 0)
			{
				redirect_uri = redirect_uri + (redirect_uri.contains("?") ? "&" : "?") + ssoticket;
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
			session.setAttribute(LOGINER, AuthGlobal.gson.toJson(user));
			return true;
		}
		else
		{
			// 登录不成则退出
			session.removeAttribute(LOGINER);
			return false;
		}
	}
}
