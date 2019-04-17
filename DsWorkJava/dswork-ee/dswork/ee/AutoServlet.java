package dswork.ee;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@WebServlet(name="x666", loadOnStartup=1, urlPatterns={"/x666"})
public class AutoServlet extends HttpServlet
{
	static
	{
		try
		{
			Class.forName("dswork.jdbc.SpyLog");
			dswork.jdbc.SpyLog.log = LoggerFactory.getLogger("jdbc.sqlonly");
		}
		catch(Exception c)
		{
		}
		try
		{
			Class.forName("dswork.sso.AuthGlobal");
			dswork.sso.AuthGlobal.log = LoggerFactory.getLogger("dswork.sso");
		}
		catch(Exception e)
		{
		}
	}
}
