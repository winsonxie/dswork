package dswork.core.page;

import java.io.Serializable;
import java.util.Map;

/**
 * 分页请求信息
 */
public class PageRequest implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Map<String, Object> filters = null;
	private int page;
	private int pagesize;
	//private String pageName = "page";
	//private String pageSizeName = "pageSize";

	/**
	 * 构造函数
	 */
	public PageRequest()
	{
		this(0, 0);
	}

	/**
	 * 构造函数
	 * @param filters 条件
	 */
	public PageRequest(Map<String, Object> filters)
	{
		this(0, 0, filters);
	}

	/**
	 * 构造函数
	 * @param page 当前页码
	 * @param pagesize 一页显示的条数
	 */
	public PageRequest(int page, int pagesize)
	{
		this(page, pagesize, null);
	}

	/**
	 * 构造函数
	 * @param page 当前页码
	 * @param pagesize 一页显示的条数
	 * @param filters 条件
	 */
	public PageRequest(int page, int pagesize, Map<String, Object> filters)
	{
		setPage(page);
		setPagesize(pagesize);
		setFilters(filters);
	}

	/**
	 * 取得当前页
	 * @return int
	 */
	public int getPage()
	{
		return page;
	}

	/**
	 * 取得当前页getPage
	 * @return int
	 */
	@Deprecated
	public int getCurrentPage()
	{
		return getPage();
	}

	/**
	 * 设置当前页
	 * @param currentPage int
	 */
	public void setPage(int page)
	{
		this.page = page < 0 ? 1 : page;
	}

	/**
	 * 设置当前页setPage
	 * @param page int
	 */
	@Deprecated
	public void setCurrentPage(int page)
	{
		setPage(page);
	}

	/**
	 * 取得泛型参数
	 * @return Map&lt;String, Object&gt;
	 */
	public Map<String, Object> getFilters()
	{
		return filters;
	}

	/**
	 * 设置泛型参数
	 * @param filters 条件
	 */
	public void setFilters(Map<String, Object> filters)
	{
		this.filters = filters;
	}
//
//	/**
//	 * 取得当前页码的参数名
//	 * @return String，默认值是page
//	 */
//	public String getPageName()
//	{
//		return String.valueOf(pageName);
//	}
//
//	/**
//	 * 设置当前页码的参数名
//	 * @param pageName 默认值是page
//	 */
//	public void setPageName(String pageName)
//	{
//		this.pageName = pageName;
//	}

	/**
	 * 取得一页显示的条数
	 * @return int
	 */
	public int getPagesize()
	{
		return pagesize;
	}

	/**
	 * 取得一页显示的条数getPagesize()
	 * @return int
	 */
	@Deprecated
	public int getPageSize()
	{
		return getPagesize();
	}

	/**
	 * 设置一页显示的条数
	 * @param pagesize int
	 */
	public void setPagesize(int pagesize)
	{
		this.pagesize = pagesize < 0 ? 10 : pagesize;
	}

	/**
	 * 设置一页显示的条数setPagesize
	 * @param pagesize int
	 */
	@Deprecated
	public void setPageSize(int pagesize)
	{
		setPagesize(pagesize);
	}

//	/**
//	 * 取得一页显示的条数的参数名
//	 * @return String，默认值是pageSize
//	 */
//	public String getPageSizeName()
//	{
//		return String.valueOf(pageSizeName);
//	}
//
//	/**
//	 * 设置一页显示的条数的参数名
//	 * @param pageSizeName 默认值是pageSize
//	 */
//	public void setPageSizeName(String pageSizeName)
//	{
//		this.pageSizeName = pageSizeName;
//	}
}
