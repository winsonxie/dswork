/**
 * 调用webapi的工厂类
 */
package dswork.sso;

import java.util.ArrayList;
import java.util.List;

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
	static org.slf4j.Logger log = AuthGlobal.log;

	private AuthFactory()
	{
	}
	
	public static String getEncodeURL(String url)
	{
		try
		{
			return java.net.URLEncoder.encode(url, "UTF-8");
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	public static String getRedirectURI(String url)
	{
		return AuthWebConfig.getSystemRedirectURI().length() > 0 ? AuthWebConfig.getSystemRedirectURI() : url;
	}
	
	public static HttpUtil getAppHttp(String path)
	{
		String token = AuthGlobal.getAccessToken();
		if(!AuthGlobal.getInitAccessToken())
		{
			if(token.length() > 0)
			{
				log.warn("sso.initAccessToken=false");
			}
			else
			{
				log.error("sso.initAccessToken=false");
			}
		}
		return AuthGlobal.getHttp(path).addForm("access_token", token);
	}

	public static HttpUtil getSystemHttp(String path)
	{
		return getAppHttp(path).addForm("systemAlias", AuthWebConfig.getSystemAlias()).addForm("systemPassword", AuthWebConfig.getSystemPassword());
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
		return AuthGlobal.getUserAccessToken(code);
	}

	/**
	 * 前端检查用户凭证(access_token)是否还有效
	 * @param openid 用户标识
	 * @param access_token 用户凭证
	 * @return JsonResult&lt;String&gt;
	 */
	public static JsonResult<String> getUserAuthToken(String openid, String access_token)
	{
		return AuthGlobal.getUserAuthToken(openid, access_token);
	}

	/**
	 * 前端账户信息
	 * @param openid 用户标识
	 * @param access_token 用户凭证
	 * @return JsonResult&lt;IUser&gt;
	 */
	public static JsonResult<IUser> getUserUserinfo(String openid, String access_token)
	{
		return AuthGlobal.getUserUserinfo(openid, access_token);
	}

	/**
	 * 前端授权页面地址
	 * @param redirect_uri 重定向地址，如果为配置的redirect_uri不为空，则忽略
	 * @return String
	 */
	public static String getUserAuthorizeURL(String redirect_uri)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(AuthWebConfig.getSystemLoginURL()).append("&redirect_uri=");
		String uri = AuthWebConfig.getSystemWebURL() + "/sso/login?url=" + getEncodeURL(redirect_uri);
		sb.append(getEncodeURL(uri));
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
		Authcode ac = Authcode.code_create(AuthGlobal.getAppSecret());
		StringBuilder sb = new StringBuilder();
		sb.append(AuthWebConfig.getSsoWebURL()).append("/user/login").append("?appid=").append(AuthGlobal.getAppID()).append("&response_type=").append(isCode ? "code" : "token");
		sb.append("&authtime=").append(ac.getAuthtime()).append("&authcode=").append(ac.getAuthcode());
		String uri = AuthWebConfig.getSystemWebURL() + "/sso/login?url=" + getEncodeURL(redirect_uri);
		sb.append("&redirect_uri=").append(getEncodeURL(uri));
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
		sb.append(AuthWebConfig.getSsoWebURL()).append("/user/logout").append("?appid=").append(AuthGlobal.getAppID()).append("&openid=").append(openid).append("&access_token=").append(access_token);
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
		sb.append(AuthWebConfig.getSsoWebURL()).append("/user/redirect").append("?appid=").append(AuthGlobal.getAppID()).append("&code=").append(code);
		return sb.toString();
	}

	/**
	 * 修改密码地址
	 * @param openid 用户标识
	 * @param access_token 用户凭证
	 * @param redirect_uri 重定向地址，即修改完密码需跳转至指定地址，为空则不跳转
	 * @return String /user/password?appid=应用ID&openid=用户标识&access_token=用户凭证&
	 */
	public static String getUserPasswordURL(String openid, String access_token, String redirect_uri)
	{
		if(redirect_uri == null)
		{
			redirect_uri = "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(AuthWebConfig.getSsoWebURL()).append("/user/password").append("?appid=").append(AuthGlobal.getAppID()).append("&openid=").append(openid).append("&access_token=").append(access_token);
		sb.append("&redirect_uri=").append(getEncodeURL(redirect_uri));
		return sb.toString();
	}

	/**
	 * 前后端发送6位短信验证码
	 * @param mobile 手机号码
	 * @return JsonResult&lt;String&gt;
	 */
	public static JsonResult<String> getSmsCode(String mobile)
	{
		HttpUtil h = getAppHttp("/sms/code").addForm("mobile", mobile);
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
	// 组织机构及用户的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * @note 获取组织机构（单位、部门、岗位）
	 * @param orgId 组织机构ID（单位ID、部门ID、岗位ID）
	 * @return IOrg
	 */
	public static IOrg getOrg(String orgId)
	{
		HttpUtil h = getAppHttp("/api/getOrg").addForm("orgId", orgId);
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
		HttpUtil h = getAppHttp("/api/queryOrgByOrgParent").addForm("orgPid", orgPid);
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
		return list == null ? new IOrg[0] : list.toArray(new IOrg[list.size()]);
	}

	/**
	 * @note 获取指定用户的基本信息
	 * @param userAccount 用户帐号
	 * @return IUser
	 */
	public static IUser getUser(String userAccount)
	{
		HttpUtil h = getAppHttp("/api/getUser").addForm("userAccount", userAccount);
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
		HttpUtil h = getAppHttp("/api/getUserByOpenid").addForm("userOpenid", userOpenid);
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
		HttpUtil h = getAppHttp("/api/queryUserByOrgParent").addForm("orgPid", orgPid);
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
			list = new ArrayList<IUser>();
			log.error("AuthFactory:url=" + h.getUrl() + ", json:" + v);
		}
		return list == null ? new IUser[0] : list.toArray(new IUser[list.size()]);
	}

	/**
	 * @note 获取指定部门下的用户，不含子部门
	 * @param orgId 部门 ID
	 * @return IUser[]
	 */
	public static IUser[] queryUserByOrg(String orgId)
	{
		HttpUtil h = getAppHttp("/api/queryUserByOrg").addForm("orgId", orgId);
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
		return list == null ? new IUser[0] : list.toArray(new IUser[list.size()]);
	}

	//////////////////////////////////////////////////////////////////////////////
	// 统一权限相关的方法，以下方法需要systemAlias和systemPassword
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取子系统信息
	 * @return ISystem
	 */
	public static ISystem getSystem()
	{
		HttpUtil h = getSystemHttp("/api/getSystem");
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
		HttpUtil h = getSystemHttp("/api/getSystemByUser").addForm("userAccount", userAccount);
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
		return list == null ? new ISystem[0] : list.toArray(new ISystem[list.size()]);
	}

	/**
	 * 获取系统的功能结构
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionBySystem()
	{
		HttpUtil h = getSystemHttp("/api/getFunctionBySystem");
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
		return list == null ? new IFunc[0] : list.toArray(new IFunc[list.size()]);
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
		HttpUtil h = getSystemHttp("/api/getFunctionByUser").addForm("userAccount", userAccount);
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
		return list == null ? new IFunc[0] : list.toArray(new IFunc[list.size()]);
	}

	/**
	 * 获取岗位权限范围内的系统功能结构
	 * @param orgId 组织机构ID（单位ID、部门ID、岗位ID）
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionByOrg(String orgId)
	{
		HttpUtil h = getSystemHttp("/api/getFunctionByOrg").addForm("orgId", orgId);
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
		return list == null ? new IFunc[0] : list.toArray(new IFunc[list.size()]);
	}
}
