package dswork.sso;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dswork.sso.model.IUser;
import dswork.sso.model.JsonResult;

@WebServlet(name="SSOMenuServlet", loadOnStartup = 3, urlPatterns={"/sso/menu"})
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
			String[] arr = AuthWebConfig.getSSOTicket(request);// 获取是否存在ticket信息
			IUser user = null;
			if(arr != null)
			{
				String openid = arr[0];
				String access_token = arr[1];
				user = WebFilter.getLoginer(request.getSession());
				if(user != null)
				{
					try
					{
						if(!arr[2].equals(user.getSsoticket()))
						{
							user = null;
						}
					}
					catch(Exception e)
					{
						user = null;
					}
				}
				if(user == null)
				{
					try
					{
						JsonResult<IUser> result = AuthFactory.getUserUserinfo(openid, access_token);
						if(result.getCode() == AuthGlobal.CODE_001)
						{
							user = result.getData();
						}
						if(!SSOLoginServlet.refreshUser(request.getSession(), user, openid, access_token))
						{
							user = null;
						}
					}
					catch(Exception e)
					{
					}
				}
			}
			PrintWriter out = response.getWriter();
			out.print(jsoncallback + "([");
			if(user != null)
			{
				// {id:1, pid:0, name:'门户菜单', img:"", imgOpen:"", url:""}
				// ,{id:2, pid:1, name:'门户首页', img:"", imgOpen:"", url:"/frame/main.jsp"}
				dswork.sso.model.IFunc[] list = getFuncByUser(user.getAccount());
				StringBuilder sb = new StringBuilder(300);
				if(list != null)
				{
					for(dswork.sso.model.IFunc m : list)
					{
						sb.append(",{id:" + m.getId() + ", pid:" + m.getPid() + ", name:\"" + String.valueOf(m.getName()).trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"") + "\", img:\"\", imgOpen:\"\", url:\"" + m.getUri() + "\"}");
					}
					if(list.length > 0)
					{
						out.print(sb.substring(1));
					}
				}
			}
			out.print("])");
		}
		catch(Exception ex)
		{
		}
		return;
	}
}
