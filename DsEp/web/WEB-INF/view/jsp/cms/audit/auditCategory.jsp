<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp" %>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type==1){
	location.reload();
}}
$(function(){
	$(".form_title").css("width", "8%");
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">审核</td>
		<c:if test="${scope==0}"><a class="back" href="getPage.htm?id=${po.id}">返回</a></c:if>
		<c:if test="${po.audit}">
		<td class="menuTool">
			<a class="submit" onclick="_pass();" href="#">通过</a>
			<a class="close" onclick="_nopass();" href="#">不通过</a>
			<script type="text/javascript">
			function _pass(){if(confirm('确认通过？')){
				$('input[name="action"]').val('pass');
				$('#dataForm').ajaxSubmit($dswork.doAjaxOption);
			}}
			function _nopass(){if(confirm('确认不通过？')){
				$('input[name="action"]').val('nopass');
				$('#dataForm').ajaxSubmit($dswork.doAjaxOption);
			}}
			</script>
		</td>
		</c:if>
	</tr>
</table>
<div class="line"></div>
<c:if test="${!po.audit}">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr><td class="form_input" style="color:red;text-align:center;">没有新提交的页面</td></tr>
</table>
</c:if>
<c:if test="${po.audit}">
<form id="dataForm" method="post" action="auditCategory2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
<c:if test="${scope==0 || scope==1}">
	<tr>
		<td class="form_title">摘要</td>
		<td class="form_input">${fn:escapeXml(po.summary)}</td>
	</tr>
	<tr>
		<td class="form_title">meta关键词</td>
		<td class="form_input">${fn:escapeXml(po.metakeywords)}</td>
	</tr>
	<tr>
		<td class="form_title">meta描述</td>
		<td class="form_input">${fn:escapeXml(po.metadescription)}</td>
	</tr>
	<tr>
		<td class="form_title">来源</td>
		<td class="form_input">${fn:escapeXml(po.releasesource)}</td>
	</tr>
	<tr>
		<td class="form_title">作者</td>
		<td class="form_input">${fn:escapeXml(po.releaseuser)}</td>
	</tr>
	<tr>
		<td class="form_title">图片</td>
		<td class="form_input">${fn:escapeXml(po.img)}</td>
	</tr>
	<tr>
		<td class="form_title">预览</td>
		<td class="form_input menuTool">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${po.siteid}&categoryid=${po.id}">查看栏目内容</a><c:if test="${enablemobile}">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${po.siteid}&categoryid=${po.id}&mobile=true">查看移动版栏目内容</a></c:if>
		</td>
	</tr>
	<tr>
		<td class="form_title">发布</td>
		<td class="form_input">
			发布时间：${fn:escapeXml(po.releasetime)}
		</td>
	</tr>
</c:if>
<c:if test="${scope==2}">
	<tr>
		<td class="form_title">URL</td>
		<td class="form_input">${fn:escapeXml(po.url)}</td>
	</tr>
	<tr>
		<td class="form_title">预览</td>
		<td class="form_input menuTool">
			<a class="look" target="_blank" href="${po.url}">查看外链</a>
		</td>
	</tr>
</c:if>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">审核意见</td>
		<td class="form_input"><textarea name="msg" style="width:99%;heigth:100px;"></textarea></td>
	</tr>
</table>
<input type="hidden" name="id" value="${po.id}" />
<input type="hidden" name="action" value="" />
</form>
</c:if>
</body>
</html>
