<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<c:if test="${siteid<0}">
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">模板管理：没有可管理的站点</td>
	</tr>
</table>
</body>
</c:if>
<c:if test="${siteid>=0}">
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
var textChange = false;
function setTextChange(t){
	textChange = t;
}
$dswork.callback = null;
function refreshNode(re){$dswork.ztree.refreshNode(re);}
$dswork.ztree.beforeClick = function(){
	var treeNode = $dswork.ztree.getSelectedNode();
	if(!treeNode.isParent){
		if(textChange){
			if(confirm('确定不保存就切换吗？')){
				textChange = false;
				return true;
			}
			else{
				return false;
			}
		}
	}
	return true;
};
$dswork.ztree.click = function(){
	attachUrl("editTemplate1.htm?siteid=${siteid}&path="+$dswork.ztree.getSelectedNode().path);
};
$dswork.ztree.root.name = "模板文件";
$dswork.ztree.root.path = "";
$dswork.ztree.url = function(n){return "getTemplateTreeJson.htm?siteid=${siteid}&pid="+n.id+"&path="+n.path};
$(function(){
	var $z = $dswork.ztree;
	$z.load();
	$z.expandRoot();
	$("#site").bind("change", function(){
		if($(this).val()!="${siteid}"){
			location.href = "getTemplateTree.htm?siteid="+$(this).val();
		}
	});
});
$dswork.ztree.dataFilter = function (treeId, parentNode, data){
	if(data){for(var i =0; i < data.length; i++){data[i].name = data[i].name.replace(".jsp", "");}}
	return data;
};
</script>
</head>
<body class="easyui-layout treebody" fit="true">
<div region="north" style="overflow:hidden;border:0px;height:30px;">
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">
			切换站点：
			<select id="site">
				<c:forEach items="${siteList}" var="d">
					<option value="${d.id}"<c:if test="${d.id==siteid}"> selected="selected"</c:if>>${fn:escapeXml(d.name)}</option>
				</c:forEach>
			</select>
		</td>
		<td class="menuTool">
			<a class="select" href="#" onclick="$jskey.dialog.showDialog({url:'readme.htm',title:'说明',fit:true,draggable:false});">说明</a>
		</td>
	</tr>
</table>
</div>
<div region="west" split="true" title="模板管理" style="width:250px;">
 	<div class="treediv">
		<ul id="mytree" class="ztree tree" />
	</div>
</div>
<div region="center" style="overflow:hidden;">
	<iframe id="mainFrame" name="mainFrame" src="" frameborder="0" scrolling="auto" style="width:100%;height:100%;"></iframe>
</div>
</body>
</c:if>
</html>
	