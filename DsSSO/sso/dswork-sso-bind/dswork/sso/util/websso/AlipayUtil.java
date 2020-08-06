package dswork.sso.util.websso;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dswork.sso.model.alipay.AlipayAccessToken;
import dswork.sso.model.alipay.AlipayResponse;
import dswork.sso.model.alipay.AlipayUserinfo;
import dswork.common.model.IBind;
import dswork.common.model.IUserBind;
import dswork.core.util.EncryptUtil;
import dswork.core.util.TimeUtil;
import dswork.http.HttpUtil;
import dswork.http.NameValue;

public class AlipayUtil
{
	public static final String API_GATEWAY = "https://openapi.alipay.com/gateway.do";

	private AlipayUtil()
	{
	}

	public static String encryptRsa2(String str, String privateKey)
	{
		try
		{
			Signature signature = Signature.getInstance("SHA256WithRSA");
			signature.initSign(KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(EncryptUtil.decodeByteBase64(privateKey))));
			signature.update(str.getBytes("UTF-8"));
			return EncryptUtil.encodeByteBase64(signature.sign());
		}
		catch(Exception e)
		{
		}
		return null;
	}

	public static AlipayAccessToken getAccessToken(String bind, String auth_code)
	{
		IBind ws = WebssoUtil.get(bind);
		Map<String, String> map = new HashMap<String, String>();
		map.put("app_id", ws.getAppid());
		map.put("method", "alipay.system.oauth.token");
		map.put("charset", "GBK");
		map.put("sign_type", "RSA2");
		map.put("timestamp", TimeUtil.getCurrentTime());
		map.put("version", "1.0");
		map.put("grant_type", "authorization_code");
		map.put("code", auth_code);
		map.put("sign", sign(ws.getAppkeyprivate(), map));
		String json = new HttpUtil().create(API_GATEWAY).addForms(getNameValues(map)).connect("GBK");
		AlipayResponse resp = WebssoUtil.toBean(json, AlipayResponse.class);
		return resp.getAccesstoken();
	}

	public static AlipayUserinfo getUserInfo(String bind, String access_token)
	{
		IBind ws = WebssoUtil.get(bind);
		Map<String, String> map = new HashMap<String, String>();
		map.put("app_id", ws.getAppid());
		map.put("method", "alipay.user.info.share");
		map.put("charset", "GBK");
		map.put("sign_type", "RSA2");
		map.put("timestamp", TimeUtil.getCurrentTime());
		map.put("version", "1.0");
		map.put("auth_token", access_token);
		map.put("sign", sign(ws.getAppkeyprivate(), map));
		String json = new HttpUtil().create(API_GATEWAY).addForms(getNameValues(map)).connect("GBK");
		AlipayResponse resp = WebssoUtil.toBean(json, AlipayResponse.class);
		return resp.getUserinfo();
	}

	private static NameValue[] getNameValues(Map<String, String> map)
	{
		NameValue[] arr = new NameValue[map.size()];
		List<String> keys = new ArrayList<String>(map.keySet());
		for(int i = 0; i < keys.size(); i++)
		{
			String k = keys.get(i);
			String v = map.get(k);
			arr[i] = new NameValue(k, v);
		}
		return arr;
	}

	private static String sign(String keyprivate, Map<String, String> map)
	{
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		String k = keys.get(0);
		String v = map.get(k);
		StringBuffer sb = new StringBuffer(k + "=" + v);
		for(int i = 1; i < keys.size(); i++)
		{
			k = keys.get(i);
			v = map.get(k);
			sb.append("&" + k + "=" + v);
		}
		return encryptRsa2(sb.toString(), keyprivate);
	}
	
	public static IUserBind getUserBind(String bindid, String code)
	{
		IUserBind userBind = null;
		AlipayAccessToken accessToken = getAccessToken(bindid, code);
		if(accessToken != null)
		{
			AlipayUserinfo user = getUserInfo(bindid, accessToken.getAccesstoken());
			if(user != null)
			{
				int sex = 1;
				if("T".equals(user.getIscertified()))
				{
					// 男性if("M".equals(user.getGender())){sex = 1;}
					if("F".equals(user.getGender()))
					{
						sex = 2;// 女性
					}
				}
				userBind = new IUserBind();
				userBind.setOpenid(user.getUserid());
				userBind.setUnionid(user.getUserid());
				userBind.setName(user.getNickname());
				userBind.setSex(sex);
				userBind.setAvatar(user.getAvatar());
				userBind.setCountry("中国");
				userBind.setProvince(user.getProvince());
				userBind.setCity(user.getCity());
			}
		}
		return userBind;
	}
}
