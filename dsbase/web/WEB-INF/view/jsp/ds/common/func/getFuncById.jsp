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
			<a class="back" onclick="parent.$dswork.ztree.click();return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input">${fn:escapeXml(po.name)}</td>
	</tr>
	<tr>
		<td class="form_title">标识</td>
		<td class="form_input">${fn:escapeXml(po.alias)}</td>
	</tr>
	<tr>
		<td class="form_title">资源地址</td>
		<td class="form_input">${fn:escapeXml(po.uri)}</td>
	</tr>
	<%--<tr>
		<td class="form_title">图标</td>
		<td class="form_input"><img id="img_img" /></td>
	</tr>--%>
	<tr>
		<td class="form_title">显示到菜单</td>
		<td class="form_input">${1==po.status?"是":"否"}</td>
	</tr>
	<tr>
		<td class="form_title">扩展信息</td>
		<td class="form_input"><textarea id="memo" name="memo" style="width:400px;height:60px;" class="readonlytext" readonly="readonly">${fn:escapeXml(po.memo)}</textarea></td>
	</tr>
</table>
<div class="line"></div>
<c:if test="${0<fn:length(list)}">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>权限资源[参数]列表</td>
	</tr>
	<c:forEach items="${list}" var="d" varStatus="status">
	<tr class="${0==status.index%2?'list_even':'list_odd'}">
		<td class="form_input">&nbsp;${fn:escapeXml(d.url)}&nbsp;[${fn:escapeXml(d.param)}]</td>
	</tr>
	</c:forEach>
</table>
</c:if>
</body>
</html>
