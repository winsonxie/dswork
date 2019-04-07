/**
 * 站点Model
 */
package dswork.bbs.model;
public class DsBbsSite
{
	//主键
	private Long id = 0L;
	//拥有者
	private String own = "";
	//站点名称
	private String name = "";
	//目录名称
	private String folder = "";
	//链接
	private String url = "";
	//图片
	private String img = "";
	//网站模板
	private String viewsite = "";

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getOwn()
	{
		return own;
	}

	public void setOwn(String own)
	{
		this.own = own;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getFolder()
	{
		return folder;
	}

	public void setFolder(String folder)
	{
		this.folder = folder;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getImg()
	{
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getViewsite()
	{
		return viewsite;
	}

	public void setViewsite(String viewsite)
	{
		this.viewsite = viewsite;
	}
}