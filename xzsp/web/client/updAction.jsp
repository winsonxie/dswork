<%@page pageEncoding="UTF-8" import="
MQAPI.*,
common.gov.DsXzsp,
common.gov.DsXzspService,
dswork.spring.BeanFactory,
dswork.web.MyRequest,
dswork.core.page.PageRequest,
java.util.List,
common.gov.JSONUtil,
java.security.Provider.Service,
com.mysql.fabric.Server,
dswork.core.page.PageRequest
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%><%
try
{
	MyRequest req = new MyRequest(request);
	DsXzspService service = (DsXzspService) BeanFactory.getBean("dsXzspService");
	DsXzsp po = service.get(req.getLong("keyIndex"));
	int resend = req.getInt("resend");
	if(resend == 1)
	{
		po.setFszt(0);
		po.setFscs(0);
	}
	else
	{
		Object o = null;
		switch(po.getSptype())
		{
			case 0: o = new ApplicationOB(); break;
			case 1: o = new PreAcceptOB(); break;
			case 2: o = new AcceptOB(); break;
			case 3: o = new SubmitOB(); break;
			case 4: o = new CompleteOB(); break;
			case 5: o = new BlockOB(); break;
			case 6: o = new ResumeOB(); break;
			case 7: o = new SupplyOB(); break;
			case 8: o = new SupplyAcceptOB(); break;
			case 9: o = new ReceiveRegOB(); break;
			default: throw new Exception("sptype错误");
		}
		req.getFillObject(o);
		po.setSpobject(JSONUtil.toJson(o));
		if(resend == 2)
		{
			po.setFszt(0);
			po.setFscs(0);
		}
	}
	service.updateData(po);
	out.print(1);
}
catch(Exception e)
{
	out.print("0:" + e.getMessage());
}
%>