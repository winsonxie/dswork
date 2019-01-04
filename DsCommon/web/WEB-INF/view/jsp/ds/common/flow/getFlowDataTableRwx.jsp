<%@page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
var model = parent.getModel();
$(function(){
	if(model != null && model != ""){
		var datatable = JSON.parse(model);
		for (var i = 0; i < datatable.length; i++) {
			paintTable(datatable, datatable[i]);
		}
	}
	else{
		$("#datatable").append('<tr class="list_title"><td>无可授权信息</td></tr>');
	}
});

function paintTable(datatable, row){
	var rwx = row.trwx;
	$("#datatable").append(''
	+ '<tr><td class="form_title">字段名称</td>'
	+ '<td class="form_input"><input type="hidden" name="tname" value="' + row.tname + '" />' + row.talias + "[" + row.tname + "]" + '</td>'
	+ '<td class="form_title">字段权限</td>'
	+ '<td class="form_input">'
	+ '<input type="radio" name="' + row.tname + '_rwx" value="400" ' + (rwx=="400"?"checked":"") + ' />只读 '
	+ '<input type="radio" name="' + row.tname + '_rwx" value="420" ' + (rwx=="420"?"checked":"") + ' />编辑 '
	+ '<input type="radio" name="' + row.tname + '_rwx" value="001" ' + (rwx=="001"?"checked":"") + ' />隐藏 '
	+ '</td></tr>'
	);
	/* 
	$("input[name='"+row.tname+"_rwx']").each(function(i){
		$(this).click(function(){
            if(this.checked==true){
				row.trwx = this.value;
				parent.setModel(JSON.stringify(datatable));
            }
         });
	}); */
}

$(function(){
	$("input[type=radio]").click(function(){
		if(model != null && model != ""){
			var datatable = JSON.parse(model);
			for (var i = 0; i < datatable.length; i++) {
				var row = datatable[i];
				$("input[name='"+row.tname+"_rwx']").each(function(i){
		            if(this.checked==true){
		            	 row.trwx = this.value;
		                 parent.setModel(JSON.stringify(datatable));
		            }
				});
			} 
		}
	});
});

function cancel(){
	parent.setModel(model);
	parent.$jskey.dialog.close();
}
function save(){
	parent.$jskey.dialog.close();
}
function chooseRWX(val){
	$('input[type="radio"]').each(function(){
		if($(this).val()==val){
			$(this).prop("checked", true);
			$(this).attr("checked","");
		}
		else{
			$(this).removeAttr("checked");
		}
	});
	if(model != null && model != ""){
		var datatable = JSON.parse(model);
		for (var i = 0; i < datatable.length; i++) {
			var row = datatable[i];
			$("input[name='"+row.tname+"_rwx']").each(function(i){
	            if(this.checked==true){
	            	 row.trwx = this.value;
	                 parent.setModel(JSON.stringify(datatable));
	            }
			});
		} 
	}
}
</script>
</head>
<body>
<form id="dataForm" method="post" action="updFlowDataTableRwx.htm">
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="menuTool">
			<a class="select" onclick="chooseRWX(400);return false;" href="#">只读全选</a>
			<a class="select" onclick="chooseRWX(420);return false;" href="#">编辑全选</a>
			<a class="select" onclick="chooseRWX(001);return false;" href="#">隐藏全选</a>
			<a class="save" onclick="save();return false;" href="#">确定修改</a>
			<a class="close" onclick="cancel();return false;" href="#">取消修改</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable" id="datatable">
	<tbody></tbody>
</table>
</form>
</body>
</html>
