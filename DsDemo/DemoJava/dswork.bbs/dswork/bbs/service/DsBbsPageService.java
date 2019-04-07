/**
 * 主题Service
 */
package dswork.bbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.core.db.EntityDao;
import dswork.core.db.BaseService;
import dswork.core.page.PageRequest;
import dswork.bbs.model.DsBbsForum;
import dswork.bbs.model.DsBbsPage;
import dswork.bbs.model.DsBbsSite;
import dswork.bbs.dao.DsBbsForumDao;
import dswork.bbs.dao.DsBbsPageDao;
import dswork.bbs.dao.DsBbsSiteDao;

@Service
@SuppressWarnings("all")
public class DsBbsPageService extends BaseService<DsBbsPage, Long>
{
	@Autowired
	private DsBbsPageDao dao;
	@Autowired
	private DsBbsForumDao forumDao;
	@Autowired
	private DsBbsSiteDao siteDao;

	@Override
	protected EntityDao getEntityDao()
	{
		return dao;
	}
	
	public List<DsBbsForum> queryListForum(Long siteid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteid", siteid);
		return forumDao.queryList(map);
	}

	public DsBbsForum getForum(Long forumid)
	{
		return (DsBbsForum) forumDao.get(forumid);
	}
	
	public DsBbsSite getSite(Long siteid)
	{
		return (DsBbsSite) siteDao.get(siteid);
	}

	public List<DsBbsSite> queryListSite(Map<String, Object> map)
	{
		return siteDao.queryList(map);
	}
}
