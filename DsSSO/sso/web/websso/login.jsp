<%@page language="java" pageEncoding="UTF-8"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%!
// 获取当前会话的sso账户
public dswork.common.model.IUser getCurrLoginUser(long appkey, HttpServletRequest request)
{
	dswork.common.model.IUser user = null;
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
			String userinfoJSON = dswork.common.util.TokenUserUtil.tokenGet(appkey, openid, access_token);
			if(userinfoJSON != null)
			{
				user = dswork.common.util.ResponseUtil.toBean(userinfoJSON, dswork.common.model.IUser.class);
			}
		}
	}
	return user;
}
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
		if("".equals(c))// 首次进入，需重定向至第三方登录
		{
			c = dswork.core.util.EncryptUtil.encryptMd5(date + bindid);
			String authorizeURL = "about:blank";
			String redirectURL = "%s%s";
			String return_url = "about:blank";
			return_url = java.net.URLEncoder.encode(
					bind.getAppreturnurl() + (bind.getAppreturnurl().indexOf("?")>0?"&":"?")
					+ "appid=" + appid + "&bindid=" + bindid + "&c=" + c + (reg?"&reg=true":"") + "&url=" + url
			, "UTF-8");
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
			dswork.common.model.IUserBind po = null;
			if("wechat-app".equals(bind.getApptype()))
			{
				po = dswork.sso.util.websso.WebssoUtil.getUserBind(bindid, req.getString("accessToken", null), req.getString("openid", null), null);
			}
			else
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
				po = dswork.sso.util.websso.WebssoUtil.getUserBind(bindid, code, data, iv);
			}
			
			if(po != null)
			{
				dswork.common.model.IUser bindUser = null;
				// TODO 再此可获取当前登录的用户，绑定已登录的用户
				bindUser = getCurrLoginUser(unit.getType(), request);
				po = dswork.common.SsoFactory.getSsoService().saveOrUpdateUserBind(po, reg, bindUser);
				if(po.getUserid() != 0)
				{
					dswork.common.model.IUser user = dswork.common.SsoFactory.getSsoService().getUserById(po.getUserid());
					if("wechat-mini".equals(bind.getApptype()) || "wechat-app".equals(bind.getApptype()))// 微信小程序或APP
					{
						String userjson = dswork.common.util.ResponseUtil.toJson(user);
						dswork.common.model.ZAuthtoken token = dswork.common.util.TokenUserUtil.tokenCreate(unit.getType(), String.valueOf(user.getId()), userjson);
						dswork.common.util.ResponseUtil.printJson(response, dswork.common.util.ResponseUtil.getJsonUserToken(token));
						return;
					}
					else
					{
						url += (url.indexOf("?") > 0 ? "&" : "?") + "appid=" + appid + "&bindid=" + bindid + "&bindtype=" + bind.getApptype() + "&userbind=" + po.getId();
						String userjson = dswork.common.util.ResponseUtil.toJson(user);
						dswork.common.model.ZAuthorizecode _code = dswork.common.util.TokenUserUtil.codeCreate(bind.getAppreturnurl(), userjson);// 获取code
						url += "&code=" + _code.getCode();
					}
				}
				else
				{
					if("wechat-mini".equals(bind.getApptype()) || "wechat-app".equals(bind.getApptype()))// 微信小程序或APP
					{
						dswork.common.util.ResponseUtil.printJson(response, "{\"code\":404}");
						return;
					}
					else
					{
						url += (url.indexOf("?") > 0 ? "&" : "?") + "appid=" + appid + "&bindid=" + bindid + "&bindtype=" + bind.getApptype() + "&userbind=" + po.getId();
						url += "&error=404";// 用户不存在
					}
				}
				response.sendRedirect(url);
				return;
			}
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
<%} %>
</body>
</html>
