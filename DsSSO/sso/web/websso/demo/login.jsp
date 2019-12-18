<%@page contentType="text/html; charset=UTF-8"%><%
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
<title>统一身份认证平台</title>
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
body {background:#fff url(/sso/themes/share/bg/wave.png) bottom center repeat-x;}
.fieldset a {margin:0 10px;}
</style>
<link rel="stylesheet" type="text/css" href="${ctx}/themes/ssomedia.css"/>
</head>
<body>
<div class="bg"></div>
<div class="title">&nbsp;统一身份认证平台</div>
<div class="view">
	<div class="box"><div class="vbox">
		<fieldset class="fieldset">
			<legend align="center" class="legend">其他方式登录</legend>
				<%
				String loginURL = java.net.URLEncoder.encode(java.net.URLEncoder.encode("/sso/websso/demo/success.jsp", "UTF-8"), "UTF-8");
				request.setAttribute("loginURL", java.net.URLEncoder.encode(loginURL, "UTF-8"));
				%>
				<a href="${ctx}/websso/login.jsp?bindid=1&bindtype=qq&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(loginURL)}" class="icon_qq" title="QQ登录"></a>
				<a href="${ctx}/websso/login.jsp?bindid=2&bindtype=weibo&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(loginURL)}" class="icon_weibo" title="微博登录"></a>
				<a href="${ctx}/websso/login.jsp?bindid=3&bindtype=alipay&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(loginURL)}" class="icon_alipay" title="支付宝登录"></a>
				<a href="${ctx}/websso/login.jsp?bindid=6&bindtype=wechat-qrcode&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(loginURL)}" class="icon_wechat" title="微信扫码登录"></a>
				<a href="${ctx}/websso/login.jsp?bindid=4&bindtype=wechat&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(loginURL)}" class="icon_wechat" title="微信公众号登录"></a>
		</fieldset>
	</div></div>
	<div class="box"><div class="vbox">
		<fieldset class="fieldset">
			<legend align="center" class="legend">其他方式注册</legend>
				<%
				String registerURL = java.net.URLEncoder.encode(java.net.URLEncoder.encode("/sso/websso/demo/success.jsp", "UTF-8"), "UTF-8");
				request.setAttribute("registerURL", java.net.URLEncoder.encode(registerURL, "UTF-8"));
				%>
				<a href="${ctx}/websso/login.jsp?bindid=1&reg=true&bindtype=qq&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(registerURL)}" class="icon_qq" title="QQ注册"></a>
				<a href="${ctx}/websso/login.jsp?bindid=2&reg=true&bindtype=weibo&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(registerURL)}" class="icon_weibo" title="微博注册"></a>
				<a href="${ctx}/websso/login.jsp?bindid=3&reg=true&bindtype=alipay&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(registerURL)}" class="icon_alipay" title="支付宝注册"></a>
				<a href="${ctx}/websso/login.jsp?bindid=6&reg=true&bindtype=wechat-qrcode&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(registerURL)}" class="icon_wechat" title="微信扫码注册"></a>
				<a href="${ctx}/websso/login.jsp?bindid=4&reg=true&bindtype=wechat&appid=CA1DB36591314A59EB5B449EF61D03E6&url=${fn:escapeXml(registerURL)}" class="icon_wechat" title="微信公众号注册"></a>
		</fieldset>
	</div></div>
</div>
<div class="cp">
	&copy; 249725997@qq.com
</div>
</body>
</html>
