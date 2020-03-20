package dswork.sso.util.websso;

import java.util.Map;

import dswork.sso.model.wechat.WechatAccessToken;
import dswork.sso.model.wechat.WechatUserinfo;
import dswork.common.model.IBind;
import dswork.common.model.IUserBind;
import dswork.http.HttpUtil;

@SuppressWarnings("all")
public class WechatUtil
{
	public static final String API_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static final String API_USERINFO = "https://api.weixin.qq.com/sns/userinfo";
	public static final String API_MINI_SESSION = "https://api.weixin.qq.com/sns/jscode2session";

	public static WechatAccessToken getAccessToken(String bind, String auth_code)
	{
		IBind ws = WebssoUtil.get(bind);
		StringBuilder sb = new StringBuilder().append(API_ACCESSTOKEN)//
				.append("?appid=").append(ws.getAppid())//
				.append("&secret=").append(ws.getAppsecret())//
				.append("&code=").append(auth_code)//
				.append("&grant_type=authorization_code");
		String json = new HttpUtil().create(sb.toString()).connect();
		return WebssoUtil.toBean(json, WechatAccessToken.class);
	}

	public static WechatUserinfo getUserInfo(String access_token, String openid)
	{
		StringBuilder sb = new StringBuilder().append(API_USERINFO)//
				.append("?access_token=").append(access_token)//
				.append("&openid=").append(openid);
		String json = new HttpUtil().create(sb.toString()).connect();
		return WebssoUtil.toBean(json, WechatUserinfo.class);
	}

	/**
	 * 小程序登录凭证校验
	 * @param bind
	 * @param code
	 * @return
	 */
	public static Map<String, Object> code2Session(String bind, String code)
	{
		IBind ws = WebssoUtil.get(bind);
		StringBuilder sb = new StringBuilder().append(API_MINI_SESSION)//
				.append("?appid=").append(ws.getAppid())//
				.append("&secret=").append(ws.getAppsecret())//
				.append("&js_code=").append(code)//
				.append("&grant_type=").append("authorization_code");
		String json = new HttpUtil().create(sb.toString()).connect();
		return WebssoUtil.toBean(json, Map.class);
	}

	/**
	 * @param bindid
	 * @param codeOraccessToken Apptype：[wecha-app：accessToken，wechat-mini：code]
	 * @param dataOrOpenid Apptype：[wecha-app：openid，wechat-mini：data]
	 * @param iv Apptype：[wecha-app：null，wechat-mini：iv]
	 * @return
	 */
	public static IUserBind getUserBind(String bindid, String codeOraccessToken, String dataOrOpenid, String iv)
	{
		IUserBind userBind = null;
		IBind ws = WebssoUtil.get(bindid);
		if("wechat-mini".equals(ws.getApptype()))
		{
			Map<String, Object> map = code2Session(bindid, codeOraccessToken);
			if(map != null && 0D == Double.parseDouble(map.get("errcode").toString()))
			{
				String session_key = map.get("session_key").toString();
				String user = decodeAes(dataOrOpenid, session_key, iv);// 解密
				map = WebssoUtil.toBean(user, Map.class);
				userBind = new IUserBind();
				userBind.setOpenid(map.get("openId").toString());
				userBind.setUnionid(map.get("unionId").toString());
				userBind.setName(map.get("nickName").toString());
				userBind.setSex(Integer.parseInt(map.get("gender").toString()));
				userBind.setAvatar(map.get("avatarUrl").toString());
				userBind.setCountry(map.get("country").toString());
				userBind.setProvince(map.get("province").toString());
				userBind.setCity(map.get("city").toString());
			}
		}
		else
		{
			if(!"wechat-app".equals(ws.getApptype()))
			{
				WechatAccessToken waccessToken = getAccessToken(bindid, codeOraccessToken);
				codeOraccessToken = waccessToken.getAccesstoken();
				dataOrOpenid = waccessToken.getOpenid();
			}
			if(codeOraccessToken != null && dataOrOpenid != null)
			{
				WechatUserinfo user = getUserInfo(codeOraccessToken, dataOrOpenid);
				if(user != null)
				{
					int sex = 1;// 男性"1".equals(user.getSex())
					if("2".equals(user.getSex()))
					{
						sex = 2;// 女性
					}
					userBind = new IUserBind();
					userBind.setOpenid(user.getOpenid());
					userBind.setUnionid(user.getUnionid());
					userBind.setName(user.getNickname());
					userBind.setSex(sex);
					userBind.setAvatar(user.getHeadimgurl());
					userBind.setCountry(user.getCountry());
					userBind.setProvince(user.getProvince());
					userBind.setCity(user.getCity());
				}
			}
		}
		return userBind;
	}

	private static String decodeAes(String data, String key, String iv)
	{
		try
		{
			// 对称解密的目标密文
			byte[] dataByte = dswork.core.util.EncryptUtil.decodeByteBase64(data);
			// 对称解密秘钥
			byte[] keyByte = dswork.core.util.EncryptUtil.decodeByteBase64(key);
			// 对称解密算法初始向量
			byte[] ivByte = dswork.core.util.EncryptUtil.decodeByteBase64(iv);
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS7Padding");
			javax.crypto.spec.SecretKeySpec spec = new javax.crypto.spec.SecretKeySpec(keyByte, "AES");
			java.security.AlgorithmParameters parameters = java.security.AlgorithmParameters.getInstance("AES");
			parameters.init(new javax.crypto.spec.IvParameterSpec(ivByte));
			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if(null != resultByte && resultByte.length > 0)
			{
				return new String(resultByte, "UTF-8");
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}
}
