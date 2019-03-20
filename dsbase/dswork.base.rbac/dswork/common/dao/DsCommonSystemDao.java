/**
 * 应用系统Dao
 */
package dswork.common.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonFunc;
import dswork.common.model.DsCommonSystem;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsCommonSystemDao extends BaseDao<DsCommonSystem, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonSystemDao.class;
	}

	/**
	 * 修改状态
	 * @param id 系统主键
	 * @param status 状态
	 */
	public void updateStatus(long id, int status)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		executeUpdate("updateStatus", map);
	}

	/**
	 * 判断标识是否存在
	 * @param alias 标识
	 * @return boolean 存在true，不存在false
	 */
	public boolean isExistsByAlias(String alias)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alias", alias);
		DsCommonSystem m = (DsCommonSystem) executeSelect("query", map);
		if(m != null && m.getId().longValue() != 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 排序节点
	 * @param id 功能主键
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