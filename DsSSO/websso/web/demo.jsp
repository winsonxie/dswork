<%@page language="java" pageEncoding="UTF-8" import="
java.net.URLEncoder,
dswork.websso.util.WebssoUtil,
dswork.web.MyRequest
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
%><%
request.setAttribute("hasQq", WebssoUtil.HAS_QQ);
request.setAttribute("hasWechat", WebssoUtil.HAS_WECHAT);
request.setAttribute("hasAlipay", WebssoUtil.HAS_ALIPAY);
request.setAttribute("weblogin", WebssoUtil.WEBLOGIN);

MyRequest req = new MyRequest(request);
String serviceURL = getUrl(request) + "/loginAction.jsp?serviceURL=" + URLEncoder.encode(req.getString("serviceURL"), "UTF-8");
request.setAttribute("serviceURL", URLEncoder.encode(serviceURL, "UTF-8"));
%><html>
<body>
<c:if test="${hasQq}"><a href="${weblogin}/login.jsp?type=qq&serviceURL=${serviceURL}"><img alt="QQ登录" src="${weblogin}/img/qq_logo.png"></a></c:if>
<c:if test="${hasWechat}"><a href="${weblogin}/login.jsp?type=wechat&serviceURL=${serviceURL}"><img alt="微信登录" src="${weblogin}/img/wechat_logo.png"></a></c:if>
<c:if test="${hasAlipay}"><a href="${weblogin}/login.jsp?type=alipay&serviceURL=${serviceURL}"><img alt="支付宝登录" src="${weblogin}/img/alipay_logo.jpg"></a></c:if>
</body>
</html>