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
li,a {line-height:2em;}
</style>
</head>
<body>
<%
MyRequest req = new MyRequest(request);
Long wid = req.getLong("wid");
String olduser = "admin";
String oldname = "管理员";
%>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">代办流程</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="waiting.jsp">返回待办列表</a>
		</td>
	</tr>
</table>
<br />
<form id="dataForm" method="post" action="doChangeExe.jsp">
	代办人账号:<input type="text" name="newuser" /><br />
	代办人姓名:<input type="text" name="newname" />
	<input type="hidden" name="wid" value="<%=wid%>" />
	<input type="hidden" name="olduser" value="<%=olduser%>" />
	<input type="hidden" name="oldname" value="<%=oldname%>" />
</form>
<br />
</body>
</html>