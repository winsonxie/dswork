<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
$(function(){
	$dswork.page.menu("delDict.htm?status=${fn:escapeXml(param.status)}", "updDict1.htm?status=${fn:escapeXml(param.status)}", "", "${pageModel.page}");
	$("#status").bind("change", function(){
		$("#queryForm").submit();
	});
	$("#dataTable>tbody>tr>td.v").each(function(){try{
		var v = $(this).text();
		if(v == "1"){v = "树形集合";}
		else if(v == "0"){v = "列表集合";}
		else {v = "未知";}
		$(this).text(v);
	}catch(e){}});
});
function getDictDataTree(id){
	$jskey.dialog.showDialog({url:"getDictDataTree.htm?status=${fn:escapeXml(param.status)}&keyIndex=" + id,title:"【" + $("#td_l" + id).text() + "】字典管理：引用名《" + $("#td_n" + id).text() + "》",fit:true,draggable:false});
	return false;
}
$dswork.page.join = function(td, menu, id){
	$(menu).append($('<div iconCls="menuTool-graph">设置</div>').bind("click", function(){
		getDictDataTree(id);
	}));
	td.parent().css("cursor", "pointer").bind("dblclick", function(event){getDictDataTree(id);});
};
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getDict.htm?status=${fn:escapeXml(param.status)}&page=${pageModel.page}";
}};
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">字典列表</td>
		<td class="menuTool">
			<a class="insert" href="addDict1.htm?status=${fn:escapeXml(param.status)}&page=${pageModel.page}">添加</a>
			<a class="delete" id="listFormDelAll" href="#">删除所选</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getDict.htm">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;名称：<input type="text" name="label" value="${fn:escapeXml(param.label)}" />
			&nbsp;引用名：<input type="text" name="name" value="${fn:escapeXml(param.name)}" />
			&nbsp;分类：<select name="status" v="${fn:escapeXml(param.status)}"><option value="">全部</option><option value="1">树形集合</option><option value="0">列表集合</option></select>
		</td>
		<td class="query"><input id="_querySubmit_" type="button" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<form id="listForm" method="post" action="delDict.htm?status=${fn:escapeXml(param.status)}">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:2%"><input id="chkall" type="checkbox" /></td>
		<td style="width:5%">操作</td>
		<td>引用名</td>
		<td style="width:30%">名称</td>
		<td style="width:15%">分类</td>
		<td style="width:15%">操作</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d">
	<tr>
		<td><input name="keyIndex" type="checkbox" value="${d.id}" /></td>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td id="td_n${d.id}">${fn:escapeXml(d.name)}</td>
		<td id="td_l${d.id}">${fn:escapeXml(d.label)}</td>
		<td class="v">${d.status}</td>
		<td class="menuTool">
			<a class="graph" onclick="return getDictDataTree('${d.id}');" href="#">设置</a>
			<a class="update" href="updDict1.htm?status=${fn:escapeXml(param.status)}&keyIndex=${d.id}&page=${pageModel.page}">修改</a>
		</td>
	</tr>
</c:forEach>
</table>
<input name="page" type="hidden" value="${pageModel.page}" />
</form>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
<br />
</body>
</html>
