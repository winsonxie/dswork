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
			<a class="back" onclick="javascript:window.history.back();return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">帐号</td>
		<td class="form_input">${fn:escapeXml(po.account)}</td>
	</tr>
	<tr>
		<td class="form_title">姓名</td>
		<td class="form_input">${fn:escapeXml(po.name)}</td>
	</tr>
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input">${fn:escapeXml(po.typename)}&nbsp;&nbsp;&nbsp;&nbsp;${fn:escapeXml(po.exname)}</td>
	</tr>
	<tr>
		<td class="form_title">身份证号</td>
		<td class="form_input">${fn:escapeXml(po.idcard)}</td>
	</tr>
	<tr>
		<td class="form_title">邮箱</td>
		<td class="form_input">${fn:escapeXml(po.email)}</td>
	</tr>
	<tr>
		<td class="form_title">手机</td>
		<td class="form_input">${fn:escapeXml(po.mobile)}</td>
	</tr>
	<tr>
		<td class="form_title">电话</td>
		<td class="form_input">${fn:escapeXml(po.phone)}</td>
	</tr>
	<tr>
		<td class="form_title">工作证号</td>
		<td class="form_input">${fn:escapeXml(po.workcard)}</td>
	</tr>
	<tr>
		<td class="form_title">CA证书</td>
		<td class="form_input">${fn:escapeXml(po.cakey)}</td>
	</tr>
	<tr>
		<td class="form_title">状态</td>
		<td class="form_input">${fn:escapeXml(1==po.status?"启用":"禁用")}</td>
	</tr>
	<tr>
		<td class="form_title">创建时间</td>
		<td class="form_input">${fn:escapeXml(po.createtime)}</td>
	</tr>
</table>
</body>
</html>
