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
public class DsCommonExUserService extends BaseService<DsCommonUser, java.lang.Long>
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

	public List<DsCommonOrg> queryListOrgByPid(Long pid)
	{
		List<DsCommonOrg> rawList = orgDao.queryList(new HashMap<String, Object>());
		Map<Long, List<DsCommonOrg>> map = new HashMap<Long, List<DsCommonOrg>>();
		for(DsCommonOrg org : rawList)
		{
			List<DsCommonOrg> ls = map.get(org.getPid());
			if(ls == null)
			{
				ls = new ArrayList<DsCommonOrg>();
				map.put(org.getPid(), ls);
			}
			ls.add(org);
		}
		Queue<DsCommonOrg> queue = new LinkedList<DsCommonOrg>();
		List<DsCommonOrg> list = new ArrayList<DsCommonOrg>();
		queue.addAll(map.get(pid));
		while(queue.size() > 0)
		{
			DsCommonOrg org = queue.poll();
			queue.addAll(map.get(org.getId()));
			list.add(org);
		}
		return list;
	}

	public Page<DsCommonUser> queryPageByOrgpid(PageRequest pr, Long orgpid)
	{
		List<DsCommonUser> rawList = dao.queryList(pr);
		List<DsCommonOrg> orgList = queryListOrgByPid(orgpid);
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
				if(count >= firstResultIndex && count <= lastResultIndex)
				{
					result.add(u);
				}
			}
		}
		Page page = new Page(currentPage, pageSize, count);
		page.setPageName(pr.getPageName());
		page.setPageSizeName(pr.getPageSizeName());
		page.setResult(result);
		return page;
	}
}