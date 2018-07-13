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
$dswork.callback = function(){if($dswork.result.type==1){
	location.reload();
}};
function show(){
	var i = new Image();
	i.src = $("#inputImg").val();
	i.onload = function(){$("#imgShow").attr("src",this.src).show()};
	i.onerror = function(){$("#imgShow").hide()};
}
$(function(){
	$(".form_title").css("width", "8%");
	$("#btn_category").bind("click", function(){
		if(confirm("是否发布")){
			$dswork.doAjaxObject.show("发布中");
			$.post("build.htm",{siteid:"${po.siteid}",categoryid:"${po.categoryid}",pageid:"${po.id}"},function(data){
				$dswork.doAjaxShow(data, $dswork.callback);
			});
		}
	});
	show();
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">页面明细</td>
		<td class="menuTool">
		<c:if test="${po.scope==2}">
			<a class="look" target="_blank" href="${fn:escapeXml(po.url)}">预览外链</a>
		</c:if>
		<c:if test="${po.scope!=2}">
			<a class="look" target="_blank" href="${ctx}/cmsbuild/buildHTML.chtml?view=true&siteid=${po.siteid}&categoryid=${po.categoryid}&pageid=${po.id}">预览</a>
			<c:if test="enablemobile"><a class="look" target="_blank" href="${ctx}/cmsbuild/buildHTML.chtml?view=true&siteid=${po.siteid}&categoryid=${po.categoryid}&pageid=${po.id}&mobile=true">预览</a></c:if>
		</c:if>
			<a class="graph" id="btn_category" href="#">发布</a>
			<a class="back" href="getPage.htm?id=${po.categoryid}&page=${page}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<c:if test="${po.status==-1}">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr><td class="form_input" style="color:red;text-align:center;">此条处于【删除发布】状态，当处于此状态的条目被发布时，条目的数据及页面都将被删除</td></tr>
</table>
<div class="line"></div>
</c:if>
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
		<td class="form_title">图片</td>
		<td class="form_input">
			图片地址：${fn:escapeXml(po.img)}<br/>
			焦点图：${po.imgtop==1?'是':'否'}<br/>
			<input type="hidden" id="inputImg" value="${fn:escapeXml(po.img)}" /><img id="imgShow" style="width:100px">
		</td>
	</tr>
	<tr>
		<td class="form_title">发布</td>
		<td class="form_input">
			首页推荐：${po.pagetop==1?'是':'否'}<br/>
			发布时间：${fn:escapeXml(po.releasetime)}
		</td>
	</tr>
	<tr>
		<td class="form_title">状态</td>
		<td class="form_input">${po.status==-1?'删除发布':po.status==8?'已发布':'待发布'}</td>
	</tr>
<c:forEach items="${columns}" var="d">
	<tr>
		<td class="form_title">${fn:escapeXml(d.cname)}</td>
		<td class="form_input">${fn:escapeXml(d.cvalue)}</td>
	</tr>
</c:forEach>
</table>
</body>
</html>
