package dswork.core.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息, 第一页从1开始，使用PageData
 * @author skey
 */
@Deprecated
public class Page<T> extends PageData<T>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 * @param page 当前页码
	 * @param pagesize 一页显示的条数
	 * @param totalsize 数据总条数
	 */
	public Page(int page, int pagesize, int totalsize)
	{
		super(page, pagesize, totalsize, new ArrayList<T>(0));
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
		super(page, pagesize, totalsize, result);
	}

	/**
	 * 取得当前页页码getPage
	 * @return int
	 */
	public int getCurrentPage()
	{
		return getPage();
	}

	/**
	 * 取得一页显示的条数getPagesize
	 * @return int
	 */
	public int getPageSize()
	{
		return getPagesize();
	}

	/**
	 * 取得数据总页数getTotalpage
	 * @return int
	 */
	public int getTotalPage()
	{
		return getTotalpage();
	}
}
