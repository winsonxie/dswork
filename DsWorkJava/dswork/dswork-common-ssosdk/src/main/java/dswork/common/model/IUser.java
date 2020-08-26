package dswork.common.model;

/**
 * 所有需要返回给第三方的信息，都需要调用clearSecret()再返回
 * @author skey
 */
public class IUser
{
	private long id = 0L;// 主键，登录时读到的用户只有id=id这个账号
	private int status = 0;// 状态（0禁用，1启用）
	private String own = "";// 扩展用户身份标识
	private String ssqy = "";// 最长12位的区域编码-区域名称
	private String name = "";// 姓名
	private String mobile = "";// 手机
	private String account = "";// 自定义账号
	private String password = "";// 密码
	private String workcard = "";// 工作证号
	private int sex = 0;// 性别（0未知，1男，2女）
	private String avatar = "";// 头像
	private String idcard = "";// 身份证号
	private String email = "";// 电子邮件
	private String phone = "";// 电话
	private long orgpid = 0L;// 所属单位
	private long orgid = 0L;// 所属部门
	private String type = "";// 类型
	private String typename = "";// 类型名称
	private String exalias = "";// 类型扩展标识
	private String exname = "";// 类型扩展名称
	private String createtime = "";// 创建时间
	private long lasttime = 0L;// 最后更新时间
	private String exdata = "";// 用于存放自定义对象的json值
	private long binduserid = 0L;// 当前登录用户子用户id，一般设置为哪个第三方账号登录的

	public IUser clearSecret()
	{
		this.password = null;
		return this;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(long id)
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

	public long getOrgpid()
	{
		return orgpid;
	}

	public void setOrgpid(long orgpid)
	{
		this.orgpid = orgpid;
	}

	public long getOrgid()
	{
		return orgid;
	}

	public void setOrgid(long orgid)
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

	public String getExdata()
	{
		return exdata;
	}

	public void setExdata(String exdata)
	{
		this.exdata = exdata;
	}

	public long getBinduserid()
	{
		return binduserid;
	}

	public void setBinduserid(long binduserid)
	{
		this.binduserid = binduserid;
	}
}
