package dswork.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息, 第一页从1开始
 * @author skey
 */
public class Page<T> implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<T> result = new ArrayList<T>();
	private int pagesize;
	private int page;
	private int size = 0;

	/**
	 * 构造函数
	 * @param currentPage 当前页码
	 * @param pageSize 一页显示的条数
	 * @param size 数据总条数
	 */
	public Page(int currentPage, int pagesize, int size)
	{
		this(currentPage, pagesize, size, new ArrayList<T>(0));
	}

	/**
	 * 构造函数
	 * @param page 当前页码
	 * @param pageSize 一页显示的条数
	 * @param totalCount 数据总条数
	 * @param result 结果集List&lt;T&gt;，其长度不影响计数
	 */
	public Page(int page, int pagesize, int size, List<T> result)
	{
		if(pagesize <= 0)
		{
			throw new IllegalArgumentException("[pagesize] must great than zero");
		}
		int lastPage = getTotalPage();
		this.pagesize = pagesize;
		if(page < 1)
		{
			page = 1;
		}
		else if(Integer.MAX_VALUE == page || page > lastPage)
		{
			page = lastPage;
		}
		this.page = page;
		this.size = size;
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
	public int getPageSize()
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
	public int getSize()
	{
		return size;
	}

	/**
	 * 取得数据总页数
	 * @return int
	 */
	public int getTotalPage()
	{
		int lastPage = size % pagesize == 0 ? size / pagesize : size / pagesize + 1;
		if(lastPage < 1)
		{
			lastPage = 1;
		}
		return lastPage;
	}
}
