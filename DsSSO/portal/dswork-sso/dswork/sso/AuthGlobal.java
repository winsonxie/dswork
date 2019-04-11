package dswork.sso;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dswork.sso.http.HttpUtil;
import dswork.sso.model.AccessToken;
import dswork.sso.model.IUser;
import dswork.sso.model.JsonResult;

import com.google.gson.reflect.TypeToken;

public class AuthGlobal
{
	private AuthGlobal()
	{
	}
	/**
	 * 1: 成功
	 */
	public static final int CODE_001 = 1;// 成功
	/**
	 * 400: 请求失败或参数不正确
	 */
	public static final int CODE_400 = 400;
	/**
	 * 406：验证码无效，access_token无效， CODE无效
	 */
	public static final int CODE_406 = 406;
	public static Logger log = LoggerFactory.getLogger("dswork.sso");
	public static com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
	private static String APPID = null;
	private static String APPSECRET = null;
	private static String APIURL = null;
	private static String ACCESS_TOKEN = "";
	private static long ACCESS_TOKEN_TIMEOUT = 0L;
	private static boolean INIT_ACCESSTOKEN = false;
	private static boolean https = false;
	
	private static HttpUtil getConnectHttp()
	{
		return new HttpUtil().create(APIURL + "/unit/access_token", https).addForm("appid", APPID).addForm("appsecret", APPSECRET).addForm("grant_type", "client_credential");
	}

	private static synchronized JsonResult<AccessToken> getUnitAccessToken(boolean hasMust)// hasMust用于防止为空时，无数请求都去重新获取
	{
		long now = System.currentTimeMillis();
		// 如果之前获取的已经过期，则直接清空
		if(ACCESS_TOKEN.length() > 0 && now > ACCESS_TOKEN_TIMEOUT)
		{
			ACCESS_TOKEN = "";
		}
		if(!hasMust && ACCESS_TOKEN.length() > 0)
		{
			return null;// 已经有人查过了，不用再查
		}
		JsonResult<AccessToken> result = null;
		try
		{
			result = gson.fromJson(getConnectHttp().connect(), (new TypeToken<JsonResult<AccessToken>>()
			{
			}).getType());
		}
		catch(Exception e)
		{
			try
			{
				result = gson.fromJson(getConnectHttp().connect(), new TypeToken<JsonResult<AccessToken>>()
				{
				}.getType());
			}
			catch(Exception ee)
			{
				log.error("dswork.sso.AuthGlobal获取unit的access_token失败，" + ee.getMessage());
			}
		}
		if(result != null)
		{
			if(result.getCode() == CODE_001)
			{
				ACCESS_TOKEN = result.getData().getAccess_token();
				ACCESS_TOKEN_TIMEOUT = result.getData().getExpires_in() * 1000 + now;
				log.info("dswork.sso.AuthGlobal获取unit的access_token成功，成功结果：" + result.getData().getAccess_token());
			}
			else
			{
				log.error("dswork.sso.AuthGlobal获取unit的access_token失败，失败结果：" + result.getCode());
			}
		}
		return result;
	}

	/**
	 * 一次性用于强制刷新token，可能是token被动过期，或临时需要使用UnitAccessToken
	 */
	public static String refreshUnitAccessToken()
	{
		getUnitAccessToken(true);
		return ACCESS_TOKEN;
	}
	
	private static Timer _timer = new Timer(true);
	private static long time = 10000L;// 默认10秒
	private static TimerTask _tokenTask1;
	private static TimerTask _tokenTask2;
	static
	{
		_tokenTask1 = new TimerTask()
		{
			public void run()
			{
				try
				{
					JsonResult<AccessToken> result = getUnitAccessToken(true);
					if(result != null)
					{
						if(result.getCode() == CODE_001)
						{
							time = 1000L * result.getData().getExpires_in() - 3000000;// 提前五分钟刷新
							//_timer.cancel();
							_timer.schedule(_tokenTask2, time);
							return;
						}
					}
				}
				catch(Exception ex){}
				try{_timer.schedule(_tokenTask2, 10000L);}catch(Exception e){}
			}
		};
		_tokenTask2 = new TimerTask()
		{
			public void run()
			{
				try
				{
					JsonResult<AccessToken> result = getUnitAccessToken(true);
					if(result != null)
					{
						if(result.getCode() == CODE_001)
						{
							time = 1000L * result.getData().getExpires_in() - 3000000;// 提前五分钟刷新
							_timer.schedule(_tokenTask1, time);
							return;
						}
					}
				}
				catch(Exception ex){}
				try{_timer.schedule(_tokenTask1, 10000L);}catch(Exception e){}
			}
		};
	}

	public static String getAppID()
	{
		return APPID;
	}

	public static String getAppSecret()
	{
		return APPSECRET;
	}

	public static String getApiURL()
	{
		return APIURL;
	}

	/**
	 * 返回unit的access_token，失败返回空“”，如果初始时initAccessToken为false，则永远返回“”
	 * @return unit的access_token
	 */
	public static String getAccessToken()
	{
		if(INIT_ACCESSTOKEN)
		{
			if(ACCESS_TOKEN_TIMEOUT < System.currentTimeMillis())
			{
				getUnitAccessToken(false);// 需要刷新Unit的access_token
			}
		}
		else
		{
			if(ACCESS_TOKEN_TIMEOUT < System.currentTimeMillis())
			{
				ACCESS_TOKEN = "";// 过期了
			}
		}
		return ACCESS_TOKEN;
	}
	
	public static boolean getInitAccessToken()
	{
		return INIT_ACCESSTOKEN;
	}

	public static boolean isHttps()
	{
		return https;
	}

	/**
	 * 一般只初始化一次，且必须初始化
	 * @param appid
	 * @param appsecret
	 * @param apiURL
	 */
	public static void initConfig(String appid, String appsecret, String apiURL, boolean initAccessToken)
	{
		APPID = appid == null ? "portal" : appid;
		APPSECRET = appsecret == null ? "portal" : appsecret;
		APIURL = apiURL == null ? "http://127.0.0.1:8888/sso" : apiURL;
		https = APIURL.startsWith("https");
		INIT_ACCESSTOKEN = initAccessToken;
		if(INIT_ACCESSTOKEN)
		{
			_timer.schedule(_tokenTask1, 100L);// 重新启动
		}
	}

	/**
	 * 默认添加了表单参数appid
	 * @param path
	 * @return HttpUtil
	 */
	public static HttpUtil getHttp(String path)
	{
		return new HttpUtil().create(new StringBuilder(47).append(getApiURL()).append(path).toString(), isHttps()).addForm("appid", getAppID());
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
		HttpUtil h = getHttp("/user/access_token").addForm("appsecret", getAppSecret()).addForm("grant_type", "authorization_code").addForm("code", code);
		JsonResult<AccessToken> result = null;
		String v = "";
		try
		{
			v = h.connect().trim();
			result = gson.fromJson(v, new TypeToken<JsonResult<AccessToken>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("getUserAccessToken:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("getUserAccessToken:url=" + h.getUrl() + ", json:" + v);
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
			result = gson.fromJson(v, new TypeToken<JsonResult<IUser>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("getUserAuthToken:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("getUserAuthToken:url=" + h.getUrl() + ", json:" + v);
		}
		return result;
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
			result = gson.fromJson(v, new TypeToken<JsonResult<IUser>>()
			{
			}.getType());
			if(log.isDebugEnabled())
			{
				log.debug("getUserUserinfo:url=" + h.getUrl() + ", json:" + v);
			}
		}
		catch(Exception e)
		{
			log.error("getUserUserinfo:url=" + h.getUrl() + ", json:" + v);
		}
		return result;
	}
}
