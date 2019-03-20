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
var _systemid = ${(po == null)?'-100000000':po.id};
var _systemname = "${fn:escapeXml(po.name)}";
if(_systemid == -100000000){
	_systemname = "未选择系统";
}
$dswork.ztree.root.name = "角色(" + _systemname + ")";
$dswork.ztree.url = function(treeNode){return "getRoleJson.htm?pid=" + treeNode.id + "&systemid=" + _systemid;};
var str_wx="-未选(点击选择)";
var str_yx="-已选(取消选择)";
$dswork.ztree.click = function(event, treeId, node){
	var _id = node.id;
	if(0 == _id){return false;}
	if(node.checked){//取消选择
		parent.parent.setModel(node, false);
		node.name = node.sname + str_wx;
		node.checked = false;
	}
	else{
		parent.parent.setModel(node, true);
		node.name = "<span style='color:#ff0000'>" + node.sname + str_yx; + "</span>";
		node.checked = true;
	}
	$dswork.ztree.updateNode(node, true);
};
$dswork.ztree.dataFilter = function(treeId, parentNode, childNodes){
	if(childNodes){
		for(var i =0; i < childNodes.length; i++){
			var m = childNodes[i];
			m.sname = m.name;//保存原有名称
			m.systemname = _systemname;
			try{if(parent.parent.refreshModel(m)){//已选
					m.name = "<span style='color:#ff0000'>" + m.name + str_yx + "</span>";
					m.checked = true;
				}
				else{
					m.name = m.name + str_wx;
					m.checked = false;
				}
			}catch(e){}
			childNodes[i] = m;
		}
	}
	return childNodes;
};
$(function(){
	$dswork.ztree.config.view.nameIsHTML = true;
	$dswork.ztree.load();
	$dswork.ztree.expandRoot();
});
</script>
</head>
<body class="treebody">
<ul id="mytree" class="ztree tree" />
</body>
</html>
