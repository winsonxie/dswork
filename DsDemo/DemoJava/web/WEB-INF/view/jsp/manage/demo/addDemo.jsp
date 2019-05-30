<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getDemo.htm";
}};
</script>
</head>
<body>
<table class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getDemo.htm?page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addDemo2.htm">
<table class="listTable">
	<tr>
		<td class="form_title">标题</td>
		<td class="form_input"><input type="text" name="title" maxlength="30" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">内容</td>
		<td class="form_input"><input type="text" name="content" maxlength="300" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">创建时间</td>
		<td class="form_input"><input id="a1" tomax="a2" type="text" name="foundtime" maxlength="19" class="WebDate" format="yyyy-MM-dd" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">创建时间(测试时间)</td>
		<td class="form_input"><input id="a2" tomin="a1" type="text" name="foundtime1" maxlength="19" class="WebDate" format="yyyy-MM-dd" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">创建时间测试时间)</td>
		<td class="form_input"><input id="a3" tomin="a1" tomax="a2" type="text" name="foundtime2" maxlength="19" class="WebDate" format="yyyy-MM-dd" value="" /></td>
	</tr>
</table>
</form>
</body>
</html>
