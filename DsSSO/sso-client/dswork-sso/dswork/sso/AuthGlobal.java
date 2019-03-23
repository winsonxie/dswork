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
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	public static com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
	private static String APPID = null;
	private static String APPSECRET = null;
	private static String APIURL = null;
	private static String ACCESS_TOKEN = null;
	private static boolean https = false;
	
	private static HttpUtil getConnectHttp()
	{
		return new HttpUtil().create(APIURL + "/unit/access_token", https).addForm("appid", APPID).addForm("appsecret", APPSECRET).addForm("grant_type", "client_credential");
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
	 * 用于强制刷新token，可能是token被动过期
	 */
	public static void refreshUnitAccessToken()
	{
		getUnitAccessToken(true);
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

	public static String getAccessToken()
	{
		if(ACCESS_TOKEN == null)
		{
			getUnitAccessToken(false);
		}
		return ACCESS_TOKEN;
	}

	public static boolean isHttps()
	{
		return https;
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

	/**
	 * 一般只初始化一次，且必须初始化
	 * @param appid
	 * @param appsecret
	 * @param apiURL
	 */
	public static void initConfig(String appid, String appsecret, String apiURL)
	{
		APPID = appid == null ? "portal" : appid;
		APPSECRET = appsecret == null ? "portal" : appsecret;
		APIURL = apiURL == null ? "http://127.0.0.1:8888/sso" : apiURL;
		https = APIURL.startsWith("https");
		_timer.schedule(_tokenTask1, 100L);// 重新启动
	}
}
