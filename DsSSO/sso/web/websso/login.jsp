<%@page language="java" pageEncoding="UTF-8"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%
int i = 0;
String url = "";
try
{
	dswork.web.MyRequest req = new dswork.web.MyRequest(request);
	String c = req.getString("c", "");
	String date = dswork.core.util.TimeUtil.getCurrentDate();
	String bindid = req.getString("bindid", "");
	String appid = req.getString("appid", "");
	url = java.net.URLEncoder.encode(req.getString("url"), "UTF-8");
	dswork.common.model.IBind bind = dswork.sso.util.websso.WebssoUtil.get(bindid);
	dswork.common.model.IUnit unit = dswork.common.util.UnitUtil.get(appid);
	if(bind != null && unit != null && url.startsWith(unit.getReturnurl()))
	{
		//dswork.common.service.DsBaseUserService service = (dswork.common.service.DsBaseUserService)dswork.spring.BeanFactory.getBean("dsBaseUserService");
		boolean reg = "true".equals(req.getString("reg", req.getString("register", "false")));
		String type = req.getString("type");
		if("".equals(c))// 首次进入，需重定向至第三方登录
		{
			c = dswork.core.util.EncryptUtil.encryptMd5(date + bindid);
			String authorizeURL = "about:blank";
			String redirectURL = "%s%s";
			String return_url = "about:blank";
			return_url = java.net.URLEncoder.encode(bind.getAppreturnurl()+(bind.getAppreturnurl().indexOf("?")>0?"&":"?")+"appid="+appid+"&bindid="+bindid+"&c=" + (reg?c + "&reg=true":c) + (type.length()>0?"&type="+type:"") + "&url=" + url, "UTF-8");
			appid = bind.getAppid();
			if("weibo".equals(bind.getApptype()))
			{
				redirectURL = "https://api.weibo.com/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s";
			}
			else if(bind.getApptype().startsWith("wechat"))
			{
				if("wechat-qrcode".equals(bind.getApptype()))
				{
					redirectURL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=1#wechat_redirect";
				}
				else// wechat-mini || wechat
				{// scope=snsapi_base可获取用户openid且无需授权，但无法获取别的信息（如用户关注过，则可以获取别的信息）；已关注用户scope=snsapi_userinfo拉取用户信息也是无需授权的
					redirectURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo#wechat_redirect";
				}
			}
			else if("qq".equals(bind.getApptype()))
			{
				redirectURL = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=1&scope=get_user_info";
			}
			else if("alipay".equals(bind.getApptype()))
			{
				redirectURL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_user&redirect_uri=%s";
			}
			authorizeURL = String.format(redirectURL, appid, return_url);
			request.setAttribute("authorizeURL", authorizeURL);
			i = 1;
			if(!"qq".equals(bind.getApptype()))
			{
				response.sendRedirect(authorizeURL);
				return;
			}
		}
		else // 第三方授权回调
		{
			url = java.net.URLDecoder.decode(java.net.URLDecoder.decode(req.getString("url", ""), "UTF-8"), "UTF-8");// get方式传过来的
			String code = "";
			if("alipay".equals(bind.getApptype()))
			{
				code = req.getString("auth_code");
			}
			else
			{
				code = req.getString("code");
			}
			String data = req.getString("data");
			String iv = req.getString("iv");
			dswork.common.model.IUserBind po = dswork.sso.util.websso.WebssoUtil.getUserBind(bindid, code, data, iv);
			if(po != null)
			{
				po = dswork.common.SsoFactory.getSsoService().saveOrUpdateUserBind(po, reg, type);
				if("wechat-mini".equals(bind.getApptype()))// 微信小程序回调
				{
					dswork.common.model.IUser user = dswork.common.SsoFactory.getSsoService().getUserById(po.getUserid());
					String userjson = dswork.common.util.ResponseUtil.toJson(user);
					dswork.common.model.ZAuthtoken token = dswork.common.util.TokenUserUtil.tokenCreate(unit.getType(), String.valueOf(user.getId()), userjson);
					dswork.common.util.ResponseUtil.printJson(response, dswork.common.util.ResponseUtil.getJsonUserToken(token));// token只能同源获取
					return;
				}
				else
				{
					url += (url.indexOf("?") > 0 ? "&" : "?") + "appid=" + appid + "&bindid=" + bindid + "&bindtype=" + bind.getApptype();
					if(po.getUserid() != 0)
					{
						dswork.common.model.IUser user = dswork.common.SsoFactory.getSsoService().getUserById(po.getUserid());
						String userjson = dswork.common.util.ResponseUtil.toJson(user);
						dswork.common.model.ZAuthorizecode _code = dswork.common.util.TokenUserUtil.codeCreate(bind.getAppreturnurl(), userjson);// 获取code
						url += "&code=" + _code.getCode();
					}
					else
					{
						// 返回userbind
						// String userbind = dswork.core.util.EncryptUtil.encodeDes(String.valueOf(po.getId()), "userbind");
						// url += "&userbind=" + userbind;
						url += "&error=404";// 用户不存在
					}
					response.sendRedirect(url);
					return;
				}
			}
			//i = 2;
		}
	}
}
catch(Exception e)
{
	e.printStackTrace();
}
if(i == 0)
{
	response.setCharacterEncoding("UTF-8");
	response.setContentType("application/json;charset=UTF-8");
	response.setHeader("P3P", "CP=CAO PSA OUR");
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.getWriter().write("{\"code\":400}");
	return;
}
request.setAttribute("ctx", "/sso");
request.setAttribute("c", "#2a92eb");// #003c7b #b71d29 #125995 #d3880d #2a92eb%><!DOCTYPE html>
<html>
<head><meta charset="UTF-8" /><title>统一身份认证平台跳转</title>
<%if(i == 2){ %>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/share/fonts/dsworkfont.css"/>
<style type="text/css">
html,body{height:100%;margin:0px auto;}
body {background-color:#fff;font-family:tahoma,arial,"Microsoft YaHei","\5B8B\4F53",sans-serif;color:${c};font-size:16px;line-height:120%;}
i{font-family:dsworkfont;font-weight:normal;font-style:normal;}
.center-in-center{position:absolute;top:50%;left:50%;-webkit-transform:translate(-50%, -50%);-moz-transform:translate(-50%, -50%);-ms-transform:translate(-50%, -50%);-o-transform:translate(-50%, -50%);transform:translate(-50%, -50%);}

.view {overflow:hidden;margin:0 auto;width:100%;min-width:300px;max-width:1000px;overflow:hidden;padding:8px 0;}
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
.box .vbox span {border-radius:6px 0 0 6px;vertical-align:middle;height:48px;line-height:48px;background-color:${c};border:${c} 1px solid;font-size:24px;margin:0;padding:0 20px;display:inline-block;color:#fff;}
.box .button{background-color:${c};color:#fff;width:280px;height:50px;line-height:50px;cursor:pointer;border:none;border-radius:6px;-webkit-appearance:none;}
.box .button:hover{filter:alpha(opacity:80);opacity:0.8;}

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
</style>
<%} %>
</head>
<body>
<%if(i == 1){ %>
<script type="text/javascript">
var authorize = "${authorizeURL}";
function isPc(){return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)==null}
if(!isPc()){
	authorize = authorize + "&display=mobile";
}
location.href = authorize;
</script>
<%}else if(i == 2){ %>
<div class="view center-in-center">
  <div class="login">
	<div class="box boxname"><div class="name">是否绑定已有账号</div></div>
	<div class="box"><div class="vbox">
		<span><i>&#xf1001;</i></span><input type="text" id="account" name="account" autocomplete="off" class="input" value="" title="账号" placeholder="账号" />
	</div></div>
	<!-- <div class="box"><div class="vbox">
		<span><i>&#xf1002;</i></span><input type="password" id="password" name="password" autocomplete="off" class="input" value="" title="密码" placeholder="密码" />
	</div></div> -->
	<div class="box boxmsg" id="msgdiv"><div class="vbox">
		<div id="msg" class="msg"></div>
	</div></div>
	<div class="box">
		<input type="button" class="button" value="绑 定" onclick="doclick()" />
	</div>
	<div class="box"><div class="vbox link">
		<b class="left">不需要绑定账号，</b><a href="loginAction.jsp?id=${po.id}&service=${url}" class="left">直接登录</a>
	</div></div>
  </div>
</div>
<script type="text/javascript">
function doclick(){
	var account = _$("account").value;
	if(!account){show("请输入需要绑定的账号！");return false;}
	else{ajax({method: 'POST',url:"loginAction.jsp",data:{"id":"${po.id}","account":account,"bindid":"${bindid}"},success: function(response){var json= eval("("+response+")");if(json.code==1){countDown(json.data.code);}else{show("绑定失败！");}}});}
}
var count = 3;
function countDown(code){if(count==0){location.href = ("${url}" + ("${url}".indexOf("?") > 0 ? "&code=" : "?code=") + code);}show("绑定成功！" + (count--) +"秒后跳转页面");setTimeout("countDown(\""+code+"\")",1000);}
function ajax(opt){opt = opt || {};opt.method = opt.method.toUpperCase() || 'POST';opt.url = opt.url || '';opt.async = opt.async || true;opt.data = opt.data || null;opt.success = opt.success || function(){};var xmlHttp = null;if(XMLHttpRequest){xmlHttp = new XMLHttpRequest();}else{xmlHttp = new ActiveXObject('Microsoft.XMLHTTP');}var params = [];for(var key in opt.data){params.push(key + '=' + opt.data[key]);}var postData = params.join('&');if(opt.method.toUpperCase() === 'POST') {xmlHttp.open(opt.method, opt.url, opt.async);xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');xmlHttp.send(postData);}else if(opt.method.toUpperCase() === 'GET') {xmlHttp.open(opt.method, opt.url + '?' + postData, opt.async);xmlHttp.send(null);}xmlHttp.onreadystatechange = function (){if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {opt.success(xmlHttp.responseText);}};}
function show(msg){if(_$("msg")){_$("msg").innerHTML = (msg==""?"":"<i>&#xf1010;</i> "+msg+"<br>");}if(_$("msgdiv")){_$("msgdiv").style.display = (msg==""?"none":"block");}};
function _$(id){return document.getElementById(id);}
</script>
<%} %>
</body>
</html>
