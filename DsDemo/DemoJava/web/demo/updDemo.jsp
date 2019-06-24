<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="dswork.web.MyRequest" %>
<%
	MyRequest req = new MyRequest(request);
	System.out.println(req.getLong("keyIndex"));
	request.setAttribute("keyIndex", req.getLong("keyIndex"));
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<%@include file="/commons/include/web.jsp"%>
	<script type="text/javascript">
		$(function(){
			$.getJSON("/DsWorkJava/manage/my/get/Demo.htm?keyIndex=${keyIndex}", function(data){
				var v = "<tr><td class=\"form_title\">标题：</td><td class=\"form_input\"><input type=\"text\" id=\"title\" name=\"title\" value=\""+data.po.title+"\" /></td></tr>"
				 +"<tr><td class=\"form_title\">内容：</td><td class=\"form_input\"><input type=\"text\" id=\"content\" name=\"content\" value=\""+data.po.content+"\" /></td></tr>"
				 +"<tr><td class=\"form_title\">时间：</td><td class=\"form_input\"><input type=\"text\" id=\"foundtime\" name=\"foundtime\" value=\""+data.po.foundtime+"\" /><input type=\"hidden\" name=\"id\" value=\""+data.po.id+"\" /></td></tr>";
				 
				 document.getElementById('view').innerHTML = v;
			});
		});
	</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getDemo.htm?page=${page}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="${ctx}/manage/my/update/Demo.htm">
<table id="view" border="0" cellspacing="1" cellpadding="0" class="listTable">
</table>
</form>
</body>
</html>
