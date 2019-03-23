<%@page language="java" pageEncoding="UTF-8" import="dswork.sso.model.IUser"%><%
String path = request.getContextPath();
IUser user = dswork.sso.WebFilter.getLoginer(session);
if(user.getId() == 0)
{
	response.sendRedirect(request.getContextPath() + "/50x.jsp");// dswork.sso.WebFilter.getLoginURL(request)
	return;
}
else
{
	Cookie cookie = new Cookie(dswork.sso.WebFilter.SSOTICKET, user.getSsoticket());
	cookie.setMaxAge(-1);
	cookie.setPath("/");
	cookie.setSecure(false);
	cookie.setHttpOnly(true);
	response.addCookie(cookie);
	response.sendRedirect(path + "/frame/index.jsp");
}
%><%-- 此页需要放在sso过滤的页面中，用于服务器主动获取账号 --%>