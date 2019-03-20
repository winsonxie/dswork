package dswork.sso;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="SSOMenuServlet", loadOnStartup=3, urlPatterns={"/sso/menu"})
public class SSOMenuServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public static dswork.sso.model.IFunc[] getFuncByUser(String account)
	{
		java.util.List<dswork.sso.model.IFunc> list = new java.util.ArrayList<dswork.sso.model.IFunc>();
		if(account != null && !account.equals(""))
		{
			dswork.sso.model.IFunc[] arr = dswork.sso.AuthFactory.getFunctionByUser(account);
			if(arr != null)
			{
				for(dswork.sso.model.IFunc m : arr)
				{
					if(m.getStatus() == 1)
					{
						list.add(m);
					}
				}
			}
			return list.toArray(new dswork.sso.model.IFunc[list.size()]);
		}
		return null;
	}

	public SSOMenuServlet()
	{
		super();
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String jsoncallback = String.valueOf(request.getParameter("jsoncallback")).replaceAll("<", "").replaceAll(">", "").replaceAll("\"", "").replaceAll("'", "");
			String user = String.valueOf(request.getParameter("user"));
			PrintWriter out = response.getWriter();
			out.print(jsoncallback + "([");
			// {id:1, pid:0, name:'门户菜单', img:"", imgOpen:"", url:""}
			// ,{id:2, pid:1, name:'门户首页', img:"", imgOpen:"", url:"/frame/main.jsp"}
			dswork.sso.model.IFunc[] list = getFuncByUser(user);
			StringBuilder sb = new StringBuilder(300);
			if(list != null)
			{
				for(dswork.sso.model.IFunc m : list)
				{
					sb.append(",{id:" + m.getId() + ", pid:" + m.getPid() + ", name:\"" + m.getName() + "\", img:\"\", imgOpen:\"\", url:\"" + m.getUri() + "\"}");
				}
				if(list.length > 0)
				{
					out.print(sb.substring(1));
				}
			}
			out.print(jsoncallback + "])");
		}
		catch(Exception ex)
		{
		}
		return;
	}
}
