package common.cms.model;

import java.util.List;

public class ViewArticleSet
{
	private int size;
	private int page;
	private int pagesize;
	private int totalpage;
	private List<ViewArticle> rows;
	private int status;
	private String msg;

	private String categoryids;
	private String categoryid;
	private ViewCategory catetory;

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getPagesize()
	{
		return pagesize;
	}

	public void setPagesize(int pagesize)
	{
		this.pagesize = pagesize;
	}

	public int getTotalpage()
	{
		return totalpage;
	}

	public void setTotalpage(int totalpage)
	{
		this.totalpage = totalpage;
	}

	public List<ViewArticle> getRows()
	{
		return rows;
	}

	public void setRows(List<ViewArticle> rows)
	{
		this.rows = rows;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getCategoryids()
	{
		return categoryids;
	}

	public void setCategoryids(String categoryids)
	{
		this.categoryids = categoryids;
	}

	public String getCategoryid()
	{
		return categoryid;
	}

	public void setCategoryid(String categoryid)
	{
		this.categoryid = categoryid;
	}

	public ViewCategory getCatetory()
	{
		return catetory;
	}

	public void setCatetory(ViewCategory catetory)
	{
		this.catetory = catetory;
	}
}
