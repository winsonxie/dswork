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
response.sendRedirect(request.getContextPath() + "/frame/index.jsp" + param);
%>