package dswork.common.model;

public class IBind
{
	private long id = 0L;// 应用ID
	private int status = 0;// 状态（1启用，0禁用）
	private String name = "";// 应用名称
	private String appid = "";// 第三方应用ID
	private String appsecret = "";// 第三方应用密钥
	private String appkeyprivate = "";// 第三方应用私钥
	private String appkeypublic = "";// 第三方应用公钥
	private String appreturnurl = "";// 第三方应用回调地址
	private String apptype = "";// 第三方应用类型
	private String createtime = "";// 创建时间
	private long lasttime = 0L;// 最后更新时间
	private String memo = "";// 备注

	public long getId()
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAppid()
	{
		return appid;
	}

	public void setAppid(String appid)
	{
		this.appid = appid;
	}

	public String getAppsecret()
	{
		return appsecret;
	}

	public void setAppsecret(String appsecret)
	{
		this.appsecret = appsecret;
	}

	public String getAppkeyprivate()
	{
		return appkeyprivate;
	}

	public void setAppkeyprivate(String appkeyprivate)
	{
		this.appkeyprivate = appkeyprivate;
	}

	public String getAppkeypublic()
	{
		return appkeypublic;
	}

	public void setAppkeypublic(String appkeypublic)
	{
		this.appkeypublic = appkeypublic;
	}

	public String getAppreturnurl()
	{
		return appreturnurl;
	}

	public void setAppreturnurl(String appreturnurl)
	{
		this.appreturnurl = appreturnurl;
	}

	public String getApptype()
	{
		return apptype;
	}

	public void setApptype(String apptype)
	{
		this.apptype = apptype;
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

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}
}
