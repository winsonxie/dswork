<%@page language="java" pageEncoding="UTF-8"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><c:set var="ctx" value="${pageContext.request.contextPath}"/><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>${site.name}</title>
<link rel="stylesheet" href="${ctx}/bbs/web.css" />
<script type="text/javascript" src="/web/js/jquery/jquery.js"></script>
<style type="text/css">
body {overflow-x:hidden;}
.box {width:100%;line-height:20px;clear:both;}
.cl {}
.ct {padding:10px;color:#4CB0E1;font-size:18px;line-height:18px;font-weight:bold;border-bottom:1px solid #ddd;margin-top:15px;}
.cd {overflow:hidden;}
.cd .bk {padding:10px;color:#999;}
.cd dl {float:left;width:200px;padding:10px;border:1px solid #ddd;margin:10px;overflow:hidden;}
.cd dl:hover {background-color:#ddd;cursor:pointer;}
.cd dt {font-size:18px;}
.cd dd {font-size:12px;}
</style>
</head>
<body>
<body>
<div class="ds"></div>
<div class="ds ds_top">
	<div class="left">
		&nbsp;<span class="a">首页</span>
	</div>
	<div class="right">
		<a href="#">我的轻应用</a>
		<a href="#">帮助中心</a>
		&nbsp;
	</div>
</div>

<c:forEach items="${list}" var="d">
<div class="box">
	<div id="cl${d.id}" class="cl">
		<div class="ct">${fn:escapeXml(d.name)}</div>
		<div class="line"></div>
		<div class="cd"><c:forEach items="${d.list}" var="f">
			<dl onclick="location.href='forum-${f.id}-1.htm';" id="dl${f.id}">
				<dt>${fn:escapeXml(f.name)}</dt>
				<dd>${fn:escapeXml(f.summary)}</dd>
			</dl>
		</c:forEach></div>
	</div>
</div>
</c:forEach>
<div class="ds ds_copyright">
	&copy; 2014-2015 skey_chen@163.com
	&nbsp;&nbsp;<a href="#">帮助中心</a>
</div>
</body>
</html>
