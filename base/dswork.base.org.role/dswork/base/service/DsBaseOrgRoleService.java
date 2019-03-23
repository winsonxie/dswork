/**
 * 岗位角色Service
 */
package dswork.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.base.dao.DsBaseOrgDao;
import dswork.base.dao.DsBaseOrgRoleDao;
import dswork.base.model.DsBaseOrg;
import dswork.base.model.DsBaseOrgRole;
import dswork.core.util.UniqueId;

@Service
public class DsBaseOrgRoleService
{
	@Autowired
	private DsBaseOrgRoleDao orgroleDao;
	@Autowired
	private DsBaseOrgDao orgDao;

	/**
	 * 新增对象
	 * @param orgid 岗位ID
	 * @param roleidList 角色ID集合
	 */
	public void save(Long orgid, List<Long> roleidList)
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

	/**
	 * 根据岗位获得授权角色
	 * @param orgid 岗位主键
	 * @return List&lt;DsBaseOrgRole&gt;
	 */
	public List<DsBaseOrgRole> queryList(Long orgid)
	{
		return orgroleDao.queryList(orgid);
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
}
