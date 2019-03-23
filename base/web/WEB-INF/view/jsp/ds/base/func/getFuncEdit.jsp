<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp"%>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">资源配置<span style="color:#ff0000;font-weight:bold;">（本模块非技术维护人员，请勿操作）</span></td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存资源</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="getFuncEdit2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td>
			<textarea id="menujson" name="menujson" rows="40" style="width:100%;">${json}</textarea>
		</td>
	</tr>
</table>
<input type="hidden" name="systemid" value="${param.systemid}" />
</form>
</body>
</html>
