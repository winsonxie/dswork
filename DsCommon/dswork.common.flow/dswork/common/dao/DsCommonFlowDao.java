/**
 * 流程Dao
 */
package dswork.common.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonFlow;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsCommonFlowDao extends BaseDao<DsCommonFlow, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonFlowDao.class;
	}

	public void updateStatus(Long id, int status)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		executeUpdate("updateStatus", map);
	}

	/**
	 * 判断标识是否存在
	 * @param alias
	 * @return 存在返回true，不存在返回false
	 */
	public boolean isExistsByAlias(String alias)
	{
		DsCommonFlow flow = getByAlias(alias);
		if(flow != null && flow.getId().longValue() != 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 根据用户输入的标识来获得流程对象(vnum为0)，有则返回流程对象，无则返回null。
	 * @param alias - 用户输入的标识字符串。
	 * @return DsCommonFlow - 流程对象。
	 */
	public DsCommonFlow getByAlias(String alias)
	{
		return (DsCommonFlow) executeSelect("getByAlias", alias);
	}

	public void updateDeployid(Long id, String deployid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("deployid", deployid);
		executeUpdate("updateDeployid", map);
	}
}
