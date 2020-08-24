package dswork.sso.util.websso;

import java.util.Map;

import dswork.common.model.IBind;
import dswork.common.model.IUserBind;
import dswork.common.util.ResponseUtil;
import dswork.http.HttpUtil;
import dswork.sso.model.wechat.WechatAccessToken;
import dswork.sso.model.wechat.WechatMiniCode2Session;
import dswork.sso.model.wechat.WechatMiniUserinfo;
import dswork.sso.model.wechat.WechatUserinfo;

@SuppressWarnings("all")
public class WechatUtil
{
	public static final String API_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static final String API_USERINFO = "https://api.weixin.qq.com/sns/userinfo";
	public static final String API_MINI_SESSION = "https://api.weixin.qq.com/sns/jscode2session";
	
	static
	{
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
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
	public static WechatMiniCode2Session code2Session(String bind, String code)
	{
		IBind ws = WebssoUtil.get(bind);
		StringBuilder sb = new StringBuilder().append(API_MINI_SESSION)//
				.append("?appid=").append(ws.getAppid())//
				.append("&secret=").append(ws.getAppsecret())//
				.append("&js_code=").append(code)//
				.append("&grant_type=").append("authorization_code");
		String json = new HttpUtil().create(sb.toString()).connect();
		return WebssoUtil.toBean(json, WechatMiniCode2Session.class);
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
			WechatMiniCode2Session c2s = code2Session(bindid, codeOraccessToken);
			if(c2s != null && !"".equals(c2s.getSession_key()))
			{
				String user = decodeAes(dataOrOpenid, c2s.getSession_key(), iv);// 解密
				WechatMiniUserinfo mini = WebssoUtil.toBean(user, WechatMiniUserinfo.class);
				userBind = new IUserBind();
				userBind.setOpenid(mini.getOpenId());
				userBind.setUnionid(mini.getUnionId());
				userBind.setName(mini.getNickName());
				userBind.setSex(mini.getGender());
				userBind.setAvatar(mini.getAvatarUrl());
				userBind.setCountry(mini.getCountry());
				userBind.setProvince(mini.getProvince());
				userBind.setCity(mini.getCity());
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
			// AES/CBC/PKCS7Padding，需要bcprov-jdk16-1.46.jar，微信文档要求使用
			// AES/CBC/PKCS5Padding，第一次doFinal不行，后面就可以
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
			e.printStackTrace();
		}
		return null;
	}
}
