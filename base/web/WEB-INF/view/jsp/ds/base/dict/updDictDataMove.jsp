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
<c:if test="${po.level==1}">parent.$jskey.dialog.close();</c:if>
<c:if test="${po.level!=1}">
var data = parent.$jskey.dialog.dialogArguments.args.data;
var arr = data.split(",");
$(function(){
	$dswork.ztree.root.label = "${fn:escapeXml(po.label)}";
	$dswork.ztree.config.check.chkStyle = "radio";
	$dswork.ztree.config.check.enable = true;
	$dswork.ztree.url = function(treeNode){return "getDictDataJson.htm?mark=${fn:escapeXml(param.mark)}&dictid=${po.id}&pid=" + treeNode.id;};
	$dswork.ztree.load();
	$dswork.ztree.expandRoot();
});
$dswork.ztree.beforeCheck = function(treeId, treeNode){
	if(!treeNode.isParent){return false;}
};
$dswork.ztree.check = function(event, treeId, treeNode){
	parent.$jskey.dialog.returnValue = treeNode.id;
};
$dswork.ztree.dataFilter = function (treeId, parentNode, responseData)//异步获取数据后未加载到树
{
	var newData = [];var has = false;
	if(responseData){for(var i=0; i<responseData.length; i++){
		has = false;
		for(var j=0;j<arr.length;j++){
			if(responseData[i].id+"" == arr[j]){
				has = true;
				break;
			}
		}
		if(!has && responseData[i].isParent){newData.push(responseData[i]);}
	}}
	return newData;
};
</c:if>
</script>
</head>
<body>
<ul id="mytree" class="ztree"></ul>
</body>
</html>
