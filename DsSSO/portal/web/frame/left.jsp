<%@page language="java" pageEncoding="UTF-8" import="
	dswork.sso.WebFilter,
	dswork.sso.AuthFactory,
	dswork.sso.model.ISystem,
	dswork.sso.model.IUser"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><c:set var="ctx" value="${pageContext.request.contextPath}" /><%
IUser user = WebFilter.getLoginer(session);
ISystem[] arr = AuthFactory.getSystemByUser(user.getAccount());
%>
<!--<!DOCTYPE html>-->
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<title></title>
<link rel="stylesheet" type="text/css" href="${ctx}/frame/js/jskey/themes/menu/jskey.menu.css?version=20190911" />
<script type="text/javascript" src="${ctx}/frame/js/jskey/jskey.menu.js?version=20190911"></script>
<script type="text/javascript" src="${ctx}/frame/js/jquery.js"></script>
<script type="text/javascript">
if(top.location == this.location){top.location = "${ctx}/index.jsp";}
var sys = [];
<%--
sys[0] = {index:0,data:[],id:0,name:"门户",alias:"",domainurl:"",rooturl:"${ctx}",menuurl:"${ctx}/menu.jsp"};
<%if(arr != null){for(int i = 0; i < arr.length; i++){%>
sys[sys.length] = {index:<%=i+1%>,data:[],id:<%=arr[i].getId()%>,name:"<%=arr[i].getName().replaceAll("\"", "\\\\\"")%>",alias:"<%=arr[i].getAlias()%>",domainurl:"<%=arr[i].getDomainurl().replaceAll("\"", "\\\\\"")%>",rooturl:"<%=arr[i].getRooturl().replaceAll("\"", "\\\\\"")%>",menuurl:"<%=arr[i].getMenuurl().replaceAll("\"", "\\\\\"")%>"};
<%}}%>
--%>
<%if(arr != null){for(int i = 0; i < arr.length; i++){%>
sys[sys.length] = {index:<%=i%>,data:[],id:<%=arr[i].getId()%>,name:"<%=arr[i].getName().replaceAll("\"", "\\\\\"")%>",alias:"<%=arr[i].getAlias()%>",domainurl:"<%=arr[i].getDomainurl().replaceAll("\"", "\\\\\"")%>",rooturl:"<%=arr[i].getRooturl().replaceAll("\"", "\\\\\"")%>",menuurl:"<%=arr[i].getMenuurl().replaceAll("\"", "\\\\\"")%>"};
<%}}%>
function menuload(o){
	var url = "", d = {};
	if(o.menuurl.length == 0){
		url = "${ctx}/frame/ssomenu.jsp?jsoncallback=?";
		d.otherAlias = o.alias;
	}
	else{
		url = o.domainurl + ((o.menuurl.length == 0) ? o.rooturl + "/sso/menu" : o.menuurl);
		url += ((url.indexOf("?") == -1)?"?":"&") + "ssoticket=<%=user.getSsoticket()%>" + "&jsoncallback=?";
	}
	$.getJSON(url, d, function(data){
		try{o.data = $jskey.menu.format(data);$jskey.menu.showNode(o.index, o.data, o.domainurl + o.rooturl);}catch(e){alert(e.message);}
	});
}
</script>
<script type="text/javascript">
<%--这个是全部加载在同一个页面--%>
$jskey.menu.load = function(index, id){
	for(var i = 0; i < sys.length; i++){
		var o = sys[i];
		if(i == index){
			if(o.data == null || o.data.length == 0){menuload(o);return;}
			break;
		}
	}
};
function init(){
	var treedata = [];
	for(var i=0; i<sys.length; i++){treedata[i] = {index:i, id:sys[i].id, name:sys[i].name, img:"", imgOpen:"", url:"", items:[]};}
	$jskey.menu.show(treedata, null, null);
	$jskey.menu.clickBar(0);
}
function showSystem(){alert("please use script to one system");}
<%----%>
<%--这个是每次只加载一个系统，在index.jsp上增加切换系统的调用脚本window.frames['leftFrame'].showSystem();--%>
<%--
function show(url, args, width, height){
	url += ((url.indexOf("?") == -1) ? "?jskey=" : "&jskey=") + (new Date().getTime());//防止缓存
	return window.showModalDialog(url, args, "dialogWidth:" + width + "px;dialogHeight:" + height//
			+ "px;help:no;center:yes;status:no;scroll:auto;resizable:yes"//
			+ ";dialogTop:" + ((window.screen.availHeight - height) / 3)//
			+ ";dialogLeft:" + ((window.screen.availWidth - width) / 2)//
	);
}
var myindex = 0;
$jskey.menu.showNode = function(i, data){
	if(sys.length > i){
		myindex = i;
		$jskey.menu.show(data, true, sys[i].domainurl + sys[i].rooturl);
		if(data.length > 0){$jskey.menu.clickBar(0);}
	}
};
function showSystem(){
	try{var i = show("./system.html", {sys:sys,index:myindex}, 300, 200);
		if(i != null){
			var o = sys[i]; 
			if(o.data == null || o.data.length == 0){menuload(o);}
			else{$jskey.menu.showNode(o.index, o.data, o.domainurl + o.rooturl);}
		}
	}catch(e){}
}
function init(){menuload(sys[0]);}
--%>
</script>
</head>
<body onselectstart="return false;" oncontextmenu="return true;">
</body>
<script type="text/javascript">
$jskey.menu.isTabs = <%=String.valueOf(request.getParameter("isTabs")).equalsIgnoreCase("true") ? "true" : "false"%>;
init();
</script>
</html>
