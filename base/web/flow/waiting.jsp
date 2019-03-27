<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="dswork.common.DsFactory"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<style type="text/css">
li,a {line-height:2em;}
</style>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">流程测试</td>
		<td class="menuTool">
			<a class="start" href="start.jsp">启动新流程</a>
		</td>
	</tr>
</table>
<%request.setAttribute("list", DsFactory.getFlow().queryWaiting("admin"));%>
<ul>
<c:forEach items="${list}" var="d" varStatus="status">
	<li>
		${fn:escapeXml(d.flowname)}[${fn:escapeXml(d.talias)}]<c:if test="${fn:indexOf(d.tuser,'|,admin,') > 0}">[会签结束]</c:if>
		<c:if test="${d.tuser!=''}">
		&nbsp;<a class="update" href="do.jsp?wid=${d.id}">办理</a>&nbsp;<a target="_blank" class="update" href="${ctx}/common/share/showFlowRunning.htm?piid=${d.piid}">查看图形</a>
		&nbsp;<a class="update" href="changeExe.jsp?wid=${d.id}">代办</a>
		</c:if>
		<c:if test="${d.tusers!=''}">
		&nbsp;<a class="update" href="take.jsp?wid=${d.id}">取得任务</a>
		</c:if>
	</li>
</c:forEach>
</ul>
<br />
</body>
</html>