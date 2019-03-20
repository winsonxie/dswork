package common.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import dswork.core.util.EncryptUtil;
import dswork.spring.BeanFactory;
import dswork.web.MyAuthCodeServlet;

/**
 * 用户登录类
 * @author skey
 */
public class AuthUtil
{
	public static boolean canSendMsg = true;
	public static final String SessionName_LoginUser = "COMMON_LOGIN_USER";
	private HttpServletRequest request;
	private String msg = "";
	
	/**
	 * 构造方法
	 */
	public AuthUtil(PageContext context)
	{
		this.request = (HttpServletRequest)context.getRequest();
	}
	public AuthUtil(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
	}

	private boolean isCode(String authcode)
	{
		boolean re = true;
		String randcode = (String) this.request.getSession().getAttribute(MyAuthCodeServlet.SessionName_Randcode);
		if(randcode == null || randcode.equals(""))
		{
			this.msg = "验证码已过期!";
			re = false;
		}
		else if(!randcode.toLowerCase().equals(authcode.toLowerCase()))
		{
			this.msg = "验证码输入错误,请重新输入!";
			re = false;
		}
		this.request.getSession().setAttribute(MyAuthCodeServlet.SessionName_Randcode, "");
		return re;
	}

	private boolean login(Auth auth, String validCode)
	{
		if(!this.isCode(validCode))
		{
			return false;
		}
		try
		{
			Auth loginUser = null;
			if(auth.isAdmin())
			{
				loginUser = ((AuthService)BeanFactory.getBean("authService")).getUserByAccount(auth.getAccount());
			}
			else if(auth.isEnterprise())
			{
				loginUser = ((AuthService)BeanFactory.getBean("authService")).getEpByAccount(auth.getAccount());
			}
			else if(auth.isUser())
			{
				loginUser = ((AuthService)BeanFactory.getBean("authService")).getPersonByAccount(auth.getAccount());
			}
			if(loginUser != null)
			{
				if(loginUser.getLoginstatus() == 1)// 正常
				{
					String password = EncryptUtil.decodeDes(auth.getPassword(), "dswork");
					password = EncryptUtil.encryptMd5(password).toLowerCase();
					if(password.equalsIgnoreCase(loginUser.getPassword().toLowerCase()))
					{
						setLoginUserInfo(loginUser);
						return true;
					}
					else
					{
						this.msg = "密码错误！";
					}
				}
				else
				{
					this.msg = "用户已禁用！";
				}
			}
			else
			{
				this.msg = "用户不存在！";
			}
		}
		catch(Exception ex)
		{
		}
		return false;
	}

	/**
	 * 登入
	 * @param account 用户帐号
	 * @param password 密码
	 * @param logintype 用户类型
	 * @param validCode 验证码
	 * @return boolean
	 */
	public boolean login(String account, String password, int logintype, String validCode)
	{
		Auth auth = new Auth();
		auth.setAccount(account);
		auth.setPassword(password);
		auth.setLogintype(logintype);
		return login(auth, validCode);
	}

	/**
	 * 登出
	 * @param request HttpServletRequest
	 */
	public static void logout(HttpServletRequest request)
	{
		request.getSession().setAttribute(SessionName_LoginUser, null);
	}

	/**
	 * 设置当前的用户登录信息；
	 */
	private void setLoginUserInfo(Auth user)
	{
		this.request.getSession().setAttribute(SessionName_LoginUser, user);
	}

	/**
	 * 得到登录出错的信息
	 * @return String
	 */
	public String getMsg()
	{
		return this.msg;
	}

	/**
	 * 所有方法通过此函数获取当前用户信息
	 */
	public static Auth getLoginUser(HttpServletRequest request)
	{
		Auth user = (Auth) request.getSession().getAttribute(SessionName_LoginUser);
		if(user != null)
		{
			return user;
		}
		return null;
	}
}
