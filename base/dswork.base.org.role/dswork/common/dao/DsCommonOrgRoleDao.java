/**
 * 岗位角色Dao
 */
package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonOrgRole;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsCommonOrgRoleDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonOrgRoleDao.class;
	}

	/**
	 * 新增对象
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	public int save(DsCommonOrgRole entity)
	{
		return executeInsert("insert", entity);
	}

	/**
	 * 删除该岗位下授权角色
	 * @param orgid 岗位主键
	 * @return int
	 */
	public int deleteByOrgid(Long orgid)
	{
		return executeDelete("deleteByOrgid", orgid);
	}

	/**
	 * 删除该角色所有授权
	 * @param roleid 角色主键
	 * @return int
	 */
	public int deleteByRoleid(Long roleid)
	{
		return executeDelete("deleteByRoleid", roleid);
	}

	/**
	 * 根据岗位获得授权角色
	 * @param orgid 岗位主键
	 * @return List&lt;DsCommonOrgRole&gt;
	 */
	public List<DsCommonOrgRole> queryList(Long orgid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgid", orgid);
		return executeSelectList("query", map);
	}
}
