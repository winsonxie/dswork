<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
$dswork.page.join = function(td, menu, id){
	$(menu).append($('<div iconCls="menuTool-user">重置密码</div>').bind("click", function(){
		location.href = "updPassword1.htm?keyIndex=" + id;
	}));
	$(menu).append($('<div iconCls="menuTool-user">修改</div>').bind("click", function(){
		location.href = "updEnterprise1.htm?keyIndex=" + id;
	}));
};
$(function(){
	$dswork.page.menu("", "", "getEnterpriseById.htm", "${pageModel.page}");
	$("#status").val("${fn:escapeXml(param.status)}");
	$("#type").val("${fn:escapeXml(param.type)}");
	$("#dataTable>tbody>tr>td.status").each(function(){
		$(this).text($(this).text()=="1"?"正常运营":$(this).text()=="0"?"禁用":$(this).text()=="2"?"待审":"已注销");
	});
});
$dswork.doAjax = true;
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getEnterprise.htm?page=${pageModel.page}";
}};
</script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">企业列表</td>
		<td class="menuTool">
			<a class="insert" href="addEnterprise1.htm?page=${pageModel.page}">添加</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getEnterprise.htm">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;关键字查询：<input type="text" name="keyvalue" style="width:200px;" title="企业编码或企业名称" value="${fn:escapeXml(param.keyvalue)}" />
			&nbsp;状态：<select id="status" name="status" style="width:150px;">
				<option value="">全部</option>
				<option value="0">禁用</option>
				<option value="2">待审</option>
				<option value="3">已注销</option>
			</select>
			&nbsp;类型：<select id="type" name="type" style="width:150px;">
				<option value="">全部</option>
				<option value="有限责任公司">有限责任公司</option>
				<option value="股份有限公司">股份有限公司</option>
				<option value="内资企业">内资企业</option>
				<option value="国有企业">国有企业</option>
				<option value="集体企业">集体企业</option>
				<option value="股份合作企业">股份合作企业</option>
				<option value="联营企业">联营企业</option>
				<option value="私营企业">私营企业</option>
				<option value="其他企业">其他企业</option>
				<option value="港、澳、台商投资企业">港、澳、台商投资企业</option>
				<option value="合资经营企业（港或澳、台资）">合资经营企业（港或澳、台资）</option>
				<option value="合作经营企业（港或澳、台资）">合作经营企业（港或澳、台资）</option>
				<option value="港、澳、台商独资经营企业">港、澳、台商独资经营企业</option>
				<option value="港、澳、台商投资股份有限公司">港、澳、台商投资股份有限公司</option>
				<option value="其他港、澳、台商投资企业">其他港、澳、台商投资企业</option>
				<option value="外商投资企业">外商投资企业</option>
				<option value="中外合资经营企业">中外合资经营企业</option>
				<option value="中外合作经营企业">中外合作经营企业</option>
				<option value="外资企业">外资企业</option>
				<option value="外商投资股份有限公司">外商投资股份有限公司</option>
				<option value="其他外商投资企业">其他外商投资企业</option>
			</select>
		</td>
		<td class="query"><input id="_querySubmit_" type="button" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:5%">操作</td>
		<td style="width:15%">企业编码</td>
		<td>企业名称</td>
		<td style="width:20%">类型</td>
		<td style="width:10%">状态</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d">
	<tr>
		<td class="menuTool" keyIndex="${d.id}" qybm="${fn:escapeXml(d.qybm)}">&nbsp;</td>
		<td>${fn:escapeXml(d.qybm)}</td>
		<td>${fn:escapeXml(d.name)}</td>
		<td>${fn:escapeXml(d.type)}</td>
		<td class="status">${fn:escapeXml(d.status)}</td>
	</tr>
</c:forEach>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
</body>
</html>
