<%@page contentType="text/html; charset=UTF-8"%><%
boolean error = "404".equals(String.valueOf(request.getParameter("error")));
String code = String.valueOf(request.getParameter("code"));
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<title>统一身份认证平台</title>
</head>
<body>
跳转成功
<%=error?"、用户不存在":"" %><br>
appid=<%=String.valueOf(request.getParameter("appid")) %><br>
bindid=<%=String.valueOf(request.getParameter("bindid")) %><br>
bindtype=<%=String.valueOf(request.getParameter("bindtype")) %><br>
userbind=<%=String.valueOf(request.getParameter("userbind")) %><br>
code=<%=String.valueOf(request.getParameter("code")) %><br>
<%if(!error){%>
<input type="button" value="确认转到Portal" onclick="location.href='/portal/sso/login?code=<%=code%>&url=%2Fportal%2Fframe%2Findex.jsp';"/><br>
<input type="button" value="确认转到明细" onclick="location.href='/sso/websso/demo/ticket.jsp';"/><br>
<%}%>
<input type="button" value="返回测试页面" onclick="location.href='/sso/websso/demo/login.jsp';"/><br>
</body>
</html>
