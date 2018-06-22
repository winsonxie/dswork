/**
 * CMSService
 */
package common.cms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import common.cms.model.ViewCategory;
import common.cms.model.ViewArticle;
import common.cms.model.ViewArticleNav;
import common.cms.model.ViewArticleSet;
import common.cms.model.ViewSite;
import dswork.core.page.Page;

public class CmsFactory
{
	protected static Long toLong(Object v)
	{
		try
		{
			return Long.parseLong(String.valueOf(v));
		}
		catch(Exception e)
		{
			return 0L;
		}
	}
	protected static DsCmsDao dao = null;

	protected long siteid = 0L;
	protected ViewSite site = new ViewSite();
	protected List<ViewCategory> categoryList = new ArrayList<ViewCategory>();
	protected Map<String, ViewCategory> categoryMap = new HashMap<String, ViewCategory>();

	protected HttpServletRequest request;

	public CmsFactory()
	{
	}

	public CmsFactory(long siteid)
	{
		try
		{
			if(getDao() == null)
			{
				init();
			}
			this.siteid = siteid;
			this.site = getDao().getSite(siteid);
			if(this.site != null)
			{
				List<ViewCategory> clist = getDao().queryCategoryList(siteid);
				for(ViewCategory m : clist)
				{
					if(m.getPid() == null)
					{
						m.setPid(0L);
						m.setParent(m);// 顶层节点的父节点为节点自己
						categoryList.add(m);
					}
					categoryMap.put(String.valueOf(m.getId()), m);
				}
				for(ViewCategory m : clist)
				{
					String pid = String.valueOf(m.getPid());
					if(!pid.equals("0") && categoryMap.get(pid) != null)
					{
						ViewCategory p = categoryMap.get(pid);
						m.setParent(p);
						p.addList(m);
					}
				}
			}
		}
		catch(Exception ex)
		{
		}
	}

	protected DsCmsDao getDao()
	{
		return dao;
	}

	protected void init()
	{
		dao = (DsCmsDao) dswork.spring.BeanFactory.getBean("dsCmsDao");
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public ViewSite getSite()
	{
		return site;
	}

	public ViewCategory getCategory(Object categoryid)
	{
		return categoryMap.get(String.valueOf(categoryid));
	}

	public ViewArticle get(String pageid)
	{
		return getDao().getArticle(siteid, toLong(pageid));
	}

	public List<ViewArticle> queryList(int currentPage, int pageSize, boolean onlyImageTop, boolean onlyPageTop, boolean isDesc, Object... categoryids)
	{
		return doQueryList(currentPage, pageSize, isDesc, onlyImageTop, onlyPageTop, null, categoryids);
	}

	public ViewArticleNav queryPage(int currentPage, int pageSize, boolean onlyImageTop, boolean onlyPageTop, boolean isDesc, String url, Object categoryid)
	{
		if(currentPage <= 0)
		{
			currentPage = 1;
		}
		if(pageSize <= 0)
		{
			pageSize = 25;
		}
		StringBuilder idArray = new StringBuilder();
		idArray.append(toLong(categoryid));
		Page<ViewArticle> page = getDao().queryArticlePage(siteid, currentPage, pageSize, idArray.toString(), isDesc, onlyImageTop, onlyPageTop, null);
		ViewArticleNav nav = new ViewArticleNav();
		currentPage = page.getCurrentPage();// 更新当前页
		nav.setList(page.getResult());
		nav.getDatapage().setPage(currentPage);
		nav.getDatapage().setPagesize(pageSize);
		nav.getDatapage().setFirst(1);
		nav.getDatapage().setFirsturl(url);
		int tmp = initpage(currentPage - 1, page.getLastPage());
		nav.getDatapage().setPrev(tmp);
		nav.getDatapage().setPrevurl(tmp == 1 ? url : (url.replaceAll("\\.html", "_" + tmp + ".html")));
		tmp = initpage(currentPage + 1, page.getLastPage());
		nav.getDatapage().setNext(tmp);
		nav.getDatapage().setNexturl(tmp == 1 ? url : (url.replaceAll("\\.html", "_" + tmp + ".html")));
		tmp = page.getLastPage();
		nav.getDatapage().setLast(tmp);
		nav.getDatapage().setLasturl(tmp == 1 ? url : (url.replaceAll("\\.html", "_" + tmp + ".html")));
		nav.setDatauri(url.replaceAll("\\.html", ""));
		StringBuilder sb = new StringBuilder();
		int viewpage = 3, temppage = 1;// 左右显示个数
		sb.append("<a");
		if(currentPage == 1)
		{
			sb.append(" class=\"selected\"");
		}
		else
		{
			sb.append(" href=\"").append(site.getUrl()).append(url).append("\"");
		}
		sb.append(">1</a>");
		temppage = currentPage - viewpage - 1;
		if(temppage > 1)
		{
			String u = url.replaceAll("\\.html", "_" + temppage + ".html");
			sb.append("<a href=\"").append(site.getUrl()).append(u).append("\">...</a>");
		}
		for(int i = currentPage - viewpage; i <= currentPage + viewpage && i < page.getLastPage(); i++)
		{
			if(i > 1)
			{
				String u = (i == 1 ? url : (url.replaceAll("\\.html", "_" + i + ".html")));
				sb.append("<a");
				if(currentPage == i)
				{
					sb.append(" class=\"selected\"");
				}
				else
				{
					sb.append(" href=\"").append(site.getUrl()).append(u).append("\"");
				}
				sb.append(">").append(i).append("</a>");
			}
		}
		temppage = currentPage + viewpage + 1;
		if(temppage < page.getLastPage())
		{
			String u = url.replaceAll("\\.html", "_" + temppage + ".html");
			sb.append("<a href=\"").append(site.getUrl()).append(u).append("\">...</a>");
		}
		if(page.getLastPage() != 1)
		{
			String u = url.replaceAll("\\.html", "_" + page.getLastPage() + ".html");
			sb.append("<a");
			if(currentPage == page.getLastPage())
			{
				sb.append(" class=\"selected\"");
			}
			else
			{
				sb.append(" href=\"").append(site.getUrl()).append(u).append("\"");
			}
			sb.append(">").append(page.getLastPage()).append("</a>");
		}
		nav.setDatapageview(sb.toString());// 翻页字符串
		return nav;
	}

	public ViewArticleSet queryPage(int currentPage, int pageSize, boolean isDesc, String keyvalue, Object... categoryids)
	{
		return doQueryPage(currentPage, pageSize, isDesc, false, false, keyvalue, categoryids);
	}

	private List<ViewArticle> doQueryList(int currentPage, int pageSize, boolean isDesc, boolean onlyImageTop, boolean onlyPageTop, String keyvalue, Object... categoryids)
	{
		StringBuilder idArray = new StringBuilder();
		if(categoryids.length > 0)
		{
			idArray.append("0");
			for(int i = 0; i < categoryids.length; i++)
			{
				idArray.append(",").append(toLong(categoryids[i]));
			}
		}
		return getDao().queryArticlePage(siteid, currentPage, pageSize, idArray.toString(), isDesc, onlyImageTop, onlyPageTop, keyvalue).getResult();
	}

	private ViewArticleSet doQueryPage(int currentPage, int pageSize, boolean isDesc, boolean onlyImageTop, boolean onlyPageTop, String keyvalue, Object... categoryids)
	{
		StringBuilder idArray = new StringBuilder();
		if(categoryids.length > 0)
		{
			idArray.append("0");
			for(int i = 0; i < categoryids.length; i++)
			{
				idArray.append(",").append(toLong(categoryids[i]));
			}
		}
		ViewArticleSet set = new ViewArticleSet();
		try
		{
			Page<ViewArticle> page = getDao().queryArticlePage(siteid, currentPage, pageSize, idArray.toString(), isDesc, onlyImageTop, onlyPageTop, keyvalue);
			set.setStatus(1);// success
			set.setMsg("success");
			set.setSize(page.getTotalCount());
			set.setPage(page.getCurrentPage());
			set.setPagesize(page.getPageSize());
			set.setTotalpage(page.getTotalPage());
			set.setRows(page.getResult());
		}
		catch(Exception e)
		{
			set.setStatus(0);
			set.setMsg("error");
		}
		return set;
	}

	public void put(String name, boolean listOrPage, int currentPage, int pageSize, boolean isDesc, boolean onlyImageTop, boolean onlyPageTop, String keyvalue, Object... categoryids)
	{
		if(listOrPage)
		{
			request.setAttribute(name, doQueryList(currentPage, pageSize, isDesc, onlyImageTop, onlyPageTop, keyvalue, categoryids));
		}
		else
		{
			request.setAttribute(name, doQueryPage(currentPage, pageSize, isDesc, onlyImageTop, onlyPageTop, keyvalue, categoryids));
		}
	}

	public void putCategory(String name, boolean listOrOne, Object categoryid)
	{
		if(listOrOne)
		{
			request.setAttribute(name, queryCategory(categoryid));
		}
		else
		{
			request.setAttribute(name, getCategory(categoryid));
		}
		
	}

	protected int initpage(int page, int total)
	{
		if(page <= 0)
		{
			page = 1;
		}
		if(page > total)
		{
			page = total;
		}
		return page;
	}

	/**
	 * 查询指定栏目下的子栏目(包括所有递归子栏目)
	 * @param categoryid
	 *        父栏目，查询根栏目为空
	 * @return List&lt;Map&lt;String, Object&gt;&gt;
	 */
	public List<ViewCategory> queryCategory(Object categoryid)
	{
		String pid = String.valueOf(toLong(categoryid));
		if(pid.equals("0"))
		{
			return categoryList;
		}
		ViewCategory p = categoryMap.get(pid);
		if(p == null)
		{
			p = new ViewCategory(); 
		}
		return p.getList();
	}
}
