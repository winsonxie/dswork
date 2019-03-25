package dswork.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息, 第一页从1开始，使用PageData
 * @author skey
 */
public class Page<T> implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<T> result = new ArrayList<T>();
	private int pagesize;
	private int page;
	private int totalsize = 0;

	/**
	 * 构造函数
	 * @param page 当前页码
	 * @param pagesize 一页显示的条数
	 * @param totalsize 数据总条数
	 */
	public Page(int page, int pagesize, int totalsize)
	{
		this(page, pagesize, totalsize, new ArrayList<T>(0));
	}

	/**
	 * 构造函数
	 * @param page 当前页码
	 * @param pagesize 一页显示的条数
	 * @param totalsize 数据总条数
	 * @param result 结果集List&lt;T&gt;，其长度不影响计数
	 */
	public Page(int page, int pagesize, int totalsize, List<T> result)
	{
		if(pagesize <= 0)
		{
			throw new IllegalArgumentException("[pagesize] must great than zero");
		}
		this.totalsize = totalsize;
		this.pagesize = pagesize;
		int lastPage = getTotalpage();
		if(page < 1)
		{
			page = 1;
		}
		else if(Integer.MAX_VALUE == page || page > lastPage)
		{
			page = lastPage;
		}
		this.page = page;
		setResult(result);
	}

	/**
	 * 取得当前页页码
	 * @return int
	 */
	public int getPage()
	{
		return page;
	}

	/**
	 * 取得一页显示的条数
	 * @return int
	 */
	public int getPagesize()
	{
		return pagesize;
	}

	/**
	 * 取得结果集
	 * @return 结果集List&lt;T&gt;
	 */
	public List<T> getResult()
	{
		return result;
	}

	/**
	 * 设置结果集，其值与长度不影响计数
	 * @param result 结果集List&lt;T&gt;
	 */
	public void setResult(List<T> result)
	{
		if(result == null)
		{
			throw new IllegalArgumentException("'result' must be not null");
		}
		this.result = result;
	}

	/**
	 * 取得数据总条数，0表示没有数据
	 * @return int
	 */
	public int getTotalsize()
	{
		return totalsize;
	}

	/**
	 * 取得数据总页数
	 * @return int
	 */
	public int getTotalpage()
	{
		int lastPage = totalsize % pagesize == 0 ? totalsize / pagesize : totalsize / pagesize + 1;
		if(lastPage < 1)
		{
			lastPage = 1;
		}
		return lastPage;
	}

	/**
	 * 取得当前页页码getPage
	 * @return int
	 */
	@Deprecated
	public int getCurrentPage()
	{
		return getPage();
	}

	/**
	 * 取得一页显示的条数getPagesize
	 * @return int
	 */
	@Deprecated
	public int getPageSize()
	{
		return getPagesize();
	}

	/**
	 * 取得数据总页数getTotalpage
	 * @return int
	 */
	@Deprecated
	public int getTotalPage()
	{
		return getTotalpage();
	}
}
