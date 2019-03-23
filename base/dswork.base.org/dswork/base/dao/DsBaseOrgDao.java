/**
 * 组织机构Dao
 */
package dswork.base.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

import dswork.base.model.DsBaseOrg;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsBaseOrgDao extends BaseDao<DsBaseOrg, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseOrgDao.class;
	}

	/**
	 * 移动节点
	 * @param id 组织机构主键
	 * @param pid 小于等于0则是根节点
	 */
	public void updatePid(Long id, long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("pid", pid);
		executeUpdate("updatePid", map);
	}

	/**
	 * 排序节点
	 * @param id 组织机构主键
	 * @param seq 排序位置
	 */
	public void updateSeq(Long id, Long seq)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("seq", seq);
		executeUpdate("updateSeq", map);
	}
}
