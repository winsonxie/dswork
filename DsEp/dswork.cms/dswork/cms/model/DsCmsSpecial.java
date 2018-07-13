package dswork.cms.model;

public class DsCmsSpecial
{
	// 主键
	private Long id = 0L;
	// 站点ID
	private Long siteid = 0L;
	// 标题
	private String title = "";
	// 模板
	private String viewsite = "";
	// 链接
	private String url = "";

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getSiteid()
	{
		return siteid;
	}

	public void setSiteid(Long siteid)
	{
		this.siteid = siteid;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getViewsite()
	{
		return viewsite;
	}

	public void setViewsite(String viewsite)
	{
		this.viewsite = viewsite;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
}
