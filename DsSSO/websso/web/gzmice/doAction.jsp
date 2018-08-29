<%@page language="java" pageEncoding="UTF-8" import="
java.net.URLEncoder,
dswork.spring.BeanFactory,
dswork.web.MyRequest,
dswork.websso.model.DsWebssoUser,
dswork.websso.service.DsWebssoUserService
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%!
private String todo(HttpServletRequest request)
{
	try
	{
		MyRequest req = new MyRequest(request);
		DsWebssoUser po = new DsWebssoUser();
		req.getFillObject(po);

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
		DsWebssoUser u = service.getByUseraccount(account);
		if(u == null)
		{
			return "用户不存在";
		}
		DsWebssoUser uu = service.getByOpendid(po);
		// 账号已经绑定，否则为未绑定
		if(uu != null)
		{
			// 账号已经绑定其他账号，否则为重新绑定
			if(uu.getId().longValue() != u.getId().longValue())
			{
				return "该第三方账号已被绑定";
			}
		}

		if(po.getOpenidqq().length() > 0){u.setOpenidqq(po.getOpenidqq());}
		else if(po.getOpenidwechat().length() > 0){u.setOpenidwechat(po.getOpenidwechat());}
		else if(po.getOpenidalipay().length() > 0){u.setOpenidalipay(po.getOpenidalipay());}
		service.update(u);
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
<c:if test="${msg!=''}">${msg}</c:if>
<c:if test="${msg==''}"><h3>绑定成功！</h3></c:if>
</body>
</html>