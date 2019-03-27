<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getUser.htm?page=${pageModel.page}&qybm=${qybm}";
}};
$dswork.page.join = function(td, menu, id){
	var usertype  = $(td).attr("usertype");
	if(usertype == null || typeof(usertype)=="undefined"){return true;}
	if(usertype != "1"){
		$(menu).append($('<div iconCls="menuTool-user">删除</div>').bind("click", function(event){
			if(confirm("确认删除吗？")){$dswork.page.del(event, "delUser.htm", id, "${pageModel.page}", td);}
		}));
		$(menu).append($('<div iconCls="menuTool-user">修改密码</div>').bind("click", function(event){
			location.href = "updUserPassword1.htm?keyIndex=" + id;
		}));
	}
};
$(function(){
	try{$("#status").val("${fn:escapeXml(param.status)}");}catch(e){}
	$dswork.page.menu("", "updUser1.htm", "getUserById.htm", "${pageModel.page}");
});
function updStatus(objid, id){
	var obj = $("#" + objid), o = document.getElementById(objid);
	$.post("updUserStatus.htm",{"keyIndex":id,"status":obj.attr("v")==0?1:0},function(data){
		$dswork.checkResult(data);
		if($dswork.result.type == 1){
		obj.removeClass("pause").removeClass("start");
		if(1 == obj.attr("v")){
			obj.text("启用").attr("v", 0).addClass("start");$("#td_" + objid).text("禁用").css("color", "red");
		}
		else{
			obj.text("禁用").attr("v", 1).addClass("pause");$("#td_" + objid).text("启用").css("color", "");
		}}else{alert($dswork.result.msg);}
	});
	return false;
}
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">${ssdw}用户列表</td>
		<td class="menuTool">
			<a class="insert" href="addUser1.htm?page=${pageModel.page}">添加</a>
			<a class="delete" id="listFormDelAll" href="#">删除所选</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getUser.htm">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;关键字查询：<input type="text" name="keyvalue" style="width:200px;" title="企业编码、手机或电话" value="${fn:escapeXml(param.keyvalue)}" />
			&nbsp;账号：<input type="text" name="account" style="width:100px;" value="${fn:escapeXml(param.account)}" />
			&nbsp;姓名：<input type="text" name="name" style="width:100px;" value="${fn:escapeXml(param.name)}" />
			&nbsp;状态：<select id="status" name="status" style="width:55px;"><option value="">全部</option><option value="1">启用</option><option value="0">禁用</option></select>
		</td>
		<td class="query"><input id="_querySubmit_" type="button" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<form id="listForm" method="post" action="delUser.htm">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:2%"><input id="chkall" type="checkbox" /></td>
		<td style="width:5%">操作</td>
		<td>姓名(帐号)</td>
		<td style="width:12%">手机</td>
		<td style="width:20%">Email</td>
		<td style="width:9%">用户类型</td>
		<td style="width:17%">创建时间</td>
		<td style="width:5%;">状态</td>
		<td style="width:7%;">操作</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d" varStatus="status">
	<tr>
		<td><c:if test="${d.usertype != 1}"><input name="keyIndex" type="checkbox" value="${d.id}" /></c:if></td>
		<td class="menuTool" usertype="${d.usertype}" keyIndex="${d.id}">&nbsp;</td>
		<td>${fn:escapeXml(d.name)}(${fn:escapeXml(d.account)})</td>
		<td>${fn:escapeXml(d.mobile)}</td>
		<td>${fn:escapeXml(d.email)}</td>
		<td class="usertype" style="${d.usertype == 1?'color:red;':''}">${d.usertype==1?"管理员":"用户"}</td>
		<td>${fn:escapeXml(d.createtime)}</td>
		<td id="td_a_status${status.index}" style="color:${1==d.status?"":"red"}">${1==d.status?"启用":"禁用"}</td>
		<td class="menuTool">
			<a ${1==d.usertype?'style="display:none;"':''} id="a_status${status.index}" name="a_status" v="${d.status}" class="${1==d.status?'pause':'start'}" href="#" onclick="return updStatus('a_status${status.index}', '${d.id}');">${1==d.status?'禁用':'启用'}</a>
		</td>
	</tr>
</c:forEach>
</table>
<input name="page" type="hidden" value="${pageModel.page}" />
</form>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
</body>
</html>
