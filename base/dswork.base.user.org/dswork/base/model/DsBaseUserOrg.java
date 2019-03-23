/**
 * 用户岗位Model
 */
package dswork.base.model;

public class DsBaseUserOrg
{
	// ID
	private Long id = 0L;
	// 岗位ID
	private Long orgid = 0L;
	// 用户ID
	private Long userid = 0L;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getOrgid()
	{
		return orgid;
	}

	public void setOrgid(Long orgid)
	{
		this.orgid = orgid;
	}

	public Long getUserid()
	{
		return userid;
	}

	public void setUserid(Long userid)
	{
		this.userid = userid;
	}
}
