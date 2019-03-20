/**
 * 岗位角色Service
 */
package dswork.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsCommonOrgDao;
import dswork.common.dao.DsCommonOrgRoleDao;
import dswork.common.model.DsCommonOrg;
import dswork.common.model.DsCommonOrgRole;
import dswork.core.util.UniqueId;

@Service
public class DsCommonOrgRoleService
{
	@Autowired
	private DsCommonOrgRoleDao orgroleDao;
	@Autowired
	private DsCommonOrgDao orgDao;

	/**
	 * 新增对象
	 * @param orgid 岗位ID
	 * @param roleidList 角色ID集合
	 */
	public void save(Long orgid, List<Long> roleidList)
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

	/**
	 * 根据岗位获得授权角色
	 * @param orgid 岗位主键
	 * @return List&lt;DsCommonOrgRole&gt;
	 */
	public List<DsCommonOrgRole> queryList(Long orgid)
	{
		return orgroleDao.queryList(orgid);
	}

	/**
	 * 查询单个组织机构对象
	 * @param primaryKey 组织机构主键
	 * @return DsCommonOrg
	 */
	public DsCommonOrg get(Long primaryKey)
	{
		return (DsCommonOrg) orgDao.get(primaryKey);
	}
}
