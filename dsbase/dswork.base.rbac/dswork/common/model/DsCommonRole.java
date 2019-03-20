/**
 * 角色Model
 */
package dswork.common.model;

public class DsCommonRole
{
	// 主键
	private Long id = 0L;
	// 上级ID(本表)
	private Long pid = 0L;
	// 所属系统ID
	private Long systemid = 0L;
	// 名称
	private String name = "";
	// 备注
	private String memo = "";
	// 排序
	private Integer seq = 0;
	// 选择角色时处理使用
	boolean checked = false;

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

	public void setSystemid(Long systemid)
	{
		this.systemid = (systemid == null || systemid <= 0) ? 0 : systemid;
	}

	public Long getSystemid()
	{
		return this.systemid;
	}

	public void setName(String name)
	{
		this.name = String.valueOf(name).trim();
	}

	public String getName()
	{
		return this.name;
	}

	public Integer getSeq()
	{
		return seq;
	}

	public void setSeq(Integer seq)
	{
		this.seq = seq;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public String getMemo()
	{
		return this.memo;
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	@Override
	public String toString()
	{
		try
		{
			return new StringBuilder().append("{\"id\":").append(id).append(",\"pid\":").append(pid).append(",\"isParent\":true").append(",\"checked\":").append(isChecked()).append(",\"name\":\"").append(name.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")).append("\"}").toString();
		}
		catch(Exception e)
		{
			return "{\"id\":0,\"pid\":-1,\"isParent\":true,\"checked\":false,\"name\":\"\"}";
		}
	}
}
