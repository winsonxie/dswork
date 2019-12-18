package dswork.sso.util.websso;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import dswork.common.SsoFactory;
import dswork.common.model.IBind;
import dswork.common.model.IUserBind;

public class WebssoUtil
{
	static com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	static com.google.gson.Gson gson = builder.create();

	public static String toJson(Object object)
	{
		return gson.toJson(object);
	}

	public static <T> T toBean(String json, Class<T> classOfT)
	{
		return gson.fromJson(json, classOfT);
	}

	private WebssoUtil()
	{
	}
	private static ConcurrentMap<String, IBind> map = new ConcurrentHashMap<String, IBind>();

	public static void refresh()
	{
		List<IBind> list = SsoFactory.getSsoDao().queryListBind();
		ConcurrentMap<String, IBind> _map = new ConcurrentHashMap<String, IBind>();
		for(IBind m : list)
		{
			_map.put(m.getId() + "", m);
		}
		map = _map;
	}

	public static IBind get(String bindid)
	{
		return map.get(bindid);
	}

	/**
	 * @param bindid
	 * @param code
	 * @param data 微信小程序的用户信息的加密数据
	 * @param iv 微信小程序的加密算法的对称解密算法初始向量
	 * @return
	 */
	public static IUserBind getUserBind(String bindid, String code, String data, String iv)
	{
		IUserBind userBind = null;
		IBind bind = get(bindid);
		if(bind != null)
		{
			if("weibo".equals(bind.getApptype()))
			{
				userBind = WeiboUtil.getUserBind(bindid, code);
			}
			else if(bind.getApptype().startsWith("wechat"))
			{
				userBind = WechatUtil.getUserBind(bindid, code, data, iv);
				// if("wechat-qrcode".equals(bind.getApptype()))
				// {
				// userBind = WechatUtil.getUserBind(bindid, code);
				// }
				// else if("wechat-mini".equals(bind.getApptype()))// wechat-mini || wechat
				// {
				// userBind = WechatUtil.getUserBind(bindid, code, data, iv);
				// }
			}
			else if("qq".equals(bind.getApptype()))
			{
				userBind = QqUtil.getUserBind(bindid, code);
			}
			else if("alipay".equals(bind.getApptype()))
			{
				userBind = AlipayUtil.getUserBind(bindid, code);
			}
			if(null != userBind)
			{
				userBind.setBindid(bind.getId());
			}
		}
		return userBind;
	}
}
