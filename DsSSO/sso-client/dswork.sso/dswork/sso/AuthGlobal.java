package dswork.sso;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dswork.sso.http.HttpUtil;
import dswork.sso.model.AccessToken;
import dswork.sso.model.JsonResult;

import com.google.gson.reflect.TypeToken;

public class AuthGlobal
{
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
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	public static com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	private static String APPID = null;
	private static String APPSECRET = null;
	private static String APIURL = null;
	private static String ACCESS_TOKEN = null;
	private static HttpUtil http = null;
	private static boolean https;

	private AuthGlobal()
	{
	}

	public static String getAppid()
	{
		return APPID;
	}

	public static String getAppsecret()
	{
		return APPSECRET;
	}

	public static String getApiURL()
	{
		return APIURL;
	}

	public static String getAccessToken()
	{
		if(ACCESS_TOKEN == null)
		{
			getUnitAccessToken(false);
		}
		return ACCESS_TOKEN;
	}

	public static <T> T toBean(String json, Class<T> classOfT)
	{
		return gson.fromJson(json, classOfT);
	}
	
	public static boolean isHttps()
	{
		return https;
	}

	public static void setHttps(boolean https)
	{
		AuthGlobal.https = https;
	}

	public static HttpUtil getHttp(String path)
	{
		return new HttpUtil().create(new StringBuilder(30).append(APIURL).append(path).toString(), https);
	}

	/**
	 * 用于强制刷新token，可能是token被动过期
	 */
	public static void refreshUnitAccessToken()
	{
		getUnitAccessToken(true);
	}

	private static synchronized JsonResult<AccessToken> getUnitAccessToken(boolean hasMust)// hasMust用于防止为空时，无数请求都去重新获取
	{
		if(!hasMust && ACCESS_TOKEN != null)
		{
			return null;// 已经有人查过了，不用再查
		}
		JsonResult<AccessToken> result = null;
		try
		{
			result = gson.fromJson(http.connect(), (new TypeToken<JsonResult<AccessToken>>(){}).getType());
		}
		catch(Exception e)
		{
			try
			{
				result = gson.fromJson(http.connect(), new TypeToken<JsonResult<AccessToken>>(){}.getType());
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
				log.error("dswork.sso.AuthGlobal获取unit的access_token成功，失败结果：" + result.getData().getAccess_token());
			}
			else
			{
				log.error("dswork.sso.AuthGlobal获取unit的access_token失败，失败结果：" + result.getCode());
			}
		}
		return result;
	}
	private static Timer _timer = new Timer(true);
	private static long time = 10000L;// 默认10秒
	private static TimerTask _tokenTask = new TimerTask()
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
						_timer.schedule(_tokenTask, time);
						return;
					}
				}
			}
			catch(Exception ex)
			{
			}
			_timer.schedule(_tokenTask, 10000L);
		}
	};

	/**
	 * 一般只初始化一次，且必须初始化
	 * @param appid
	 * @param appsecret
	 * @param apiURL
	 */
	public static void runAppConfig(String appid, String appsecret, String apiURL)
	{
		APPID = appid == null ? "portal" : appid;
		APPSECRET = appsecret == null ? "portal" : appsecret;
		APIURL = apiURL == null ? "http://127.0.0.1:8888/sso" : apiURL;
		http = new HttpUtil().create(APIURL + "/unit/access_token", https).addForm("appid", getAppid()).addForm("appsecret", getAppsecret()).addForm("grant_type", "client_credential");
		_timer.schedule(_tokenTask, 100L);// 重新启动
	}
}
