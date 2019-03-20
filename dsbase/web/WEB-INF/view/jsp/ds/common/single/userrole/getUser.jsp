<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
$dswork.page.join = function(td, menu, id){
	$(menu).append($('<div iconCls="menuTool-key">受权</div>').bind("click", function(){
		updUserRole(id);
	})).append($('<div iconCls="menuTool-select">用户受权信息</div>').bind("click", function(){
		getUserById(id);
	}));
};
function updUserRole(id){
	$jskey.dialog.showDialog({url:"updUserRole1.htm?systemid=${param.systemid}&id=" + id,title:"用户受权",fit:true,draggable:false});
}
function getUserById(id){
	$jskey.dialog.showDialog({url:"getUserById.htm?systemid=${param.systemid}&id=" + id,title:"用户授权信息",fit:true,draggable:false});
}
$(function(){
	$dswork.page.menu("", "", "", "");
});
</script>
</head>
<body><table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">授权管理</td>
	</tr>
</table>
<div class="line"></div>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:5%">操作</td>
		<td>姓名(帐号)</td>
		<td style="width:15%">操作</td>
	</tr>
<c:forEach items="${list}" var="d">
	<tr>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td>${d.name}</td>
		<td class="menuTool">
			<a class="key" onclick="updUserRole(${d.id});return false;">受权</a>
			<a class="select" onclick="getUserById(${d.id});return false;">用户受权信息</a>
		</td>
	</tr>
</c:forEach>
</table>
</body>
</html>
