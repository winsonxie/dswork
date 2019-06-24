<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/web.jsp"%>
<script type="text/javascript">
$(function(){
	$("#status").text($("#status").text()=="1"?"正常运营":$("#status").text()=="0"?"禁用":$("#status").text()=="2"?"待审":"已注销");
});
</script>
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
		<td class="form_title">企业名称</td>
		<td class="form_input">${fn:escapeXml(po.name)}</td>
	</tr>
	<tr>
		<td class="form_title">企业编码</td>
		<td class="form_input">${fn:escapeXml(po.qybm)}</td>
	</tr>
	<tr>
		<td class="form_title">状态</td>
		<td id="status" class="form_input">${fn:escapeXml(po.status)}</td>
	</tr>
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input">${fn:escapeXml(po.type)}</td>
	</tr>
	<tr>
		<td class="form_title">账号</td>
		<td class="form_input">${fn:escapeXml(admin.account)}</td>
	</tr>
	<tr>
		<td class="form_title">姓名</td>
		<td class="form_input">${fn:escapeXml(admin.name)}</td>
	</tr>
	<tr>
		<td class="form_title">身份证号</td>
		<td class="form_input">${fn:escapeXml(admin.idcard)}</td>
	</tr>
	<tr>
		<td class="form_title">电子邮件</td>
		<td class="form_input">${fn:escapeXml(admin.email)}</td>
	</tr>
	<tr>
		<td class="form_title">手机</td>
		<td class="form_input">${fn:escapeXml(admin.mobile)}</td>
	</tr>
	<tr>
		<td class="form_title">电话</td>
		<td class="form_input">${fn:escapeXml(admin.phone)}</td>
	</tr>
	<tr>
		<td class="form_title">传真</td>
		<td class="form_input">${fn:escapeXml(admin.fax)}</td>
	</tr>
	<tr>
		<td class="form_title">创建时间</td>
		<td class="form_input">${fn:escapeXml(admin.createtime)}</td>
	</tr>
</table>
</body>
</html>
