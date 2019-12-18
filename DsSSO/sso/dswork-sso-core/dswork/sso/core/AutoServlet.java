package dswork.sso.core;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
@WebServlet(name="autoload", loadOnStartup=1, urlPatterns={"/AutoServlet"})
public class AutoServlet extends HttpServlet
{
	static
	{
		AutoTimerExecute.toStart();
	}
}
