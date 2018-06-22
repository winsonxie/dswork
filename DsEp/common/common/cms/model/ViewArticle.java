package common.cms.model;

import java.util.HashMap;
import java.util.Map;

import common.cms.GsonUtil;

public class ViewArticle
{
	private Long id;
	private Long siteid;
	private Long categoryid;
	private String title;
	private int scope;
	private int status;
	private String url;
	private String metakeywords;
	private String metadescription;
	private String summary;
	private String releasetime;
	private String releasesource;
	private String releaseuser;
	private String img;
	private String content;
	private String jsondata;

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

	public Long getCategoryid()
	{
		return categoryid;
	}

	public void setCategoryid(Long categoryid)
	{
		this.categoryid = categoryid;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getScope()
	{
		return scope;
	}

	public void setScope(int scope)
	{
		this.scope = scope;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getMetakeywords()
	{
		return metakeywords;
	}

	public void setMetakeywords(String metakeywords)
	{
		this.metakeywords = metakeywords;
	}

	public String getMetadescription()
	{
		return metadescription;
	}

	public void setMetadescription(String metadescription)
	{
		this.metadescription = metadescription;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getReleasetime()
	{
		return releasetime;
	}

	public void setReleasetime(String releasetime)
	{
		this.releasetime = releasetime;
	}

	public String getReleasesource()
	{
		return releasesource;
	}

	public void setReleasesource(String releasesource)
	{
		this.releasesource = releasesource;
	}

	public String getReleaseuser()
	{
		return releaseuser;
	}

	public void setReleaseuser(String releaseuser)
	{
		this.releaseuser = releaseuser;
	}

	public String getImg()
	{
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getJsondata()
	{
		return jsondata;
	}

	public void setJsondata(String jsondata)
	{
		this.jsondata = jsondata;
	}

	@SuppressWarnings("rawtypes")
	public Map getVo()
	{
		if(jsondata == null || jsondata.length() < 2)
		{
			return new HashMap();
		}
		return GsonUtil.toBean(jsondata, Map.class);
	}
}
