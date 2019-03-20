<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
var uList = [], oList = [];
<c:forEach items="${userList}" var="d">
uList.push({id:"${d.id}",name:"${fn:escapeXml(d.name)}(${fn:escapeXml(d.account)})"});
</c:forEach>
<c:forEach items="${orgList}" var="d">
oList.push({id:"${d.id}",name:"${fn:escapeXml(d.name)}"});
</c:forEach>
$(function(){
	$("#userTable>tbody>tr>td>a.key").each(function(){
		var o = $(this);var id = o.attr("keyIndex");if(id == null || typeof(id)=="undefined"){return true;}
		o.click(function(event){
			return parent.callfn(true, "设置岗位，用户："+o.attr("keyName"), id, "updSetUser1.htm?id=" + id, uList, oList);
		});
	});
	$("#userTable>tbody>tr>td>a.select").each(function(){
		var o = $(this);var id = o.attr("keyIndex");if(id == null || typeof(id)=="undefined"){return true;}
		o.click(function(event){
			$jskey.dialog.showDialog({url:"getUserById.htm?id=" + id,title:"用户受权信息",fit:true,draggable:false});
		});
	});
	$("#orgTable>tbody>tr>td>a.key").each(function(){
		var o = $(this);var id = o.attr("keyIndex");if(id == null || typeof(id)=="undefined"){return true;}
		o.click(function(event){
			return parent.callfn(false, "设置用户，岗位："+o.attr("keyName"), id, "updSetOrg1.htm?id=" + id, uList, oList);
		});
	});
	$("#orgTable>tbody>tr>td>a.select").each(function(){
		var o = $(this);var id = o.attr("keyIndex");if(id == null || typeof(id)=="undefined"){return true;}
		o.click(function(event){
			$jskey.dialog.showDialog({url:"getOrgById.htm?id=" + id,title:"岗位授权信息",fit:true,draggable:false});
		});
	});
	$("#orgTable>tbody>tr>td>a.edit").each(function(){
		var o = $(this);var id = o.attr("keyIndex");if(id == null || typeof(id)=="undefined"){return true;}
		o.click(function(event){
			$jskey.dialog.showDialog({url:"updOrgRole1.htm?keyIndex=" + id,title:"岗位授予角色",fit:true,draggable:false});
		});
	});
});
function choose(data){
	$jskey.dialog.callback = function(){
		var result = $jskey.dialog.returnValue;
		if(result != null){
			var map = new $jskey.Map(), o;
			for(var i = 0; i < result.length; i++){o=result[i];map.put(o.id + "", o);}
			try{window.frames["mainFrame"].callback(map);}catch(e){alert(e.name + "\n" + e.message);}
		}
	};
	$jskey.dialog.showChoose({id:"chooseSystem", title:"选择角色", args:{url:"../../rolechoose/getRoleChoose.htm", data:data}, width:"600", height:"450"});
	return false;
}
</script>
</head>
<body class="easyui-tabs" fit="true">
<div title="用户授权" style="padding:5px;overflow:hidden;">
<table id="userTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>姓名(帐号)</td>
		<td style="width:15%">操作</td>
	</tr>
<c:forEach items="${userList}" var="d">
	<tr>
		<td>${d.name}</td>
		<td class="menuTool">
			<a class="key" keyIndex="${d.id}" keyName="${d.name}" style="cursor:pointer;">受权</a>
			<a class="select" keyIndex="${d.id}" keyName="${d.name}" style="cursor:pointer;">用户受权信息</a>
		</td>
	</tr>
</c:forEach>
</table>
</div>
<div title="岗位授权" style="padding:5px;overflow:hidden;">
<table id="orgTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>岗位名称</td>
		<td style="width:22%">操作</td>
	</tr>
<c:forEach items="${orgList}" var="d">
	<tr>
		<td>${d.name}</td>
		<td class="menuTool">
			<a class="key" keyIndex="${d.id}" keyName="${d.name}" style="cursor:pointer;">授权</a>
			<a class="select" keyIndex="${d.id}" keyName="${d.name}" style="cursor:pointer;">岗位授权信息</a>
			<a class="edit" keyIndex="${d.id}" keyName="${d.name}" style="cursor:pointer;">岗位授予角色</a>
		</td>
	</tr>
</c:forEach>
</table>
</div>
</body>
</html>
