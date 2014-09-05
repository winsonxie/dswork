<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp" %>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getCategory.htm?siteid=${param.siteid}";
}};
$(function(){
	$("#status").bind("click", function(){
		if($("#status").val() == 2){
			$("#mylink").show();
			$("#url").attr("require", "true");
		}
		else{
			$("#mylink").hide();
			$("#url").attr("require", "false");
		}
	});
	$("#status").click();
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getCategory.htm?siteid=${param.siteid}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addCategory2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">上级栏目</td>
		<td class="form_input"><select name="pid"><option value="0">≡顶级栏目≡</option>
		<c:forEach items="${list}" var="d">
			<option value="${d.id}">${d.label}${fn:escapeXml(d.name)}</option>
		</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td class="form_title">栏目名称</td>
		<td class="form_input"><input type="text" name="name" maxlength="100" dataType="Require" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">目录名称</td>
		<td class="form_input"><input type="text" name="folder" maxlength="50" dataType="Char" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input"><select id="status" name="status" style="width:100px;">
			<option value="0">列表</option>
			<option value="1">单页</option>
			<option value="2">外链</option>
		</select> <span style="font-weight:bold;">添加后不可修改</span></td>
	</tr>
	<tbody id="mylink">
	<tr>
		<td class="form_title">链接</td>
		<td class="form_input"><input type="text" id="url" name="url" maxlength="100" style="width:400px;" dataType="Require" require="false" value="" /></td>
	</tr>
	</tbody>
	<tr>
		<td class="form_title">图片</td>
		<td class="form_input"><input type="text" name="img" maxlength="100" style="width:400px;" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">栏目模板</td>
		<td class="form_input"><select name="viewsite" style="width:400px;"><option value=""></option>
			<c:forEach items="${templates}" var="v"><option value="${v}">${v}</option></c:forEach>
		</select></td>
	</tr>
	<tr>
		<td class="form_title">栏目APP模板</td>
		<td class="form_input"><select name="viewapp" style="width:400px;"><option value=""></option>
			<c:forEach items="${templates}" var="v"><option value="${v}">${v}</option></c:forEach>
		</select></td>
	</tr>
	<tr>
		<td class="form_title">内容模板</td>
		<td class="form_input"><select name="pageviewsite" style="width:400px;"><option value=""></option>
			<c:forEach items="${templates}" var="v"><option value="${v}">${v}</option></c:forEach>
		</select></td>
	</tr>
	<tr>
		<td class="form_title">内容APP模板</td>
		<td class="form_input"><select name="pageviewapp" style="width:400px;"><option value=""></option>
			<c:forEach items="${templates}" var="v"><option value="${v}">${v}</option></c:forEach>
		</select></td>
	</tr>
</table>
<input type="hidden" name="siteid" value="${param.siteid}" />
</form>
</body>
</html>
