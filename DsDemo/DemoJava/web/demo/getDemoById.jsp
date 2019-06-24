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
				var v = "<tr><td class=\"form_title\">标题：</td><td class=\"form_input\">"+data.po.title+"</td></tr>"
				 +"<tr><td class=\"form_title\">内容：</td><td class=\"form_input\">"+data.po.content+"</td></tr>"
				 +"<tr><td class=\"form_title\">时间：</td><td class=\"form_input\">"+data.po.foundtime+"</td></tr>";
				 
				 document.getElementById('view').innerHTML = v;
			});
		});
	</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">明细</td>
		<td class="menuTool">
			<a class="back" onclick="window.history.back();return false;" href="#">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table id="view" border="0" cellspacing="1" cellpadding="0" class="listTable">
	
</table>
</body>
</html>
