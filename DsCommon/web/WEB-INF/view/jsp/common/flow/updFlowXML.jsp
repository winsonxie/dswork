<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp"%>
<script type="text/javascript" src="/web/js/flow/dswork.flow.js"></script>
<script type="text/javascript" src="/web/js/flow/dswork.flow.check.js"></script>
<script type="text/javascript" src="/web/js/flow/dswork.flow.event.js"></script>
<style type="text/css">
.calias{width:80%;}
.cname{width:80%;}
.ctask{width:75%;margin:1px 0 0 1px;}
.ccount{width:30px;margin:1px 0 0 1px;}
.cparam{width:65%;margin:1px 0 0 1px;}
tr.list td {text-align:left;padding-left:1px;}

input[type="button"]:disabled{cursor:not-allowed;background-color:#eeeeee;border-color:#dddddd;background-image:none;opacity:.65;filter:alpha(opacity=65);box-shadow:none;}
select:disabled,input[type="text"]:disabled{cursor:not-allowed;background-color:#eeeeee;border-color:#dddddd;background-image:none;opacity:.65;filter:alpha(opacity=65);box-shadow:none;}
body, svg *{
	-webkit-touch-callout:none; /* iOS Safari */
	  -webkit-user-select:none; /* Safari */
	   -khtml-user-select:none; /* Konqueror HTML */
	     -moz-user-select:none; /* Firefox */
	      -ms-user-select:none; /* Internet Explorer/Edge */
	          user-select:none; /* Non-prefixed version, currently supported by Chrome and Opera */
}
</style>
<script type="text/javascript">
$dswork.validCallBack = function(){
	var msg = $dswork.flow.check($dswork.flow.p.flow);
	if(msg == ""){
		console.log($dswork.flow.p.flow.toXml(true));
		$("#flowxml").val($dswork.flow.p.flow.toXml());
		return true;
	}else{
		$dswork.doAjaxObject.autoDelayHide(msg.replace("\n", "<br/>"), 2000);
		$("#flowxml").val("");
		return false;
	}
};
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getFlow.htm?categoryid=${po.categoryid}";
}};
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getFlow.htm?categoryid=${po.categoryid}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updFlow2.htm">
<input type="hidden" name="id" value="${po.id}" />
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">流程标识</td>
		<td class="form_input">${fn:escapeXml(po.alias)}</td>
	</tr>
	<tr>
		<td class="form_title">流程名字</td>
		<td class="form_input"><input type="text" name="name" style="width:200px;" maxlength="300" dataType="Require" value="${fn:escapeXml(po.name)}" /></td>
	</tr>
</table>
<input type="hidden" id="flowxml" name="flowxml" value="" />
<div class="line"></div>
<div style="margin:0 auto;width:99%;text-align:left;height:68px;border:1px solid #999;">
	<div style="float:left;width:72px;height:68px;padding:0 3px;">
		<div style="float:left;width:72px;padding:8px 0 0 0;">
			<input id="btn_edit" type="button" class="button" style="padding:14px 10px;" style="display:none;" value="取消绘线" 
			/><input id="btn_line" type="button" class="button" style="padding:14px 10px;" value="绘制线路" />
		</div>
	</div>
	<div style="float:left;width:550px;height:68px;padding:0 3px;border-left:1px solid #999;">
		<div style="float:left;width:385px;padding:8px 0 0 0;">
			<div style="margin-bottom:5px;">
				标识：<input id="txt_alias" type="text" class="text" style="width:116px;" value="" />&nbsp;
				用户：<input id="txt_users" type="text" class="text" style="width:180px;" value="" />
			</div>
			<div>
				合并：<input id="txt_count" type="number" min="1" max="100" step="1" class="text" style="width:80px;" value="" />个任务&nbsp;
				名称：<input id="txt_name" type="text" class="text" style="width:180px;" value="" />
			</div>
		</div>
		<div style="float:left;width:80px;padding:8px 0 0 0;">
			<input id="btn_save" type="button" class="button" value="增改任务" />
			<div style="padding:6px 0 0 0;">
				<input id="btn_delete" type="button" class="button" value="不可操作" />
			</div>
		</div>
		<div style="float:left;width:72px;padding:8px 0;">
			<div style="margin-bottom:5px;">
				<input id="btn_check" type="button" class="button" value="校验流程" />
			</div>
			<div>
				<select id="txt_forks"><option value="">默认分支</option>
				<option value="A">分支组A</option>
				<option value="B">分支组B</option>
				<option value="C">分支组C</option>
				<option value="D">分支组D</option>
				<option value="E">分支组E</option>
				<option value="F">分支组F</option>
				<option value="G">分支组G</option>
				</select>
			</div>
		</div>
	</div>
	<div class="clearfix"></div>
</div>
<div id="myFlowSVG"></div>
<script type="text/xml" id="myFlowXML">
${po.flowxml}
</script>
</body>
</html>