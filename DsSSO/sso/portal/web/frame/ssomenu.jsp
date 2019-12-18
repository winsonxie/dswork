<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%><%!
public static dswork.sso.model.IFunc[] getFuncByUser(String account, String otherAlias){
	if(account != null && !account.equals("") && otherAlias != null && !otherAlias.equals("")){
		dswork.sso.http.HttpUtil h = dswork.sso.AuthFactory.getSystemHttp("/api/getFunctionByUser").addForm("userAccount", account).addForm("otherAlias", otherAlias);
		String v = "";
		java.util.List<dswork.sso.model.IFunc> list = null;
		try
		{
			v = h.connect().trim();
			list = dswork.sso.AuthGlobal.gson.fromJson(v, new com.google.gson.reflect.TypeToken<java.util.List<dswork.sso.model.IFunc>>(){}.getType());
		}
		catch(Exception e)
		{
		}
		java.util.List<dswork.sso.model.IFunc> xx = new java.util.ArrayList<dswork.sso.model.IFunc>();
		if(list != null){
			for(dswork.sso.model.IFunc m : list){if(m.getStatus() == 1){xx.add(m);}}
		}return xx.toArray(new dswork.sso.model.IFunc[xx.size()]);
	}return null;
}
%><%
String jsoncallback  = String.valueOf(request.getParameter("jsoncallback")).replaceAll("<", "").replaceAll(">", "").replaceAll("\"", "").replaceAll("'", "");
String otherAlias = String.valueOf(request.getParameter("otherAlias"));
%><%=jsoncallback%>([<%
//{id:1, pid:0, name:'门户菜单', img:"", imgOpen:"", url:""}
//,{id:2, pid:1, name:'门户首页', img:"", imgOpen:"", url:"/frame/main.jsp"}
dswork.sso.model.IFunc[] list = getFuncByUser(dswork.sso.WebFilter.getLoginer(session).getAccount(), otherAlias);
StringBuilder sb = new StringBuilder(300);
if(list != null){
	for(dswork.sso.model.IFunc m : list){
		sb.append(",{id:" + m.getId() + ", pid:" + m.getPid() + ", name:\"" + m.getName() + "\", img:\"\", imgOpen:\"\", url:\"" + m.getUri() + "\"}");
	}
	if(list.length > 0){%><%=sb.substring(1)%><%}
}
%>])