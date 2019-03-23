<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getDict.htm?mark=${fn:escapeXml(param.mark)}&islist=${fn:escapeXml(param.islist)}&page=${fn:escapeXml(param.page)}";
}};
$(function(){
	var v = $("#level").text();
	if(v == "0"){v = "树形集合";}
	else if(v == "1"){v = "列表集合";}
	else if(v > 1){v = v + "级树形集合"}
	else {v = "";}
	$("#level").text(v);
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getDict.htm?mark=${fn:escapeXml(param.mark)}&islist=${fn:escapeXml(param.islist)}&page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updDict2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">引用名</td>
		<td class="form_input"><input id="v1" type="text" name="name" datatype="Char" maxlength="100" value="${fn:escapeXml(po.name)}" /></td>
	</tr>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input"><input type="text" name="label" datatype="Require" maxlength="100" value="${fn:escapeXml(po.label)}" /></td>
	</tr>
	<tr>
		<td class="form_title">分类</td>
		<td class="form_input" id="level">${po.level}</td>
	</tr>
<c:if test="${fn:length(po.rules) > 0}">
	<tr>
		<td class="form_title">编码规则</td>
		<td class="form_input" id="level">
			<c:forEach items="${po.rules}" var="d" varStatus="status">
				${d}<c:if test="${!status.last}"> - </c:if>
			</c:forEach>
		</td>
	</tr>
</c:if>
</table>
<input type="hidden" name="id" value="${po.id}" />
</form>
</body>
</html>
