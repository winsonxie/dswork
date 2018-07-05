<%@page language="java" pageEncoding="UTF-8" import="
dswork.spring.BeanFactory,
dswork.web.MyRequest,
dswork.web.MyAuthCodeServlet,
dswork.websso.service.DsCommonUserService,
dswork.websso.model.DsCommonUser,
dswork.core.util.EncryptUtil,
dswork.core.util.UniqueId,
dswork.core.util.TimeUtil
"%><%!
private String isCode(String authcode, HttpServletRequest request)
{
	String randcode = (String) request.getSession().getAttribute(MyAuthCodeServlet.SessionName_Randcode);
	if(randcode == null || randcode.equals(""))
	{
		return "验证码已过期!";
	}
	else if(!randcode.toLowerCase().equals(authcode.toLowerCase()))
	{
		return "验证码输入错误,请重新输入!";
	}
	request.getSession().setAttribute(MyAuthCodeServlet.SessionName_Randcode, "");
	return "";
}
%><%
try
{
	MyRequest req = new MyRequest(request);
	String msg = isCode(req.getString("authcode"), request);
	if(!"".equals(msg))
	{
		out.print("0:" + msg);
		return;
	}
	DsCommonUserService service = (DsCommonUserService)BeanFactory.getBean("dsCommonUserService");
	DsCommonUser po = new DsCommonUser();
	req.getFillObject(po);
	if(service.getByAccount(po.getAccount()) != null)
	{
		out.print("0:账号已存在");
		return;
	}
	if(service.getByIdcard(po.getIdcard()) != null)
	{
		out.print("0:身份证号已存在");
		return;
	}
	if(service.getByMobile(po.getMobile()) != null)
	{
		out.print("0:手机号已存在");
		return;
	}
	if(service.getByEmail(po.getEmail()) != null)
	{
		out.print("0:邮箱已存在");
		return;
	}
	po.setId(UniqueId.genId());
	String password = EncryptUtil.decodeDes(po.getPassword(), "dswork");
	password = EncryptUtil.encryptMd5(password).toLowerCase();
	po.setPassword(password);
	po.setCreatetime(TimeUtil.getCurrentTime());
	po.setStatus(1);
	service.save(po);
	out.print(1);
	return;
}
catch(Exception e)
{
	out.print("0:" + e.getMessage());
}
%>