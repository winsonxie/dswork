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
	var t = $("#userTable"),m,n;
	for(var i = 0; i < uList.length; i++){m=uList[i];
		var r = $("<tr></tr>");
		$("<td></td>").html(m.name).appendTo(r);
		$("<td class='menuTool'></td>").append($("<a class='key'>受权</a>").attr("keyIndex", m.id).attr("keyName", m.name).css("cursor", "pointer")).appendTo(r);
		t.append(r);
	}
	t = $("#orgTable");
	for(var i = 0; i < oList.length; i++){m=oList[i];
		var r = $("<tr></tr>");
		$("<td></td>").html(m.name).appendTo(r);
		$("<td class='menuTool'></td>").append($("<a class='key'>授权</a>").attr("keyIndex", m.id).attr("keyName", m.name).css("cursor", "pointer")).appendTo(r);
		t.append(r);
	}
	$("#userTable>tbody>tr>td>a.key").each(function(){
		var o = $(this);var id = o.attr("keyIndex");if(id == null || typeof(id)=="undefined"){return true;}
		o.click(function(event){
			return parent.callfn(true, "设置岗位，用户："+o.attr("keyName"), id, "updSetUser1.htm?id=" + id, uList, oList);
		});
	});
	$("#orgTable>tbody>tr>td>a.key").each(function(){
		var o = $(this);var id = o.attr("keyIndex");if(id == null || typeof(id)=="undefined"){return true;}
		o.click(function(event){
			return parent.callfn(false, "设置用户，岗位："+o.attr("keyName"), id, "updSetOrg1.htm?id=" + id, uList, oList);
		});
	});
});
</script>
</head>
<body class="easyui-tabs" fit="true">
<div title="用户授权" style="padding:5px;">
<table id="userTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>姓名(帐号)</td>
		<td style="width:15%">操作</td>
	</tr>
</table>
</div>
<div title="岗位授权" style="padding:5px;">
<table id="orgTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>岗位名称</td>
		<td style="width:15%">操作</td>
	</tr>
</table>
</div>
</body>
</html>
