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
	treeNode = $dswork.ztree.getSelectedNode();
	if(treeNode.status > 0 || treeNode.id == 0){attachUrl("getOrg.htm?rootid=${po.id}&pid=" + treeNode.id);}
	else{attachUrl("getOrgById.htm?noback=true&keyIndex=" + treeNode.id);}
	return false;
};
$dswork.ztree.showMenu = function(type, x, y){
	var _node = $dswork.ztree.getSelectedNode();
	$("#menu_refresh").show();$("#menu_add").show();$("#menu_del").show();$("#menu_upd").show();$("#menu_sort").show();$("#menu_select").show();
	if(${po.id} == _node.id){$("#menu_del").hide();$("#menu_upd").hide();if(_node.id == 0){$("#menu_select").hide();}}
	if(_node.status == 0){$("#menu_refresh").hide();$("#menu_add").hide();$("#menu_sort").hide();$("#menu_select").hide();}
	$("#" + $dswork.ztree.menuName).menu('show', {left:x,top:y});
};
$dswork.ztree.root.name = "${po.id > 0?fn:escapeXml(po.name):'组织机构'}";
$dswork.ztree.root.id = ${po.id};
$dswork.ztree.root.status = ${po.status};
$dswork.ztree.url = function(treeNode){return "getOrgJson.htm?pid=" + treeNode.id;};
$(function(){
	var $z = $dswork.ztree;
	$z.load();
	$("#menu_refresh").click(function(){$z.refreshNode();$z.hideMenu();});
	$("#menu_add").click(function(){
		attachUrl("addOrg1.htm?pid=" + $z.getSelectedNode().id);$z.hideMenu();
	});
	$("#menu_upd").click(function(){
		attachUrl("updOrg1.htm?keyIndex=" + $z.getSelectedNode().id);$z.hideMenu();
	});
	$("#menu_del").click(function(){
		var _node = $z.getSelectedNode();
		if(confirm("以下情况删除失败：下级节点不为空\n是否删除？")){
			$dswork.showRequest();
			$.post("delOrg.htm",{keyIndex:_node.id},function(data){
				$dswork.doAjaxShow(data, function(){
					if($dswork.result.type == 1){attachUrl("");$z.refreshNode(true);}
				});
			});
		}
		$z.hideMenu();
		return false;
	});
	$("#menu_sort").click(function(){
		attachUrl("updOrgSeq1.htm?pid=" + $z.getSelectedNode().id);$z.hideMenu();
	});
	$("#menu_select").click(function(){
		attachUrl("getOrgById.htm?keyIndex=" + $z.getSelectedNode().id);$z.hideMenu();
	});
	$z.expandRoot();
});
$dswork.ztree.root.iconSkin = "groups";
$dswork.ztree.dataFilter = function (treeId, parentNode, data){
	if(data){for(var i = 0;i < data.length;i++){
		if(data[i].status == 2){
			data[i].iconSkin = "groups";
		}
		else if(data[i].status == 1){
			data[i].iconSkin = "group";
		}
		else{
			data[i].iconSkin = "user";
		}
	}}
	return data;
};
</script>
</head>
<body class="easyui-layout treebody" fit="true">
<div region="north" style="overflow:hidden;border:0px;height:30px;">
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">组织管理</td>
		<td class="menuTool">
			<span style="color:#ff0000;font-weight:bold;">节点“移动”或修改“类型”需要重新设置所有相关用户，请谨慎操作</span>
		</td>
	</tr>
</table>
</div>
<div region="west" split="true" title="组织管理" style="width:250px;">
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
<div region="center" style="overflow:hidden;">
	<iframe id="mainFrame" src="" frameborder="0" scrolling="auto" style="width:100%;height:100%;"></iframe>
</div>
</body>
</html>
	