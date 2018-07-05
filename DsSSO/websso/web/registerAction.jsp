<%@page language="java" pageEncoding="UTF-8" import="
java.net.URLEncoder,
dswork.spring.BeanFactory,
dswork.web.MyRequest,
dswork.websso.model.DsWebssoUser,
dswork.websso.service.DsWebssoUserService
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><%!
private String todo(HttpServletRequest request)
{
	try
	{
		MyRequest req = new MyRequest(request);
		String msg = req.getString("msg", "");
		if(msg.length() > 0)
		{
			return msg;
		}
		DsWebssoUser po = new DsWebssoUser();
		req.getFillObject(po);
		DsWebssoUserService service = (DsWebssoUserService)BeanFactory.getBean("dsWebssoUserService");
		service.save(po);
		request.setAttribute("po", po);

		String serviceURL = req.getString("serviceURL");
		if(serviceURL == null || serviceURL.length() == 0)
		{
			serviceURL = "about:blank";
		}
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
<form action="${fn:escapeXml(serviceURL)}" method="post" style="display:none" id="myform">
<input name="name" value="${fn:escapeXml(po.name)}" />
<input name="account" value="${fn:escapeXml(po.useraccount)}" />
</form>
<script type="text/javascript">
document.getElementById('myform').submit();
</script>
</c:if>
<c:if test="${msg!=''}">
${msg}
</c:if>
</body>
</html>