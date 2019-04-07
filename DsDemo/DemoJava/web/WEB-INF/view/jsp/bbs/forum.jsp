<%@page language="java" pageEncoding="UTF-8"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><c:set var="ctx" value="${pageContext.request.contextPath}"/><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>${fn:escapeXml(forum.name)}-${site.name}</title>
<link rel="stylesheet" href="${ctx}/bbs/web.css" />
<script type="text/javascript" src="/web/js/jquery/jquery.js"></script>
<style type="text/css">
body {overflow-x:hidden;}
.box {width:800px;margin:0 auto;line-height:20px;clear:both;}
.cl {}
.ct {padding:10px;color:#4CB0E1;font-size:18px;line-height:18px;font-weight:bold;margin-top:15px;}
.cd {overflow:hidden;}
.cd .bk {padding:10px;color:#999;}
.cd dl {float:left;width:200px;padding:10px;border:1px solid #ddd;margin:10px;overflow:hidden;}
.cd dl:hover {background-color:#ddd;cursor:pointer;}
.cd dt {font-size:18px;}
.cd dd {font-size:12px;}

.cc {overflow:hidden;padding:10px;height:70px;}
.cc div {float:left;}
.cc div.right {float:right;}
.cc .isessence, .cc .istop {display:block;width:50px;height:20px;line-height:20px;margin:5px auto;text-align:center;border-radius:3px;}
.cc .isessence {background-color:#64BB25;color:#fff;}
.cc .istop {background-color:#ff0000;color:#fff;}
.cc .msg {float:left;overflow:hidden;margin-left:20px;}
.cc .msg dl {}
.cc .msg dt {font-size:18px;line-height:30px;}
.cc .msg dt:hover {cursor:pointer;text-decoration:underline;}
.cc .msg dd {font-size:12px;line-height:20px;margin-left:10px;}
.cc .msg dd label {color:#999999;}
.cc .red {color:#ff0000;}
.cc img {width:64px;height:64px;}
.cc a {color:#2aa8e2;text-decoration:none;}
.cc a:link{}
.cc a:visited{}
.cc a:hover{text-decoration:underline;}
.cc a:active{}
</style>
</head>
<body>
<div class="ds"></div>
<div class="ds ds_top">
	<div class="left">
		&nbsp;<a href="index.htm">首页</a> >> <c:if test="${forum.pid >0 && forum.parent.pid >0}"><c:if test="${forum.parent.pid >0 && forum.parent.parent.pid >0}"><a href="forum-${forum.parent.parent.id}-1.htm">${fn:escapeXml(forum.parent.parent.name)}</a> >> </c:if><a href="forum-${forum.parent.id}-1.htm">${fn:escapeXml(forum.parent.name)}</a> >> </c:if><span class="a">${fn:escapeXml(forum.name)}</span>
	</div>
	<div class="right">
		<a href="#">我的轻应用</a>
		<a href="#">帮助中心</a>
		&nbsp;
	</div>
</div>
<c:if test="${fn:length(list)>0}">
<div class="box">
	<div class="cl">
		<div class="ct">子版块</div>
		<div class="line"></div>
		<div class="cd"><c:forEach items="${list}" var="f">
			<dl onclick="location.href='forum-${f.id}-1.htm';" id="dl${f.id}">
				<dt>${fn:escapeXml(f.name)}</dt>
				<dd>${fn:escapeXml(f.summary)}</dd>
			</dl>
		</c:forEach></div>
	</div>
</div>
</c:if>
<div class="box">
	<div id="bk${forum.id}" class="cl">
		<div class="ct">${forum.name}</div>
		<div class="line"></div>
		<div class="cd">
			<div class="bk">
				主题：(<span id="show_zts"></span>)
				&nbsp;
				帖数：(<span id="show_tzs"></span>)
				<br />
				摘要：${fn:escapeXml(forum.summary)}
			</div>
		</div>
	</div>
</div>
<div class="box">
	<div id="zt${forum.id}" class="cl">
		<div class="ct">主题</div>
	</div>
	<div class="line"></div>
</div>
<c:forEach items="${pageModel.result}" var="d">
<div class="box">
	<div class="cc">
		<div class="msg">
			<dl>
				<dt${d.istop == 1 ? ' class="red"' : ''}>${fn:escapeXml(d.title)}</dt>
				<dd>${fn:escapeXml(d.summary)}</dd>
				<dd>
					<a href="#">${fn:escapeXml(d.releaseuser)}</a>
					<label>发表于：</label>${fn:escapeXml(d.releasetime)}&nbsp;
					<label>浏览：</label>(${fn:escapeXml(d.numpv)})&nbsp;
					<label>回复：</label>(${fn:escapeXml(d.numht)})&nbsp;
					<label>最后回复：</label>${fn:escapeXml(d.lasttime)}
				</dd>
			</dl>
		</div>
		<div class="right">
			${d.istop == 1 ? '<a class="istop">置顶</a>' : ''}
			${d.isessence == 1 ? '<a class="isessence">精华</a>' : ''}
		</div>
	</div>
	<div class="line"></div>
</div>
</c:forEach>
<div class="ds ds_copyright">
	&copy; 2014-2015 skey_chen@163.com
	&nbsp;&nbsp;<a href="#">帮助中心</a>
</div>
</html>
