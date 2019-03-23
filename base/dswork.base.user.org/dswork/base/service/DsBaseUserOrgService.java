/**
 * 用户岗位Service
 */
package dswork.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.base.dao.DsBaseOrgDao;
import dswork.base.dao.DsBaseUserDao;
import dswork.base.dao.DsBaseUserOrgDao;
import dswork.base.model.DsBaseOrg;
import dswork.base.model.DsBaseUser;
import dswork.base.model.DsBaseUserOrg;
import dswork.core.page.PageRequest;
import dswork.core.util.UniqueId;

@Service
@SuppressWarnings("all")
public class DsBaseUserOrgService
{
	@Autowired
	private DsBaseUserOrgDao userorgDao;
	@Autowired
	private DsBaseOrgDao orgDao;
	@Autowired
	private DsBaseUserDao userDao;

	/**
	 * 新增对象
	 * @param orgid 岗位ID
	 * @param useridList 用户ID集合
	 */
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

	/**
	 * 新增对象
	 * @param userid 用户ID
	 * @param orgidList 岗位ID集合
	 */
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

	/**
	 * 根据岗位获得授权用户
	 * @param orgid 岗位主键
	 * @return List&lt;DsBaseUserOrg&gt;
	 */
	public List<DsBaseUserOrg> queryListByOrgid(Long orgid)
	{
		return userorgDao.queryListByOrgid(orgid);
	}

	/**
	 * 根据用户获得授权岗位
	 * @param userid 用户主键
	 * @return List&lt;DsBaseUserOrg&gt;
	 */
	public List<DsBaseUserOrg> queryListByUserid(Long userid)
	{
		return userorgDao.queryListByUserid(userid);
	}

	/**
	 * 查询单个组织机构对象
	 * @param primaryKey 组织机构主键
	 * @return DsBaseOrg
	 */
	public DsBaseOrg get(Long primaryKey)
	{
		return (DsBaseOrg) orgDao.get(primaryKey);
	}
	
	/**
	 * 根据部门ID查询岗位列表
	 * @param pid 部门ID
	 * @return List&gt;DsBaseOrg&lt;
	 */
	public List<DsBaseOrg> queryOrgList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("status", 0);
		return orgDao.queryList(map);
	}
	
	/**
	 * 根据部门ID查询用户列表
	 * @param pid 部门ID
	 * @return List&gt;DsBaseUser&lt;
	 */
	public List<DsBaseUser> queryUserList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgid", pid);
		return userDao.queryList(map);
	}
}
