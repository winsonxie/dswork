<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<script type="text/javascript">
$(function(){
	
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">用户明细</td>
	</tr>
</table>
<div class="line"></div>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input">${fn:escapeXml(po.typename)}</td>
	</tr>
	<tr>
		<td class="form_title">姓名(账号)</td>
		<td class="form_input">${fn:escapeXml(po.name)}(${fn:escapeXml(po.account)})</td>
	</tr>
	<tr>
		<td class="form_title">单位</td>
		<td class="form_input">${fn:escapeXml(po.orgpname)}</td>
	</tr>
	<tr>
		<td class="form_title">部门</td>
		<td class="form_input">${fn:escapeXml(po.orgname)}</td>
	</tr>
	<tr>
		<td class="form_title">状态</td>
		<td class="form_input">${po.status==1?"启用":"禁用"}</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">岗位列表</td>
	</tr>
</table>
<div class="line"></div>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>岗位名称</td>
	</tr>
<c:forEach items="${list}" var="d">
	<tr>
		<td>${d.name}</td>
	</tr>
</c:forEach>
</table>
</body>
</html>
