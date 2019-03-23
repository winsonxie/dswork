/**
 * 字典分类Model
 */
package dswork.base.model;

public class DsBaseDict
{
	// 主键
	private Long id = 0L;
	// 引用名
	private String name = "";
	// 名称
	private String label = "";
	// 层级(0任意树形,1列表,n=>n级属性)
	private Integer level = 0;
	// 编码规则(level>1时可选)
	private String rule = "";
	// 排序
	private Integer seq = 0;
	// 最后更新时间
	private long updatetime = 0;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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

	public Integer getLevel()
	{
		return level;
	}

	public void setLevel(Integer level)
	{
		this.level = level;
	}

	public String getRule()
	{
		return rule;
	}

	public void setRule(String rule)
	{
		this.rule = rule;
	}

	public int[] getRules()
	{
		if(rule != null && !rule.isEmpty())
		{
			try
			{
				String[] ss = rule.split(",");
				int[] arr = new int[ss.length];
				for(int i = 0; i < ss.length; i++)
				{
					arr[i] = Integer.parseInt(ss[i]);
				}
				return arr;
			}
			catch(Exception e)
			{
			}
		}
		return new int[0];
	}

	public void setRules(int[] rule)
	{
		String[] ss = new String[rule.length];
		for(int i = 0; i < ss.length; i++)
		{
			ss[i] = String.valueOf(rule[i]);
		}
		this.rule = String.join(",", ss);
	}

	public Integer getSeq()
	{
		return seq;
	}

	public void setSeq(Integer seq)
	{
		this.seq = seq;
	}

	public long getUpdatetime()
	{
		return updatetime;
	}

	public void setUpdatetime(long updatetime)
	{
		this.updatetime = updatetime;
	}

	public boolean isLimitedRule()
	{
		return level > 1 && getRules().length > 0;
	}
}
