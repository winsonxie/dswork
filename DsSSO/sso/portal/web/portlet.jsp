<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<style type="text/css">html,body{width:100%;height:100%;}</style>
<%--
<link rel="stylesheet" type="text/css" href="/web/js/easyui/themes/default/easyui.css" />
<script type="text/javascript" src="/web/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/web/js/easyui/jquery.easyui.js"></script>
--%>
<style type="text/css">
.box {width:45%;height:40%;border:1px solid #ccc;}
.box .title {width:100%;height:10%;border:none;border-bottom:1px solid #ccc;background:#E0ECFF;color:#0E2D5F;font-size:14px;line-height:30px;font-weight:bold;font-family:"微软雅黑","宋体"}
.box iframe {width:100%;height:90%;overflow:hidden;border:none;}
</style>
</head>
<body style="background-color:#E0ECFF;position:relative;overflow:hidden;">
<div class="easyui-draggable box" data-options="handle:'#v1'" style="left:0;top:0;position:absolute;">
	<div id="v1" class="title">&nbsp; 通知公告</div>
	<iframe frameborder="0" src="${ctx}/frame/main.jsp"></iframe>
</div>
<div class="easyui-draggable box" data-options="handle:'#v2'" style="left:50%;top:0;position:absolute;">
	<div id="v2" class="title">&nbsp; 通知公告</div>
	<iframe frameborder="0" src="${ctx}/frame/main.jsp"></iframe>
</div>
<div class="easyui-draggable box" data-options="handle:'#v3'" style="left:0;top:50%;position:absolute;">
	<div id="v3" class="title">&nbsp; 通知公告</div>
	<iframe frameborder="0" src="${ctx}/frame/main.jsp"></iframe>
</div>
<div class="easyui-draggable box" data-options="handle:'#v4'" style="left:50%;top:50%;position:absolute;">
	<div id="v4" class="title">&nbsp; 通知公告</div>
	<iframe frameborder="0" src="${ctx}/frame/main.jsp"></iframe>
</div>
</body>
</html>