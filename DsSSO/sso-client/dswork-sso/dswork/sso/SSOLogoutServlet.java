package dswork.sso;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name="SSOLogoutServlet", loadOnStartup = 2, urlPatterns={"/sso/logout"})
public class SSOLogoutServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	static Logger log = LoggerFactory.getLogger("dswork.sso.logout");

	public SSOLogoutServlet()
	{
		super();
	}
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			HttpSession session = request.getSession();
			session.removeAttribute(WebFilter.LOGINER);
			session.invalidate();
			String jsoncallback = request.getParameter("jsoncallback");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("P3P", "CP=CAO PSA OUR");
			response.setHeader("Access-Control-Allow-Origin", "*");
			PrintWriter out = response.getWriter();
			if(jsoncallback != null)
			{
				jsoncallback = jsoncallback.replaceAll("<", "").replaceAll(">", "").replaceAll("\"", "").replaceAll("'", "");
				out.print(jsoncallback + "([])");
			}
			else
			{
				response.getWriter().print("{\"code\":1}");// 退出成功
			}
		}
		catch(Exception ex)
		{
			log.error(ex.getMessage());
		}
		return;
	}
}
