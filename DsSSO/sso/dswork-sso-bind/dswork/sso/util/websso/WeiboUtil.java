package dswork.sso.util.websso;

import dswork.sso.model.weibo.WeiboAccessToken;
import dswork.sso.model.weibo.WeiboTokenInfo;
import dswork.sso.model.weibo.WeiboUser;
import dswork.common.model.IBind;
import dswork.common.model.IUserBind;
import dswork.http.HttpUtil;

public class WeiboUtil
{
	public static final String API_ACCESSTOKEN = "https://api.weibo.com/oauth2/access_token";
	public static final String API_TOKEN_INFO = "https://api.weibo.com/oauth2/get_token_info";
	public static final String API_USER = "https://api.weibo.com/2/users/show.json";

	private WeiboUtil()
	{
	}

	public static WeiboAccessToken getAccessToken(String bind, String code)
	{
		IBind ws = WebssoUtil.get(bind);
		String url = new StringBuilder(API_ACCESSTOKEN)//
				.append("?client_id=").append(ws.getAppid())//
				.append("&client_secret=").append(ws.getAppsecret())//
				.append("&grant_type=").append("authorization_code")//
				.append("&redirect_uri=").append(ws.getAppreturnurl())//
				.append("&code=").append(code)//
				.toString();
		String json = new HttpUtil().create(url).setRequestMethod("POST").connect();
		return WebssoUtil.toBean(json, WeiboAccessToken.class);
	}

	public static WeiboTokenInfo getOpenid(String access_token)
	{
		String url = new StringBuilder(API_TOKEN_INFO)//
				.append("?access_token=").append(access_token)//
				.toString();
		String json = new HttpUtil().create(url).setRequestMethod("POST").connect();
		return WebssoUtil.toBean(json, WeiboTokenInfo.class);
	}

	public static WeiboUser getUser(String access_token, String openid)
	{
		String url = new StringBuilder(API_USER)//
				.append("?access_token=").append(access_token)//
				.append("&uid=").append(openid)//
				.toString();
		String json = new HttpUtil().create(url).setRequestMethod("GET").connect();
		WeiboUser po = WebssoUtil.toBean(json, WeiboUser.class);
		// po.getOpenid(openid);// 保存openid
		return po;
	}
	
	public static IUserBind getUserBind(String bindid, String code)
	{
		IUserBind userBind = null;
		WeiboAccessToken accessToken = getAccessToken(bindid, code);
		WeiboTokenInfo tokenInfo = null;
		if(accessToken != null)
		{
			tokenInfo = getOpenid(accessToken.getAccesstoken());
			if(tokenInfo != null)
			{
				WeiboUser user = getUser(accessToken.getAccesstoken(), tokenInfo.getOpenid());
				if(user != null)
				{
					int sex = 1;// m男:n未知:f女
					if("f".equals(user.getGender()))
					{
						sex = 2;// 女性
					}
					String province = "";
					String city = "";
					try
					{
						String[] arr = (user.getLocation() + "").trim().split(" ", -1);
						if(arr.length == 2)
						{
							province = arr[0];
							city = arr[1];
						}
					}
					catch(Exception x)
					{
					}
					userBind = new IUserBind();
					userBind.setOpenid(user.getOpenid());
					userBind.setUnionid("");
					userBind.setName(user.getName());
					userBind.setSex(sex);
					userBind.setAvatar(user.getProfileImageUrl());
					userBind.setCountry("中国");
					userBind.setProvince(province);
					userBind.setCity(city);
				}
			}
		}
		return userBind;
	}
}
