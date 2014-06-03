<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<%@include file="/commons/include/addAjax.jsp" %>
	<script type="text/javascript">
	$dswork.callback = function(){if($dswork.result.type == 1){
		if(!confirm("是否留在当前页面？")){location.href = "getUser.htm";}
	}};
	</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getUser.htm?page=${param.page}">返回</a> 
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addUser2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">帐号：</td>
		<td class="form_input"><input type="text" id="account" name="account" dataType="Char" maxlength="50" value="" /> <span class="imp">*</span> <span style="font-weight:bold;">添加后不可修改</span></td>
	</tr>
	<tr>
		<td class="form_title">姓名：</td>
		<td class="form_input"><input type="text" id="name" name="name" dataType="Chinese" maxlength="25" value="" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">密码：</td>
		<td class="form_input"><input type="password" id="password" name="password" style="width:130px;" dataType="Require" maxlength="32" value="000000" /> <span class="imp">*</span> <span style="font-weight:bold;">默认密码为：000000</span></td>
	</tr>
	<tr>
		<td class="form_title">确认密码：</td>
		<td class="form_input"><input type="password" id="password2" name="password2" style="width:130px;" dataType="Repeat" to="password" msg="两次输入的密码不一致" value="000000" /> <span class="imp">*</span></td>
	</tr>
	<tr>
		<td class="form_title">身份证号：</td>
		<td class="form_input"><input type="text" id="idcard" name="idcard" style="width:200px;" require="false" dataType="IdCard" maxlength="18" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">邮箱：</td>
		<td class="form_input"><input type="text" id="email" name="email" style="width:200px;" require="false" dataType="Email" maxlength="250" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">手机：</td>
		<td class="form_input"><input type="text" id="mobile" name="mobile" require="false" dataType="Mobile" maxlength="50" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">电话：</td>
		<td class="form_input"><input type="text" id="phone" name="phone" require="false" dataType="Phone" maxlength="50" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">工作证号：</td>
		<td class="form_input"><input type="text" id="workcard" name="workcard" style="width:200px;" require="false" dataType="Require" maxlength="64" value="" /></td>
	</tr>
</table>
</form>
</body>
</html>
