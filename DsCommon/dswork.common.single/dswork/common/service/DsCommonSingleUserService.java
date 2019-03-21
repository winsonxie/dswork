/**
 * 用户Service
 */
package dswork.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsCommonOrgDao;
import dswork.common.dao.DsCommonUserDao;
import dswork.common.dao.DsCommonUsertypeDao;
import dswork.common.model.DsCommonOrg;
import dswork.common.model.DsCommonUser;
import dswork.common.model.DsCommonUsertype;
import dswork.core.db.BaseService;
import dswork.core.db.EntityDao;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;
import dswork.core.util.EncryptUtil;

@Service
@SuppressWarnings("all")
public class DsCommonSingleUserService extends BaseService<DsCommonUser, Long>
{
	@Autowired
	private DsCommonUserDao dao;
	@Autowired
	private DsCommonUsertypeDao userTypeDao;
	@Autowired
	private DsCommonOrgDao orgDao;

	@Override
	protected EntityDao getEntityDao()
	{
		return dao;
	}

	@Override
	public int save(DsCommonUser po)
	{
		DsCommonOrg org = new DsCommonOrg();
		org.setId(po.getId());
		org.setPid(po.getOrgid());
		org.setName(po.getName());
		org.setStatus(0);
		orgDao.save(org);
		dao.save(po);
		return 1;
	}

	@Override
	public int update(DsCommonUser po)
	{
		DsCommonOrg org = (DsCommonOrg) orgDao.get(po.getId());
		if(!org.getName().equals(po.getName()))
		{
			org.setName(po.getName());
			orgDao.update(org);
		}
		if(org.getPid().longValue() != po.getOrgid().longValue())
		{
			orgDao.updatePid(po.getId(), po.getOrgid());
		}
		dao.update(po);
		return 1;
	}

	@Override
	public int delete(Long id)
	{
		orgDao.delete(id);
		dao.delete(id);
		return 1;
	}

	public void updateCAKey(long id, String cakey)
	{
		dao.updateCAKey(id, cakey);
	}

	public void updateOrg(long id, Long orgpid, Long orgid)
	{
		orgDao.updatePid(id, orgid);
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

	public DsCommonUser getByAccount(String account)
	{
		return dao.getByAccount(account);
	}

	public List<DsCommonUsertype> queryListForUsertype(String alias)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(alias != null)
		{
			map.put("xalias", alias);
		}
		return userTypeDao.queryList(map);
	}

	public List<DsCommonOrg> queryListOrgById(Long id)
	{
		List<DsCommonOrg> rawList = orgDao.queryList(new HashMap<String, Object>());
		Map<Long, List<DsCommonOrg>> map = new HashMap<Long, List<DsCommonOrg>>();
		List<DsCommonOrg> list = new ArrayList<DsCommonOrg>();
		for(DsCommonOrg org : rawList)
		{
			List<DsCommonOrg> ls = map.get(org.getPid());
			if(ls == null)
			{
				ls = new ArrayList<DsCommonOrg>();
				map.put(org.getPid(), ls);
			}
			ls.add(org);
			if(id == org.getId() || id.equals(org.getId()))// 将根节点放入结果集
			{
				list.add(org);
			}
		}
		Queue<DsCommonOrg> queue = new LinkedList<DsCommonOrg>();
		// 将根节点的直接子节点放入队列
		// Q:为什么不直接把根节点放入队列? A:因为根节点可能不存在
		queue.addAll(map.get(id));
		while(queue.size() > 0)
		{
			DsCommonOrg org = queue.poll();
			List<DsCommonOrg> ls = map.get(org.getId());
			if(ls != null)
			{
				queue.addAll(ls);// 将节点的直接子节点放入队列
			}
			list.add(org);// 队列中取出的节点放入结果集
		}
		return list;
	}

	public Page<DsCommonUser> queryPageByOrgpid(PageRequest pr, Long orgpid)
	{
		pr.getFilters().remove("orgpid");
		List<DsCommonUser> rawList = dao.queryList(pr.getFilters());
		List<DsCommonOrg> orgList = queryListOrgById(orgpid);
		Map<Long, DsCommonOrg> map = new HashMap<Long, DsCommonOrg>();
		for(DsCommonOrg org : orgList)
		{
			map.put(org.getId(), org);
		}

		int currentPage = pr.getCurrentPage();
		int pageSize = pr.getPageSize();
		int firstResultIndex = (currentPage - 1) * pageSize;
		int lastResultIndex = (currentPage) * pageSize;

		int count = 0;
		List<DsCommonUser> result = new ArrayList<DsCommonUser>();
		for(DsCommonUser u : rawList)
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

	public DsCommonOrg getOrg(Long id)
	{
		return (DsCommonOrg) orgDao.get(id);
	}

	public List<DsCommonOrg> queryOrgList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("status", 0);
		return orgDao.queryList(map);
	}
}
