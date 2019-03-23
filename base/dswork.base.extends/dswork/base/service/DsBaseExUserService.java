/**
 * 用户Service
 */
package dswork.base.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.base.dao.DsBaseOrgDao;
import dswork.base.dao.DsBaseUserDao;
import dswork.base.dao.DsBaseUsertypeDao;
import dswork.base.model.DsBaseOrg;
import dswork.base.model.DsBaseUser;
import dswork.base.model.DsBaseUsertype;
import dswork.core.db.BaseService;
import dswork.core.db.EntityDao;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;
import dswork.core.util.EncryptUtil;

@Service
@SuppressWarnings("all")
public class DsBaseExUserService extends BaseService<DsBaseUser, java.lang.Long>
{
	@Autowired
	private DsBaseUserDao dao;
	@Autowired
	private DsBaseUsertypeDao userTypeDao;
	@Autowired
	private DsBaseOrgDao orgDao;

	@Override
	protected EntityDao getEntityDao()
	{
		return dao;
	}

	public void updateCAKey(long id, String cakey)
	{
		dao.updateCAKey(id, cakey);
	}

	public void updateOrg(long id, Long orgpid, Long orgid)
	{
		dao.updateOrg(id, orgpid, orgid);
	}

	public void updatePassword(long id, String password)
	{
		password = EncryptUtil.encryptMd5(password);
		dao.updatePassword(id, password);
	}

	public void updateStatus(long id, int status)
	{
		dao.updateStatus(id, status);
	}

	public boolean isExistsByAccount(String account)
	{
		return dao.isExistsByAccount(account);
	}

	public DsBaseUser getByAccount(String account)
	{
		return dao.getByAccount(account);
	}

	public List<DsBaseUsertype> queryListForUsertype(String alias)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(alias != null)
		{
			map.put("xalias", alias);
		}
		return userTypeDao.queryList(map);
	}

	public List<DsBaseOrg> queryListOrgById(Long id)
	{
		List<DsBaseOrg> rawList = orgDao.queryList(new HashMap<String, Object>());
		Map<Long, List<DsBaseOrg>> map = new HashMap<Long, List<DsBaseOrg>>();
		List<DsBaseOrg> list = new ArrayList<DsBaseOrg>();
		for(DsBaseOrg org : rawList)
		{
			List<DsBaseOrg> ls = map.get(org.getPid());
			if(ls == null)
			{
				ls = new ArrayList<DsBaseOrg>();
				map.put(org.getPid(), ls);
			}
			ls.add(org);
			if(id == org.getId() || id.equals(org.getId()))// 将根节点放入结果集
			{
				list.add(org);
			}
		}
		Queue<DsBaseOrg> queue = new LinkedList<DsBaseOrg>();
		// 将根节点的直接子节点放入队列
		// Q:为什么不直接把根节点放入队列? A:因为根节点可能不存在
		queue.addAll(map.get(id));
		while(queue.size() > 0)
		{
			DsBaseOrg org = queue.poll();
			List<DsBaseOrg> ls = map.get(org.getId());
			if(ls != null)
			{
				queue.addAll(ls);// 将节点的直接子节点放入队列
			}
			list.add(org);// 队列中取出的节点放入结果集
		}
		return list;
	}

	public Page<DsBaseUser> queryPageByOrgpid(PageRequest pr, Long orgpid)
	{
		pr.getFilters().remove("orgpid");
		List<DsBaseUser> rawList = dao.queryList(pr.getFilters());
		List<DsBaseOrg> orgList = queryListOrgById(orgpid);
		Map<Long, DsBaseOrg> map = new HashMap<Long, DsBaseOrg>();
		for(DsBaseOrg org : orgList)
		{
			map.put(org.getId(), org);
		}

		int currentPage = pr.getCurrentPage();
		int pageSize = pr.getPageSize();
		int firstResultIndex = (currentPage - 1) * pageSize;
		int lastResultIndex = (currentPage) * pageSize;

		int count = 0;
		List<DsBaseUser> result = new ArrayList<DsBaseUser>();
		for(DsBaseUser u : rawList)
		{
			if(map.get(u.getOrgpid()) != null)
			{
				count++;
				if(firstResultIndex < count && count <= lastResultIndex)
				{
					result.add(u);
				}
			}
		}
		Page page = new Page(currentPage, pageSize, count);
		page.setResult(result);
		return page;
	}

	public DsBaseOrg getOrg(Long id)
	{
		return (DsBaseOrg) orgDao.get(id);
	}

	public List<DsBaseOrg> queryOrgList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("status", 0);
		return orgDao.queryList(map);
	}
}
