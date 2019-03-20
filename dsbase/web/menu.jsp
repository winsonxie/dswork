<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%><%!public static dswork.sso.model.IFunc[] getFuncByUser(String account){
	java.util.List<dswork.sso.model.IFunc> list = new java.util.ArrayList<dswork.sso.model.IFunc>();
	if(account != null && !account.equals("")){
		dswork.sso.model.IFunc[] arr = dswork.sso.AuthFactory.getFunctionByUser(account);
		if(arr != null){
			for(dswork.sso.model.IFunc m : arr){if(m.getStatus() == 1){list.add(m);}}
		}return list.toArray(new dswork.sso.model.IFunc[list.size()]);
	}return null;
}
//static{dswork.sso.AuthGlobal.init("https://127.0.0.1/sso/api", "DsCommon", "1");}%><%
String jsoncallback  = String.valueOf(request.getParameter("jsoncallback")).replaceAll("<", "").replaceAll(">", "").replaceAll("\"", "").replaceAll("'", "");
String user = String.valueOf(request.getParameter("user"));
%><%=jsoncallback%>([<%
dswork.sso.model.IFunc[] list = getFuncByUser(user);
StringBuilder sb = new StringBuilder(300);
if(list != null){
	for(dswork.sso.model.IFunc m : list){
		sb.append(",{id:" + m.getId() + ", pid:" + m.getPid() + ", name:\"" + m.getName() + "\", img:\"\", imgOpen:\"\", url:\"" + m.getUri() + "\"}");
	}
	if(list.length > 0){%><%=sb.substring(1)%><%}
}
%><%--
{id:1, pid:0, name:'门户菜单', img:"", imgOpen:"", url:""}
,{id:2, pid:1, name:'门户首页', img:"", imgOpen:"", url:"/frame/main.jsp"}
--%>])