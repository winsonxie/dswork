package dswork.sso.util.websso;

import dswork.sso.model.qq.QqAccessToken;
import dswork.sso.model.qq.QqOpenid;
import dswork.sso.model.qq.QqUserinfo;
import dswork.common.model.IBind;
import dswork.common.model.IUserBind;
import dswork.http.HttpUtil;

public class QqUtil
{
	public static final String API_ACCESSTOKEN = "https://graph.qq.com/oauth2.0/token";
	public static final String API_OPENID = "https://graph.qq.com/oauth2.0/me";
	public static final String API_USERINFO = "https://graph.qq.com/user/get_user_info";

	private QqUtil()
	{
	}

	public static QqAccessToken getAccessToken(String bind, String code)
	{
		IBind ws = WebssoUtil.get(bind);
		String url = new StringBuilder(API_ACCESSTOKEN)//
				.append("?grant_type=").append("authorization_code")//
				.append("&client_id=").append(ws.getAppid())//
				.append("&client_secret=").append(ws.getAppsecret())//
				.append("&code=").append(code)//
				.append("&redirect_uri=").append(ws.getAppreturnurl())//
				.toString();
		String query = new HttpUtil().create(url).setRequestMethod("GET").connect();
		String json = queryToJson(query);
		return WebssoUtil.toBean(json, QqAccessToken.class);
	}

	public static QqOpenid getOpenid(String access_token)
	{
		String url = new StringBuilder(API_OPENID).append("?access_token=").append(access_token).toString();
		String json = new HttpUtil().create(url).setRequestMethod("GET").connect();
		json = json.substring(json.indexOf("(") + 1, json.indexOf(")")).trim();
		return WebssoUtil.toBean(json, QqOpenid.class);
	}

	public static QqUserinfo getUserInfo(String bind, String access_token, String openid)
	{
		IBind ws = WebssoUtil.get(bind);
		String url = new StringBuilder(API_USERINFO)//
				.append("?access_token=").append(access_token)//
				.append("&oauth_consumer_key=").append(ws.getAppid())//
				.append("&openid=").append(openid)//
				.toString();
		String json = new HttpUtil().create(url).setRequestMethod("GET").connect();
		QqUserinfo po = WebssoUtil.toBean(json, QqUserinfo.class);
		po.setOpenid(openid);// 保存openid
		return po;
	}

	public static String queryToJson(String query)
	{
		StringBuilder sb = new StringBuilder("{");
		String[] term = query.split("&");
		if(term.length > 0)
		{
			String[] ss = term[0].split("=");
			if(ss.length == 2)
			{
				sb.append("\"").append(ss[0]).append("\":");
				sb.append("\"").append(ss[1]).append("\"");
			}
			for(int i = 1; i < term.length; i++)
			{
				ss = term[i].split("=");
				if(ss.length == 2)
				{
					sb.append(",");
					sb.append("\"").append(ss[0]).append("\":");
					sb.append("\"").append(ss[1]).append("\"");
				}
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	public static IUserBind getUserBind(String bindid, String code)
	{
		IUserBind userBind = null;
		QqAccessToken accessToken = getAccessToken(bindid, code);
		QqOpenid openid = null;
		if(accessToken != null)
		{
			openid = getOpenid(accessToken.getAccesstoken());
			if(openid != null)
			{
				QqUserinfo user = getUserInfo(bindid, accessToken.getAccesstoken(), openid.getOpenid());
				if(user != null)
				{
					int sex = 1;
					if(!"男".equals(user.getGender()))
					{
						sex = 2;// 女性
					}
					userBind = new IUserBind();
					userBind.setOpenid(user.getOpenid());
					userBind.setUnionid("");
					userBind.setName(user.getNickname());
					userBind.setSex(sex);
					userBind.setAvatar(user.getFigureurl());
					userBind.setCountry("中国");
					userBind.setProvince("");
					userBind.setCity("");
				}
			}
		}
		return userBind;
	}
}
