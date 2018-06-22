<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp" %>
<script type="text/javascript">
$dswork.deleteRow = function(obj){$(obj).parent().parent().remove();};
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getCategory.htm?siteid=${fn:escapeXml(param.siteid)}";
}};
$(function(){
	$("#scope").bind("click", function(){
		if($("#scope").val() == 2){
			$("#mylink").show();
			$("#template_category").val("").hide();
			$("#template_page").val("").hide();
			$(".viewsite").val("");
			$(".pageviewsite").val("");
			$("#url").attr("require", "true");
		}
		else if($("#scope").val() == 1){
			$("#mylink").val("").hide();
			$("#template_category").show();
			$("#template_page").val("").hide();
			$(".pageviewsite").val("");
			$("#url").attr("require", "false");
		}
		else{
			$("#mylink").val("").hide();
			$("#template_category").show();
			$("#template_page").show();
			$("#url").attr("require", "false");
		}
	}).bind("change", function(){
		$("#scope").click();
	});
	$("#scope").click();
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getCategory.htm?siteid=${fn:escapeXml(param.siteid)}">返回</a>
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
			<option value="${d.id}">${d.label}${fn:escapeXml(d.name)}&nbsp;[${d.scope==0?'列表':d.scope==1?'单页':'外链'}]</option>
		</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td class="form_title">栏目名称</td>
		<td class="form_input"><input type="text" name="name" maxlength="100" dataType="Require" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input">
			<select id="scope" name="scope" style="width:100px;">
				<option value="0">列表</option>
				<option value="1">单页</option>
				<option value="2">外链</option>
			</select>
		</td>
	</tr>
	<tbody id="mylink">
	<tr>
		<td class="form_title">链接</td>
		<td class="form_input"><input type="text" id="url" name="url" maxlength="100" style="width:400px;" dataType="Require" require="false" value="" /></td>
	</tr>
	</tbody>
	<tbody id="template_category">
	<tr>
		<td class="form_title">栏目模板</td>
		<td class="form_input"><select class="viewsite" name="viewsite" style="width:400px;"><option value=""></option>
			<c:forEach items="${templates}" var="v"><c:if test="${not fn:startsWith(v,'page')}"><option value="${v}">${fn:replace(v,'.jsp','')}</option></c:if></c:forEach>
		</select></td>
	</tr>
<c:if test="${enablemobile}">
	<tr>
		<td class="form_title">移动版栏目模板</td>
		<td class="form_input"><select class="viewsite" name="mviewsite" style="width:400px;"><option value=""></option>
			<c:forEach items="${mtemplates}" var="v"><c:if test="${not fn:startsWith(v,'page')}"><option value="${v}">${fn:replace(v,'.jsp','')}</option></c:if></c:forEach>
		</select></td>
	</tr>
</c:if>
	</tbody>
	<tbody id="template_page">
	<tr>
		<td class="form_title">内容模板</td>
		<td class="form_input"><select class="pageviewsite" name="pageviewsite" style="width:400px;"><option value=""></option>
			<c:forEach items="${templates}" var="v"><c:if test="${fn:startsWith(v,'page')}"><option value="${v}">${fn:replace(v,'.jsp','')}</option></c:if></c:forEach>
		</select></td>
	</tr>
<c:if test="${enablemobile}">
	<tr>
		<td class="form_title">移动版内容模板</td>
		<td class="form_input"><select class="pageviewsite" name="mpageviewsite" style="width:400px;"><option value=""></option>
			<c:forEach items="${mtemplates}" var="v"><c:if test="${fn:startsWith(v,'page')}"><option value="${v}">${fn:replace(v,'.jsp','')}</option></c:if></c:forEach>
		</select></td>
	</tr>
</c:if>
	</tbody>
</table>
<div class="line"></div>
<table id="contactTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:35%;">变量名</td>
		<td style="width:35%;">名称</td>
		<td style="width:10%;">类型</td>
		<td class="menuTool"><a class="insert" onclick="$('#contactTable>tbody').append($('#cloneTable').html());" href="#">添加项</a></td>
	</tr>
</table>
<script type="text/template" id="cloneTable">
	<tr class="list">
		<td><input type="text" name="ctitle" dataType="Char" value="" /></td>
		<td><input type="text" name="cname" value="" /></td>
		<td><select name="cdatatype">
			<option value="">无校验</option>
			<option value="Char">Char</option>
			<option value="Chinese">Chinese</option>
			<option value="Email">Email</option>
			<option value="IdCard">IdCard</option>
			<option value="UserCard">UserCard</option>
			<option value="UnitCode">UnitCode</option>
			<option value="OrgCode">OrgCode</option>
			<option value="Mobile">Mobile</option>
			<option value="Money">Money</option>
			<option value="Numeral">Numeral</option>
			<option value="Phone">Phone</option>
			<option value="Require">Require</option>
			<option value="RequireCompact">RequireCompact</option>
			<option value="RequireTrim">RequireTrim</option>
			<option value="Url">Url</option>
			<option value="Zip">Zip</option>
			<option value="Number">Number</option>
			<option value="NumberPlus">NumberPlus</option>
			<option value="NumberMinus">NumberMinus</option>
			<option value="Integer">Integer</option>
			<option value="IntegerPlus">IntegerPlus</option>
			<option value="IntegerMinus">IntegerMinus</option>
			<option value="Filename">Filename</option>
			<option value="Password">Password</option>
		</select></td>
		<td><input type="button" class="delete" onclick="$dswork.deleteRow(this)" /></td>
	</tr>
</script>
<input type="hidden" name="siteid" value="${fn:escapeXml(param.siteid)}" />
</form>
</body>
</html>
