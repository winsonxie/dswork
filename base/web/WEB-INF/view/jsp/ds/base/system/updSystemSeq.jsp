<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<script type="text/javascript">
$(function(){
	$("#dataForm").submit(function(event){
		event.preventDefault();
		$jskey.select.reselect('keyIndex', true);
		$("#dataForm").ajaxSubmit($dswork.doAjaxOption);
	});
	try{$(".form_title").css("width", "70%");}catch(e){}
}); 
$dswork.callback = function(){
	try{if($dswork.result.type == 1){parent.$jskey.dialog.close();}}catch(e){}
};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">排序</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updSystemSeq2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>名称</td>
		<td>操作</td> 
	</tr>
	<tr>
		<td class="form_title" style="text-align:center;">
			<select multiple="multiple" id="keyIndex" name="keyIndex" style="width:100%;height:380px;">
				<c:forEach items="${list}" var="d">
					<option value="${d.id}">${fn:escapeXml(d.name)}(${fn:escapeXml(d.alias)})</option>
				</c:forEach>
			</select>
		</td>
		<td class="form_input" style="text-align:center;">
			<input type="button" class="button" value="移至最前" onclick="$jskey.select.move('keyIndex', 0, true);" /><br /><br />
			<input type="button" class="button" value="向上移动" onclick="$jskey.select.move('keyIndex', document.getElementById('moveCount').value, true);" /><br /><br />
			<input type="button" class="button" value="向下移动" onclick="$jskey.select.move('keyIndex', document.getElementById('moveCount').value, false);" /><br /><br />
			<input type="button" class="button" value="移至最后" onclick="$jskey.select.move('keyIndex', 0, false);" /><br /><br />
			向上(下)移动<select id="moveCount">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
				<option value="10">10</option>
			</select>格
		</td>
	</tr>
</table>
</form>
</body>
</html>
