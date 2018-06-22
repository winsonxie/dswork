package common.cms.model;

import java.util.List;

public class ViewArticleNav
{
	private String datauri;
	private String datapageview;
	private Datapage datapage = new Datapage();
	private List<ViewArticle> list;

	public String getDatauri()
	{
		return datauri;
	}

	public void setDatauri(String datauri)
	{
		this.datauri = datauri;
	}

	public String getDatapageview()
	{
		return datapageview;
	}

	public void setDatapageview(String datapageview)
	{
		this.datapageview = datapageview;
	}

	public Datapage getDatapage()
	{
		return datapage;
	}

	public List<ViewArticle> getList()
	{
		return list;
	}

	public void setList(List<ViewArticle> list)
	{
		this.list = list;
	}

	public class Datapage
	{
		private int page;
		private int pagesize;
		private int first;
		private int prev;
		private int next;
		private int last;
		private String firsturl;
		private String prevurl;
		private String nexturl;
		private String lasturl;

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

		public int getFirst()
		{
			return first;
		}

		public void setFirst(int first)
		{
			this.first = first;
		}

		public int getPrev()
		{
			return prev;
		}

		public void setPrev(int prev)
		{
			this.prev = prev;
		}

		public int getNext()
		{
			return next;
		}

		public void setNext(int next)
		{
			this.next = next;
		}

		public int getLast()
		{
			return last;
		}

		public void setLast(int last)
		{
			this.last = last;
		}

		public String getFirsturl()
		{
			return firsturl;
		}

		public void setFirsturl(String firsturl)
		{
			this.firsturl = firsturl;
		}

		public String getPrevurl()
		{
			return prevurl;
		}

		public void setPrevurl(String prevurl)
		{
			this.prevurl = prevurl;
		}

		public String getNexturl()
		{
			return nexturl;
		}

		public void setNexturl(String nexturl)
		{
			this.nexturl = nexturl;
		}

		public String getLasturl()
		{
			return lasturl;
		}

		public void setLasturl(String lasturl)
		{
			this.lasturl = lasturl;
		}
	}
}
