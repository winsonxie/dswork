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
<title></title>
<style type="text/css">
*{word-wrap:break-word;}
html,body,p,form,input{margin:0;padding:0;}
ul,ol,dl{list-style-type:none;}
html,body{*position:static;}
html{font-family:sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;}
input{font-family:inherit;font-size:inherit;font-style:inherit;font-weight:inherit;}
input{overflow:visible;vertical-align:middle;outline:none;}
body,input{font-family:"Microsoft Yahei","Hiragino Sans GB","Helvetica Neue",Helvetica,tahoma,arial,Verdana,sans-serif,"\5B8B\4F53";font-size:14px;color:#333;-webkit-font-smoothing:antialiased;-moz-font-smoothing:antialiased;}
body{line-height:1.6;}
input.text{border:#c2c2c2 solid 1px;min-height:32px;width:300px;}
input.text:hover{border:#0192d0 solid 1px;}
input.button{border:none;background:none;line-height:1.5em;cursor:pointer;background-color:#003c7b;color:#fff;border-radius:3px;}
input.button:hover{background-color:#004ca3;}
.clearfix {clear:both;}

body{font-size:120%;line-height:2em;}
.ds {display:block;border:none;padding:0;margin:0;width:100%;height:60px;text-align:center;color:#ffffff;}
.ds_top {position:fixed;left:0;top:0;overflow:hidden;background-color:#003c7b;}
.ds_top .ds_title{margin:0 auto;font-size:24px;line-height:60px;color:#ffffff;font-weight:bold;}
.pt-5{padding-top:5px;}
div.fbox {width:720px;margin:0 auto;border:2px solid #ccc;margin-top:20px;}
.box {display:block;}
.box .top, .box .bottom{border:none;background-color:#eeeeee;text-align:center;padding:5px 0;}
.box .top    {border-bottom:2px solid #ccc;}
.box .bottom {border-top:2px solid #ccc;}
.box .left {display:block;width:168px;text-align:right;}
.box .right{display:block;width:550px;text-align:left;float:right;}
</style>
<script type="text/javascript" src="${ctx}/js/jskey/jskey_md5.js"></script>
<script type="text/javascript" src="${ctx}/js/jskey/jskey_des.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/share/fonts/dsworkfont.css"/>
<script type="text/javascript">
if(top.location != this.location){top.location = this.location;}
function _$(id){return document.getElementById(id);}
function doclick(){
	var s = "";
	if(!_$('oldpassword').value){s += "原密码不能为空\n";}
	if(!_$('password').value){s += "密码不能为空\n";}
	if(_$('password').value != _$('password2').value){s += "确认密码不一致\n";}
	if(!_$('authcode').value){s += "验证码不能为空\n";}
	if(s != ""){alert(s);return;}
	try{_$('oldpassword').value = $jskey.md5($jskey.md5(_$('oldpassword').value)+_$('authcode').value);}catch(e){}
	try{_$('password').value = $jskey.encodeDes(_$('password').value, _$('authcode').value);}catch(e){}
	_$('v').submit();
}
</script>
</head>
<body>
<div class="ds"></div>
<div class="ds ds_top">
	<div class="left"></div>
	<div class="right"></div>
	<div class="ds_title">统一身份认证平台</div>
</div>
<form id="v" method="post" action="passwordAction">
<div class="fbox">
	<div class="box clearfix">
		<div class="top">
			<div style="line-height:50px;text-align:center;">密码重置</div>
		</div>
	</div>
	<div class="box clearfix pt-5">
		<div class="right"><input class="text" type="text" name="account" style="width:250px;background-color:#eee;" readonly="readonly" value="${fn:escapeXml(account)}" /></div>
		<div class="left">账号：</div>
	</div>
	<div class="box clearfix pt-5">
		<div class="right"><input class="text" type="password" id="oldpassword" name="oldpassword" autocomplete="off" style="width:250px;" dataType="Require" maxlength="32" value="" /></div>
		<div class="left">原密码：</div>
	</div>
	<div class="box clearfix pt-5">
		<div class="right"><input class="text" type="password" id="password" name="password" autocomplete="off" style="width:250px;" dataType="Require" maxlength="32" value="" /></div>
		<div class="left">密码：</div>
	</div>
	<div class="box clearfix pt-5">
		<div class="right"><input class="text" type="password" id="password2" autocomplete="off" style="width:250px;" dataType="Repeat" to="password" msg="两次输入的密码不一致" value="" /></div>
		<div class="left">确认密码：</div>
	</div>
	<div class="box clearfix pt-5">
		<div class="right"><input type="text" class="text" id="authcode" name="authcode" style="width:100px;" dataType="RequireCompact" msg="必填" maxlength="4" value="" /><img id="mycode" alt="请点击" src="about:blank" style="cursor:pointer;vertical-align:middle;width:90px;height:32px;" onclick="this.src='${ctx}/authcode?r=' + Math.random();" /></div>
		<div class="left">验证码：</div>
	</div>
	<div class="box clearfix pt-5"></div>
	<div class="box clearfix pt-5">
		<div class="bottom">
			<input type="button" style="padding:10px 100px;" class="button" value="提交" onclick="doclick()" />
		</div>
	</div>
</div>
  <input type="hidden" name="service" value="${fn:escapeXml(service)}" />
  <input type="hidden" name="loginURL" value="${fn:escapeXml(loginURL)}" />
</form>
</body>
<script type="text/javascript">
<c:if test="${errorMsg != ''}">alert("${errorMsg}");</c:if>
_$('mycode').click();
</script>
</html>
