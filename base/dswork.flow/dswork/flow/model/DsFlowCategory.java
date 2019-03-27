/**
 * 流程管理Model
 */
package dswork.flow.model;

public class DsFlowCategory
{
	// 部门ID
	private Long id = 0L;
	// 上级ID(本表,所属组织)
	private Long pid = 0L;
	// 名称
	private String name = "";
	// 排序
	private Long seq = 0L;

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
		this.name = String.valueOf(name).trim();
	}

	public String getName()
	{
		return this.name;
	}

	public Long getSeq()
	{
		return seq;
	}

	public void setSeq(Long seq)
	{
		this.seq = seq;
	}

	@Override
	public String toString()
	{
		try
		{
			return new StringBuilder().append("{\"id\":").append(id).append(",\"pid\":").append(pid).append(",\"isParent\":true").append(",\"name\":\"").append(name.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")).append("\"}").toString();
		}
		catch(Exception e)
		{
			return "{\"id\":0,\"pid\":-1,\"isParent\":false,\"name\":\"\"}";
		}
	}
}
