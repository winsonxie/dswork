<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/getById.jsp"%>
<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
var map = new $jskey.Map();
var isload = false;
$(function(){
	$dswork.ztree.config.async.enable = false;
	$dswork.ztree.config.check.enable = true;
	$dswork.ztree.root = {id:0, pid:-1, isParent:true, name:"${fn:escapeXml(sys.name)}", checked:true};
	$dswork.ztree.nodeArray = ${list};
	$dswork.ztree.nodeArray.push($dswork.ztree.root);
	for(var i = ($dswork.ztree.nodeArray.length - 1); i >=0; i--){
		map.put($dswork.ztree.nodeArray[i].id, $dswork.ztree.nodeArray[i]);
	}
	var notRoot = true, pid = 0, node = null;
	for(var i = ($dswork.ztree.nodeArray.length - 1); i >=0; i--){
		node = $dswork.ztree.nodeArray[i];
		notRoot = true;
		if(node.checked){
			while(notRoot){
				if(node == null){notRoot = false;break;}
				pid = node.pid;
				if(pid == null || pid <= 0){notRoot = false;break;}
				node = map.get(pid);
				node.checked = true;
			}
		}
	}
	for(var i = ($dswork.ztree.nodeArray.length - 1); i >=0; i--){
		if(!$dswork.ztree.nodeArray[i].checked) $dswork.ztree.nodeArray.splice(i, 1);
	}
	$dswork.ztree.load();
	$dswork.ztree.expandAll(true);
});
$dswork.ztree.beforeCheck = function(treeId, treeNode){return false;};
</script>
</head>
<body><ul id="mytree" class="ztree" /></body>
</html>
