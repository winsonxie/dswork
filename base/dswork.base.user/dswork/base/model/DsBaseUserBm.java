/**
 * 登录用户账号Model
 */
package dswork.base.model;

public class DsBaseUserBm
{
	// 账号唯一标识
	private String bm = "";
	// USERID
	private long userid = 0L;
	// 类型(0账号,1手机号,2身份证)
	private int type = 0;

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
		this.type = type == 2 ? 2 : (type == 1 ? 1 : 0);
	}
}
