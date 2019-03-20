/**
 * 用户岗位Service
 */
package dswork.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsCommonOrgDao;
import dswork.common.dao.DsCommonOrgRoleDao;
import dswork.common.dao.DsCommonUserDao;
import dswork.common.model.DsCommonOrg;
import dswork.common.model.DsCommonOrgRole;
import dswork.common.model.DsCommonUser;
import dswork.core.util.UniqueId;

@Service
@SuppressWarnings("all")
public class DsCommonSingleUserRoleService
{
	@Autowired
	private DsCommonUserDao userDao;
	@Autowired
	private DsCommonOrgDao orgDao;
	@Autowired
	private DsCommonOrgRoleDao orgroleDao;

	public DsCommonUser getUserByAccount(String account)
	{
		return userDao.getByAccount(account);
	}

	public DsCommonOrg getOrg(Long id)
	{
		return (DsCommonOrg) orgDao.get(id);
	}

	public DsCommonUser getUser(Long id)
	{
		return (DsCommonUser) userDao.get(id);
	}

	public List<DsCommonUser> queryUserList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgid", pid);
		return userDao.queryList(map);
	}

	public List<DsCommonOrgRole> queryOrgRoleList(Long orgid, Long systemid)
	{
		List<DsCommonOrgRole> list = new ArrayList<DsCommonOrgRole>();
		List<DsCommonOrgRole> rawList = orgroleDao.queryList(orgid);
		for(DsCommonOrgRole e : rawList)
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
		DsCommonOrgRole o = new DsCommonOrgRole();
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
