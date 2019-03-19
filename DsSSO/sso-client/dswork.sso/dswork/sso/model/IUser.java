/**
 * @描述：用户
 */
package dswork.sso.model;

import java.io.Serializable;

public class IUser implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Long id = 0L;// 主键
	// private Long bindid = 0L;// 所属来源应用ID
	private int status = 0;// 状态（0禁用，1启用）
	// private String openid = "";// openid
	// private String unionid = "";// unionid
	private String bm = "";// 唯一标识（可为id或身份证等信息）
	private String ssqy = "";// 最长12位的区域编码
	private String name = "";// 姓名
	private String mobile = "";// 手机
	private String account = "";// 自定义账号
	// private String password = "";// 密码
	private String workcard = "";// 工作证号
	private int sex = 0;// 性别（0未知，1男，2女）
	private String country = "";// 国家
	private String province = "";// 省份
	private String city = "";// 城市
	private String avatar = "";// 头像
	private String idcard = "";// 身份证号
	private String email = "";// 电子邮件
	private String phone = "";// 电话
	private Long orgpid = 0L;// 所属单位
	private Long orgid = 0L;// 所属部门
	private String type = "";// 类型
	private String typename = "";// 类型名称
	private String exalias = "";// 类型扩展标识
	private String exname = "";// 类型扩展名称
	private String createtime = "";// 创建时间
	private long lasttime = 0L;// 最后更新时间

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getBm()
	{
		return bm;
	}

	public void setBm(String bm)
	{
		this.bm = bm;
	}

	public String getSsqy()
	{
		return ssqy;
	}

	public void setSsqy(String ssqy)
	{
		this.ssqy = ssqy;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getWorkcard()
	{
		return workcard;
	}

	public void setWorkcard(String workcard)
	{
		this.workcard = workcard;
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

	public String getIdcard()
	{
		return idcard;
	}

	public void setIdcard(String idcard)
	{
		this.idcard = idcard;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Long getOrgpid()
	{
		return orgpid;
	}

	public void setOrgpid(Long orgpid)
	{
		this.orgpid = orgpid;
	}

	public Long getOrgid()
	{
		return orgid;
	}

	public void setOrgid(Long orgid)
	{
		this.orgid = orgid;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTypename()
	{
		return typename;
	}

	public void setTypename(String typename)
	{
		this.typename = typename;
	}

	public String getExalias()
	{
		return exalias;
	}

	public void setExalias(String exalias)
	{
		this.exalias = exalias;
	}

	public String getExname()
	{
		return exname;
	}

	public void setExname(String exname)
	{
		this.exname = exname;
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
