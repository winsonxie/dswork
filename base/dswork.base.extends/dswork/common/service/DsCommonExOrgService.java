/**
 * 组织机构Service
 */
package dswork.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsCommonOrgDao;
import dswork.common.dao.DsCommonUserDao;
import dswork.common.model.DsCommonOrg;
import dswork.common.model.DsCommonUser;
import dswork.core.page.PageRequest;
import dswork.core.util.UniqueId;

@Service
public class DsCommonExOrgService
{
	@Autowired
	private DsCommonOrgDao dao;
	@Autowired
	private DsCommonUserDao userDao;

	public DsCommonUser getUserByAccount(String account)
	{
		return userDao.getByAccount(account);
	}

	public void save(DsCommonOrg entity)
	{
		entity.setId(UniqueId.genUniqueId());
		dao.save(entity);
	}

	public int delete(Long id)
	{
		return dao.delete(id);
	}

	public int update(DsCommonOrg entity)
	{
		return dao.update(entity);
	}

	public void updatePid(Long[] ids, long targetId)
	{
		for(int i = 0; i < ids.length; i++)
		{
			if(ids[i] > 0)
			{
				dao.updatePid(ids[i], targetId);
			}
		}
	}

	public void updateSeq(Long[] ids)
	{
		for (int i = 0; i < ids.length; i++)
		{
			dao.updateSeq(ids[i], i + 1L);
		}
	}

	public DsCommonOrg get(Long primaryKey)
	{
		return (DsCommonOrg) dao.get(primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<DsCommonOrg> queryList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		return dao.queryList(map);
	}

	public int getCountByPid(long pid, Integer status)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("status", status);
		return dao.queryCount(new PageRequest(map));
	}
}
