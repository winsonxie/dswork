/**
 * CMSService
 */
package common.cms;

import common.cms.model.ViewArticle;
import common.cms.model.ViewArticleNav;
import dswork.core.page.Page;

public class CmsFactoryMobile extends CmsFactory
{
	@Deprecated
	public CmsFactoryMobile(long siteid)
	{
		super(siteid);
	}

	public CmsFactoryMobile(CmsFactory cms)
	{
		siteid = cms.siteid;
		site = cms.site;
		dao = cms.getDao();
		categoryList = cms.categoryList;
		categoryMap = cms.categoryMap;
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

		url = "/m" + url;
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
}
