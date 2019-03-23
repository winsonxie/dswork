<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/getById.jsp"%>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">明细</td>
		<td class="menuTool">
			<a class="back" onclick="parent.refreshNode(false);return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
<c:if test="${dict.level > 1}">
<c:if test="${fn:length(dict.rules) > 0}">
	<tr>
		<td class="form_title">编号</td>
		<td class="form_input">${fn:escapeXml(po.id)}</td>
	</tr>
</c:if>
	<tr>
		<td class="form_title">层级</td>
		<td class="form_input">${fn:escapeXml(po.level)}</td>
	</tr>
</c:if>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input">${fn:escapeXml(po.label)}</td>
	</tr>
	<tr>
		<td class="form_title">备注</td>
		<td class="form_input">${fn:escapeXml(po.memo)}</td>
	</tr>
</table>
</body>
</html>
