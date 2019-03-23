/**
 * 功能Model
 */
package dswork.base.model;

import java.util.ArrayList;
import java.util.List;

public class DsBaseFunc
{
	// 主键
	private Long id = 0L;
	// 上级ID(本表)
	private Long pid = 0L;
	// 所属系统ID
	private Long systemid = 0L;
	// 名称
	private String name = "";
	// 标识
	private String alias = "";
	// 对应的URI
	private String uri = "";
	// 状态(0不是菜单,1菜单,不是菜单不能添加下级)
	private Integer status = 2;
	// 显示图标
	private String img = "";
	// 排序
	private Integer seq = 0;
	// 扩展信息
	private String memo = "";
	// 资源集合
	private String resources = "";
	// 资源集合转化的List
	private List<DsBaseRes> resourcesList = null;
	// 对Role设置Func时使用
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

	public void setAlias(String alias)
	{
		this.alias = String.valueOf(alias).trim();
	}

	public String getAlias()
	{
		return this.alias;
	}

	public void setUri(String uri)
	{
		this.uri = String.valueOf(uri).trim();
	}

	public String getUri()
	{
		return this.uri;
	}
	
	public Integer getStatus()
	{
		return status;
	}
	
	public void setStatus(Integer status)
	{
		this.status = (status == null || status.intValue() != 1) ? 0 : 1;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getImg()
	{
		return this.img;
	}

	public void setSeq(Integer seq)
	{
		this.seq = seq;
	}

	public Integer getSeq()
	{
		return this.seq;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public String getMemo()
	{
		return this.memo;
	}

	public String getResources()
	{
		return resources;
	}

	/**
	 * 会清空resourcesList的值(格式:url|param \n url|param)
	 * @param resources
	 */
	public void setResources(String resources)
	{
		this.resources = resources;
		this.resourcesList = null;
	}
	
	public List<DsBaseRes> getResourcesList()
	{
		if(resourcesList == null)
		{
			resourcesList = new ArrayList<DsBaseRes>();
			if(resources != null && resources.length() > 0)
			{
				String[] list = resources.split("\n", -1);
				int index = -1;
				for(String s : list)
				{
					DsBaseRes o = new DsBaseRes();
					index = s.indexOf("|");
					if(index != -1)
					{
						o.setUrl(s.substring(0, index));
						o.setParam(s.substring(index+1));
					}
					else
					{
						o.setUrl(s);
					}
					resourcesList.add(o);
				}
			}
		}
		return resourcesList;
	}
	
	public void setResourcesList(List<DsBaseRes> list)
	{
		if(list != null && list.size() > 0)
		{
			StringBuilder sb = new StringBuilder();
			for(DsBaseRes o : list)
			{
				sb.append(o.getUrl()).append("|").append(o.getParam()).append("\n");
			}
			this.resources = sb.toString().trim();
		}
		else
		{
			this.resources = "";
		}
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
			return new StringBuilder().append("{\"id\":").append(id).append(",\"pid\":").append(pid).append(",\"status\":").append(status).append(",\"isParent\":").append((status == 0)?"false":"true").append(",\"checked\":").append(isChecked()).append(",\"name\":\"").append(name.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")).append("\",\"uri\":\"").append(uri.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"")).append("\"}").toString();
		}
		catch (Exception e)
		{
			return "{\"id\":0,\"pid\":-1,\"status\":0,\"isParent\":true,\"checked\":false,\"name\":\"\",\"uri\":\"\"}";
		}
	}
}
