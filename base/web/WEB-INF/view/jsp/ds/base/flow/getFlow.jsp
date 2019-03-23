<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
function updStatus(objid, id){
	var obj = $("#" + objid), o = document.getElementById(objid);
	$.post("updStatus.htm",{"keyIndex":id,"status":obj.attr("v")==0?1:0},function(data){
		$dswork.checkResult(data);if($dswork.result.type == 1){
		obj.removeClass("pause").removeClass("start");
		if(1 == obj.attr("v")){
			obj.text("启用").attr("v", 0).addClass("start");$("#td_" + objid).text("禁用").css("color", "red");
		}
		else{
			obj.text("禁用").attr("v", 1).addClass("pause");$("#td_" + objid).text("启用").css("color", "");
		}}else{alert($dswork.result.msg);}
	});
	return false;
};
function deployFlow(id){
	if(confirm("确认将当前流程配置发布成为正式版本吗？")){
		$.post("deployFlow.htm",{"keyIndex":id},function(data){$dswork.checkResult(data);if($dswork.result.type == 1){
			$("#queryForm").submit();
		}});
	}
	return false;
};
$dswork.page.join = function(td, menu, id){
	$(menu).append($('<div iconCls="menuTool-user">发布</div>').bind("click", function(){
		return deployFlow(id);
	}));
};
$(function(){
	$dswork.page.menu("", "updFlow1.htm", "getFlowById.htm", "1");
});
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getFlow.htm?categoryid=${fn:escapeXml(param.categoryid)}";
}};
$(function(){
	$("#status").bind("change", function(){
		$("#queryForm").submit();
	});
});
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">${fn:escapeXml(po.name)}列表</td>
		<td class="menuTool">
			<a class="insert" href="addFlow1.htm?categoryid=${fn:escapeXml(param.categoryid)}">添加</a>
			<a class="delete" id="listFormDelAll" href="#">删除所选</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getFlow.htm?categoryid=${fn:escapeXml(param.categoryid)}">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;关键字：<input type="text" name="keyvalue" style="width:200px;" value="${fn:escapeXml(param.keyvalue)}" />
			&nbsp;状态:<select name="status" style="width:55px;" v="${fn:escapeXml(param.status)}"><option value="">全部</option><option value="1">启用</option><option value="0">禁用</option></select>
		</td>
		<td class="query"><input id="_querySubmit_" type="button" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<form id="listForm" method="post" action="delFlow.htm">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:2%"><input id="chkall" type="checkbox" /></td>
		<td style="width:5%">操作</td>
		<td>流程标识</td>
		<td>名字</td>
		<td>流程发布ID</td>
		<td>状态</td>
		<td>操作</td>
	</tr>
<c:forEach items="${list}" var="d" varStatus="status">
	<tr>
		<td><input name="keyIndex" type="checkbox" value="${d.id}"${''==d.deployid?'':' style="display:none;"'} /></td>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td>${fn:escapeXml(d.alias)}</td>
		<td>${fn:escapeXml(d.name)}</td>
		<td>${fn:escapeXml(d.deployid)}</td>
		<td id="td_a_status${status.index}" style="color:${1==d.status?"":"red"}">${1==d.status?"启用":"禁用"}</td>
		<td class="menuTool">
			<a id="a_status${status.index}" name="a_status" v="${d.status}" class="${1==d.status?'pause':'start'}"${''==d.deployid?' style="display:none;"':''} href="#" onclick="return updStatus('a_status${status.index}', '${d.id}');">${1==d.status?'禁用':'启用'}</a>
			<a class="update" href="updFlow1.htm?keyIndex=${d.id}">修改</a>
			<a class="menuTool-user" onclick="return deployFlow('${d.id}');">发布</a>
		</td>
	</tr>
</c:forEach>
</table>
</form>
</body>
</html>
