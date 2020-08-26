package dswork.authown;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthOwnSSOFilter implements Filter
{
	public AuthOwnSSOFilter()
	{
	}

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		try
		{
			if(dswork.sso.WebFilter.isUse())
			{
				dswork.sso.model.IUser m = dswork.sso.WebFilter.getLoginer(req.getSession());
				if(m.getId() == Long.MIN_VALUE)
				{
					AuthOwnUtil.clearUser(req);
					return;
				}
				AuthOwn auth = AuthOwnUtil.getUser(req);
				if(auth == null || !auth.getAccount().equals(m.getAccount()))
				{
					try
					{
						if(m.getStatus() != 0)
						{
							String c = String.valueOf(m.getOwn()).trim();
							AuthOwnUtil.login(req, res, String.valueOf(m.getId()), m.getAccount(), m.getName(), c);
							AuthOwnUtil.setUser(req, String.valueOf(m.getId()), m.getAccount(), m.getName(), c);
							chain.doFilter(request, response);
							return;
						}
					}
					catch(Exception xx)
					{
					}
					AuthOwnUtil.clearUser(req);
					return;
				}
				else
				{
					chain.doFilter(request, response);
					return;
				}
			}
			else
			{
				AuthOwn authOwn = AuthOwnUtil.getUserCookie(req, res);
				if(authOwn == null)
				{
					AuthOwnUtil.clearUser(req);
					return;
				}
				AuthOwn m = AuthOwnUtil.getUser(req);
				if(m == null || !m.getAccount().equals(authOwn.getAccount()))
				{
					AuthOwnUtil.setUser(req, authOwn.getId().toString(), authOwn.getAccount(), authOwn.getName(), authOwn.getOwn());
				}
				chain.doFilter(request, response);
			}
		}
		catch(Exception e)
		{
		}
	}

	public void init(FilterConfig fConfig) throws ServletException
	{
	}
}
