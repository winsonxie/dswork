<%@page contentType="text/html; charset=UTF-8"%><%
request.setAttribute("use", dswork.sso.controller.UserController.useSMS);
response.setDateHeader("Expires", 0);
response.setHeader("Cache-Control","no-store");
response.setHeader("Pragma","no-cache"); 
request.setAttribute("c", "#2a92eb");// #003c7b #b71d29 #125995 #d3880d #2a92eb
request.setAttribute("ctx", request.getContextPath());
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<title>用户登录</title>
<script type="text/javascript">if(top.location != this.location){top.location = this.location;}</script>
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
.view .login{margin:0 auto;padding:0 0 8px 0;width:360px;max-width:360px;border-radius:5px;border:${c} solid 0px;overflow:hidden;background-color:#fff;box-shadow:0 0 8px 0px ${c};}

.boxmsg{padding:0;display:none;}
.boxname{padding:0;}
.box{overflow:hidden;text-align:center;width:100%;margin:0 auto;padding:8px 0;border:none;}
.box .name{background-color:#fff;width:100%;padding:16px 0 8px 0;margin:0 auto;font-size:22px;line-height:22px;text-align:center;font-weight:normal;}
.box .msg{color:#ff0000;line-height:25px;}
.box .vbox{margin:0 auto;padding:0;overflow:hidden;text-align:left;vertical-align:top;width:275px;}
.box .vbox .input{border-radius:0 6px 6px 0;vertical-align:middle;height:48px;line-height:48px;border:#d6e5ef 1px solid;border-left:none;width:194px;outline:none;padding:0 0 0 12px;}
.box .vbox .input:focus{border-color:${c};}
.box .vbox .input::placeholder{color:#999;}
.box .vbox .mobile{border-radius:0 6px 6px 0;vertical-align:middle;height:48px;line-height:48px;;border:#d6e5ef 1px solid;border-left:none;width:174px;outline:none;padding:0 0 0 12px;}
.box .vbox .code{border:#d6e5ef 1px solid;border-radius:6px;width:120px;margin:0 12px 0 0;}
.box .vbox .code:focus{border-color:${c};}
.box .vbox span {border-radius:6px 0 0 6px;vertical-align:middle;height:48px;line-height:48px;background-color:${c};border:${c} 1px solid;font-size:24px;margin:0;padding:0 20px;display:inline-block;color:#fff;}
.box .vbox span.sms{border-radius:0 6px 6px 0;font-size:18px;cursor:pointer;}
.box .vbox span.sms[disabled] {background-color:gray;border:gray 1px solid;font-size:16px;cursor:wait;}
.box .vbox img{width:120px;height:46px;border:none;cursor:pointer;vertical-align:middle;}
.box .vbox .smscode{border:#d6e5ef 1px solid;border-radius:6px 0 0 6px;width:120px;}
.box .vbox .smscode:focus{border-color:${c};}
.box .vbox .authcode {border-radius:0 6px 6px 0;vertical-align:middle;height:48px;line-height:48px;background-color:${c};border:${c} 1px solid;font-size:18px;margin:0;padding:0 20px;display:inline-block;color:#fff;cursor:pointer;}
.box .button{background-color:${c};color:#fff;width:280px;height:50px;line-height:50px;cursor:pointer;border:none;border-radius:6px;-webkit-appearance:none;font-size: 18px;}
.box .button:hover{filter:alpha(opacity:80);opacity:0.8;}

.box .left{float:left;}
.box .right{float:right;}
.box .changeLogin{cursor:pointer;}
.box .link a,
.box .link a:link,
.box .link a:visited,
.box .link a:active,
.box .link b{color:${c};font-size:12px;text-decoration:none;outline:none;}
.box .link b{margin:0 5px;}
.box .link a:hover{filter:alpha(opacity:80);opacity:0.8;text-decoration:none;cursor:pointer}

.cp{color:#666;font-size:12px;width:80%;overflow:hidden;text-align:center;padding:15px 0;margin:20px auto 0 auto;border:none;border-top:solid #ccc 1px;}
.cp a,
.cp a:active{font-size:12px;font-weight:normal;font-family:arial;color:${c};text-decoration:underline;outline:none;}
</style>
<style type="text/css">
body {background:#fff url(${ctx}/themes/share/bg/wave.png) bottom center repeat-x;}
</style>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/ssomedia.css"/>
</head>
<body>
<div class="bg"></div>
<div class="title">&nbsp;统一身份认证平台</div>
<div class="view">
  <div class="login">
	<div class="box boxname"><div class="name">用户登录</div></div>
	<div class="box boxmsg" id="msgdiv"><div class="vbox">
		<div id="msg" class="msg"></div>
	</div></div>
	<div class="box"><div class="vbox" id="divaccount">
		<span><i>&#xf1001;</i></span><input type="text" id="account" name="account" autocomplete="off" class="input" value="" tip="账号不能为空" placeholder="账号" />
	</div></div>
	<div class="box"><div class="vbox" id="divpassword">
		<span><i>&#xf1002;</i></span><input id="password" type="password" name="password" autocomplete="off" class="input" value="" tip="密码不能为空" placeholder="密码" />
	</div></div>
	<%--
	<div class="box"><div class="vbox">
		<input type="text" id="authcode" name="authcode" autocomplete="off" maxlength="4" class="input smscode" value="" tip="验证码不能为空" placeholder="验证码" />
		<img id="mycode" alt="请点击" src="about:blank" onclick="this.src='${ctx}/authcode?r=' + Math.random();" />
	</div></div>
	--%>
	<div class="box">
		<input type="button" class="button" id="submit" value="登录" onclick="doclick()" />
	</div>
<c:if test="${use}">
	<div class="box"><div class="vbox link">
		<label class="left changeLogin" id="changeLogin" onclick="changeLogin();">手机短信登录</label>
	</div></div>
</c:if>
	<div class="box"><div class="vbox link">
		<label class="right"><input id="savename" type="checkbox" autocomplete="off" class="checkbox" onclick="">&nbsp;记住用户名</label>
		<%-- 
		<a href="${ctx}/user/register.jsp?appid=${fn:escapeXml(param.appid)}" class="left">立即注册</a><b class="left">|</b><a href="${ctx}/user/update/reset.jsp?appid=${fn:escapeXml(param.appid)}" class="left">忘记密码?</a>
		 --%>
	</div></div>
	<%--
	<div class="box"><div class="vbox">
		<fieldset class="fieldset">
			<legend align="center" class="legend">其他方式登录</legend>
			<a onclick="return false;" href="#" class="icon_qq"></a><a onclick="return false;" href="#" class="icon_weibo"></a><a onclick="return false;" href="#" class="icon_alipay"></a><a onclick="return false;" href="#" class="icon_wechat"></a>
		</fieldset>
	</div></div>
	--%>
  </div>
</div>
<%--
<div class="cp">
	版权所有 &copy; 249725997@qq.com
</div>--%>
<input type="hidden" id="logintype" value="0" />
</body>
<script type="text/javascript" src="${ctx}/js/jskey/jskey_sso.js?version=2"></script>
<script type="text/javascript">
<c:if test="${!use}">
initAccount();
</c:if>
<c:if test="${use}">
function changeLogin(){
	var o = _$("logintype").value;
	if(o == 0){
		o = 1;
		_$("changeLogin").innerText="用户名密码登录";
		_$("divaccount").innerHTML='<span>+86</span><input type="text" id="account" name="account" autocomplete="off" class="input mobile" value="" tip="手机号码错误" placeholder="手机号码" />';
		_$("divpassword").innerHTML='<input type="text" id="password" name="password" autocomplete="off" class="input smscode" value="" tip="短信验证码不能为空" placeholder="短信验证码" /><span class="sms" onclick="getSmsCode(this);">获取验证码</span>';
		registKey("account");
		registKey("password");
	}else{
		o = 0;
		_$("changeLogin").innerText="手机短信登录";
		_$("divaccount").innerHTML='<span><i>&#xf1001;</i></span><input type="text" id="account" name="account" autocomplete="off" class="input" value="" tip="账号不能为空" placeholder="账号" />';
		_$("divpassword").innerHTML='<span><i>&#xf1002;</i></span><input id="password" type="password" name="password" autocomplete="off" class="input" value="" tip="密码不能为空" placeholder="密码" />';
		registKey("account");
		registKey("password");
	}
	_$("logintype").value = o;
	initAccount();
}
function getSmsCode(obj){
	if(!obj.getAttribute("disabled")){
		wait=60;
		var mobile = _$("account").value;
		if(checkMobile(mobile)){
			ajax({method:"POST",url:"${ctx}/sms/code",
			    data:{"mobile":mobile,"appid":"${fn:escapeXml(param.appid)}"},
			    success: function(response) {
			    	var json= eval("("+response+")");
			    	if(json.code==1){time(obj);}else{$jskey.sso.show({code:"4060"});}
			    }
			});
		}
	}
}
// changeLogin();默认是账号密码，是否切换为短信
</c:if>
var rsa = new $jskey.rsa();
rsa.setPublicKey("<%=dswork.common.util.CodeUtil.publicKey%>");
function doclick(){
	var type = _$("logintype").value==1?1:0;
	<c:if test="${!use}">type=0;</c:if>
	if(docheck()){return;}
	ajax({
	    method: 'POST',
	    url:"${loginURI}",
	    data: {
			"grant_type":type==0?"password":"sms",
			"account":_$("account").value,
			"password":type==0 ? rsa.encrypt($jskey.md5(_$("password").value)) : _$("password").value
	    },
	    success:function(response){
	    	var json= eval("("+response+")");
	    	if(json.code == 1){
	    		window.location.href="${ctx}/user/redirect?appid=${fn:escapeXml(param.appid)}&code="+json.data.code;
			}else{
				$jskey.sso.show(json);
			}
	    }
	});
}
</script>
</html>
