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
import dswork.base.dao.DsBaseUserOrgDao;
import dswork.base.model.DsBaseOrg;
import dswork.base.model.DsBaseOrgRole;
import dswork.base.model.DsBaseUser;
import dswork.base.model.DsBaseUserOrg;
import dswork.core.util.UniqueId;

@Service
@SuppressWarnings("all")
public class DsBaseExUserOrgService
{
	@Autowired
	private DsBaseUserOrgDao userorgDao;
	@Autowired
	private DsBaseOrgDao orgDao;
	@Autowired
	private DsBaseUserDao userDao;
	@Autowired
	private DsBaseOrgRoleDao orgroleDao;

	public DsBaseUser getUserByAccount(String account)
	{
		return userDao.getByAccount(account);
	}

	public void saveByOrg(Long orgid, List<Long> useridList)
	{
		userorgDao.deleteByOrgid(orgid);
		DsBaseUserOrg o = new DsBaseUserOrg();
		o.setOrgid(orgid);
		for(Long id : useridList)
		{
			if(id != 0)
			{
				o.setId(UniqueId.genUniqueId());
				o.setUserid(id);
				userorgDao.save(o);
			}
		}
	}

	public void saveByUser(Long userid, List<Long> orgidList)
	{
		userorgDao.deleteByUserid(userid);
		DsBaseUserOrg o = new DsBaseUserOrg();
		o.setUserid(userid);
		for(Long id : orgidList)
		{
			if(id > 0)
			{
				o.setId(UniqueId.genUniqueId());
				o.setOrgid(id);
				userorgDao.save(o);
			}
		}
	}

	public List<DsBaseUserOrg> queryListByOrgid(Long orgid)
	{
		return userorgDao.queryListByOrgid(orgid);
	}

	public List<DsBaseUserOrg> queryListByUserid(Long userid)
	{
		return userorgDao.queryListByUserid(userid);
	}

	public DsBaseOrg getOrg(Long primaryKey)
	{
		return (DsBaseOrg) orgDao.get(primaryKey);
	}

	public DsBaseUser getUser(Long userid)
	{
		return (DsBaseUser) userDao.get(userid);
	}

	public List<DsBaseOrg> queryOrgList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("status", 0);
		return orgDao.queryList(map);
	}

	public List<DsBaseUser> queryUserList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgid", pid);
		return userDao.queryList(map);
	}

	public List<DsBaseOrg> queryOrgListByUserid(Long userid)
	{
		List<DsBaseUserOrg> ls = userorgDao.queryListByUserid(userid);
		List<DsBaseOrg> list = new ArrayList<DsBaseOrg>();
		for(DsBaseUserOrg u : ls)
		{
			DsBaseOrg org = (DsBaseOrg) orgDao.get(u.getOrgid());
			list.add(org);
		}
		return list;
	}

	public List<DsBaseUser> queryUserListByOrgid(Long orgid)
	{
		List<DsBaseUserOrg> ls = userorgDao.queryListByOrgid(orgid);
		List<DsBaseUser> list = new ArrayList<DsBaseUser>();
		for(DsBaseUserOrg u : ls)
		{
			DsBaseUser user = (DsBaseUser) userDao.get(u.getUserid());
			list.add(user);
		}
		return list;
	}

	public List<DsBaseOrgRole> queryOrgRoleList(Long orgid)
	{
		return orgroleDao.queryList(orgid);
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
