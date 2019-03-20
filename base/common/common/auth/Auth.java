package common.auth;

// 根据实际的需要，可以增减里面的属性
public class Auth
{
	public static final int ADMIN = 0;
	public static final int USER = 1;
	public static final int ENTERPRISE = 2;
	
	private Long id;
	private String account;
	private String password;
	private String name;
	private String email;
	private String mobile;
	
	private Integer logintype;// 0系统用户，1个人用户，2企业用户
	private Integer loginstatus;// 1正常，0禁用
	private Integer usertype;// 系统（），个人（），企业（1管理员,0普通用户）
	
	private String ssxq;// 所属辖区，仅系统用户使用
	private String ssdw;// 所属单位，仅系统用户使用
	private String ssbm;// 所属部门，仅系统用户使用
	
	private String idcard;// 企业编码，仅个人用户使用
	
	private String qybm;// 企业编码，仅企业用户使用
	private Long qyid;// 企业ID，仅企业用户使用
	private Long qypid;// 企业PID，仅企业用户使用

	@Deprecated
	public Integer getLogintype()
	{
		return logintype;
	}
	public Auth setLogintype(Integer logintype)
	{
		this.logintype = logintype;
		return this;
	}
	public boolean isAdmin()
	{
		return logintype.intValue() == ADMIN;
	}
	public boolean isEnterprise()
	{
		return logintype.intValue() == ENTERPRISE;
	}
	public boolean isUser()
	{
		return logintype.intValue() == USER;
	}

	// 用于判断cms、bbs的拥有者
	public String getOwn()
	{
		if(isAdmin())
		{
			return "admin" + getAccount();
		}
		else if(isEnterprise())
		{
			return "ep" + getQybm();
		}
		else if(isUser())
		{
			return "person" + getAccount();
		}
		return "";
	}

	public Long getId()
	{
		return id;
	}

	public Auth setId(Long id)
	{
		this.id = id;
		return this;
	}

	public String getAccount()
	{
		return account;
	}

	public Auth setAccount(String account)
	{
		this.account = account;
		return this;
	}

	public String getPassword()
	{
		return password;
	}

	public Auth setPassword(String password)
	{
		this.password = password;
		return this;
	}

	public String getName()
	{
		return name;
	}

	public Auth setName(String name)
	{
		this.name = name;
		return this;
	}

	public String getEmail()
	{
		return email;
	}

	public Auth setEmail(String email)
	{
		this.email = email;
		return this;
	}

	public String getMobile()
	{
		return mobile;
	}

	public Auth setMobile(String mobile)
	{
		this.mobile = mobile;
		return this;
	}

	public Integer getLoginstatus()
	{
		return loginstatus;
	}

	public Auth setLoginstatus(Integer loginstatus)
	{
		this.loginstatus = loginstatus;
		return this;
	}

	public Integer getUsertype()
	{
		return usertype;
	}

	public Auth setUsertype(Integer usertype)
	{
		this.usertype = usertype;
		return this;
	}

	public String getSsxq()
	{
		return ssxq;
	}

	public Auth setSsxq(String ssxq)
	{
		this.ssxq = ssxq;
		return this;
	}

	public String getSsdw()
	{
		return ssdw;
	}

	public Auth setSsdw(String ssdw)
	{
		this.ssdw = ssdw;
		return this;
	}

	public String getSsbm()
	{
		return ssbm;
	}

	public Auth setSsbm(String ssbm)
	{
		this.ssbm = ssbm;
		return this;
	}

	public String getIdcard()
	{
		return idcard;
	}

	public Auth setIdcard(String idcard)
	{
		this.idcard = idcard;
		return this;
	}

	public String getQybm()
	{
		return qybm;
	}

	public Auth setQybm(String qybm)
	{
		this.qybm = qybm;
		return this;
	}

	public Long getQyid()
	{
		return qyid;
	}

	public Auth setQyid(Long qyid)
	{
		this.qyid = qyid;
		return this;
	}

	public Long getQypid()
	{
		return qypid;
	}

	public Auth setQypid(Long qypid)
	{
		this.qypid = qypid;
		return this;
	}
}
