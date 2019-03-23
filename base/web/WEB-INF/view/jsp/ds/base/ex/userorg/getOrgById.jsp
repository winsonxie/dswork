<%@page language="java" pageEncoding="utf-8"%>
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
		<td class="title">岗位授权信息</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">名称：</td>
		<td class="form_input">${fn:escapeXml(po.name)}</td>
	</tr>
	<tr>
		<td class="form_title">职责范围：</td>
		<td class="form_input"><textarea style="width:400px;height:60px;" class="readonlytext" readonly="readonly">${fn:escapeXml(po.dutyscope)}</textarea></td>
	</tr>
	<tr>
		<td class="form_title">备注：</td>
		<td class="form_input"><textarea style="width:400px;height:40px;" class="readonlytext" readonly="readonly">${fn:escapeXml(po.memo)}</textarea></td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">岗位下用户</td>
	</tr>
</table>
<div class="line"></div>
<table id="contactTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:15%">类型</td>
		<td style="width:25%;">姓名(帐号)</td>
		<td>单位</td>
		<td style="width:15%;">部门</td>
		<td style="width:7%;">状态</td>
	</tr>
<c:forEach items="${list}" var="d">
	<tr>
		<td>${fn:escapeXml(d.typename)}</td>
		<td style="text-align:left;">&nbsp;${fn:escapeXml(d.name)}(${fn:escapeXml(d.account)})</td>
		<td>${fn:escapeXml(d.orgpname)}</td>
		<td>${fn:escapeXml(d.orgname)}</td>
		<td>${1==d.status?"启用":"禁用"}</td>
	</tr>
</c:forEach>
</table>
</body>
</html>
