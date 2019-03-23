/**
 * 角色Dao
 */
package dswork.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import dswork.base.model.DsBaseRole;
import dswork.base.model.DsBaseRoleFunc;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsBaseRoleDao extends BaseDao<DsBaseRole, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseRoleDao.class;
	}

	/**
	 * 移动节点
	 * @param id 角色主键
	 * @param pid 小于等于0则是根节点
	 * @param map 临时map对象，传递进来后会被clear，并放入id和seq
	 */
	public void updatePid(Long id, long pid, Map<String, Object> map)
	{
		map.clear();
		map.put("id", id);
		map.put("pid", pid);
		executeUpdate("updatePid", map);
	}

	/**
	 * 排序节点
	 * @param id 角色主键
	 * @param seq 排序位置
	 */
	public void updateSeq(Long id, Long seq)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("seq", seq);
		executeUpdate("updateSeq", map);
	}

	/**
	 * 新增对象
	 * @param entity 角色功能对象
	 * @return int
	 */
	public int saveRoleFunc(DsBaseRoleFunc entity)
	{
		return executeInsert("insertRoleFunc", entity);
	}

	/**
	 * 删除功能下所有角色功能
	 * @param funcid 功能主键
	 */
	public void deleteRoleFuncByFuncid(Long funcid)
	{
		executeDelete("deleteRoleFuncByFuncid", funcid);
	}

	/**
	 * 删除角色下所有角色功能
	 * @param roleid 角色主键
	 */
	public void deleteRoleFuncByRoleid(Long roleid)
	{
		executeDelete("deleteRoleFuncByRoleid", roleid);
	}

	/**
	 * 删除系统下所有角色功能
	 * @param systemid 系统主键
	 */
	public void deleteRoleFuncBySystemid(Long systemid)
	{
		executeDelete("deleteRoleFuncBySystemid", systemid);
	}
	
	/**
	 * 根据角色获得角色功能
	 * @param roleid 角色主键
	 * @return List&lt;DsBaseRoleFunc&gt;
	 */
	public List<DsBaseRoleFunc> queryRoleFuncByRoleid(Long roleid)
	{
		return executeSelectList("queryRoleFuncByRoleid", roleid);
	}
}
