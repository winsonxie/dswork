<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$(function(){
var model = parent.getModel();
if(model != null && model != ""){
	$("#typeJson").val(model);
}
else{
	$("#typeJson").val("[]");
}
});

function dataTableSave(){
	parent.setModel($("#typeJson").val());
	parent.$jskey.dialog.close();
}
function cancel(){parent.$jskey.dialog.close();}
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="menuTool">
			<a class="save" id="dataTableSave" onclick="dataTableSave();return false;" href="#">确定修改</a>
			<a class="close" id="close" onclick="cancel()" href="#">取消修改</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<textarea id="typeJson" style="width:100%;height:300px;"></textarea>
</body>
</html>