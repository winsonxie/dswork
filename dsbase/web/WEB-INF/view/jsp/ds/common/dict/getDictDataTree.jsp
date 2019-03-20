<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<c:if test="${po.status==1}">
<%@include file="/commons/include/ztree.jsp"%>
</c:if>
<script type="text/javascript">
$dswork.callback = null;
function refreshNode(re){
	<c:if test="${po.status==1}">$dswork.ztree.refreshNode(re);</c:if>
	<c:if test="${po.status!=1}">attachUrl("getDictData.htm?dictid=${po.id}&pid=0");</c:if>
}
<c:if test="${po.status==1}">
$dswork.ztree.click = function(){
	var _id = $dswork.ztree.getSelectedNode().id;
	attachUrl("getDictData.htm?dictid=${po.id}&pid=" + _id);
	return false;
};
$dswork.ztree.showMenu = function(type, x, y){
	var _node = $dswork.ztree.getSelectedNode();
	$("#menu_refresh").show();$("#menu_add").show();$("#menu_del").show();$("#menu_upd").show();$("#menu_sort").show();$("#menu_select").show();
	if(0 == _node.id){$("#menu_del").hide();$("#menu_upd").hide();$("#menu_select").hide();}
	else{if(!_node.isParent){$("#menu_refresh").hide();$("#menu_add").hide();$("#menu_sort").hide();}}
	$("#" + $dswork.ztree.menuName).menu('show', {left:x,top:y});
};
$dswork.ztree.root.name = "${fn:escapeXml(po.label)}";
$dswork.ztree.url = function(treeNode){return "getDictDataJson.htm?dictid=${po.id}&pid=" + treeNode.id;};
$(function(){
	var $z = $dswork.ztree;
	$z.load();
	$("#menu_refresh").click(function(){$z.refreshNode();$z.hideMenu();});
	$("#menu_add").click(function(){
		attachUrl("addDictData1.htm?dictid=${po.id}&pid=" + $z.getSelectedNode().id);$z.hideMenu();
	});
	$("#menu_upd").click(function(){
		attachUrl("updDictData1.htm?keyIndex=" + $z.getSelectedNode().id);$z.hideMenu();
	});
	$("#menu_del").click(function(){
		var _node = $z.getSelectedNode();
		if(confirm("以下情况删除失败：下级节点不为空\n是否删除？")){
			$dswork.showRequest();
			$.post("delDictData.htm",{keyIndex:_node.id},function(data){
				$dswork.doAjaxShow(data, function(){
					if($dswork.result.type == 1){attachUrl("");$z.refreshNode(true);}
				});
			});
		}
		$z.hideMenu();
		return false;
	});
	$("#menu_sort").click(function(){
		attachUrl("updDictDataSeq1.htm?dictid=${po.id}&pid=" + $z.getSelectedNode().id);$z.hideMenu();
	});
	$("#menu_select").click(function(){
		attachUrl("getDictDataById.htm?keyIndex=" + $z.getSelectedNode().id);$z.hideMenu();
	});
	$z.expandRoot();
});
</c:if>
<c:if test="${po.status!=1}">
$(function(){
	attachUrl("getDictData.htm?dictid=${po.id}&pid=0");
});
</c:if>
</script>
</head>
<body class="easyui-layout treebody" fit="true">
<c:if test="${po.status==1}">
<div region="west" split="true" title="字典管理" style="width:250px;">
	<div class="treediv">
		<ul id="mytree" class="ztree tree" />
	</div>
	<div id="divMenu" class="easyui-menu easyuiMenu">
		<div id="menu_refresh" iconCls="menuTool-refresh">刷新</div>
		<div id="menu_add" iconCls="menuTool-insert">添加</div>
		<div id="menu_del" iconCls="menuTool-delete">删除</div>
		<div id="menu_upd" iconCls="menuTool-update">修改</div>
		<div id="menu_sort" iconCls="menuTool-sort">排序</div>
		<div id="menu_select" iconCls="menuTool-select">明细</div>
	</div>
</div>
</c:if>
<div region="center" style="overflow:hidden;${po.status!=1?'border:0px;height:100%;':''}">
	<iframe id="mainFrame" src="" frameborder="0" scrolling="auto" style="width:100%;height:100%;"></iframe>
</div>
</body>
</html>
