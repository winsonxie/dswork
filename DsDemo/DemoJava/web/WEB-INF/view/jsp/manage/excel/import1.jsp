<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/web.jsp"%>
<script type="text/javascript" src="/web/js/layui/layer/layer.js"></script>
<script type="text/javascript">
$dswork.callback = function(){
	if($dswork.result.code == 1){
		layer.msg('导入成功！', {
		    icon: 1,
		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		}, function(){
			location.href="import1.htm";
		});
	}
	else{
		var d = $dswork.result.data;
		if(d && d.length > 0){
			$("#showBOX").show();
			for(var i = 0; i < d.length; i++){
				var p = d[i];
				$("#dataTable").append("<tr><td>第"+p.i+"行</td><td>"+p.msg+"</td></tr>");
			}
		}
	}
};
$dswork.validCallBack = function(){
	$("#showBOX").hide();
	$("#dataTable>tbody>tr:gt(0)").remove();
	if($("#excel").val()==""){
		layer.msg("请先上传文件!", {icon:2,time:3000});
		return false;
	}
	return true;
};
</script>
</head>
<body>
<div class="line"></div>
<form id="dataForm" enctype="multipart/form-data" method="post" action="import2.htm">
<table border="0" class="listTable">
	<tr><td class="form_title">上传设置文件(首行为《标题、内容、时间》)</td>
		<td class="form_input">
			&nbsp;<input id="excel" name="excel" type="file" accept=".xls, .xlsx" onclick="$('#showBOX').hide();$('#dataTable>tbody>tr:gt(0)').remove();" />
		</td>
	</tr>
	<tr><td colspan="2" style="text-align:center;">
		<input type="button" id="dataFormSave" style="width:100px;height:30px;margin:10px;" value="提交" />
	</td></tr>
</table>
</form>
<div id="showBOX" style="display:none;">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td colspan="2">问题详情</td>
	</tr>
</table>
</div>
</body>
</html>
