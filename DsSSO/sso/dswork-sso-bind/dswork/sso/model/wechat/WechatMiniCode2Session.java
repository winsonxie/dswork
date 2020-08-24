package dswork.sso.model.wechat;

public class WechatMiniCode2Session
{
	// 0: 正确返回
	private Integer errcode;
	// 错误信息
	private String errmsg = "";
	// 会话密钥
	private String session_key = "";
	// 普通用户的标识，对当前开发者帐号唯一
	private String openid = "";
	// 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的(开发者最好保存用户unionID信息，以便以后在不同应用中进行用户信息互通。)
	private String unionid = "";

	public Integer getErrcode()
	{
		return errcode;
	}

	public void setErrcode(Integer errcode)
	{
		this.errcode = errcode;
	}

	public String getErrmsg()
	{
		return errmsg;
	}

	public void setErrmsg(String errmsg)
	{
		this.errmsg = errmsg;
	}

	public String getSession_key()
	{
		return session_key;
	}

	public void setSession_key(String session_key)
	{
		this.session_key = session_key;
	}

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
	}

	public String getUnionid()
	{
		return unionid;
	}

	public void setUnionid(String unionid)
	{
		this.unionid = unionid;
	}
}
