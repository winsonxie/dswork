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

public class AuthOwnFilter implements Filter
{
	public AuthOwnFilter()
	{
	}

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
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

	public void init(FilterConfig fConfig) throws ServletException
	{
	}
}
