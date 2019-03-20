<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp"%>
<style type="text/css">.menuTool-save {background-position:center left;}</style>
<script type="text/javascript">
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){parent.refreshNode(true);}};
$(function(){
$dswork.page.menu("", "updOrg1.htm", "getOrgById.htm", "");
$("#listFormMoveAll").click(function(){
	var _c = 0, v = "0", isPost = false, isSub = false, isUnit = false;
	$("input[name='keyIndex']:checked").each(function(){
		_c++;v=v+","+$(this).val();
		var vv = $(this).attr("v");
		isSub = (vv != "2") || isSub;
		if(vv == "2"){isUnit = true;}
		if(vv == "0"){isPost = true;}
	});
	if(_c > 0){
		v = v.substring(2);
		$("#moveids").val(v);
		var obj = {"title":"移动到选中节点","args":{"data":v}, "url":"updOrgMove1.htm?rootid=${rootid}"};
		obj.buttons = [{text:"移动",iconCls:"menuTool-save",handler:function(){
			var re = $jskey.dialog.returnValue;
			if(re != null){
				if(re.id == "${pid}"){alert("目标节点与当前节点相同");return false;}
				if(re.id == "0" && isSub){alert("部门或岗位不能移到根节点");return false;}
				if(re.status != "2"){
					if(isUnit){alert("不能移到非单位节点！");return false;}
				}
				else if(re.status == "2" && isPost){
					alert("岗位不能移到单位节点！");return false;
				}
				$("#movepid").val(re.id);
				$("#moveForm").ajaxSubmit({
					beforeSubmit:$dswork.showRequest,
					success:function(data){
						$dswork.doAjaxShow(data, function(){if($dswork.result.type == 1){
							parent.$dswork.ztree.expandAll(false);//收缩掉其余的
							parent.$dswork.ztree.moveUpdate("${pid}", $("#movepid").val());
						}});
					}
				});
				$jskey.dialog.close();
			}
			else{alert("请选择需要移动的目标节点位置");}
		}}];
		$jskey.dialog.showDialog(obj);
		return true;
	}else{alert("请选择记录！");}
	return false;
});
$("#dataTable>tbody>tr>td.v").each(function(){try{$(this).text($(this).text()=="2"?"单位":($(this).text()=="1"?"部门":"岗位"));}catch(e){}});
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">组织机构列表</td>
		<td class="menuTool">
			<a class="insert" href="addOrg1.htm?pid=${pid}">添加</a>
			<a class="sort" href="updOrgSeq1.htm?pid=${pid}">排序</a>
			<a class="move" id="listFormMoveAll" href="#">移动所选</a>
			<a class="delete" id="listFormDelAll" href="#">删除所选</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getOrg.htm?rootid=${rootid}&pid=${pid}">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;名称：<input type="text" name="name" value="${fn:escapeXml(param.name)}" />
			&nbsp;职责范围：<input type="text" name="dutyscope" value="${fn:escapeXml(param.dutyscope)}" />
		</td>
		<td class="query"><input id="_querySubmit_" type="button" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<form id="listForm" method="post" action="delOrg.htm">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:2%"><input id="chkall" type="checkbox" /></td>
		<td style="width:5%">操作</td>
		<td style="width:8%">类型</td>
		<td>名称</td>
		<td style="width:10%">操作</td>
	</tr>
<c:forEach items="${list}" var="d">
	<tr>
		<td><input name="keyIndex" type="checkbox" value="${d.id}" v="${d.status}" /></td>
		<td class="menuTool" keyIndex="${d.id}">&nbsp;</td>
		<td class="v">${d.status}</td>
		<td>${fn:escapeXml(d.name)}</td>
		<td class="menuTool">
			<a class="update" href="updOrg1.htm?keyIndex=${d.id}">修改</a>
		</td>
	</tr>
</c:forEach>
</table>
</form>
<div style="display:none;">
<form id="moveForm" method="post" action="updOrgMove2.htm">
	<input id="moveids" name="ids" type="hidden" value="" />
	<input id="movepid" name="pid" type="hidden" value="" />
</form>
</div>
<br />
</body>
</html>
