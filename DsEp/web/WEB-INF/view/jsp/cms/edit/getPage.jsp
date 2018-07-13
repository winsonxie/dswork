<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
$(function(){
	$dswork.page.menu("", "updPage1.htm", "", "${pageModel.currentPage}");
});
<c:if test="${po.siteid==0}">$dswork.page.join = function(td, menu, id){
	$(menu).append($('<div iconCls="menuTool-graph">同步到栏目</div>').bind("click", function(){
		$jskey.dialog.showDialog({title:'同步到栏目',fit:true,url:'copyPage1.htm?id=${po.id}&keyIndex='+id});
	}));
};</c:if>
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getPage.htm?id=${po.id}&page=${pageModel.currentPage}";
}};
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">${fn:escapeXml(po.name)}-内容列表</td>
		<td class="menuTool">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${po.siteid}&categoryid=${po.id}">预览本栏目</a><c:if test="${enablemobile}">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${po.siteid}&categoryid=${po.id}&mobile=true">预览移动版本栏目</a></c:if>
			<a class="update" href="updCategory1.htm?id=${po.id}">编辑本栏目</a>
			<a class="insert" href="addPage1.htm?categoryid=${po.id}&page=${pageModel.currentPage}">添加</a>
			<a class="delete" id="listFormDelAll" href="#">删除所选</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getPage.htm?id=${po.id}">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;关键字：<input type="text" class="text" name="keyvalue" style="width:300px;" value="${fn:escapeXml(param.keyvalue)}" />
		</td>
		<td class="query"><input id="_querySubmit_" type="button" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<form id="listForm" method="post" action="delPage.htm?id=${po.id}">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:2%"><input id="chkall" type="checkbox" /></td>
		<td style="width:5%">操作</td>
		<td style="width:48%">标题</td>
		<td style="width:13%">发布时间</td>
		<td style="width:8%">是否外链</td>
		<td style="width:8%">首页推荐</td>
		<td style="width:8%">焦点图</td>
		<td style="width:8%">状态</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d">
	<tr>
		<td><c:if test="${!d.audit}"><input name="keyIndex" type="checkbox" value="${d.id}" /></c:if></td>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td>${fn:escapeXml(d.title)}</td>
		<td>${fn:escapeXml(d.releasetime)}</td>
		<td>${d.scope==2?'是':'否'}</td>
		<td>${d.pagetop==1?'是':'否'}</td>
		<td>${d.imgtop==1?'是':'否'}</td>
		<td><c:if test="${categoryNeedAudit}">
			${d.edit?'未提交':d.audit?'审核中':d.nopass?'未通过':d.pass?'已通过':''}</c:if><c:if test="${not categoryNeedAudit}">
			${d.status==0?'未提交':d.status==1?'已提交':''}</c:if>
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
