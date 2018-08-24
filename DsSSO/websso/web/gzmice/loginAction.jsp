<%@page language="java" pageEncoding="UTF-8"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%
String msg = "";
String ssotoken = "";
dswork.websso.model.DsWebssoUser po = new dswork.websso.model.DsWebssoUser();
dswork.web.MyRequest req = new dswork.web.MyRequest(request);
String serviceURL = java.net.URLDecoder.decode(req.getString("service", ""), "UTF-8");// get方式传过来的
if(serviceURL.length() == 0)
{
	msg = "参数错误";
}
else
{
	req.getFillObject(po);
	try
	{
		dswork.websso.service.DsWebssoUserService service = (dswork.websso.service.DsWebssoUserService)dswork.spring.BeanFactory.getBean("dsWebssoUserService");
		po = service.getByOpendid(po);
		if(po == null)
		{
			msg = "用户不存在";
		}
		ssotoken = dswork.core.util.EncryptUtil.encryptMd5(po.getUseraccount() + "skeywebsso");
	}
	catch(Exception e)
	{
		msg = e.getMessage();
	}
}
request.setAttribute("serviceURL", serviceURL);
request.setAttribute("msg", java.net.URLEncoder.encode(msg, "UTF-8"));// 服务器认get传来的
request.setAttribute("po", po);
request.setAttribute("ssotoken", ssotoken);
%><!DOCTYPE html><html>
<head>
<meta charset="UTF-8" /><title>统一身份认证平台</title>
</head>
<body>
<form action="/sso/webssoAction.jsp" method="post" style="display:none" id="myform">
<input name="account" value="${fn:escapeXml(po.useraccount)}" />
<input name="ssotoken" value="${fn:escapeXml(ssotoken)}" />
<input name="loginURL" value="/websso/gzmice/login.jsp" />
<input name="service" value="${fn:escapeXml(serviceURL)}" />
<input name="msg" value="${fn:escapeXml(msg)}" />
</form>
<script type="text/javascript">
document.getElementById('myform').submit();
</script>
</body>
</html>
