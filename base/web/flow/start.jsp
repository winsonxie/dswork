<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="dswork.common.DsFactory"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/web.jsp"%>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">流程测试</td>
		<td class="menuTool">
			<a class="back" href="waiting.jsp">返回待办任务</a>
		</td>
	</tr>
</table>
<%
//String piid = DsFactory.getFlow().start("tech_duty", "1000", "0", "admin", "管理员", 0, true);
String piid = DsFactory.getFlow().start("sw", "admin", "1000", "0", "admin", "管理员", 0, true, true);
%>
<%=piid.equals("")?"启动失败":piid%>
<br />
</body>
</html>