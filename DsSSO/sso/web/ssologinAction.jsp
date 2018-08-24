<%@page language="java" pageEncoding="UTF-8" import="
dswork.web.MyRequest,
dswork.web.MyAuthCodeServlet,
dswork.core.util.EncryptUtil,
dswork.sso.model.LoginUser,
dswork.sso.service.AuthFactoryService,
dswork.sso.service.TicketService,
dswork.sso.controller.AuthController,
java.net.URLEncoder
"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%
MyRequest req = new MyRequest(request);
String account = req.getString("account");
String serviceURL = req.getString("service", request.getContextPath() + "/ticket.jsp");
String msg = "";
try
{
	AuthFactoryService service  = (AuthFactoryService)dswork.spring.BeanFactory.getBean("authFactoryService");
	LoginUser user = service.getLoginUserByAccount(account);
	if(user == null)
	{
		msg = "用户不存在";
	}
	else
	{
		if(user.getStatus() == 1)
		{
			String cookieTicket = AuthController.putLoginInfo(request, response, user.getAccount(), user.getName());
			try
			{
				service.saveLogLogin(cookieTicket, AuthController.getClientIp(request), user.getAccount(), user.getName(), true);
			}
			catch(Exception logex)
			{
			}
			// 成功就跳到切换系统视图
			// 无需登录，生成ticket给应用去登录
			if(serviceURL.startsWith(request.getContextPath() + "/password"))
			{
				response.sendRedirect(serviceURL);
			}
			else
			{
				response.sendRedirect(serviceURL += ((serviceURL.indexOf("?") != -1) ? "&ticket=" : "?ticket=") + TicketService.getOnceTicket(cookieTicket));
			}
			return;
		}
		else
		{
			msg = "用户已禁用，请联系管理员！";
		}
	}
	// 失败则转回来
	request.setAttribute("account", account);
	serviceURL = URLEncoder.encode(serviceURL, "UTF-8");
	request.setAttribute("serviceURL", serviceURL);
	request.setAttribute("msg", msg);
	try
	{
		service.saveLogLogin("", AuthController.getClientIp(request), account, "", false);
	}
	catch(Exception e)
	{
	}
}
catch(Exception e)
{
}
%><html>
<body>
<form action="login" method="post" style="display:none" id="myform">
<input name="account" value="${fn:escapeXml(account)}" />
<input name="service" value="${fn:escapeXml(serviceURL)}" />
<input name="msg" value="${fn:escapeXml(msg)}" />
</form>
<script type="text/javascript">
alert('登录失败：${fn:escapeXml(msg)}')
document.getElementById('myform').submit();
</script>
</body>
</html>