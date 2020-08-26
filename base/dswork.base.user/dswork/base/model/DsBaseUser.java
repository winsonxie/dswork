/**
 * 用户Model
 */
package dswork.base.model;

public class DsBaseUser
{
	private Long id = 0L;// ID
	private int status = 1;// 状态(1启用,0禁用)
	private String own;// 扩展用户身份标识
	private String ssqy = "";// 最长12位的区域编码-区域名称
	private String name = "";// 姓名
	private String mobile = "";// 手机
	private String account = "";// 帐号
	private String password = "";// 密码
	private String workcard = "";// 工作证号
	private int sex = 0;// 性别(0未知,1男,2女)
	private String avatar = "";// 头像
	private String idcard = "";// 身份证号
	private String email = "";// 电子邮箱
	private String phone = "";// 电话
	private Long orgpid = 0L;// 所属单位ID
	private Long orgid = 0L;// 所属部门ID
	private String orgpname = "";// 单位名称
	private String orgname = "";// 部门名称
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
		this.status = status != 1 ? 0 : 1;
	}

	public String getOwn()
	{
		return own;
	}

	public void setOwn(String own)
	{
		this.own = own;
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

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
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

	public String getOrgpname()
	{
		return orgpname;
	}

	public void setOrgpname(String orgpname)
	{
		this.orgpname = orgpname;
	}

	public String getOrgname()
	{
		return orgname;
	}

	public void setOrgname(String orgname)
	{
		this.orgname = orgname;
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

	@Override
	public String toString()
	{
		try
		{
			return new StringBuilder().append("{\"id\":").append(id).append(",\"name\":\"").append(name.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")).append("\"}").toString();
		}
		catch(Exception e)
		{
			return "{\"id\":0,\"name\":\"\"}";
		}
	}
}
