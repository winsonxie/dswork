package dswork.sso.model.wechat;

public class WechatMiniUserinfo
{
	// 普通用户的标识，对当前开发者帐号唯一
	private String openId = "";
	// 用户在QQ空间的昵称。
	private String nickName = "";
	// 普通用户性别，1为男性，2为女性
	private int gender = 0;
	// 普通用户个人资料填写的省份
	private String province = "";
	// 普通用户个人资料填写的城市
	private String city = "";
	// 国家，如中国为CN
	private String country = "";
	// 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	// 请注意，在用户修改微信头像后，旧的微信头像URL将会失效，因此开发者应该自己在获取用户信息后，将头像图片保存下来，避免微信头像URL失效后的异常情况
	private String avatarUrl = "";
	// 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的(开发者最好保存用户unionID信息，以便以后在不同应用中进行用户信息互通。)
	private String unionId = "";

	public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public int getGender()
	{
		return gender;
	}

	public void setGender(int gender)
	{
		this.gender = gender;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getAvatarUrl()
	{
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl)
	{
		this.avatarUrl = avatarUrl;
	}

	public String getUnionId()
	{
		return unionId;
	}

	public void setUnionId(String unionId)
	{
		this.unionId = unionId;
	}
}
