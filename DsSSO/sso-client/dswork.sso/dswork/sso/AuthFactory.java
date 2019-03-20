/**
 * 调用webapi的工厂类
 */
package dswork.sso;

import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;

import dswork.sso.http.HttpUtil;
import dswork.sso.model.AccessToken;
import dswork.sso.model.Authcode;
import dswork.sso.model.IFunc;
import dswork.sso.model.IOrg;
import dswork.sso.model.ISystem;
import dswork.sso.model.IUser;
import dswork.sso.model.JsonResult;

public class AuthFactory
{
	static String url = "";
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	private static String SYSTEM_ALIAS = "";
	private static String SYSTEM_PASSWORD = "";
	private static String SYSTEM_REDIRECTURI = "";
	private static String WEB_URI = "";

	private static String Md5(String v)
	{
		if(v != null)
		{
			StringBuilder sb = new StringBuilder();
			try
			{
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] digest = md.digest(v.getBytes("UTF-8"));
				String stmp = "";
				for(int n = 0; n < digest.length; n++)
				{
					stmp = (Integer.toHexString(digest[n] & 0XFF));
					sb.append((stmp.length() == 1) ? "0" : "").append(stmp);
				}
				return sb.toString().toUpperCase(Locale.ENGLISH);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				sb = null;
			}
		}
		return "";
	}

	public static void initConfig(String systemAlias, String systemPassword, String systemRedirectUri, String web_uri)
	{
		SYSTEM_ALIAS = systemAlias;
		SYSTEM_PASSWORD = Md5(systemPassword);
		SYSTEM_REDIRECTURI = (systemRedirectUri == null) ? "" : systemRedirectUri.trim();
		WEB_URI = (web_uri == null) ? "" : web_uri.trim();
	}

	private static HttpUtil getHttpForID(String path)
	{
		return AuthGlobal.getHttp(path).addForm("appid", AuthGlobal.getAppid());
	}

	public static HttpUtil getHttp(String path)
	{
		return getHttpForID(path).addForm("access_token", AuthGlobal.getAccessToken());
	}

	public static HttpUtil getApiHttp(String path)
	{
		return getHttpForID(path).addForm("access_token", AuthGlobal.getAccessToken()).addForm("systemAlias", SYSTEM_ALIAS).addForm("systemPassword", SYSTEM_PASSWORD);
	}

	private static String getRedirectUri(String source, String redirect_uri)
	{
		try
		{
			return java.net.URLEncoder.encode(source.length() > 0 ? source : redirect_uri, "UTF-8");
		}
		catch(Exception e)
		{
			return "";
		}
	}

	//////////////////////////////////////////////////////////////////////////////
	// 用户相关的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * 后端获取用户凭证(access_token)
	 * @param code 用户授权令牌
	 * @return JsonResult&lt;AccessToken&gt;
	 */
	public static JsonResult<AccessToken> getUserAccessToken(String code)
	{
		HttpUtil h = getHttp("/user/access_token").addForm("appsecret", AuthGlobal.getAppsecret()).addForm("grant_type", "authorization_code").addForm("code", code);
		JsonResult<AccessToken> result = null;
		String v = "";
		try
		{
			v = h.connect().trim();
			result = AuthGlobal.gson.fromJson(v, new TypeToken<JsonResult<AccessToken>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return result;
	}

	/**
	 * 前端检查用户凭证(access_token)是否还有效
	 * @param openid 用户标识
	 * @param access_token 用户凭证
	 * @return JsonResult&lt;String&gt;
	 */
	public static JsonResult<String> getUserAuthToken(String openid, String access_token)
	{
		HttpUtil h = getHttp("/user/auth_token").addForm("openid", openid).addForm("access_token", access_token);
		JsonResult<String> result = null;
		String v = "";
		try
		{
			v = h.connect().trim();
			result = AuthGlobal.gson.fromJson(v, new TypeToken<JsonResult<IUser>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return result;
	}

	/**
	 * 前端授权页面地址
	 * @param redirect_uri 重定向地址，如果为配置的redirect_uri不为空，则忽略
	 * @return String
	 */
	public static String getUserAuthorizeURL(String redirect_uri)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(WEB_URI).append("/user/authorize").append("?appid=").append(AuthGlobal.getAppid()).append("&response_type=code&redirect_uri=").append(getRedirectUri(SYSTEM_REDIRECTURI, redirect_uri));
		return sb.toString();
	}

	/**
	 * 前端登入认证地址(post)，获取用户授权令牌(code)或用户凭证(access_token)，其中该地址因authtime只有2小时有效，另还需增加grant_type=password|sms
	 * @param isCode code|token
	 * @param redirect_uri 重定向地址，如果为配置的redirect_uri不为空，则忽略
	 * @return String /user/login?appid=应用ID&response_type=code|token&redirect_uri=重定向地址&authtime=超时时间戳&authcode=应用认证码&grant_type=&account=&password=
	 */
	public static String getUserLoginURL(boolean isCode, String redirect_uri)
	{
		Authcode ac = Authcode.code_create(AuthGlobal.getAppsecret());
		StringBuilder sb = new StringBuilder();
		sb.append(WEB_URI).append("/user/login").append("?appid=").append(AuthGlobal.getAppid()).append("&response_type=").append(isCode ? "code" : "token").append("&redirect_uri=").append(getRedirectUri(SYSTEM_REDIRECTURI, redirect_uri));
		sb.append("&authtime=").append(ac.getAuthtime()).append("&authcode=").append(ac.getAuthcode());
		return sb.toString();
	}

	/**
	 * 前端登出认证地址，即取消用户凭证
	 * @param openid 用户标识
	 * @param access_token 用户凭证
	 * @return String /user/logout?appid=应用ID&openid=用户标识&access_token=用户凭证
	 */
	public static String getUserLogoutURL(String openid, String access_token)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(WEB_URI).append("/user/logout").append("?appid=").append(AuthGlobal.getAppid()).append("&openid=").append(openid).append("&access_token=").append(access_token);
		return sb.toString();
	}

	/**
	 * 授权后访问重定向地址
	 * @param code 用户授权令牌
	 * @return String /user/redirect?appid=应用ID&code=用户授权令牌
	 */
	public static String getUserRedirectURL(String code)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(WEB_URI).append("/user/redirect").append("?appid=").append(AuthGlobal.getAppid()).append("&code=").append(code);
		return sb.toString();
	}

	/**
	 * 修改密码地址
	 * @param openid 用户标识
	 * @param access_token 用户凭证
	 * @param redirect_uri 重定向地址，即修改完密码是否需跳转至指定地址，为空则使用默认地址
	 * @return String /user/password?appid=应用ID&openid=用户标识&access_token=用户凭证&
	 */
	public static String getUserPasswordURL(String openid, String access_token, String redirect_uri)
	{
		if(redirect_uri == null)
		{
			redirect_uri = "";
		}
		StringBuilder sb = new StringBuilder();
		// 此处getRedirectUri取反参数
		sb.append(WEB_URI).append("/user/password").append("?appid=").append(AuthGlobal.getAppid()).append("&openid=").append(openid).append("&access_token=").append(access_token).append("&redirect_uri=").append(getRedirectUri(redirect_uri, SYSTEM_REDIRECTURI));
		return sb.toString();
	}

	/**
	 * 前端账户信息
	 * @param openid 用户标识
	 * @param access_token 用户凭证
	 * @return JsonResult&lt;IUser&gt;
	 */
	public static JsonResult<IUser> getUserUserinfo(String openid, String access_token)
	{
		HttpUtil h = getHttp("/user/userinfo").addForm("openid", openid).addForm("access_token", access_token);
		JsonResult<IUser> result = null;
		String v = "";
		try
		{
			v = h.connect().trim();
			result = AuthGlobal.gson.fromJson(v, new TypeToken<JsonResult<IUser>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return result;
	}

	/**
	 * 前后端发送6位短信验证码
	 * @param mobile 手机号码
	 * @return JsonResult&lt;String&gt;
	 */
	public static JsonResult<String> getSmsCode(String mobile)
	{
		HttpUtil h = getHttp("/sms/code").addForm("access_token", AuthGlobal.getAccessToken()).addForm("mobile", mobile);
		JsonResult<String> result = null;
		String v = "";
		try
		{
			v = h.connect().trim();
			result = AuthGlobal.gson.fromJson(v, new TypeToken<JsonResult<String>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return result;
	}

	//////////////////////////////////////////////////////////////////////////////
	// 统一权限相关的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取子系统信息
	 * @return ISystem
	 */
	public static ISystem getSystem()
	{
		HttpUtil h = getApiHttp("/api/getSystem");
		String v = "";
		ISystem m = null;
		try
		{
			v = h.connect().trim();
			m = AuthGlobal.gson.fromJson(v, ISystem.class);
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return m;
	}

	/**
	 * 获取用户有权限访问的子系统
	 * @param userAccount 用户帐号
	 * @return ISystem[]
	 */
	public static ISystem[] getSystemByUser(String userAccount)
	{
		HttpUtil h = getApiHttp("/api/getSystemByUser").addForm("userAccount", userAccount);
		String v = "";
		List<ISystem> list = null;
		try
		{
			v = h.connect().trim();
			list = AuthGlobal.gson.fromJson(v, new TypeToken<List<ISystem>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return list.toArray(new ISystem[list.size()]);
	}

	/**
	 * 获取系统的功能结构
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionBySystem()
	{
		HttpUtil h = getApiHttp("/api/getFunctionBySystem");
		String v = "";
		List<IFunc> list = null;
		try
		{
			v = h.connect().trim();
			list = AuthGlobal.gson.fromJson(v, new TypeToken<List<IFunc>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return list.toArray(new IFunc[list.size()]);
	}

	/**
	 * 获取用户权限范围内的系统功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @param userAccount 用户帐号
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionByUser(String userAccount)
	{
		HttpUtil h = getApiHttp("/api/getFunctionByUser").addForm("userAccount", userAccount);
		String v = "";
		List<IFunc> list = null;
		try
		{
			v = h.connect().trim();
			list = AuthGlobal.gson.fromJson(v, new TypeToken<List<IFunc>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return list.toArray(new IFunc[list.size()]);
	}

	/**
	 * 获取岗位权限范围内的系统功能结构
	 * @param orgId 组织机构ID（单位ID、部门ID、岗位ID）
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionByOrg(String orgId)
	{
		HttpUtil h = getApiHttp("/api/getFunctionByOrg").addForm("orgId", orgId);
		String v = "";
		List<IFunc> list = null;
		try
		{
			v = h.connect().trim();
			list = AuthGlobal.gson.fromJson(v, new TypeToken<List<IFunc>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return list.toArray(new IFunc[list.size()]);
	}

	//////////////////////////////////////////////////////////////////////////////
	// 组织机构及用户的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * @note 获取组织机构（单位、部门、岗位）
	 * @param orgId 组织机构ID（单位ID、部门ID、岗位ID）
	 * @return IOrg
	 */
	public static IOrg getOrg(String orgId)
	{
		HttpUtil h = getApiHttp("/api/getOrg").addForm("orgId", orgId);
		String v = "";
		IOrg m = null;
		try
		{
			v = h.connect().trim();
			m = AuthGlobal.gson.fromJson(v, IOrg.class);
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return m;
	}

	/**
	 * @note 获取下级组织机构(status:2单位,1部门,0岗位)
	 * @param orgPid 组织机构ID，为0则取顶级
	 * @return IOrg[]
	 */
	public static IOrg[] queryOrgByOrgParent(String orgPid)
	{
		HttpUtil h = getApiHttp("/api/queryOrgByOrgParent").addForm("orgPid", orgPid);
		String v = "";
		List<IOrg> list = null;
		try
		{
			v = h.connect().trim();
			list = AuthGlobal.gson.fromJson(v, new TypeToken<List<IOrg>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return list.toArray(new IOrg[list.size()]);
	}

	/**
	 * @note 获取指定用户的基本信息
	 * @param userAccount 用户帐号
	 * @return IUser
	 */
	public static IUser getUser(String userAccount)
	{
		HttpUtil h = getApiHttp("/api/getUser").addForm("userAccount", userAccount);
		String v = "";
		IUser m = null;
		try
		{
			v = h.connect().trim();
			m = AuthGlobal.gson.fromJson(v, IUser.class);
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return m;
	}

	/**
	 * @note 获取指定用户的基本信息
	 * @param userOpenid 用户标识
	 * @return IUser
	 */
	public static IUser getUserByOpenid(String userOpenid)
	{
		HttpUtil h = getApiHttp("/api/getUserByOpenid").addForm("userOpenid", userOpenid);
		String v = "";
		IUser m = null;
		try
		{
			v = h.connect().trim();
			m = AuthGlobal.gson.fromJson(v, IUser.class);
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return m;
	}

	/**
	 * @note 获取指定单位下的用户，不含子单位
	 * @param orgPid 单位ID
	 * @return IUser[]
	 */
	public static IUser[] queryUserByOrgParent(String orgPid)
	{
		HttpUtil h = getApiHttp("/api/queryUserByOrgParent").addForm("orgPid", orgPid);
		String v = "";
		List<IUser> list = null;
		try
		{
			v = h.connect().trim();
			list = AuthGlobal.gson.fromJson(v, new TypeToken<List<IUser>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return list.toArray(new IUser[list.size()]);
	}

	/**
	 * @note 获取指定部门下的用户，不含子部门
	 * @param orgId 部门 ID
	 * @return IUser[]
	 */
	public static IUser[] queryUserByOrg(String orgId)
	{
		HttpUtil h = getApiHttp("/api/queryUserByOrg").addForm("orgId", orgId);
		String v = "";
		List<IUser> list = null;
		try
		{
			v = h.connect().trim();
			list = AuthGlobal.gson.fromJson(v, new TypeToken<List<IUser>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("AuthFactory:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return list.toArray(new IUser[list.size()]);
	}
}
