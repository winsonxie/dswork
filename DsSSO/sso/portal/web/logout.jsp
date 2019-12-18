<%@page language="java" pageEncoding="UTF-8" import="
	dswork.sso.WebFilter,
	dswork.sso.AuthFactory,
	dswork.sso.model.ISystem"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%
	ISystem[] arr = AuthFactory.getSystemByUser(WebFilter.getAccount(session));
%><c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<title></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<script type="text/javascript" src="${ctx}/frame/js/jquery.js"></script><%--当用户没登录时访问，此js不会被加载--%>
<script type="text/javascript">
var sys = [];
<%if(arr != null){for(int i = 0; i < arr.length; i++){%>
sys[sys.length] = {index:<%=i%>,data:[],id:<%=arr[i].getId()%>,name:"<%=arr[i].getName().replaceAll("\"", "\\\\\"")%>",alias:"<%=arr[i].getAlias()%>",domainurl:"<%=arr[i].getDomainurl().replaceAll("\"", "\\\\\"")%>",rooturl:"<%=arr[i].getRooturl().replaceAll("\"", "\\\\\"")%>"};
<%}}%>
var index = 0,count = sys.length;
var ok = 0;
var errmsg = "";
function logoutload(o){
	var url = o.domainurl + o.rooturl + "/sso/logout?jsoncallback=?";
	$("body").append("<div id='x" + o.id + "'>" + o.name + "正在退出！</div>");
	try{
	$.ajax({
		url:url,
		type:"post",
		dataType:"jsonp",
		cache:false,
		timeout:3000,
		success:function(data){
			ok++;
			$("#x" + o.id).html(o.name + "已退出！");
			if(ok >= count){
				//if(errmsg.length > 0){alert(errmsg);}
				location.href="${ctx}/logoutAction.jsp";
			}
		},
		error:function(){errmsg += ((errmsg.length == 0)?"":"\n") + o.name + "...退出失败，无法访问该服务器！！！";
			ok++;
			$("#x" + o.id).html(o.name + "...退出失败，无法访问该服务器！！！");
			if(ok >= count){
				//if(errmsg.length > 0){alert(errmsg);}
				location.href="${ctx}/logoutAction.jsp";
			}
		}
	});
	}catch(e){alert(e.message);}
}
$(function(){
if(count <= 0){
	location.href="${ctx}/logoutAction.jsp";
}
else{
	for(var i = 0; i < count; i++){logoutload(sys[i]);}
}
});
</script>
</head>
<body></body>
</html>

