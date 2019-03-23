<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="dswork.base.DsFactory, dswork.web.MyRequest"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<style type="text/css">
body {line-height:2em;}
</style>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">代办流程</td>
		<td class="menuTool">
			<a class="back" href="waiting.jsp">返回待办列表</a>
		</td>
	</tr>
</table>
<%
String msg = "";
MyRequest req = new MyRequest(request);
Long wid = req.getLong("wid");
String olduser = req.getString("olduser");
String oldname = req.getString("oldname");
String newuser = req.getString("newuser");
String newname = req.getString("newname");
try
{
  if(wid > 0){
	  if(DsFactory.getFlow().updateFlowUser(wid, olduser, oldname, newuser, newname)){msg = "处理成功";}
	  else{msg = "处理失败";}
  }else{msg = "处理失败";}
}catch(Exception ex){
	msg = "处理失败";
}%>
<%=msg%>
<br />
</body>
</html>