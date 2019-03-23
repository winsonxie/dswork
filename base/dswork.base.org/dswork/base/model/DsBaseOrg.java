/**
 * 组织机构Model
 */
package dswork.base.model;

public class DsBaseOrg
{
	// 部门ID
	private Long id = 0L;
	// 上级ID(本表)
	private Long pid = 0L;
	// 名称
	private String name = "";
	// 类型(2单位,1部门,0岗位)
	private Integer status = -1;
	// 排序
	private Long seq = 0L;
	// 职责范围
	private String dutyscope = "";
	// 备注
	private String memo = "";

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setPid(Long pid)
	{
		this.pid = (pid == null || pid <= 0) ? 0 : pid;
	}

	public Long getPid()
	{
		return this.pid;
	}

	public void setName(String name)
	{
		this.name = String.valueOf(name).trim().replaceAll("\r", "").replaceAll("\n", "");
	}

	public String getName()
	{
		return this.name;
	}

	public void setDutyscope(String dutyscope)
	{
		this.dutyscope = dutyscope;
	}

	public String getDutyscope()
	{
		return this.dutyscope;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public String getMemo()
	{
		return this.memo;
	}

	public Long getSeq()
	{
		return seq;
	}

	public void setSeq(Long seq)
	{
		this.seq = seq;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
		if(status == null || status.intValue() > 2 || status.intValue() < 0)
		{
			this.status = 0;
		}
		else
		{
			this.status = status;//0,1,2
		}
	}

	@Override
	public String toString()
	{
		try
		{
			return new StringBuilder().append("{\"id\":").append(id).append(",\"pid\":").append(pid).append(",\"status\":").append(status).append(",\"isParent\":").append(status>0?"true":"false").append(",\"name\":\"").append(name.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")).append("\"}").toString();
		}
		catch (Exception e)
		{
			return "{\"id\":0,\"pid\":-1,\"status\":-1,\"isParent\":false,\"name\":\"\"}";
		}
	}
}
