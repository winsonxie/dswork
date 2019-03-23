/**
 * 用户岗位Service
 */
package dswork.base.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.base.dao.DsBaseOrgDao;
import dswork.base.dao.DsBaseOrgRoleDao;
import dswork.base.dao.DsBaseUserDao;
import dswork.base.model.DsBaseOrg;
import dswork.base.model.DsBaseOrgRole;
import dswork.base.model.DsBaseUser;
import dswork.core.util.UniqueId;

@Service
@SuppressWarnings("all")
public class DsBaseSingleUserRoleService
{
	@Autowired
	private DsBaseUserDao userDao;
	@Autowired
	private DsBaseOrgDao orgDao;
	@Autowired
	private DsBaseOrgRoleDao orgroleDao;

	public DsBaseUser getUserByAccount(String account)
	{
		return userDao.getByAccount(account);
	}

	public DsBaseOrg getOrg(Long id)
	{
		return (DsBaseOrg) orgDao.get(id);
	}

	public DsBaseUser getUser(Long id)
	{
		return (DsBaseUser) userDao.get(id);
	}

	public List<DsBaseUser> queryUserList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgid", pid);
		return userDao.queryList(map);
	}

	public List<DsBaseOrgRole> queryOrgRoleList(Long orgid, Long systemid)
	{
		List<DsBaseOrgRole> list = new ArrayList<DsBaseOrgRole>();
		List<DsBaseOrgRole> rawList = orgroleDao.queryList(orgid);
		for(DsBaseOrgRole e : rawList)
		{
			if(e.getSystemid().equals(systemid))
			{
				list.add(e);
			}
		}
		return list;
	}

	public void saveOrgRole(Long orgid, List<Long> roleidList)
	{
		orgroleDao.deleteByOrgid(orgid);
		DsBaseOrgRole o = new DsBaseOrgRole();
		o.setOrgid(orgid);
		for(Long roleid : roleidList)
		{
			if(roleid > 0)
			{
				o.setId(UniqueId.genUniqueId());
				o.setRoleid(roleid);
				orgroleDao.save(o);
			}
		}
	}
}
