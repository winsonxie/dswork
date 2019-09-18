package dswork.common.model;

/**
 * 第三方用户表
 * @author skey
 */
public class IUserBind
{
	private long id = 0L;// 主键，登录时读到的用户只有id=id这个账号
	private long bindid = 0L;// 所属来源应用ID
	private long userid = 0L;// 主用户ID
	private String openid = "";// openid
	private String unionid = "";// unionid
	private String name = "";// 昵称
	private int sex = 0;// 性别（0未知，1男，2女）
	private String country = "";// 国家
	private String province = "";// 省份
	private String city = "";// 城市
	private String avatar = "";// 头像
	private String createtime = "";// 创建时间
	private long lasttime = 0L;// 最后更新时间

	public Long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getBindid()
	{
		return bindid;
	}

	public void setBindid(long bindid)
	{
		this.bindid = bindid;
	}

	public long getUserid()
	{
		return userid;
	}

	public void setUserid(long userid)
	{
		this.userid = userid;
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSex()
	{
		return sex;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
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

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public String getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(String createtime)
	{
		this.createtime = createtime;
	}

	public long getLasttime()
	{
		return lasttime;
	}

	public void setLasttime(long lasttime)
	{
		this.lasttime = lasttime;
	}
}
