/**
 * 栏目Service
 */
package dswork.bbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.bbs.dao.DsBbsForumDao;
import dswork.bbs.dao.DsBbsSiteDao;
import dswork.bbs.model.DsBbsForum;
import dswork.bbs.model.DsBbsSite;
import dswork.core.db.EntityDao;
import dswork.core.db.BaseService;
import dswork.core.page.PageRequest;

@Service
@SuppressWarnings("all")
public class DsBbsForumService extends BaseService<DsBbsForum, Long>
{
	@Autowired
	private DsBbsForumDao forumDao;
	@Autowired
	private DsBbsSiteDao siteDao;

	@Override
	protected EntityDao getEntityDao()
	{
		return forumDao;
	}

	@Override
	@Deprecated
	public void deleteBatch(Long[] primaryKeys)
	{
	}

	/**
	 * 批量更新节点
	 * @param ids 主键数组
	 * @param seqArr 名称数组
	 * @param seqArr 排序位置数组
	 * @param siteid 站点ID
	 */
	public void updateBatch(long[] idArr, String[] nameArr, int[] seqArr, long siteid)
	{
		for(int i = 0; i < idArr.length && i < seqArr.length; i++)
		{
			forumDao.updateBatch(idArr[i], nameArr[i], seqArr[i], siteid);
		}
	}

	/**
	 * 获得节点的子节点个数
	 * @param pid 上级栏目主键
	 * @return int
	 */
	public int getCountByPid(long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		return forumDao.queryCount(new PageRequest(map));
	}

	/**
	 * 获得栏目节点的内容数量
	 * @param siteid 站点主键
	 * @param categoryid 栏目主键
	 * @return int
	 *//*
	public int getCountByForumid(long siteid, long forumid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteid", siteid);
		map.put("forumid", forumid);
		return pageDao.queryCount(new PageRequest(map));
	}*/

	public DsBbsSite getSite(Long siteid)
	{
		return (DsBbsSite) siteDao.get(siteid);
	}

	public List<DsBbsSite> queryListSite(Map<String, Object> map)
	{
		return siteDao.queryList(map);
	}
}
