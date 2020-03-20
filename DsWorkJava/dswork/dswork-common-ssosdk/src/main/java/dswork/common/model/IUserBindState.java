package dswork.common.model;

/**
 * 用户绑定第三方状态
 * @author skey
 */
public class IUserBindState
{
	private long id = 0L;// 应用ID
	private int status = 0;// 状态（1启用，0禁用）
	private String name = "";// 应用名称
	private String appid = "";// 第三方应用ID
	private String appreturnurl = "";// 第三方应用回调地址
	private String apptype = "";// 第三方应用类型
	private String memo = "";// 备注
	private long userbindid = 0L;// 第三方用户表的ID
	private long userid = 0L;// 第三方绑定的用户ID
	private boolean isBind = false;// 绑定状态

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

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public long getUserbindid()
	{
		return userbindid;
	}

	public void setUserbindid(long userbindid)
	{
		this.userbindid = userbindid;
	}

	public void setBind(boolean isBind)
	{
		this.isBind = isBind;
	}

	public long getUserid()
	{
		return userid;
	}

	public void setUserid(long userid)
	{
		this.userid = userid;
		setBind();
	}

	public boolean isBind()
	{
		return isBind;
	}

	public void setBind()
	{
		this.isBind = this.userid != 0 ? true : false;
	}
}
