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
	<tr><td class="title">信息发布：没有可管理的站点</td></tr>
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
$dswork.ztree.beforeClick = function(treeId, treeNode, clickFlag){
	return treeNode.enable == "true";
};
$dswork.ztree.rightClick = function(event, treeId, treeNode){
	return treeNode.enable == "true";
};
$dswork.ztree.click = function(){
	var node = $dswork.ztree.getSelectedNode();
	if(node.scope == 0){
		attachUrl("getPage.htm?id=" + node.id);
		return false;
	}else{
		attachUrl("getCategoryById.htm?id=" + node.id);
		return false;
	}
	attachUrl("");
	return false;
};
function build(categoryid, pageid){
	$dswork.doAjaxObject.autoDelayHide("发布中", 2000);
	$.post("build.htm",{"siteid":"${siteid}", "categoryid":categoryid, "pageid":pageid},function(data){
		$dswork.doAjaxShow(data, function(){});
	});
}
function unbuild(categoryid, pageid){
	$dswork.doAjaxObject.autoDelayHide("删除中", 2000);
	$.post("unbuild.htm",{"siteid":"${siteid}", "categoryid":categoryid, "pageid":pageid},function(data){
		$dswork.doAjaxShow(data, function(){});
	});
}
$(function(){
	var v = [];
	<c:forEach items="${categoryList}" var="d">
	v.push({"id":"${d.id}", "pid":"${d.pid}", "name":"${fn:escapeXml(d.name)} [${d.scope==0?'列表':d.scope==1?'单页':'外链'}]", "scope":"${d.scope}", "enable":"${d.enable}"});
	</c:forEach>
	$dswork.ztree.nodeArray = v;
	$dswork.ztree.config.async.enable = false;
	var $z = $dswork.ztree;
	$z.load();
	$z.expandAll(true);
	$("#site").bind("click", function(){
		if($(this).val()!="${siteid}"){
			location.href = "getCategoryTree.htm?siteid="+$(this).val();
		}
	});
	$("#btn_site").bind("click", function(){
		if(confirm("是否发布首页")){build(-1, -1);}
	});
	$("#category").bind("click", function(){
		$(this).val()!="0" ? $("#view").show() : $("#view").hide();
	});
	$("#btn_category").bind("click", function(){
		var m = $("#category").find("option:selected");
		if(confirm("是否发布栏目\"" + m.text() + "\"")){build(m.val(), -1);}
	});
	$("#btn_page").bind("click", function(){
		var m = $("#category").find("option:selected");
		if(confirm("是否发布栏目\"" + m.text() + "\"内容")){build(m.val(), 0);}
	});
	$("#view").bind("click", function(){
		var v = $('#category').find('option:selected').val();
		if(v!="0"){
			window.open('${ctx}/cmsbuild/buildHTML.chtml?view=true&siteid=${siteid}&categoryid='+v);
		}
	});
<c:if test="${enablemobile}">
	$("#mview").bind("click", function(){
		var v = $('#category').find('option:selected').val();
		if(v!="0"){
			window.open('${ctx}/cmsbuild/buildHTML.chtml?view=true&siteid=${siteid}&mobile=true&categoryid='+v);
		}
	});
</c:if>
	$("#btn_category_d").bind("click", function(){
		var m = $("#category").find("option:selected");
		if(confirm("是否删除栏目\"" + m.text() + "\"已发布首页")){unbuild(m.val(), -1);}
	});
	$("#btn_page_d").bind("click", function(){
		var m = $("#category").find("option:selected");
		if(confirm("是否删除栏目\"" + m.text() + "\"已发布内容")){unbuild(m.val(), 0);}
	});
	$("#category").click();
});
</script>
</head>
<body class="easyui-layout treebody" fit="true">
<div region="north" style="overflow:hidden;border:0px;height:30px;">
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">切换站点：<select id="site"><c:forEach items="${siteList}" var="d"><option value="${d.id}"<c:if test="${d.id==siteid}"> selected="selected"</c:if>>${fn:escapeXml(d.name)}</option></c:forEach></select>
			<input id="btn_site" type="button" class="button" value="发布首页" />&nbsp;<input type="button" class="button" value="预览首页" onclick="window.open('${ctx}/cmsbuild/buildHTML.chtml?view=true&siteid=${siteid}');" /><c:if test="${enablemobile}">
			<input type="button" class="button" value="预览移动版首页" onclick="window.open('${ctx}/cmsbuild/buildHTML.chtml?view=true&siteid=${siteid}&mobile=true');" /></c:if>
			&nbsp;&nbsp;
			选择需要发布的栏目：<select id="category"><option value="0">全部栏目</option><c:forEach items="${categoryList}" var="d"><option value="${d.id}">${d.label}${fn:escapeXml(d.name)}</option></c:forEach></select>
			<input id="btn_category" type="button" class="button" value="发布栏目首页" />
			<input id="btn_page" type="button" class="button" value="发布栏目内容" />
			<input id="view" type="button" class="button" value="预览栏目" /><c:if test="${enablemobile}">
			<input id="mview" type="button" class="button" value="预览移动版栏目" /></c:if>
			<input id="btn_category_d" type="button" class="button" value="删除栏目首页" />
			<input id="btn_page_d" type="button" class="button" value="删除发布内容" />
		</td>
	</tr>
</table>
</div>
<div region="west" split="true" style="width:255px;">
	<div class="treediv">
		<ul id="mytree" class="ztree tree"></ul>
	</div>
</div>
<div region="center" style="overflow:hidden;">
	<iframe id="mainFrame" src="getCategoryPublish.htm?siteid=${siteid}" frameborder="0" scrolling="auto" style="width:100%;height:100%;"></iframe>
</div>
</body>
</c:if>
</html>
