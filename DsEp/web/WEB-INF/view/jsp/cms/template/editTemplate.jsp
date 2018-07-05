<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	window.parent.setTextChange(false);
}};
function onChangeListen(){
	window.parent.setTextChange(true);
}
</script>
<style type="text/css">
</style>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">&nbsp;</td>
		<td class="menuTool">
			<a class="save" href="#" id="dataFormSave">保存</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="editTemplate2.htm">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td>
			<textarea style="width:100%;height:400px;font-size:14px;overflow:scroll;" name="content" onchange="onChangeListen()">${fn:escapeXml(content)}</textarea>
		</td>
	</tr>
</table>
<input type="hidden" name="path" value="${path}"/>
<input type="hidden" name="siteid" value="${siteid}"/>
</form>
</body>
</html>
