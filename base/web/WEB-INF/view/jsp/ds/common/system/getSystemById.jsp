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
			<a class="back" onclick="window.history.back();return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">应用标识</td>
		<td class="form_input">${fn:escapeXml(po.alias)}</td>
	</tr>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input">${fn:escapeXml(po.name)}</td>
	</tr>
	<tr>
		<td class="form_title">应用密码</td>
		<td class="form_input">${fn:escapeXml(po.password)}</td>
	</tr>
	<tr>
		<td class="form_title">网络地址</td>
		<td class="form_input">${fn:escapeXml(po.domainurl)}</td>
	</tr>
	<tr>
		<td class="form_title">根路径</td>
		<td class="form_input">${fn:escapeXml(po.rooturl)}</td>
	</tr>
	<tr>
		<td class="form_title">菜单地址</td>
		<td class="form_input">${fn:escapeXml(po.menuurl)}</td>
	</tr>
	<tr>
		<td class="form_title">状态</td>
		<td class="form_input">${1==po.status?"启用":"禁用"}</td>
	</tr>
	<tr>
		<td class="form_title">备注</td>
		<td class="form_input"><textarea id="memo" name="memo" style="width:99%;height:100px;" class="readonlytext">${fn:escapeXml(po.memo)}</textarea></td>
	</tr>
</table>
</body>
</html>
