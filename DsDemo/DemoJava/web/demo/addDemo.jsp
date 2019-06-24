<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<%@include file="/commons/include/add.jsp"%>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getDemo.htm">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="${ctx}/manage/my/save/Demo.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">标题：</td>
		<td class="form_input"><input type="text" id="title" name="title" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">内容：</td>
		<td class="form_input"><input type="text" id="content" name="content" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">创建时间：</td>
		<td class="form_input"><input type="text" id="foundtime" name="foundtime" class="WebDate" format="yyyy-MM-dd" value="" /></td>
	</tr>
</table>
</form>
</body>
</html>
