<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%><%
String param = request.getQueryString();
if(param != null)
{
	param = "?" + param;
}
else
{
	param = "";
}
// Cookie cookie = new Cookie(dswork.sso.WebFilter.SSOTICKET, "");
Cookie cookie = new Cookie("ssoticket", "");
cookie.setMaxAge(0);// 删除
cookie.setPath("/");
cookie.setSecure(false);
cookie.setHttpOnly(true);
response.addCookie(cookie);
response.sendRedirect(request.getContextPath() + "/frame/index.jsp" + param);
%>