package dswork.common.util;

import javax.servlet.http.HttpServletResponse;

import dswork.common.model.ZAuthtoken;
import dswork.core.util.EnvironmentUtil;

public class ResponseUtil
{
	public static final String STORGE = EnvironmentUtil.getToString("dswork.sso.storge", "local");
	public static final boolean USE_REDIS = STORGE.equals("redis");
	
	private static final String JSON_USER = "{\"code\":1,\"data\":{\"access_token\":\"%s\",\"expires_in\":%d,\"refresh_token\":\"%s\",\"openid\":\"%s\"}}";
	private static final String JSON_UNIT = "{\"code\":1,\"data\":{\"access_token\":\"%s\",\"expires_in\":%d,\"refresh_token\":\"%s\"}}";
	private static final String JSON_CODE = "{\"code\":1,\"data\":{\"code\":\"%s\",\"expires_in\":%d}}";
	private static final String JSON_USERINFO = "{\"code\":1,\"data\":%s}";
	public static String getJsonUserToken(ZAuthtoken token)
	{
		return String.format(JSON_USER, token.getAccess_token(), token.getExpires_in(), token.getRefresh_token(), token.getOpenid());
	}
	public static String getJsonUnitToken(ZAuthtoken token)
	{
		return String.format(JSON_UNIT, token.getAccess_token(), token.getExpires_in(), token.getRefresh_token());
	}
	public static String getJsonUserCode(String authorizecode, long time)
	{
		return String.format(JSON_CODE, authorizecode, time);
	}
	public static String getJsonUserInfo(String userjson)
	{
		return String.format(JSON_USERINFO, userjson);
	}
	
	/**
	 * 输出同源json
	 * @param response
	 * @param json
	 */
	public static void printDomainJson(HttpServletResponse response, String json)
	{
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(json == null ? "" : json);
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * 输出可跨域json
	 * @param response
	 * @param json
	 */
	public static void printJson(HttpServletResponse response, String json)
	{
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("P3P", "CP=CAO PSA OUR");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().write(json == null ? "" : json);
		}
		catch(Exception e)
		{
		}
	}

	public static void sendRedirect(HttpServletResponse response, String url)
	{
		try
		{
			response.sendRedirect(url);
		}
		catch(Exception e)
		{
		}
	}
	
	public static String getEncodeURL(String url)
	{
		try
		{
			return java.net.URLEncoder.encode(url, "UTF-8");
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	public static String getDecodeURL(String url)
	{
		try
		{
			return java.net.URLDecoder.decode(url, "UTF-8");
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	private static com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
	public static String toJson(Object object)
	{
		return gson.toJson(object);
	}

	public static <T> T toBean(String json, Class<T> classOfT)
	{
		return gson.fromJson(json, classOfT);
	}
}
