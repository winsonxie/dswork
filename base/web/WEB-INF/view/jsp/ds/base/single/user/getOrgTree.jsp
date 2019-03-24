<%@page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
$dswork.callback = null;
function refreshNode(re){$dswork.ztree.refreshNode(re);}
$dswork.ztree.click = function(){
	var node = $dswork.ztree.getSelectedNode();
	if(node.status == 1){attachUrl("getUser.htm?orgid=" + node.id);}// 部门
	else if(node.status == 2){attachUrl("getUser.htm?orgpid=" + node.id);}// 单位
	else{attachUrl("about:blank");}
	return false;
};
$dswork.ztree.root.name = "${po.id>0?fn:escapeXml(po.name):'组织机构'}";
$dswork.ztree.root.id = ${po.id};
$dswork.ztree.root.status = ${po.status};
$dswork.ztree.url = function(treeNode){return "${ctx}/common/share/getJsonOrg.htm?pid=" + treeNode.id;};
$dswork.ztree.dataFilter = function (treeId, parentNode, data){
	var d=[];for(var i =0; i < data.length; i++){
		if(data[i].status == 1){data[i].iconSkin = "group";}
		if(data[i].status > 0){d.push(data[i]);}
	}return d;
};
$(function(){
	var $z = $dswork.ztree;
	$z.load();
	$z.expandRoot();
});
</script>
</head>
<body class="easyui-layout treebody" fit="true">
<div region="west" split="true" title="用户管理（选择部门）" style="width:300px;">
	<div class="treediv">
		<ul id="mytree" class="ztree tree" />
	</div>
</div>
<div region="center" style="overflow:hidden;">
	<iframe id="mainFrame" name="mainFrame" src="" frameborder="0" scrolling="auto" style="width:100%;height:100%;"></iframe>
</div>
</body>
</html>
	