package dswork.common.model;

/**
 * 用户账号
 */
public class IUserBm
{
	private String bm = "";// 唯一标识
	private long userid = 0L;// USERID
	private int type = 0;// 类型(0账号,1手机号,2身份证)

	public String getBm()
	{
		return bm;
	}

	public void setBm(String bm)
	{
		this.bm = bm;
	}

	public long getUserid()
	{
		return userid;
	}

	public void setUserid(long userid)
	{
		this.userid = userid;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
}
