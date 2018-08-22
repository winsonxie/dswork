<%@page contentType="text/html; charset=UTF-8"%><%
response.setHeader("Cache-Control","no-store");
response.setDateHeader("Expires", 0);
response.setHeader("Pragma","no-cache"); 
request.setAttribute("c", "#2A92EB");// #003c7b #71d29 #125995 #d3880d #2A92EB
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><c:set var="ctx" value="${pageContext.request.contextPath}"
/><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
<title>统一身份认证平台</title>
<script type="text/javascript">if(top.location != this.location){top.location = this.location;}</script>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/share/fonts/dsworkfont.css"/>
<style type="text/css">
html,body{height:100%;margin:0px auto;}
body {background-color:#fff;font-family:arial,"microsoft yahei","宋体";color:${c};font-size:16px;line-height:120%;}
a,a:link,a:visited,a:active{color:${c};text-decoration:underline;outline:none;}
a:hover{filter:alpha(opacity:50);opacity:0.9;}
i{font-family:dsworkfont;font-weight:normal;font-style:normal;}

.view {overflow:hidden;margin:0 auto;width:100%;min-width:300px;max-width:1000px;overflow:hidden;padding:8px 0;}
.title,
.view .title{font-weight:bold;text-align:center;font-size:32px;line-height:40px;padding:38px 0px;}
.view .login{margin:0 auto;padding:0;width:360px;max-width:360px;border:${c} solid 0px;overflow:hidden;background-color:#fff;box-shadow:0 0 8px 0px ${c};}

.boxmsg{padding:0;display:none;}
.boxname{padding:0;}
.box{overflow:hidden;text-align:center;width:100%;margin:0 auto;padding:8px 0;border:none;}
.box .name{background-color:#fff;width:100%;padding:23px 0 8px 0;margin:0 auto;font-size:22px;line-height:22px;text-align:center;font-weight:normal;}
.box .errmsg{color:#ff0000;line-height:25px;}
.box .vbox{margin:0 auto;padding:0;overflow:hidden;text-align:left;vertical-align:top;width:275px;}
.box .vbox .input{border-radius:0 6px 6px 0;vertical-align:middle;height:48px;line-height:48px;background-color:#edf2f6;border:#d6e5ef 1px solid;border-left:none;width:194px;outline:none;padding:0 0 0 12px;}
.box .vbox .input:focus{border:${c} 1px solid;border-left:none;}
.box .vbox .input::placeholder{color:#999;}
.box .vbox .nolabel{border:#d6e5ef 1px solid;border-radius:6px;margin:0 0 0 66px;}
.box .vbox .nolabel:focus{border:${c} 1px solid;}
.box .vbox .code{border:#d6e5ef 1px solid;border-radius:6px;width:120px;margin:0 12px 0 0;}
.box .vbox .code:focus{border:${c} 1px solid;}
.box .vbox span {border-radius:6px 0 0 6px;vertical-align:middle;height:48px;line-height:48px;background-color:${c};border:${c} 1px solid;font-size:24px;margin:0;padding:0 20px;display:inline-block;color:#fff;}
.box .vbox img{width:120px;height:46px;border:none;cursor:pointer;vertical-align:middle;}
.box .button{background-color:${c};color:#fff;width:280px;height:50px;line-height:50px;cursor:pointer;border:none;border-radius:6px;-webkit-appearance:none;}
.box .button:hover{filter:alpha(opacity:50);opacity:0.9;}
.box .checkbox{vertical-align:middle;}

.box label{font-weight:bold;font-size:16px;line-height:18px;}
.box label.left{float:left;margin-left:38px;}
.box label.right{float:right;margin-right:38px;}
.box label a{font-size:16px;line-height:18px;text-decoration:none;}

.cp{color:#666;font-size:12px;width:80%;overflow:hidden;text-align:center;padding:15px 0;margin:20px auto 0 auto;border:none;}
.cp a {font-size:12px;font-weight:normal;font-family:arial;}

.title{background-color:${c};color:#fff;margin:0 0 40px 0;}
.view .title{background-color:inherit;color:${c};margin:0;}

/*
.bg{width:100%;height:730px;position:absolute;top:110px;left:0;z-index:-1;background:url(${ctx}/themes/share/bg/login.gif) no-repeat top center;}
.view .login{float:right;margin:0 47px 0 auto;}
*/
body {background:#fff url(${ctx}/themes/share/bg/wave.png) bottom center repeat-x;}
</style>
<style type="text/css">
@media only screen and (max-width:999px){.title{text-align:center;}}
@media only screen and (max-width:767px){body {background:#fff;}.title{font-size:28px;padding:30px 0px;}.view .login{float:none;margin:0 auto;}}
@media only screen and (max-width:480px){.bg{background:none;}.boxname{display:none;}.title{font-size:24px;line-height:30px;margin:0 0 16px 0;}.view .title{padding:30px 0px 20px 0;margin:0;}.view .login{float:none;margin:0 auto;width:100%;border-left:none;border-right:none;box-shadow:none;}}
@media only screen and (max-width:361px){.title{font-size:20px;}.box .name{font-size:20px;}}
</style>
</head>
<body>
<div class="bg"></div>
<div class="title">&nbsp;统一身份认证平台</div>
<div class="view">
  <form id="w" action="loginAction" method="post">
  <div class="login">
	<div class="box boxname"><div class="name">用户登录</div></div>
	<div class="box"><div class="vbox">
		<span><i>&#xf1001;</i></span><input type="text" id="account" name="account" autocomplete="off" class="input" value="" title="账号" placeholder="账号" />
	</div></div>
	<div class="box"><div class="vbox">
		<span><i>&#xf1002;</i></span><input type="password" id="password" name="password" autocomplete="off" class="input" value="" title="密码" placeholder="密码" />
	</div></div>
	<div class="box boxmsg" id="errmsgdiv"><div class="vbox">
		<div id="errmsg" class="errmsg"></div>
	</div></div>
	<div class="box"><div class="vbox">
		<input type="text" id="authcode" name="authcode" autocomplete="off" maxlength="4" class="input code" value="" title="验证码" placeholder="验证码" />
		<img id="mycode" alt="请点击" src="about:blank" onclick="this.src='${ctx}/authcode?r=' + Math.random();" />
	</div></div>
	<div class="box">
		<input type="button" class="button" value="登 录" onclick="doclick()" />
	</div>
	<div class="box">
		<label class="right">&nbsp;&nbsp;<input id="savename" type="checkbox" autocomplete="off" class="checkbox" onclick="">&nbsp;记住用户名&nbsp;</label>
	</div>
  </div>
  <input type="hidden" name="service" value="${fn:escapeXml(service)}" />
  </form>
</div>
<div class="cp">
	&copy; 2014-2018 249725997@qq.com
</div>
</body>
<script type="text/javascript" src="${ctx}/js/jskey/jskey_md5.js"></script>
<script type="text/javascript">
function _$(id){return document.getElementById(id);}
var dd = document, cc = "coo" + "kie";
function setCoo(k,v,d){var x=new Date();x.setDate(x.getDate()+d);dd[cc]=k+"="+escape(v)+((d==null)?"":";expires="+x.toGMTString());}
function getCoo(k){if(dd[cc].length>0){var x1=dd[cc].indexOf(k+"=");if(x1!=-1){x1=x1+k.length+1;x2=dd[cc].indexOf(";",x1);if(x2==-1){x2=dd[cc].length;}return unescape(dd[cc].substring(x1,x2));}}return "";}
function _uncheck(id){
	var s = "";
	if(id == "account"){if(!_$(id).value){s="账号";}}
	if(id == "password"){if(!_$(id).value){s="密码";}}
	if(id == "authcode"){if(!_$(id).value){s="验证码";}}
	if(s != ""){s = "<i>&#xf1010;</i> "+s+"不能为空<br>";_$("errmsg").innerHTML = s;_$("errmsgdiv").style.display = "block";return true;}else{_$("errmsgdiv").style.display= "none";return false;}
}
function doclick(){
	if(_uncheck("account") || _uncheck("password") || _uncheck("authcode")){return;}
	if(_$("savename").checked){setCoo("savename",_$("account").value,365);}else{setCoo("savename","",0);}
	try{_$("password").value = $jskey.md5($jskey.md5(_$("password").value)+_$("authcode").value);}catch(e){}
	_$("w").submit();
}
_$("mycode").click();
var _x = getCoo("savename");
if(_x.length > 0){
	_$("account").value = _x;
	_$("savename").checked = true;
}else {
	_$("account").value = "";
	_$("savename").checked = false;
}
_$("password").value = "";
_$("authcode").value = "";
_$((_$("account").value == "")?"account":"password").focus();

function registEvent($e, et, fn){$e.attachEvent ? $e.attachEvent("on"+et, fn) : $e.addEventListener(et, fn, false);}
function registKey(id){registEvent(_$(id), "keydown", function(event){if(event.keyCode == 13){doclick();}});registEvent(_$(id), "keyup", function(event){_uncheck(this.getAttribute("id"));});}
registKey("account");
registKey("password");
registKey("authcode");
<c:if test="${errorMsg != ''}">
_$("errmsg").innerHTML = "${errorMsg}";
_$("errmsgdiv").style.display = "block";
</c:if>
</script>
<c:if test="${fn:length(loginURL)>0}"><script type="text/javascript"><c:if test="${errorMsg != ''}">alert("${errorMsg}");</c:if>location.href="${fn:escapeXml(loginURL)}";</script></c:if>
</html>
