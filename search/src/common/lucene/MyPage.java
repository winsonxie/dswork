package common.lucene;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息, 第一页从1开始
 * @author skey
 */
public class MyPage
{
	private List<MyDocument> result = new ArrayList<MyDocument>(0);
	private int pagesize;
	private int page;
	private int totalpage;
	private int totalsize = 0;
	private int searchsize = 0;

	/**
	 * 构造函数
	 * @param page 当前页码
	 * @param pagesize 一页显示的条数
	 * @param totalsize 数据总条数
	 * @param searchsize 搜索命中总条数
	 */
	public MyPage(int page, int pagesize, int totalsize, int searchsize)
	{
		if(pagesize <= 0)
		{
			throw new IllegalArgumentException("[pagesize] must great than zero");
		}
		if(page <= 1)
		{
			page = 1;
		}
		this.page = page;
		this.totalsize = totalsize;
		this.pagesize = pagesize;
		this.totalpage = totalsize % pagesize == 0 ? totalsize / pagesize : totalsize / pagesize + 1;
		if(this.totalpage < 1)
		{
			this.totalpage = 1;
		}
		if(page > 1 && (Integer.MAX_VALUE == page || page > this.totalpage))
		{
			this.page = this.totalpage;
		}
		this.searchsize = searchsize;
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
	 * 得到结果集第一条数据的行编码
	 * @return int
	 */
	public int getFirstResultIndex()
	{
		if (pagesize <= 0)
		{
			throw new IllegalArgumentException("[pagesize] must great than zero");
		}
		return (page - 1) * pagesize;
	}
	
	/**
	 * 取得数据总页数，也就是最后一页页码
	 * @return int
	 */
	public int getLastPage()
	{
		return totalpage;
	}

	/**
	 * 取得下一页页码
	 * @return int
	 */
	public int getNextPage()
	{
		return page + 1;
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
	 * 取得上一页页码
	 * @return int
	 */
	public int getPreviousPage()
	{
		return page - 1;
	}

	/**
	 * 取得结果集
	 * @return 结果集List&lt;T&gt;
	 */
	public List<MyDocument> getResult()
	{
		return result;
	}

	/**
	 * 设置结果集，其值与长度不影响计数
	 * @param result 结果集List&lt;T&gt;
	 */
	public void setResult(List<MyDocument> result)
	{
		if(result == null)
		{
			throw new IllegalArgumentException("'result' must be not null");
		}
		this.result = result;
	}

	/**
	 * 取得数据搜索总条数，0表示没有数据
	 * @return int
	 */
	public int getSearchSize()
	{
		return searchsize <= 0 ? 0 : searchsize;
	}

	/**
	 * 取得数据总条数，0表示没有数据
	 * @return int
	 */
	public int getTotalSize()
	{
		return totalsize;
	}

	/**
	 * 取得数据总页数
	 * @return int
	 */
	public int getTotalPage()
	{
		return totalpage;
	}

	/**
	 * 是否是首页（第一页），第一页页码为1
	 * @return boolean
	 */
	public boolean isFirstPage()
	{
		return page == 1;
	}

	/**
	 * 是否有下一页
	 * @return boolean
	 */
	public boolean isHasNextPage()
	{
		return totalpage > page;
	}

	/**
	 * 是否有上一页
	 * @return boolean
	 */
	public boolean isHasPreviousPage()
	{
		return page > 1;
	}

	/**
	 * 是否是最后一页
	 * @return boolean
	 */
	public boolean isLastPage()
	{
		return page >= totalpage;
	}
}
