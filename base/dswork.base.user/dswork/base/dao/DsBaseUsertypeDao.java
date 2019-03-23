/**
 * 用户类型Dao
 */
package dswork.base.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import dswork.core.db.BaseDao;
import dswork.base.model.DsBaseUsertype;

@Repository
@SuppressWarnings("all")
public class DsBaseUsertypeDao extends BaseDao<DsBaseUsertype, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseUsertypeDao.class;
	}

	/**
	 * 排序节点
	 * @param id 用户类型主键
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
	 * 判断用户类型是否存在
	 * @param alias - 用户输入的标识字符串。
	 * @return 存在返回true，不存在返回false
	 */
	public boolean isExistsByAlias(String alias)
	{
		DsBaseUsertype m = getByAlias(alias);
		if(m != null && m.getId().longValue() != 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 根据用户输入的标识来获得对象，有则返回用户类型对象，无则返回null。
	 * @param alias - 用户输入的标识字符串。
	 * @return DsBaseUsertype - 用户类型对象。
	 */
	public DsBaseUsertype getByAlias(String alias)
	{
		return (DsBaseUsertype) executeSelect("getByAlias", alias);
	}
}