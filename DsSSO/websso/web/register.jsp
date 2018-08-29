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

MyRequest req = new MyRequest(request);
String serviceURL = req.getString("serviceURL");
serviceURL = URLEncoder.encode(serviceURL, "UTF-8");
serviceURL += getUrl(request) + "/registerAction.jsp?serviceURL=" + serviceURL;
serviceURL = URLEncoder.encode(serviceURL, "UTF-8");
request.setAttribute("serviceURL", serviceURL);
request.setAttribute("ctx", request.getContextPath());
%><html>
<body>
<c:if test="${hasQq}"><a href="/websso/bind/login.jsp?type=qq&serviceURL=${serviceURL}">QQ</a></c:if>
<c:if test="${hasWechat}"><a href="/websso/bind/login.jsp?type=wechat&serviceURL=${serviceURL}">微信</a></c:if>
<c:if test="${hasAlipay}"><a href="/websso/bind/login.jsp?type=alipay&serviceURL=${serviceURL}">支付宝</a></c:if>
</body>
</html>