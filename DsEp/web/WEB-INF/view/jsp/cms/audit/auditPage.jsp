<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/updAjax.jsp" %>
<%@include file="/commons/include/editor.jsp" %>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getPage.htm?id=${po.categoryid}&page=${param.page}";
}};
$(function(){
	$(".form_title").css("width", "8%");
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">审核页面</td>
	<c:if test="${po.audit}">
		<td class="menuTool">
			<a class="submit" onclick="_pass();" href="#">通过</a>
			<a class="close" onclick="_nopass();" href="#">不通过</a>
			<a class="back" href="getPage.htm?id=${po.categoryid}&page=${param.page}">返回</a>
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
<c:if test="${po.status==-1}">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr><td class="form_input" style="color:red;text-align:center;">删除操作审核，审核通过后本条目将在该栏目发布时被删除</td></tr>
</table>
<div class="line"></div>
</c:if>
<form id="dataForm" method="post" action="auditPage2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">标题</td>
		<td class="form_input">${fn:escapeXml(po.title)}</td>
	</tr>
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
		<td class="form_title">是否外链</td>
		<td class="form_input">${po.scope==2?'是'.concat(' 链接:').concat(po.url):'否'}</td>
	</tr>
	<tr>
		<td class="form_title">操作</td>
		<td class="form_input menuTool">
		<c:if test="${po.scope==2}">
			<a class="look" target="_blank" href="${fn:escapeXml(po.url)}">查看外链</a>
		</c:if>
		<c:if test="${po.scope!=2}">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${po.siteid}&categoryid=${po.categoryid}&pageid=${po.id}">查看内容</a><c:if test="${enablemobile}">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/preview.chtml?siteid=${po.siteid}&categoryid=${po.categoryid}&pageid=${po.id}&mobile=true">查看移动版内容</a></c:if>
		</c:if>
		</td>
	</tr>
	<tr>
		<td class="form_title">图片</td>
		<td class="form_input">
			图片地址：${fn:escapeXml(po.img)}<br/>
			焦点图：${po.imgtop==1?'是':'否'}
		</td>
	</tr>
	<tr>
		<td class="form_title">发布</td>
		<td class="form_input">
			首页推荐：${po.pagetop==1?'是':'否'}<br/>
			发布时间：${fn:escapeXml(po.releasetime)}
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">审核意见</td>
		<td class="form_input"><textarea name="msg" style="width:99%;height:100px;"></textarea></td>
	</tr>
</table>
<input type="hidden" name="id" value="${po.id}" />
<input type="hidden" name="action" value="" />
</form>
</c:if>
</body>
</html>
