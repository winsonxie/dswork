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
	$dswork.page.menu("delSite.htm", "updSite1.htm", "getSiteById.htm", "");
});
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getSite.htm";
}};
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">站点列表</td>
		<td class="menuTool">
			<a class="insert" href="addSite1.htm">添加</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:5%">操作</td>
		<td>站点名称</td>
		<%--<td>目录名称</td>--%>
		<td>链接</td>
		<td>图片</td>
		<%--<td>网站模板</td>--%>
	</tr>
<c:forEach items="${list}" var="d">
	<tr>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td>${fn:escapeXml(d.name)}</td>
		<%--<td>${fn:escapeXml(d.folder)}</td>--%>
		<td>${fn:escapeXml(d.url)}</td>
		<td>${fn:escapeXml(d.img)}</td>
		<%--<td>${fn:escapeXml(d.viewsite)}</td>--%>
	</tr>
</c:forEach>
</table>
</body>
</html>
