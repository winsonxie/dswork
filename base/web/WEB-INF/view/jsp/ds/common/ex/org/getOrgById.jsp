<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/getById.jsp"%>
<script type="text/javascript">$(function(){$("#status").text($("#status").text()=="2"?"单位":($("#status").text()=="1"?"部门":"岗位"));});</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">明细</td>
		<td class="menuTool">
			<c:if test="${empty param.noback}"><a class="back" onclick="parent.$dswork.ztree.click();return false;" href="#">返回</a></c:if>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<c:if test="${0 < parent.id}">
	<tr> 
		<td class="form_title">上级名称</td>
		<td class="form_input">${fn:escapeXml(parent.name)}</td>
	</tr>
	</c:if>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input">${fn:escapeXml(po.name)}</td>
	</tr>
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input" id="status">${po.status}</td>
	</tr>
	<tr>
		<td class="form_title">职责范围</td>
		<td class="form_input"><textarea style="width:400px;height:60px;" class="readonlytext" readonly="readonly">${fn:escapeXml(po.dutyscope)}</textarea></td>
	</tr>
	<tr>
		<td class="form_title">备注</td>
		<td class="form_input"><textarea style="width:400px;height:40px;" class="readonlytext" readonly="readonly">${fn:escapeXml(po.memo)}</textarea></td>
	</tr>
</table>
</body>
</html>
