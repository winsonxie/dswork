<%@page language="java" pageEncoding="UTF-8" import="java.util.*,${frame}.web.MyRequest,
${namespace}.MyFactory"%><%
MyRequest req = new MyRequest(request); 
request.setAttribute("param", req.getParameterValueMap(false, false));
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/web.jsp"%>
<script type="text/javascript">
$${frame}.callback = function(){if($${frame}.result.code == 1){
	location.href = "list.jsp";
}};
</script>
</head>
<body>
<table class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="list.jsp?page=${'$'}{fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="add2.jsp">
<table class="listTable">
<#list table.columnNokey as c>
	<#if c.length<=4000>
	<tr>
		<td class="form_title">${c.comment}</td>
		<td class="form_input"><input type="text" name="${c.nameLowerCamel}" maxlength="${c.length}" value="" /></td>
	</tr>
	<#else>
	<tr>
		<td class="form_title">${c.comment}</td>
		<td class="form_input"><textarea name="${c.nameLowerCamel}" style="width:400px;height:60px;"></textarea></td>
	</tr>
	</#if>
</#list>
</table>
</form>
</body>
</html>
