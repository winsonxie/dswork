<%@page language="java" pageEncoding="UTF-8" import="
java.net.URLEncoder,
dswork.spring.BeanFactory,
dswork.websso.util.WebssoUtil,
dswork.websso.model.DsWebssoUser,
dswork.websso.service.DsWebssoUserService
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%!
private String getUrl(HttpServletRequest request)
{
	String url = request.getScheme() + "://" + request.getServerName();
	if(
		("http".equals(request.getScheme()) && request.getServerPort() != 80) ||
		("https".equals(request.getScheme()) && request.getServerPort() != 443)
	)
	{
		url += ":" + request.getServerPort();
	}
	url += request.getContextPath();
	return url;
}
private String todo(HttpServletRequest request)
{
	try
	{
		// 通过cookie取sso的登陆用户account
		String ticket = "";
		String code = "";
		for(Cookie c : request.getCookies())
		{
			if("DS_SSO_TICKET".equals(c.getName())){ticket = c.getValue();}
			else if("DS_SSO_CODE".equals(c.getName())){code = c.getValue();}
		}
		String s = dswork.core.util.EncryptUtil.decodeDes(code, ticket);
		String account = s.split("#")[0];
		if(account.length() == 0)
		{
			return "用户未登录";
		}
	
		DsWebssoUserService service = (DsWebssoUserService)BeanFactory.getBean("dsWebssoUserService");
		DsWebssoUser po = service.getByUseraccount(account);
		if(po == null)
		{
			return "用户不存在";
		}
		request.setAttribute("po", po);
	
		request.setAttribute("hasQq", WebssoUtil.HAS_QQ);
		request.setAttribute("hasWechat", WebssoUtil.HAS_WECHAT);
		request.setAttribute("hasAlipay", WebssoUtil.HAS_ALIPAY);
		request.setAttribute("weblogin", WebssoUtil.WEBLOGIN);

		String serviceURL = getUrl(request) +  "/bind/doAction.jsp";
		serviceURL = URLEncoder.encode(serviceURL, "UTF-8");
		request.setAttribute("serviceURL", serviceURL);
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return e.getMessage();
	}
	return "";
}
%><%
request.setAttribute("msg", todo(request));
%><html>
<body>
<c:if test="${msg==''}">
<c:if test="${hasQq}"><a href="${weblogin}/login.jsp?type=qq&serviceURL=${serviceURL}"><img alt="QQ登录" src="${weblogin}/img/qq_logo.png">${po.openidqq==''?'':'重新绑定'}</a></c:if>
<c:if test="${hasWechat}"><a href="${weblogin}/login.jsp?type=wechat&serviceURL=${serviceURL}"><img alt="微信登录" src="${weblogin}/img/wechat_logo.png">${po.openidwechat==''?'':'重新绑定'}</a></c:if>
<c:if test="${hasAlipay}"><a href="${weblogin}/login.jsp?type=alipay&serviceURL=${serviceURL}"><img alt="支付宝登录" src="${weblogin}/img/alipay_logo.jpg">${po.openidalipay==''?'':'重新绑定'}</a></c:if>
</c:if>
<c:if test="${msg!=''}">
<h2>${msg}</h2>
</c:if>
</body>
</html>