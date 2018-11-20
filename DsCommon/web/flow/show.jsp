<%@page language="java" pageEncoding="UTF-8"%><%
String url = "";
try{
String filename = String.valueOf(request.getParameter("filename"));//经由get方式传递过来的文件名
String name = String.valueOf(request.getParameter("name"));
filename = filename.substring(0, filename.lastIndexOf("."))+".pdf";
url = "/web/js/pdfjs/web/viewer.html?file=" + java.net.URLEncoder.encode("/webio/io/down.jsp?name=" + name +"&f=" + filename);
}catch(Exception e)
{
	e.printStackTrace();
}%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<style type="text/css">
html,body{width:100%;height:100%;margin:0;padding:0;overflow:hidden;}
</style>
</head>
<body>
<iframe name="childframe" src="<%=url%>" frameBorder=0 style="width:100%;height:100%;" scrolling="no"></iframe>
</body>
</html>
