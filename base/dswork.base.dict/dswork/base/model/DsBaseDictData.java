/**
 * 字典项Model
 */
package dswork.base.model;

public class DsBaseDictData
{
	// 编码
	private String id = "";
	// 父编码
	private String pid = "";
	// 引用名
	private String name = "";
	// 名称
	private String label = "";
	// 分组标记
	private String mark = "";
	// 层级(任意树形时为0,否则等于节点层级)
	private Integer level = 0;
	// 状态(1树叉,0树叶)
	private Integer status = 0;
	// 排序
	private Long seq = 0L;
	// 备注
	private String memo = "";

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = String.valueOf(id).replaceAll("\r", "").replaceAll("\n", "");
	}

	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = String.valueOf(pid).replaceAll("\r", "").replaceAll("\n", "");
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = String.valueOf(name).replaceAll("\r", "").replaceAll("\n", "");
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = String.valueOf(label).replaceAll("\r", "").replaceAll("\n", "");
	}

	public String getMark()
	{
		return mark;
	}

	public void setMark(String mark)
	{
		this.mark = mark;
	}

	public Integer getLevel()
	{
		return level;
	}

	public void setLevel(Integer level)
	{
		this.level = level;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Long getSeq()
	{
		return seq;
	}

	public void setSeq(Long seq)
	{
		this.seq = seq;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	@Override
	public String toString()
	{
		try
		{
			return new StringBuilder()
					.append("{\"id\":\"").append(id)
					.append("\",\"pid\":\"").append(pid != null ? pid : "0")
					.append("\",\"status\":").append(status)
					.append(",\"isParent\":").append((1 == status) ? "true" : "false")
					.append(",\"name\":\"").append(String.valueOf(label).replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\""))
					.append("\"}").toString();
		}
		catch(Exception e)
		{
			return "{\"id\":0,\"pid\":-1,\"status\":0,\"isParent\":true,\"name\":\"\"}";
		}
	}
}
