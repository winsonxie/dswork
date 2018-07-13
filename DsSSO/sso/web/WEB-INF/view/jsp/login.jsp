<%@page contentType="text/html; charset=UTF-8"%><%
response.setHeader("Cache-Control","no-store");
response.setDateHeader("Expires", 0);
response.setHeader("Pragma","no-cache"); 
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
body {background-color:#fff;}
*{padding:0;margin:0;font-family:arial,"microsoft yahei","宋体";border-radius:0;}
div,input,a{font-weight:bold;font-size:20px;line-height:38px;}
div,input,label{color:#333;}
a{text-decoration:underline;outline:none;}
a:link,a:visited,a:active{color:#0000bb;outline:none;}
a:hover{color:#0000ff;text-decoration:underline;}
.bg{width:100%;height:730px;position:absolute;top:70px;left:0;z-index:-1;background:url(${ctx}/themes/share/bg/login.gif) no-repeat top center;}
.view{position:relative;width:100%;min-width:300px;max-width:1000px;overflow:hidden;margin:0 auto;overflow:hidden;}
.title {color:#003c7b;font-size:38px;font-weight:bold;text-align:center;padding:80px 0 38px 0;}
.title img{height:60px;vertical-align:middle;}
.login{border:#003c7b solid 1px;padding:0;overflow:hidden;background-color:#fff;float:right;margin:0 50px 0 auto;width:360px;}
.box{overflow:hidden;text-align:center;width:100%;margin:0 auto 12px auto;padding:0;border:none;}
.box .title{background-color:#003c7b;border-bottom:#003c7b solid 1px;color:#fff;width:100%;padding:3px 0;line-height:50px;font-size:22px;text-align:center;margin:0 auto;}
.box .vbox{margin:0 auto;padding:0;overflow:hidden;text-align:left;vertical-align:middle;width:250px;}
.box .vbox span{font-family:dsworkfont;margin:0 10px;color:#003c7b;}
.box .vbox input{width:198px;height:35px;padding-left:8px;vertical-align:middle;background-color:inherit;border:#ccc solid 1px;}
.box .vbox input.code{width:98px;}
.box .vbox img{border:none;cursor:pointer;vertical-align:middle;}
.box .button{background-color:#003c7b;color:#eee;width:280px;height:50px;line-height:50px;cursor:pointer;border:none;-webkit-appearance:none;}
.box .button:hover{background-color:#da3b01;color:#fff;}
.box .checkbox{vertical-align:middle;}
.box label{font-weight:bold;font-size:16px;line-height:18px;}
.box label.left{float:left;margin-left:38px;}
.box label.right{float:right;margin-right:38px;}
.box label a{font-size:16px;line-height:18px;text-decoration:none;}
.cp{color:#666;font-size:12px;width:80%;overflow:hidden;text-align:center;padding:15px 0;margin:20px auto 0 auto;border:none;}
.cp a {font-size:12px;font-weight:normal;font-family:arial;}

@media only screen and (min-width:768px) and (max-width:999px){.title{text-align:center;}}
@media only screen and (max-width:767px){.bg{background:none;}.title{font-size:22px;text-align:center;padding:40px 0 35px 0;}.login{float:none;margin:0 auto;}}
@media only screen and (max-width:480px){.bg{background:none;}.title{text-align:center;}.login{float:none;margin:0 auto;}}
@media only screen and (max-width:361px){.login{border-left:none;border-right:none;}}

</style>
</head>
<body>
<div class="bg"></div>
<div class="view">
  <div class="title">&nbsp;统一身份认证平台</div>
  <form id="w" action="loginAction" method="post">
  <div class="login">
	<div class="box"><div class="title">用户登录</div></div>
	<div class="box"><div class="vbox">
		<span>&#xf1001;</span><input type="text" id="account" name="account" autocomplete="off" value="" title="账号" placeholder="账号" />
	</div></div>
	<div class="box"><div class="vbox">
		<span>&#xf1002;</span><input type="password" id="password" name="password" autocomplete="off" value="" title="密码" placeholder="密码" />
	</div></div>
	<div class="box"><div class="vbox">
		<span>&#xf1026;</span><input type="text" id="authcode" name="authcode" autocomplete="off" maxlength="4" class="code" value="" title="验证码" placeholder="" />
		<img id="mycode" alt="请点击" style="width:90px;height:38px;" src="about:blank" onclick="this.src='${ctx}/authcode?r=' + Math.random();" />
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
	Copyright &copy; 2014-2017
</div>
<c:if test="${errorMsg != ''}"><script type="text/javascript">alert("${errorMsg}");</script></c:if>
<c:if test="${loginURL != ''}"><script type="text/javascript">location.href="${${fn:escapeXml(loginURL)}";</script></c:if>
</body>
<script type="text/javascript" src="${ctx}/js/jskey/jskey_md5.js"></script>
<script type="text/javascript">
function _$(id){return document.getElementById(id);}
var dd = document, cc = "coo" + "kie";
function setCoo(k,v,d){var x=new Date();x.setDate(x.getDate()+d);dd[cc]=k+"="+escape(v)+((d==null)?"":";expires="+x.toGMTString());}
function getCoo(k){if(dd[cc].length>0){var x1=dd[cc].indexOf(k+"=");if(x1!=-1){x1=x1+k.length+1;x2=dd[cc].indexOf(";",x1);if(x2==-1){x2=dd[cc].length;}return unescape(dd[cc].substring(x1,x2));}}return "";}
function doclick(){
	var s = "";
	if(!_$("account").value){s += "账号不能为空\n";}
	if(!_$("password").value){s += "密码不能为空\n";}
	if(!_$("authcode").value){s += "验证码不能为空\n";}
	if(s != ""){alert(s);return;}
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
function registKeydown(id){registEvent(_$(id), "keydown", function(event){if(event.keyCode == 13){doclick();}});}
registKeydown("account");
registKeydown("password");
registKeydown("authcode");
</script>
</html>
