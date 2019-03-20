/**
 * 岗位角色Model
 */
package dswork.common.model;

public class DsCommonOrgRole
{
	// ID
	private Long id = 0L;
	// 岗位ID
	private Long orgid = 0L;
	// 角色ID
	private Long roleid = 0L;
	// 角色名称
	private String rolename = "";
	// 系统ID
	private Long systemid = 0L;
	// 系统名称
	private String systemname = "";

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

	public Long getRoleid()
	{
		return roleid;
	}

	public void setRoleid(Long roleid)
	{
		this.roleid = roleid;
	}

	public String getRolename()
	{
		return rolename;
	}

	public void setRolename(String rolename)
	{
		this.rolename = rolename;
	}

	public Long getSystemid()
	{
		return systemid;
	}

	public void setSystemid(Long systemid)
	{
		this.systemid = systemid;
	}

	public String getSystemname()
	{
		return systemname;
	}

	public void setSystemname(String systemname)
	{
		this.systemname = systemname;
	}
}
