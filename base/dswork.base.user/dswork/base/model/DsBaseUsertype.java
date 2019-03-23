/**
 * 用户类型Model
 */
package dswork.base.model;

import java.util.ArrayList;
import java.util.List;

public class DsBaseUsertype
{
	//主键
	private Long id = 0L;
	//名称
	private String name = "";
	//标识
	private String alias = "";
	//状态(1)
	private Integer status = 0;
	//排序
	private Long seq = 0L;
	//扩展信息
	private String memo = "";
	//资源集合
	private String resources = "";
	// 资源集合转化的List
	private List<DsBaseUsertypeRes> resourcesList = null;
	
	public DsBaseUsertype()
	{
	}
	
	public DsBaseUsertype(String name, String alias)
	{
		this.name = name;
		this.alias = alias;
	}

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
		this.name = name;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
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

	public String getResources()
	{
		return resources;
	}

	public void setResources(String resources)
	{
		this.resources = resources;
		this.resourcesList = null;
	}
	
	public List<DsBaseUsertypeRes> getResourcesList()
	{
		if(resourcesList == null)
		{
			resourcesList = new ArrayList<DsBaseUsertypeRes>();
			if(resources != null && resources.length() > 0)
			{
				resources = resources.replaceAll("\n", "||");
				String[] list = resources.split("\\|\\|", -1);
				int index = -1;
				for(String s : list)
				{
					DsBaseUsertypeRes o = new DsBaseUsertypeRes();
					index = s.indexOf("|");
					if(index != -1)
					{
						o.setAlias(s.substring(0, index));
						o.setName(s.substring(index+1));
					}
					else
					{
						o.setAlias(s);
					}
					resourcesList.add(o);
				}
			}
		}
		return resourcesList;
	}
	
	public void setResourcesList(List<DsBaseUsertypeRes> list)
	{
		if(list != null && list.size() > 0)
		{
			StringBuilder sb = new StringBuilder();
			for(DsBaseUsertypeRes o : list)
			{
				sb.append(o.getAlias()).append("|").append(o.getName()).append("\n");
			}
			this.resources = sb.toString().trim().replaceAll("\n", "||");// 把最后一个换行切掉
		}
		else
		{
			this.resources = "";
		}
	}
}