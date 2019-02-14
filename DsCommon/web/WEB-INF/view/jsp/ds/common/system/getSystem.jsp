<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
function updSystemStatus(objid, id){
	var obj = $("#" + objid);
	$.post("updSystemStatus.htm", {"keyIndex":id,"status":obj.attr("v")==0?1:0},function(data){
		obj.removeClass("pause").removeClass("start");
		$dswork.checkResult(data);if($dswork.result.type == 1){
		if(1 == obj.attr("v")) {
			obj.text("启用").attr("v", 0).addClass("start"); $("#td_" + objid).text("禁用").css("color", "red");
		}else{
			obj.text("禁用").attr("v", 1).addClass("pause"); $("#td_" + objid).text("启用").css("color", "");
		}}else{alert($dswork.result.msg);}
	});
	return false;
}
function updFunc(id){
	$jskey.dialog.showDialog({url:"../func/getFuncTree.htm?page=${pageModel.currentPage}&systemid=" + id,title:$("#td_n" + id).text()+'<span style="color:#ff0000;font-weight:bold;">（本模块非技术维护人员，请勿操作）</span>',fit:true,draggable:false});
	return false;
}
function updRole(id){
	$jskey.dialog.showDialog({url:"../role/getRoleTree.htm?page=${pageModel.currentPage}&systemid=" + id,title:$("#td_n" + id).text(),fit:true,draggable:false});
	return false;
}
function importFunc(id){
	$jskey.dialog.showDialog({url:"../func/getFuncEdit1.htm?systemid=" + id,title:$("#td_n" + id).text()+"资源配置",fit:true,draggable:false});
	return false;
}
function updSystemSeq(){
	$jskey.dialog.showDialog({url:"updSystemSeq1.htm",title:"系统排序",fit:true,draggable:false,callback:updSystemSeqCallback});
	return false;
}
function updSystemSeqCallback(){
	location.href = "getSystem.htm?page=${pageModel.currentPage}";
}
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getSystem.htm?page=${pageModel.currentPage}";
}};
$dswork.page.join = function(td, menu, id){
	$(menu).append($('<div iconCls="menuTool-graph">资源管理</div>').bind("click", function(){
		updFunc(id);
	})).append($('<div iconCls="menuTool-user">角色管理</div>').bind("click", function(){
		updRole(id);
	}));
};
$(function(){
	$dswork.page.menu("delSystem.htm", "updSystem1.htm", "getSystemById.htm", "${pageModel.currentPage}");
	$("#status").bind("change", function(){
		$("#queryForm").submit();
	});
});
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">应用系统列表</td>
		<td class="menuTool">
			<a class="insert" href="addSystem1.htm?page=${pageModel.currentPage}">添加</a>
			<a class="sort" href="#" onclick="updSystemSeq()">排序</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getSystem.htm">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;名称：<input type="text" class="text" id="name" name="name" value="${fn:escapeXml(param.name)}" style="width:150px;" />
			&nbsp;标识：<input type="text" class="text" id="alias" name="alias" value="${fn:escapeXml(param.alias)}" style="width:100px;" />
			&nbsp;状态：<select name="status" style="width:55px;" v="${fn:escapeXml(param.status)}"><option value="">全部</option><option value="1">启用</option><option value=0>禁用</option></select>
		</td>
		<td class="query"><input id="_querySubmit_" type="submit" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:5%">操作</td>
		<td>名称</td>
		<td style="width:15%;">标识</td>
		<td style="width:7%;">状态</td>
		<td style="width:45%;">操作</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d" varStatus="status">
	<tr>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td id="td_n${d.id}">${fn:escapeXml(d.name)}</td>
		<td>${fn:escapeXml(d.alias)}</td>
		<td id="td_a_status${status.index}" style="color:${1==d.status?"":"red"}">${1==d.status?"启用":"禁用"}</td>
		<td class="menuTool">
			<a id="a_status${status.index}" name="a_status" v="${d.status}" class="${1==d.status?'pause':'start'}" onclick="return updSystemStatus('a_status${status.index}', '${d.id}');" href="#">${1==d.status?'禁用':'启用'}</a>
			<a class="graph" onclick="return updFunc('${d.id}');" href="#">资源管理</a>
			<a class="graph" onclick="return importFunc('${d.id}');" href="#">资源配置</a>
			<a class="user" onclick="return updRole('${d.id}');" href="#">角色管理</a>
			<a class="update" href="updSystem1.htm?keyIndex=${d.id}&page=${pageModel.currentPage}">修改</a>
			<a class="delete" href="delSystem.htm?keyIndex=${d.id}&page=${pageModel.currentPage}">删除</a>
		</td>
	</tr>
</c:forEach>
</table>
<input name="page" type="hidden" value="${pageModel.currentPage}" />
</form>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
</body>
</html>
