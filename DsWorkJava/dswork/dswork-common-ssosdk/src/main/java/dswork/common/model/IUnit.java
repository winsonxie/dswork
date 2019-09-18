package dswork.common.model;

public class IUnit
{
	private Long id = 0L;// ID
	private int status = 0;// 状态（1启用，0禁用）
	private String name = "";// 应用名称
	private String appid = "";// 应用ID
	private String appsecret = "";// 应用秘钥
	private String returnurl = "";// 回调地址
	private long type = 0L;// 标记，值相同的为同一个APP
	private String createtime = "";// 创建时间
	private long lasttime = 0L;// 最后更新时间
	private String memo = "";// 备注说明

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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

	public String getReturnurl()
	{
		return returnurl;
	}

	public void setReturnurl(String returnurl)
	{
		this.returnurl = returnurl;
	}

	public long getType()
	{
		return type;
	}

	public void setType(long type)
	{
		this.type = type;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
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
