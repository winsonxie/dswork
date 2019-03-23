<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
var data = parent.$jskey.dialog.dialogArguments.args.data;
var arr = data.split(",");
$(function(){
	$dswork.ztree.root.name = "流程分类管理";
	$dswork.ztree.root.id = ${rootid};
	$dswork.ztree.config.check.chkStyle = "radio";
	$dswork.ztree.config.check.enable = true;
	$dswork.ztree.url = function(treeNode){return "getFlowCategoryJson.htm?pid=" + treeNode.id;};
	$dswork.ztree.load();
	$dswork.ztree.expandRoot();
});
$dswork.ztree.beforeCheck = function(treeId, treeNode){
	if(!treeNode.isParent){return false;}
};
$dswork.ztree.dataFilter = function (treeId, parentNode, responseData){//异步获取数据后未加载到树
	var newData = [];var has = false;
	if(responseData){for(var i=0; i<responseData.length; i++){
		var obj = responseData[i];
		has = false;
		for(var j=0;j<arr.length;j++){
			if(obj.id+"" == arr[j]){has = true;break;}
		}
		if(!has && obj.isParent){newData.push(obj);}
	}}
	return newData;
};
</script>
</head>
<body>
<ul id="mytree" class="ztree" />
</body>
</html>
