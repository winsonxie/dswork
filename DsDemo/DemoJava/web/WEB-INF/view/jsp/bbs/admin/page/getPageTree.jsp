<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>

<c:if test="${siteid<0}">
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">信息发布：没有可管理的站点</td>
	</tr>
</table>
</body>
</c:if>

<c:if test="${siteid>=0}">
<head>
	<title></title>
	<%@include file="/commons/include/get.jsp"%>
	<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
$dswork.callback = null;
$dswork.ztree.click = function(){
	var node = $dswork.ztree.getSelectedNode();
	if(!node.isParent)
	{
		if(node.status == 1 && node.pid > 0){
			attachUrl("getPage.htm?id=" + node.id);
			return false;
		}
	}
	attachUrl("");
	return false;
};
$(function(){
	var v = [];
	<c:forEach items="${list}" var="d">
	v.push({"id":"${d.id}", "pid":"${d.pid}", "name":"${fn:escapeXml(d.name)}", "status":"${d.status}"});
	</c:forEach>
	$dswork.ztree.nodeArray = v;
	$dswork.ztree.config.async.enable = false;
	var $z = $dswork.ztree;
	$z.load();
	$z.expandAll(true);
	$("#site").bind("click", function(){
		if($(this).val()!="${siteid}"){
			location.href = "getPageTree.htm?siteid="+$(this).val();
		}
	});
});
</script>
</head>
<body class="easyui-layout treebody" fit="true">
<div region="north" style="overflow:hidden;border:0px;height:30px;">
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">
			切换站点：<select id="site"><c:forEach items="${siteList}" var="d"><option value="${d.id}"<c:if test="${d.id==siteid}"> selected="selected"</c:if>>${fn:escapeXml(d.name)}</option></c:forEach></select>
			&nbsp;<input type="button" class="button" value="打开首页" onclick="window.open('${ctx}/bbs/index.htm?siteid=${siteid}');" />
		</td>
	</tr>
</table>
</div>
<div region="west" split="true" style="width:200px;">
	<div class="treediv">
		<ul id="mytree" class="ztree tree" />
	</div>
</div>
<div region="center" style="overflow:hidden;">
	<iframe id="mainFrame" src="" frameborder="0" scrolling="auto" style="width:100%;height:100%;"></iframe>
</div>
</body>
</c:if>
</html>
