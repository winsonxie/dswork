<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp"%>
<%@include file="/commons/include/ztree.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getUser.htm?xtype=${fn:escapeXml(param.xtype)}&type=${fn:escapeXml(param.type)}&orgpid=${param.orgpid}&orgid=${param.orgid}&page=${fn:escapeXml(param.page)}";
}};
$(function(){
	$("#orgpname").bind("click", function(e){
		$dswork.showTree({id:"treeid1",width:400,height:200,root:{id:"${orgpid}",name:"${orgpid==0?'组织机构':orgpname}"${orgpid==0?'':',nocheck:false'}}
			,left:$(this).offset().left, top:$(this).offset().top+20
			,url:function(node){return "${ctx}/common/share/getJsonOrg.htm?pid="+node.id;}
			,check:function(id, node){if(node.id==0){return false;}else{$("#orgpname").val(node.name);$("#orgpid").val(node.id);$("#orgname").val();$("#orgid").val();}}
			,dataFilter:function(id, pnode, data){var d=[];for(var i =0; i < data.length; i++){if(data[i].status == 2){d.push(data[i]);}}return d;}
		});
	});
	$("#orgname").bind("click", function(e){
		var rootid = $("#orgpid").val();
		if(rootid == ""){alert("请先选择单位");return false;}
		$dswork.showTree({id:"treeid2",width:400,height:200,root:{id:rootid, name:"选择部门-"+$("#orgpname").val()}
			,left:$(this).offset().left, top:$(this).offset().top+20
			,url:function(node){return "${ctx}/common/share/getJsonOrg.htm?pid="+node.id;}
			,check:function(id, node){if(node.id==0 || node.status != 1){return false;}else{$("#orgname").val(node.name);$("#orgid").val(node.id);}}
			,dataFilter:function(id, pnode, data){var d=[];for(var i =0; i < data.length; i++){if(data[i].status == 1){d.push(data[i]);}}return d;}
		});
	});
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">调职</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getUser.htm?xtype=${fn:escapeXml(param.xtype)}&orgpid=${param.orgpid}&orgid=${param.orgid}&page=${fn:escapeXml(param.page)}">返回</a> 
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updUserOrg2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">帐号</td>
		<td class="form_input">${fn:escapeXml(po.account)}</td>
	</tr>
	<tr>
		<td class="form_title">所属单位</td>
		<td class="form_input"><input type="text" id="orgpname" name="orgpname" readonly="readonly" value="${po.orgpname}" /><input type="hidden" id="orgpid" name="orgpid" value="${po.orgpid}" /></td>
	</tr>
	<tr>
		<td class="form_title">所属部门</td>
		<td class="form_input"><input type="text" id="orgname" name="orgname" readonly="readonly" value="${po.orgname}" /><input type="hidden" id="orgid" name="orgid" value="${po.orgid}" /></td>
	</tr>
</table>
<input type="hidden" name="id" value="${po.id}" />
</form>
</body>
</html>
