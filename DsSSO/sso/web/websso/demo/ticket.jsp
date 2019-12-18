<%@page contentType="text/html; charset=UTF-8"%><%!
public static String[] getSSOTicket(HttpServletRequest request){
	String ssoticket = getValueByCookie(request, "ssoticket");// cookie优先
	if(ssoticket != null && ssoticket.length() > 10){
		if(ssoticket.startsWith("-")){
			String[] arr = ssoticket.substring(1).split("-", 2);
			if(arr.length == 2){
				arr = new String[]{"-" + arr[0], arr[1], ssoticket};
				return arr;
			}
		}
		else{
			String[] arr = ssoticket.split("-", 2);
			if(arr.length == 2){
				arr = new String[]{arr[0], arr[1], ssoticket};
				return arr;
			}
		}
	}
	return null;
}
private static String getValueByCookie(HttpServletRequest request, String name){
	Cookie cookies[] = request.getCookies();
	String value = null;
	if(cookies != null){
		Cookie cookie = null;
		for(int i = 0; i < cookies.length; i++){
			cookie = cookies[i];
			if(cookie.getName().equals(name)){
				value = cookie.getValue();
				break;
			}
		}
	}
	return value;
}
%><%
String[] arr = getSSOTicket(request);// 获取是否存在ticket信息
dswork.common.model.IUser u = null;
if(arr != null){
	String openid = arr[0];
	String access_token = arr[1];
	String json = dswork.common.util.TokenUserUtil.tokenGet(0L, openid, access_token);
	u = dswork.common.util.ResponseUtil.toBean(json, dswork.common.model.IUser.class);
}
if(u == null){
	response.sendRedirect(request.getContextPath() + "/login");
	return;
}
%><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<title>Ticket</title>
<style>body{width:38em;margin:0 auto;font-family:Tahoma, Verdana, Arial, sans-serif, 微软雅黑;}</style>
<script type="text/javascript">
function WinNew(){window.location.href = "/portal";}
//window.setTimeout("WinNew()",5000);
</script>
</head>
<body>
<h1>用户账号是:<%=u.getAccount()%>。</h1>
<p><em>system administrator.</em></p>
</body>
</html>
