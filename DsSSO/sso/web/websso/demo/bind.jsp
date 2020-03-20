<%@page contentType="text/html; charset=UTF-8" import="
dswork.web.MyRequest,dswork.common.SsoFactory,java.util.List,dswork.sso.util.websso.WebssoUtil
,dswork.common.model.IUnit,dswork.common.model.ZAuthorizecode,dswork.common.model.IUser
,dswork.common.model.IUserBindState,dswork.common.model.IBind,dswork.common.model.IUserBind
,dswork.common.util.TokenUserUtil,dswork.common.util.UnitUtil,dswork.common.model.ZAuthtoken,dswork.common.util.ResponseUtil
"%><%!
// 获取当前会话的sso账户
public IUser getCurrLoginUser(long appkey, HttpServletRequest request)
{
	IUser user = null;
	String[] arr = null;
	String ssoticket = "";
	Cookie[] cookies = request.getCookies();
	if(cookies != null && cookies.length > 0)
	{
		for(Cookie cookie : cookies)
		{
			if("ssoticket".equals(cookie.getName()))
			{
				ssoticket = cookie.getValue();
				break;
			}
		}
	}
	if(ssoticket.length() > 0)
	{
		if(ssoticket != null && ssoticket.length() > 10)
		{
			if(ssoticket.startsWith("-"))
			{
				arr = ssoticket.substring(1).split("-", 2);
				if(arr.length == 2)
				{
					arr = new String[] { "-" + arr[0], arr[1], ssoticket };
				}
			}
			else
			{
				arr = ssoticket.split("-", 2);
				if(arr.length == 2)
				{
					arr = new String[] { arr[0], arr[1], ssoticket };
				}
			}
		}
		if(arr != null)
		{
			String openid = arr[0];
			String access_token = arr[1];
			String userinfoJSON = TokenUserUtil.tokenGet(appkey, openid, access_token);
			if(userinfoJSON != null)
			{
				user = ResponseUtil.toBean(userinfoJSON, IUser.class);
			}
		}
	}
	return user;
}
%><%
MyRequest req = new MyRequest(request);
String appid = req.getString("appid");
String msg = "error";
if(appid.length() > 0)
{
	IUnit unit = UnitUtil.get(appid);
	if(unit != null)
	{
		long appkey = unit.getType();
		IUser user = getCurrLoginUser(appkey, request);
		boolean isLogin = (user != null ? true : false);
		String authorizecode = req.getString("code");
		boolean isBind = "true".equals(req.getString("isBind", "false"));
		if(authorizecode.length() > 0)
		{
			if(!isLogin)
			{
				ZAuthorizecode zCode = TokenUserUtil.codeGet(authorizecode, false);
				String userinfoJSON = zCode.getValue();
				user = ResponseUtil.toBean(userinfoJSON, IUser.class);
				String openid = user.getId() + "";
				ZAuthtoken token = TokenUserUtil.tokenCreate(appkey, openid, userinfoJSON);
				String ssoticket = openid + "-" + token.getAccess_token();
				Cookie cookie = new Cookie("ssoticket", ssoticket);
				cookie.setMaxAge(-1);
				cookie.setPath("/");
				cookie.setSecure(false);
				cookie.setHttpOnly(true);
				response.addCookie(cookie);
			}
			response.sendRedirect(request.getRequestURI() + "?appid=" + appid);
			return;
		}
		else if(isBind && isLogin)// 解除绑定
		{
			String bindid = req.getString("bindid");
			IBind bind = WebssoUtil.get(bindid);
			if(bind != null)
			{
				String bindtype = req.getString("bindtype");
				if(bindtype.startsWith("wechat") || bindtype.startsWith("qq") || bindtype.startsWith("weibo") || bindtype.startsWith("alipay"))
				{
					String type = bindtype.split("-", -1)[0];
					java.util.List<dswork.common.model.IUserBindState> list = dswork.common.SsoFactory.getSsoService().getUserBindStateByUserId(user.getId());
					if(list != null && list.size() > 0)
					{
						String bindids = "";
						for(int i = 0; i < list.size(); i++)
						{
							dswork.common.model.IUserBindState bs = list.get(i);
							if(bs.getApptype().startsWith(type))
							{
								bindids = bindids + bs.getId() + ",";
							}
						}
						if(bindids.length() > 0)
						{
							if(bindids.endsWith(","))
							{
								bindids = bindids.substring(0, bindids.length() - 1);
							}
							dswork.common.SsoFactory.getSsoService().updateUserBindForUnBind(user.getId(), bindids);
							response.setCharacterEncoding("UTF-8");
							response.setContentType("application/json;charset=UTF-8");
							response.setHeader("P3P", "CP=CAO PSA OUR");
							response.setHeader("Access-Control-Allow-Origin", "*");
							out.print("{\"code\":1}");
							return;
						}
					}
				}
			}
		}
		
		if(isLogin)
		{
			msg = "";
			request.setAttribute("user", user);
			List<IUserBindState> list = SsoFactory.getSsoService().getUserBindStateByUserId(user.getId());
			request.setAttribute("list", list);
		}
	}
}
if(msg.length() > 0)
{
	Cookie cookie = new Cookie("ssoticket", "");
	cookie.setMaxAge(-1);
	cookie.setPath("/");
	cookie.setSecure(false);
	cookie.setHttpOnly(true);
	response.addCookie(cookie);
	response.sendRedirect("/sso/websso/demo/login.jsp");
	return;
}
request.setAttribute("appid", appid);
response.setHeader("Cache-Control","no-store");
response.setDateHeader("Expires", 0);
response.setHeader("Pragma","no-cache"); 
request.setAttribute("c", "#2a92eb");// #003c7b #b71d29 #125995 #d3880d #2a92eb
request.setAttribute("ctx", "/sso");
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<title>绑定第三方账号</title>
<script type="text/javascript">
if(top.location != this.location){top.location = this.location;}
var ua = window.navigator.userAgent;
if(ua.indexOf("AppleWebKit") != -1){}
else if(ua.indexOf("QQBrowser") != -1){alert("为了获得最佳体验，请使用极速模式（请击地址栏最右边的e图标进行切换）");}
</script>
<!--[if IE]><script language="javascript">alert("发现您正在使用过期的IE浏览器，为了获得最佳体验，建议下载以下浏览器最新版本进行访问：QQ浏览器、360安全浏览器、Chrome浏览器等主流浏览器 ，并使用使用极速模式进行访问");</script><![endif]-->
<link rel="stylesheet" type="text/css" href="${ctx}/themes/share/fonts/dsworkfont.css"/>
<style type="text/css">
html,body{height:100%;margin:0px auto;}
body {background-color:#fff;font-family:tahoma,arial,"Microsoft YaHei","\5B8B\4F53",sans-serif;color:${c};font-size:16px;line-height:120%;}
i{font-family:dsworkfont;font-weight:normal;font-style:normal;}

.view {overflow:hidden;margin:0 auto;width:100%;min-width:300px;max-width:1000px;overflow:hidden;padding:8px 0;}
.title{background-color:${c};color:#fff;margin:0 0 40px 0;}
.title,
.view .title{font-weight:bold;text-align:center;font-size:32px;line-height:40px;padding:38px 0px;}
.view .title{background-color:inherit;color:${c};margin:0;}
.view .login{margin:0 auto;padding:0;width:360px;max-width:360px;border:${c} solid 0px;overflow:hidden;background-color:#fff;box-shadow:0 0 8px 0px ${c};}

.boxmsg{padding:0;display:none;}
.boxname{padding:0;}
.box{overflow:hidden;text-align:center;width:100%;margin:0 auto;padding:8px 0;border:none;}
.box .name{background-color:#fff;width:100%;padding:16px 0 8px 0;margin:0 auto;font-size:22px;line-height:22px;text-align:center;font-weight:normal;}
.box .msg{color:#ff0000;line-height:25px;}
.box .vbox{margin:0 auto;padding:0;overflow:hidden;text-align:left;vertical-align:top;width:275px;}
.box .vbox .input{border-radius:0 6px 6px 0;vertical-align:middle;height:48px;line-height:48px;background-color:#edf2f6;border:#d6e5ef 1px solid;border-left:none;width:194px;outline:none;padding:0 0 0 12px;}
.box .vbox .input:focus{border-color:${c};}
.box .vbox .input::placeholder{color:#999;}
.box .vbox .nolabel{border:#d6e5ef 1px solid;border-radius:6px;margin:0 0 0 66px;}
.box .vbox .nolabel:focus{border-color:${c};}
.box .vbox .code{border:#d6e5ef 1px solid;border-radius:6px;width:120px;margin:0 12px 0 0;}
.box .vbox .code:focus{border-color:${c};}
.box .vbox span {border-radius:6px 0 0 6px;vertical-align:middle;height:48px;line-height:48px;background-color:${c};border:${c} 1px solid;font-size:24px;margin:0;padding:0 20px;display:inline-block;color:#fff;}
.box .vbox img{width:120px;height:46px;border:none;cursor:pointer;vertical-align:middle;}
.box .button{background-color:${c};color:#fff;width:280px;height:50px;line-height:50px;cursor:pointer;border:none;border-radius:6px;-webkit-appearance:none;}
.box .button:hover{filter:alpha(opacity:80);opacity:0.8;}
.box .checkbox{vertical-align:middle;}
.box .radio{vertical-align:middle;}

.box .left{float:left;}
.box .right{float:right;}
.box b{font-weight:normal;font-style:normal;text-decoration:none;}
.box label{font-size:16px;font-weight:normal;line-height:18px;cursor:pointer;}
.box .link a,
.box .link a:link,
.box .link a:visited,
.box .link a:active,
.box .link label,
.box .link b{color:${c};font-size:12px;text-decoration:none;outline:none;}
.box .link b{margin:0 5px;}
.box .link a:hover{filter:alpha(opacity:80);opacity:0.8;text-decoration:none;}

.cp{color:#666;font-size:12px;width:80%;overflow:hidden;text-align:center;padding:15px 0;margin:20px auto 0 auto;border:none;border-top:solid #ccc 1px;}
.cp a,
.cp a:link,
.cp a:visited,
.cp a:active{font-size:12px;font-weight:normal;font-family:arial;color:${c};text-decoration:underline;outline:none;}
</style>
<style type="text/css">
body {background:#fff url(${ctx}/themes/share/bg/wave.png) bottom center repeat-x;}
.fieldset a {margin:0 10px;}
.fieldset button{outline-style:none;border-style:none;cursor:pointer;margin:0 10px;}
</style>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/ssomedia.css"/>
<script type="text/javascript">
function ajax(o) {
    o = o || {};
    o.method = (o.method||"POST").toUpperCase();
    o.url = o.url || "";
    o.async = o.async || true;// 是否为异步请求，true为异步的，false为同步的
    o.data = o.data || null;
    o.success = o.success || function (){};
    var x = XMLHttpRequest ? (new XMLHttpRequest()) : (new ActiveXObject("Microsoft.XMLHTTP"));
    var p = [];
    for(var k in o.data){p.push(encodeURIComponent(k)+"="+encodeURIComponent(o.data[k]));}
    var data = p.join("&");
    if (o.method == "POST") {
        x.open(o.method, o.url, o.async);
        x.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        x.send(data);
    }
    else{
        x.open(o.method, o.url+(o.url.indexOf("?")==-1?"?":"&")+data, o.async);
        x.send(null);
    } 
    x.onreadystatechange = function () {
        if (x.readyState == 4 && x.status == 200) {
            o.success(x.responseText);
        }
    };
}
function userBind(event, bindid, isBind, bindtype){
	if(isBind){// 已绑定
		var bindName = "";
		if(new RegExp("^qq").test(bindtype)){
			bindName = "QQ";
		}else if(new RegExp("^wechat").test(bindtype)){
			bindName = "微信";
		}else if(new RegExp("^alipay").test(bindtype)){
			bindName = "支付宝";
		}else if(new RegExp("^weibo").test(bindtype)){
			bindName = "微博";
		}
		if(confirm("是否取消绑定" + bindName + "，注意会将" + bindName + "相关的类型的第三方绑定也取消掉")){
			ajax({
				url:"${ctx}/websso/demo/bind.jsp",
				data:{
					appid:"${appid}",
					bindid:bindid,
					isBind:isBind,
					bindtype:bindtype
				},
				success:function(res){
					try{res = eval("("+res+")");}catch (e) {res = {};}
					if(res.code && res.code==1){
						location.reload();
					}
				}
			});
		}
	}else{
		if(confirm("是否绑定" + event.getAttribute("title"))){
			var url = event.getAttribute("data-url");
			console.log(url);
			if(url.length > 0){
				location.href = url;
			}
		}
	}
}
</script>
</head>
<body>
<div class="bg"></div>
<div class="title">&nbsp;统一身份认证平台</div>
<div class="view">
	<div class="box"><div class="vbox">
		<fieldset class="fieldset">
			<legend align="center" class="legend">当前用户信息</legend>
			name ： ${user.name}
		</fieldset>
	</div></div>
	<div class="box"><div class="vbox">
		<fieldset class="fieldset">
			<legend align="center" class="legend">第三方账号绑定</legend>
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="d">
						<button data-url="${ctx}/websso/login.jsp?bindid=${d.id}&bindtype=${d.apptype}&appid=CA1DB36591314A59EB5B449EF61D03E6&url=<%=java.net.URLEncoder.encode("/sso/websso/demo/bind.jsp", "UTF-8")%>" class="icon_${fn:indexOf(d.apptype, 'wechat')!=-1?'wechat':d.apptype}" title="${fn:toUpperCase(d.name)}" onclick="userBind(this, ${d.id}, ${d.isBind()}, '${d.apptype}');"></button>${d.isBind()?'已绑定':'未绑定'}<br><br>
					</c:forEach>
				</c:if>
		</fieldset>
	</div></div>
</div>
<div class="cp">
	&copy; 249725997@qq.com
</div>
</body>
</html>
