/**
 * 主题Model
 */
package dswork.bbs.model;
public class DsBbsPage
{
	//主键
	private Long id = 0L;
	//站点ID
	private Long siteid = 0L;
	//版块ID
	private Long forumid = 0L;
	//发表人ID
	private String userid = "";
	//标题
	private String title = "";
	//链接
	private String url = "";
	//摘要
	private String summary = "";
	//精华(0否,1是)
	private Integer isessence = 0;
	//置顶(0否,1是)
	private Integer istop = 0;
	//meta关键词
	private String metakeywords = "";
	//meta描述
	private String metadescription = "";
	//发表人
	private String releaseuser = "";
	//发表时间
	private String releasetime = "";
	//结贴时间
	private String overtime = "";
	//最后回复人
	private String lastuser = "";
	//最后回复时间
	private String lasttime = "";
	//点击量
	private Integer numpv = 0;
	//回贴数
	private Integer numht = 0;
	//内容
	private String content = "";

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

	public Long getForumid()
	{
		return forumid;
	}

	public void setForumid(Long forumid)
	{
		this.forumid = forumid;
	}

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public Integer getIsessence()
	{
		return isessence;
	}

	public void setIsessence(Integer isessence)
	{
		this.isessence = isessence;
	}

	public Integer getIstop()
	{
		return istop;
	}

	public void setIstop(Integer istop)
	{
		this.istop = istop;
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

	public String getReleaseuser()
	{
		return releaseuser;
	}

	public void setReleaseuser(String releaseuser)
	{
		this.releaseuser = releaseuser;
	}

	public String getReleasetime()
	{
		return releasetime;
	}

	public void setReleasetime(String releasetime)
	{
		this.releasetime = releasetime;
	}

	public String getOvertime()
	{
		return overtime;
	}

	public void setOvertime(String overtime)
	{
		this.overtime = overtime;
	}

	public String getLastuser()
	{
		return lastuser;
	}

	public void setLastuser(String lastuser)
	{
		this.lastuser = lastuser;
	}

	public String getLasttime()
	{
		return lasttime;
	}

	public void setLasttime(String lasttime)
	{
		this.lasttime = lasttime;
	}

	public Integer getNumpv()
	{
		return numpv;
	}

	public void setNumpv(Integer numpv)
	{
		this.numpv = numpv;
	}

	public Integer getNumht()
	{
		return numht;
	}

	public void setNumht(Integer numht)
	{
		this.numht = numht;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}