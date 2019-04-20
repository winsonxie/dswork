<%@page language="java" pageEncoding="UTF-8" import="java.util.*,${frame}.web.MyRequest,${frame}.core.util.*,
${namespace}.model.${model},
${namespace}.MyFactory"%><%
try
{
	${model} po = new ${model}();
	MyRequest req = new MyRequest(request);
	req.getFillObject(po);
	MyFactory.get${model}Service().save(po);
	out.print(1);
}
catch (Exception e)
{
	e.printStackTrace();
	out.print("0:" + e.getMessage());
}
%>