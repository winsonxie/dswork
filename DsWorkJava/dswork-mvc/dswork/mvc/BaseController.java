package dswork.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import dswork.core.page.PageRequest;
import dswork.web.MyRequest;

public class BaseController
{
	protected static String PageSize_SessionName = "dswork_session_pagesize";
	private static final ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	private static final ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	private static final ThreadLocal<MyRequest> req = new ThreadLocal<MyRequest>();
	protected static Logger log = LoggerFactory.getLogger(BaseController.class.getName());
	private static String JSON = "{\"code\":%d,\"data\":%s,\"msg\":\"%s\"}";
	private static String JSON_DATA = "{\"code\":%d,\"data\":%s}";
	private static String JSON_MSG = "{\"code\":%d,\"msg\":\"%s\"}";
	private static String JSON_CODE = "{\"code\":%d}";

	@ModelAttribute
	public void BaseInitialization(HttpServletRequest request, HttpServletResponse response)
	{
		response.setCharacterEncoding("UTF-8");
		BaseController.request.set(request);
		BaseController.response.set(response);
		req.set(new MyRequest(request));
	}

	protected static HttpServletRequest request()
	{
		return request.get();
	}

	protected static HttpServletResponse response()
	{
		return response.get();
	}

	protected static HttpSession session()
	{
		return request().getSession();
	}

	protected static MyRequest req()
	{
		return req.get();
	}

	protected static String formatJson(int code, String jsonData, String msg)
	{
		if(jsonData == null)
		{
			if(msg == null)
			{
				return String.format(JSON_CODE, code);
			}
			else
			{
				return String.format(JSON_MSG, code, msg);
			}
		}
		else
		{
			if(msg == null)
			{
				return String.format(JSON_DATA, code, jsonData);
			}
			else
			{
				return String.format(JSON, code, jsonData, msg);
			}
		}
	}

	protected void print(Object value)
	{
		try
		{
			response().setCharacterEncoding("UTF-8");
			response().getWriter().print(value == null ? "" : value);
		}
		catch(Exception e)
		{
		}
	}

	protected void print(Object value, boolean sameDomain)
	{
		if(!sameDomain)
		{
			response().setHeader("P3P", "CP=CAO PSA OUR");
			response().setHeader("Access-Control-Allow-Origin", "*");
		}
		print(value);
	}

	protected void printJson(String value)
	{
		response().setContentType("application/json;charset=UTF-8");
		print(value);
	}

	protected void printJson(String value, boolean sameDomain)
	{
		response().setContentType("application/json;charset=UTF-8");
		print(value, sameDomain);
	}

//	protected void writeJson(String value)
//	{
//		try
//		{
//			response().setCharacterEncoding("UTF-8");
//			response().getWriter().write(value == null ? "" : value);
//		}
//		catch(Exception e)
//		{
//		}
//	}
//
//	protected void writeJson(String value, boolean sameDomain)
//	{
//		if(!sameDomain)
//		{
//			response().setHeader("P3P", "CP=CAO PSA OUR");
//			response().setHeader("Access-Control-Allow-Origin", "*");
//		}
//		writeJson(value);
//	}

	protected void put(String key, Object obj)
	{
		request().setAttribute(key, obj);
	}

	@Deprecated
	protected void sendRedirect(String url)
	{
		try
		{
			response().sendRedirect(url);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	protected PageRequest getPageRequest(int pagesize)
	{
		if(pagesize <= 0)
		{
			pagesize = 10;
		}
		PageRequest pr = new PageRequest();
		pr.setFilters(req().getParameterValueMap(false, false));
		pr.setPage(req().getInt("page", 1));
		try
		{
			pagesize = Integer.parseInt(String.valueOf(session().getAttribute(PageSize_SessionName)).trim());
		}
		catch(Exception ex)
		{
			pagesize = 10;
		}
		pagesize = req().getInt("pagesize", req().getInt("pageSize", pagesize));
		session().setAttribute(PageSize_SessionName, pagesize);
		pr.setPagesize(pagesize);
		return pr;
	}

	protected PageRequest getPageRequest()
	{
		return getPageRequest(10);
	}
}
