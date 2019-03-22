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
			session.removeAttribute(SSOLoginServlet.LOGINER);
			session.invalidate();
			String jsoncallback = request.getParameter("jsoncallback").replaceAll("<", "").replaceAll(">", "").replaceAll("\"", "").replaceAll("'", "");
			PrintWriter out = response.getWriter();
			out.print(jsoncallback + "([])");
		}
		catch(Exception ex)
		{
			log.error(ex.getMessage());
		}
		return;
	}
}
