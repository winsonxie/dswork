/**
 * 用户岗位Dao
 */
package dswork.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.base.model.DsBaseUserOrg;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsBaseUserOrgDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseUserOrgDao.class;
	}

	/**
	 * 新增对象
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	public int save(DsBaseUserOrg entity)
	{
		return executeInsert("insert", entity);
	}

	/**
	 * 删除该岗位下授权用户
	 * @param orgid 岗位主键
	 * @return int
	 */
	public int deleteByOrgid(Long orgid)
	{
		return executeDelete("deleteByOrgid", orgid);
	}

	/**
	 * 删除该用户下授权岗位
	 * @param userid 用户主键
	 * @return int
	 */
	public int deleteByUserid(Long userid)
	{
		return executeDelete("deleteByUserid", userid);
	}

	/**
	 * 根据岗位获得授权用户
	 * @param orgid 岗位主键
	 * @return List&lt;DsBaseUserOrg&gt;
	 */
	public List<DsBaseUserOrg> queryListByOrgid(Long orgid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgid", orgid);
		return executeSelectList("query", map);
	}

	/**
	 * 根据用户获得授权岗位
	 * @param userid 用户主键
	 * @return List&lt;DsBaseUserOrg&gt;
	 */
	public List<DsBaseUserOrg> queryListByUserid(Long userid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		return executeSelectList("query", map);
	}
}
