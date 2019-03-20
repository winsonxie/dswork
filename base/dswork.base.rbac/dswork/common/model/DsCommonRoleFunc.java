/**
 * 角色功能关系Model
 */
package dswork.common.model;

public class DsCommonRoleFunc
{
	// 主键ID
	private Long id = 0L;
	// 系统ID
	private Long systemid = 0L;
	// 角色ID
	private Long roleid = 0L;
	// 功能ID
	private Long funcid = 0L;

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setSystemid(Long systemid)
	{
		this.systemid = systemid;
	}

	public Long getSystemid()
	{
		return this.systemid;
	}

	public void setRoleid(Long roleid)
	{
		this.roleid = roleid;
	}

	public Long getRoleid()
	{
		return this.roleid;
	}

	public void setFuncid(Long funcid)
	{
		this.funcid = funcid;
	}

	public Long getFuncid()
	{
		return this.funcid;
	}
}
