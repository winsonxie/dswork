<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/getById.jsp" %>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">栏目 [${po.id}] 明细</td>
		<td class="menuTool">
			<c:if test="${po.scope==2}"><a class="look" target="_blank" href="${fn:escapeXml(po.url)}">预览外链</a></c:if>
			<c:if test="${po.scope!=2}">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${po.siteid}&categoryid=${po.id}">预览栏目</a><c:if test="${enablemobile}">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${po.siteid}&categoryid=${po.id}&mobile=true">预览移动版栏目</a></c:if>
			</c:if>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">栏目名称</td>
		<td class="form_input">${po.name}</td>
	</tr>
	<tr>
		<td class="form_title">类型</td>
		<td class="form_input">${po.scope==0?'列表':po.scope==1?'单页':'外链'}</td>
	</tr>
	<tr>
		<td class="form_title">链接</td>
		<td class="form_input"><a href="${po.url}" target="_blank">${po.url}</a></td>
	</tr>
<c:if test="${po.scope==0 || po.scope==1}">
	<tr>
		<td class="form_title">栏目模板</td>
		<td class="form_input">${po.viewsite}</td>
	</tr>
	<c:if test="${po.scope==0}">
	<tr>
		<td class="form_title">内容模板</td>
		<td class="form_input">${po.pageviewsite}</td>
	</tr>
	</c:if>
</c:if>
</table>
<c:if test="${po.scope==0}">
<div class="line"></div>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo" style="border-top:#c2c2c2 solid 1px;">
	<tr>
		<td class="title">栏目内容明细</td>
	</tr>
</table>
<div class="line"></div>
<form id="listForm" method="post" action="delCategories.htm">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:10%">编号</td>
		<td style="width:70%">标题</td>
		<td style="width:10%">类型</td>
		<td style="width:10%">操作</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d" varStatus="status">
	<tr>
		<td>${d.id}</td>
		<td>${d.title}</td>
		<td>${d.scope==2?'外链':'内容'}</td>
		<td class="menuTool">
			<c:if test="${d.scope==2}"><a class="look" target="_blank" href="${fn:escapeXml(d.url)}">预览外链</a></c:if>
			<c:if test="${d.scope!=2}">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${d.siteid}&categoryid=${d.categoryid}&pageid=${d.id}">预览页面</a>
			<c:if test="${enablemobile}"><a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${d.siteid}&categoryid=${d.categoryid}&pageid=${d.id}&mobile=true">预览移动版</a></c:if>
			</c:if>
		</td>
	</tr>
</c:forEach>
</table>
<input name="page" type="hidden" value="${pageModel.currentPage}" />
</form>
<div class="line"></div>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable" style="border-top:#c2c2c2 solid 1px;border-bottom:#c2c2c2 solid 1px;">
	<tr><td>${pageNav.page}</td></tr>
</table>
</c:if>
</body>
</html>
