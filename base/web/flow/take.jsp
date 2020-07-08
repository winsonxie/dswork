<%@page language="java" pageEncoding="UTF-8" import="dswork.common.DsFactory, dswork.web.MyRequest"%><%
MyRequest req = new MyRequest(request);
long waitid = req.getLong("wid");
if(waitid > 0)
{
	DsFactory.getFlow().process(waitid, null, null, null, "admin", "管理员", "", "取得任务", null, "mytype", "mystatus", "mydata");
	response.sendRedirect("waiting.jsp");
}
%>