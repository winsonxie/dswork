/**
 * 版块Model
 */
package dswork.bbs.model;

import java.util.ArrayList;
import java.util.List;

public class DsBbsForum
{
	// 主键
	private Long id = 0L;
	// 父ID
	private Long pid = 0L;
	// 站点ID
	private Long siteid = 0L;
	// 栏目名称
	private String name = "";
	// 摘要
	private String summary = "";
	// 状态(1启用,0禁用)
	private Integer status = 0;
	// 排序
	private Integer seq = 0;
	// 栏目模板
	private String viewsite = "";
	// 子栏目
	private List<DsBbsForum> list = new ArrayList<DsBbsForum>();
	// 父栏目
	private DsBbsForum parent = null;
	// 级别（管理时扩展显示）
	private int level = 0;
	// 树形标识（管理时扩展显示）
	private String label = "";

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getPid()
	{
		return pid;
	}

	public void setPid(Long pid)
	{
		this.pid = (pid == null || pid <= 0) ? 0 : pid;
	}

	public Long getSiteid()
	{
		return siteid;
	}

	public void setSiteid(Long siteid)
	{
		this.siteid = siteid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Integer getSeq()
	{
		return seq;
	}

	public void setSeq(Integer seq)
	{
		this.seq = seq;
	}

	public String getViewsite()
	{
		return viewsite;
	}

	public void setViewsite(String viewsite)
	{
		this.viewsite = viewsite;
	}

	public List<DsBbsForum> getList()
	{
		return list;
	}

	public void clearList()
	{
		this.list.clear();
	}

	public void add(DsBbsForum item)
	{
		this.list.add(item);
	}

	public DsBbsForum getParent()
	{
		return parent;
	}

	public void setParent(DsBbsForum parent)
	{
		this.parent = parent;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}
}
