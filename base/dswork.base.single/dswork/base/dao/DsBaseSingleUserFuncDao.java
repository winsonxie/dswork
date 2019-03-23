/**
 * 单系统用户资源dao
 */
package dswork.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.base.model.DsBaseFunc;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsBaseSingleUserFuncDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseSingleUserFuncDao.class;
	}

	public List<DsBaseFunc> getFuncBySystemidAndAccount(Long systemid, String account)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemid", systemid);
		map.put("account", account);
		return executeSelectList("getFuncBySystemidAndAccount", map);
	}
}
