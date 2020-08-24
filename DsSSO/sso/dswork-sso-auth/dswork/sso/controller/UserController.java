package dswork.sso.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dswork.common.SsoFactory;
import dswork.common.model.IUnit;
import dswork.common.model.IUser;
import dswork.common.model.ZAuthorizecode;
import dswork.common.model.ZAuthtoken;
import dswork.common.util.CodeUtil;
import dswork.common.util.ResponseUtil;
import dswork.common.util.TokenSmsUtil;
import dswork.common.util.TokenUserUtil;
import dswork.common.util.UnitUtil;
import dswork.core.util.EncryptUtil;
import dswork.web.MyRequest;

@Controller
@RequestMapping("/user")
public class UserController
{
	/**
	 * 设置是否已开启短信模块
	 */
	public static boolean useSMS = false;

	/**
	 * 根据code获取token
	 * @throws IOException
	 */
	@RequestMapping("/access_token")
	public void access_token(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		String appsecret = req.getString("appsecret");
		String grant_type = req.getString("grant_type");
		String authorizecode = req.getString("code");
		if("".equals(appid) || "".equals(appsecret) || !"authorization_code".equals(grant_type) || "".equals(authorizecode))
		{
		}
		else
		{
			try
			{
				IUnit unit = UnitUtil.get(appid);
				if(null == unit || !appid.equals(unit.getAppid()) || !appsecret.equals(unit.getAppsecret())) // 校验appid和appsecret
				{
					ResponseUtil.printJson(response, CodeUtil.CODE_400);
				}
				else
				{
					ZAuthorizecode code = TokenUserUtil.codeGet(authorizecode, true);// 根据code获取token
					if(code != null)
					{
						IUser user = ResponseUtil.toBean(code.getValue(), IUser.class);// 根据token获取用户
						if(user != null)
						{
							ZAuthtoken token = TokenUserUtil.tokenCreate(unit.getType(), String.valueOf(user.getId()), code.getValue());
							ResponseUtil.printJson(response, ResponseUtil.getJsonUserToken(token));
							return;
						}
					}
					ResponseUtil.printJson(response, CodeUtil.CODE_406);
				}
				return;
			}
			catch(Exception e)
			{
			}
		}
		ResponseUtil.printJson(response, CodeUtil.CODE_400);
	}

	@RequestMapping("/auth_token")
	public void auth_token(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		String openid = req.getString("openid");
		String token = req.getString("access_token");
		IUnit unit = UnitUtil.get(appid);
		if(null == unit || "".equals(token) || "".equals(openid) || "".equals(openid))
		{
			ResponseUtil.printJson(response, CodeUtil.CODE_400);
		}
		else
		{
			if(TokenUserUtil.tokenGet(unit.getType(), openid, token).length() > 0)
			{
				ResponseUtil.printJson(response, CodeUtil.CODE_001);// 可考虑增加返回还有多久过期
			}
			else
			{
				ResponseUtil.printJson(response, CodeUtil.CODE_406);
			}
		}
	}

	/**
	 * 授权登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/authorize", method = RequestMethod.GET)
	public String authorize(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String msg = "";
		String appid = req.getString("appid");
		// String response_type = req.getString("response_type");
		String redirect_uri = req.getString("redirect_uri", req.getString("service"));
		String state = req.getString("state");
		// String scope = req.getString("scope");
		String param = request.getQueryString();
		if(param == null)
		{
			msg = CodeUtil.CODE_400_RESPONSE_TYPE;// 参数不正确
		}
		else if(!param.contains("appid=") || !param.contains("response_type=code") || !param.contains("redirect_uri="))
		{
			msg = CodeUtil.CODE_400_RESPONSE_TYPE;// 参数不正确
		}
		else
		{
			IUnit unit = UnitUtil.get(appid);
			if(null != unit)
			{
				if(redirect_uri.startsWith(unit.getReturnurl()))// 前缀匹配redirect_uri
				{
				}
				else
				{
					msg = CodeUtil.CODE_400_REDIRECT_URI;// 参数不正确
				}
			}
			else
			{
				msg = CodeUtil.CODE_400_APPID;// 参数不正确
			}
		}
		if(msg.length() == 0)
		{
			String loginURI = new StringBuilder()//
					.append(request.getContextPath())//
					.append("/user/login?appid=").append(appid)//
					.append("&response_type=code")//
					.append("&redirect_uri=").append(ResponseUtil.getEncodeURL(redirect_uri))//
					.append("&state=").append(ResponseUtil.getEncodeURL(state))//
					// .append("&scope").append(ResponseUtil.getEncodeURL(scope))
					.toString();
			request.setAttribute("loginURI", loginURI);
		}
		else
		{
			request.setAttribute("loginURI", "about:blank");
		}
		request.setAttribute("state", state);
		request.setAttribute("msg", msg);
		return "/auth/authorize.jsp";
	}

	/**
	 * 授权登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			MyRequest req = new MyRequest(request);
			String msg = "";
			String appid = req.getString("appid");
			String response_type = req.getString("response_type");// code|token
			String grant_type = req.getString("grant_type");// password|sms
			String bm = req.getString("account").trim().toLowerCase(Locale.ENGLISH);
			String password = req.getString("password");// rsa(md5(password)), publicKey加密
			IUnit unit = null;
			if("".equals(bm) || "".equals(password))
			{
				msg = CodeUtil.CODE_400;// 参数不正确
			}
			else if(!("token".equals(response_type) || "code".equals(response_type)))
			{
				msg = CodeUtil.CODE_400_RESPONSE_TYPE;// 参数不正确
			}
			else if(!("password".equals(grant_type) || "sms".equals(grant_type)))
			{
				msg = CodeUtil.CODE_400_GRANT_TYPE;// 参数不正确
			}
			else
			{
				unit = UnitUtil.get(appid);
				if(null == unit)
				{
					msg = CodeUtil.CODE_400_APPID;
				}
				else
				{
					IUser loginUser = SsoFactory.getSsoService().getUserByBm(bm);
					if(null == loginUser)
					{
						msg = CodeUtil.CODE_404;// 用户不存在
					}
					else if(null != loginUser && 1 != loginUser.getStatus())
					{
						msg = CodeUtil.CODE_403;// 用户被禁用
					}
					else
					{
						if("password".equals(grant_type))
						{
							if(!loginUser.getPassword().equals(EncryptUtil.decryptRSA(password, CodeUtil.privateKey).toUpperCase(Locale.ENGLISH)))
							{
								msg = CodeUtil.CODE_402;// 密码不正确
							}
						}
						else
						{
							if(!password.equals(TokenSmsUtil.smscodeGet(appid + bm)))// 校验短信验证码
							{
								msg = CodeUtil.CODE_406;// 动态密码，即短信验证码不正确
								// if("CA1DB36591314A59EB5B449EF61D03E6".equals(appid) && "135246".equals(password))
								// {
								// msg = "";
								// }
							}
						}
					}
					// if(null == loginUser && "true".equals(req.getString("reg")) && "sms".equals(grant_type))
					// {
					// if(password.equals(TokenSmsUtil.smscodeGet(appid + account)) ||
					// ("CA1DB36591314A59EB5B449EF61D03E6".equals(appid) && "135246".equals(password)))// 校验短信验证码
					// {
					// IUser _u = new IUser();
					// _u.setAccount(account);
					// _u.setMobile(account);
					// _u.setStatus(1);
					// SsoFactory.getSsoService().saveUser(_u);
					// loginUser = SsoFactory.getSsoService().getUserByAccount(_u.getAccount());
					// msg = "";
					// }
					// else
					// {
					// msg = CodeUtil.CODE_406;// 动态密码，即短信验证码不正确
					// }
					// }
					if(msg.length() == 0)
					{
						boolean type = "code".equals(response_type);
						String userjson = ResponseUtil.toJson(loginUser);
						if(type)
						{
							String redirect_uri = req.getString("redirect_uri", req.getString("service"));
							String state = req.getString("state");
							if(state.length() > 0)
							{
								String p = (redirect_uri.contains("?") ? "&state=" : "?state=") + ResponseUtil.getEncodeURL(state);
								int i = redirect_uri.indexOf("#");
								if(i > 0)
								{
									redirect_uri = redirect_uri.substring(0, i) + p + redirect_uri.substring(i, redirect_uri.length());
								}
								else
								{
									redirect_uri = redirect_uri + p;
								}
							}
							if(!redirect_uri.startsWith(unit.getReturnurl()))
							{
								msg = CodeUtil.CODE_400_REDIRECT_URI;
								ResponseUtil.printJson(response, msg);
							}
							else
							{
								ZAuthorizecode code = TokenUserUtil.codeCreate(redirect_uri, userjson);// 获取code
								msg = ResponseUtil.getJsonUserCode(code.getCode(), code.getExpires_in() / 1000);
								ResponseUtil.printJson(response, msg);
								try
								{
									String opread = "password".equals(grant_type) ? "password login" : "smscode login";
									SsoFactory.getSsoService().saveUserLog(appid, "AUTHORIZE", code.getCode(), 1, opread, true, getClientIp(request), loginUser.getId(), bm, loginUser.getName());
								}
								catch(Exception e)
								{
								}
							}
						}
						else
						{
							ZAuthtoken token = TokenUserUtil.tokenCreate(unit.getType(), String.valueOf(loginUser.getId()), userjson);
							ResponseUtil.printDomainJson(response, ResponseUtil.getJsonUserToken(token));// token只能同源获取
							try
							{
								String opread = "password".equals(grant_type) ? "password login" : "smscode login";
								SsoFactory.getSsoService().saveUserLog(appid, "ACCESS_TOKEN", token.getOpenid() + "-" + token.getAccess_token(), 1, opread, true, getClientIp(request), loginUser.getId(), bm, loginUser.getName());
							}
							catch(Exception e)
							{
							}
						}
						return;
					}
				}
			}
			try
			{
				String atype = "code".equals(response_type) ? "AUTHORIZE" : "ACCESS_TOKEN";
				SsoFactory.getSsoService().saveUserLog(appid, atype, "", 1, "", false, getClientIp(request), 0L, bm, "");
			}
			catch(Exception e)
			{
			}
			ResponseUtil.printDomainJson(response, msg);// 此次仅有错误json
		}
		catch(Exception e)
		{
		}
	}

	@RequestMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		String openid = req.getString("openid");
		String token = req.getString("access_token");
		IUnit unit = UnitUtil.get(appid);
		if(unit != null && !"".equals(appid) && !"".equals(openid) && !"".equals(token) && TokenUserUtil.tokenDel(unit.getType(), openid, token))
		{
			try
			{
				String userJson = TokenUserUtil.tokenGet(unit.getType(), openid, token);
				if(userJson != null)
				{
					IUser user = ResponseUtil.toBean(userJson, IUser.class);
					SsoFactory.getSsoService().saveUserLog(appid, "ACCESS_TOKEN", openid + "-" + token, 0, "logout", true, getClientIp(request), Long.parseLong(openid), user.getAccount(), user.getName());
				}
			}
			catch(Exception e)
			{
			}
			ResponseUtil.printJson(response, CodeUtil.CODE_001);
		}
		else
		{
			ResponseUtil.printJson(response, CodeUtil.CODE_400);
		}
	}

	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public void redirect(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		String authorizecode = req.getString("code");
		IUnit unit = UnitUtil.get(appid);
		String url = "";
		if(null != unit && authorizecode.length() > 0)
		{
			try
			{
				ZAuthorizecode code = TokenUserUtil.codeGet(authorizecode, false);
				if(code != null)
				{
					url = code.getRedirect_uri() + (code.getRedirect_uri().contains("?") ? "&" : "?") + "code=" + code.getCode();
				}
			}
			catch(Exception e)
			{
				url = "";
			}
		}
		ResponseUtil.sendRedirect(response, url.length() == 0 ? "about:blank" : url);
	}

	@RequestMapping("/userinfo")
	public void userinfo(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		String openid = req.getString("openid");
		String access_token = req.getString("access_token");
		IUnit unit = UnitUtil.get(appid);
		if("".equals(openid) || "".equals(access_token) || null == unit)
		{
			ResponseUtil.printJson(response, CodeUtil.CODE_400);
		}
		else
		{
			String userJson = TokenUserUtil.tokenGet(unit.getType(), openid, access_token);
			if(userJson.length() > 0)
			{
				IUser user = ResponseUtil.toBean(userJson, IUser.class);// 根据token获取用户
				if(user != null)
				{
					String exdata = req.getString("exdata");
					if(!"".equals(exdata))
					{
						user.setExdata(exdata);
						TokenUserUtil.userUpdate(openid, ResponseUtil.toJson(user));
					}
					userJson = ResponseUtil.toJson(user.clearSecret());
					ResponseUtil.printJson(response, ResponseUtil.getJsonUserInfo(userJson));
					return;
				}
			}
			ResponseUtil.printJson(response, CodeUtil.CODE_406);
		}
	}

	/**
	 * 注册
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void register(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		IUnit unit = UnitUtil.get(appid);
		if(unit != null)
		{
			IUser user = null;
			String userJson = req.getString("user");
			if(!"".equals(userJson))
			{
				try
				{
					user = new IUser();
					userJson = EncryptUtil.decryptDes(userJson, unit.getAppsecret());
					user = ResponseUtil.toBean(userJson, IUser.class);
				}
				catch(Exception e)
				{
					user = null;
				}
			}
			if(user != null)
			{
				String reg_type = req.getString("reg_type", "a");// 注册账号的类型：账号a,手机m,身份证件号u,可组合
				if(reg_type.length() > 0)
				{
					if(reg_type.indexOf("a") >= 0 && SsoFactory.getSsoService().getUserBm(user.getAccount()) != null)
					{
						ResponseUtil.printJson(response, CodeUtil.CODE_410_ACCOUNT);
						return;
					}
					if(reg_type.indexOf("m") >= 0)
					{
						String smscode = req.getString("smscode");
						if(smscode.length() > 0)
						{
							if(!smscode.equals(TokenSmsUtil.smscodeGet(appid + user.getMobile())))// 校验短信验证码
							{
								ResponseUtil.printJson(response, CodeUtil.CODE_406);// 动态密码，即短信验证码不正确
								return;
							}
						}
					}
					if(SsoFactory.getSsoService().saveUser(user, reg_type) > 0)
					{
						ResponseUtil.printJson(response, CodeUtil.CODE_001);
						return;
					}
					else
					{
						ResponseUtil.printJson(response, CodeUtil.CODE_410);
						return;
					}
				}
			}
			// if(user.getAccount().length() > 0 && user.getPassword().length() == 32)
			// {
			// if(SsoFactory.getSsoService().getUserByAccount(user.getAccount()) == null)
			// {
			// user.setId(IdUtil.genId());
			// user.setStatus(1);
			// if(SsoFactory.getSsoService().saveUser(user) > 0)
			// {
			// ResponseUtil.printJson(response, CodeUtil.CODE_001);
			// return;
			// }
			// }
			// ResponseUtil.printJson(response, CodeUtil.CODE_410);
			// return;
			// }
		}
		ResponseUtil.printJson(response, CodeUtil.CODE_400);
	}

	/**
	 * 更新账户信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/update/userinfo", method = RequestMethod.POST)
	public void update_userinfo(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			MyRequest req = new MyRequest(request);
			String appid = req.getString("appid");
			long openid = req.getLong("openid");
			IUnit unit = UnitUtil.get(appid);
			if(openid == 0 || null == unit)
			{
				ResponseUtil.printJson(response, CodeUtil.CODE_400);
			}
			else
			{
				IUser _user = null;
				String _userJson = req.getString("user");
				if("".equals(_userJson))
				{
					_user = new IUser();
					req.getFillObject(_user);
				}
				else
				{
					try
					{
						_user = new IUser();
						_userJson = EncryptUtil.decryptDes(_userJson, unit.getAppsecret());
						_user = ResponseUtil.toBean(_userJson, IUser.class);
					}
					catch(Exception e)
					{
						_user = null;
					}
				}
				IUser user = SsoFactory.getSsoService().getUserById(openid);
				if(user != null && user.getId().longValue() != 0L && _user != null)
				{
					_user.setId(user.getId());
					_user.setLasttime(System.currentTimeMillis());
					if(SsoFactory.getSsoService().updateUser(_user) > 0)
					{
						String userJson = ResponseUtil.toJson(SsoFactory.getSsoService().getUserById(user.getId()));
						TokenUserUtil.userUpdate(String.valueOf(user.getId()), userJson);
						ResponseUtil.printJson(response, CodeUtil.CODE_001);
						return;
					}
				}
			}
		}
		catch(Exception e)
		{
		}
		ResponseUtil.printJson(response, CodeUtil.CODE_400);
	}

	/**
	 * 修改密码或者重置密码
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/update/password", method = RequestMethod.POST)
	public void update_password(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		String grant_type = req.getString("grant_type", "reset");// password修改密码|reset重置密码
		String password = req.getString("password");// rsa(md5(password)), publicKey加密
		IUnit unit = UnitUtil.get(appid);
		String msg = "";
		IUser user = null;
		if(unit != null)
		{
			try
			{
				password = EncryptUtil.decryptRSA(password, CodeUtil.privateKey);
			}
			catch(Exception e)
			{
				password = "";
				msg = CodeUtil.CODE_400;// 参数不正确
			}
			// reset
			String account = req.getString("account").trim().toLowerCase(Locale.ENGLISH);
			String smscode = req.getString("smscode");
			// password
			long openid = req.getLong("openid");
			String access_token = req.getString("access_token");
			if(password.length() != 32)
			{
				msg = CodeUtil.CODE_400;// 参数不正确
			}
			else if("reset".equals(grant_type)) // 忘记密码, 只能使用手机号+短信验证码进行重置
			{
				if("".equals(account))
				{
					msg = CodeUtil.CODE_400;// 参数不正确
				}
				else
				{
					user = SsoFactory.getSsoService().getUserByBm(account);
					if(null == user)
					{
						msg = CodeUtil.CODE_404;// 用户不存在
					}
					else if(null != user && 1 != user.getStatus())
					{
						msg = CodeUtil.CODE_403;// 用户被禁用
					}
					else
					{
						if(!smscode.equals(TokenSmsUtil.smscodeGet(appid + account)))// 校验短信验证码
						{
							msg = CodeUtil.CODE_406;// 动态密码，即短信验证码不正确
							// if("CA1DB36591314A59EB5B449EF61D03E6".equals(appid) && "135246".equals(smscode))
							// {
							// msg = "";
							// }
						}
					}
				}
			}
			else if("password".equals(grant_type)) // 修改密码，原密码修改
			{
				String userJson = TokenUserUtil.tokenGet(unit.getType(), String.valueOf(openid), access_token);
				if(userJson != null && userJson.length() > 0)
				{
					String oldpassword = req.getString("oldpassword");// rsa(md5(password)), publicKey加密
					try
					{
						user = ResponseUtil.toBean(userJson, IUser.class);// 根据token获取用户
						user = SsoFactory.getSsoService().getUserByBm(user.getAccount());
					}
					catch(Exception e)
					{
						user = null;
					}
					try
					{
						oldpassword = EncryptUtil.decryptRSA(oldpassword, CodeUtil.privateKey).toUpperCase(Locale.ENGLISH);
						if(null == user)
						{
							msg = CodeUtil.CODE_404;// 用户不存在
						}
						else
						{
							if(!oldpassword.equals(user.getPassword()))
							{
								msg = CodeUtil.CODE_402;// 当前密码不正确
							}
						}
					}
					catch(Exception e)
					{
						msg = CodeUtil.CODE_400;// 参数不正确
					}
				}
			}
			if(msg.length() == 0 && user != null)// user均是从数据库重新查询出来的
			{
				if(SsoFactory.getSsoService().updateUserPassword(user.getId(), password.toUpperCase(Locale.ENGLISH)) > 0)
				{
					try
					{
						String atype = "reset".equals(grant_type) ? "SMS" : "ACCESS_TOKEN";
						String acode = "reset".equals(grant_type) ? smscode : (openid + "-" + access_token);
						String bm = "reset".equals(grant_type) ? account : user.getAccount();
						SsoFactory.getSsoService().saveUserLog(appid, atype, acode, 2, grant_type, true, getClientIp(request), user.getId(), bm, user.getName());
					}
					catch(Exception e)
					{
					}
					ResponseUtil.printJson(response, CodeUtil.CODE_001);
					return;
				}
			}
		}
		ResponseUtil.printJson(response, msg.length() == 0 ? CodeUtil.CODE_400 : msg);
	}

	/**
	 * 更换账号(手机号)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/update/account", method = RequestMethod.POST)
	public void update_account(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		String oldaccount = req.getString("oldaccount").trim();
		String newaccount = req.getString("newaccount").trim();
		String smscode = req.getString("smscode").trim();// 新的手机号的验证码
		String msg = "";
		if(oldaccount.length() > 0 && newaccount.length() > 0 && smscode.length() == 6)
		{
			IUnit unit = UnitUtil.get(appid);
			if(unit != null)
			{
				if(!smscode.equals(TokenSmsUtil.smscodeGet(appid + newaccount)))// 校验短信验证码
				{
					msg = CodeUtil.CODE_406;// 动态密码，即短信验证码不正确
				}
				else
				{
					IUser iUser = SsoFactory.getSsoService().getUserByBm(oldaccount);
					if(iUser != null)
					{
						if(SsoFactory.getSsoService().updateUserid(iUser, oldaccount, newaccount) > 0)
						{
							try
							{
								String opread = "bm changed from " + oldaccount+ " to " + newaccount;
								SsoFactory.getSsoService().saveUserLog(appid, "SMS" , smscode, 3, opread, true, getClientIp(request), iUser.getId(), oldaccount, iUser.getName());
							}
							catch(Exception e)
							{
							}
							msg = CodeUtil.CODE_001;
						}
					}
				}
			}
		}
		ResponseUtil.printJson(response, msg.length() == 0 ? CodeUtil.CODE_400 : msg);
	}
	
	private static String getClientIp(HttpServletRequest request)
	{
		String ip = request.getHeader("X-Real-IP");
		if(ip != null && ip.length() > 0 && !"null".equalsIgnoreCase(ip) && !"unKnown".equalsIgnoreCase(ip))
		{
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if(ip != null && ip.length() > 0 && !"null".equalsIgnoreCase(ip) && !"unKnown".equalsIgnoreCase(ip))
		{
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if(index != -1)
			{
				return ip.substring(0, index);
			}
			else
			{
				return ip;
			}
		}
		return request.getRemoteAddr();
	}
}
