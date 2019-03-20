/**
 * 单系统用户资源dao
 */
package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonFunc;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsCommonSingleUserFuncDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonSingleUserFuncDao.class;
	}

	public List<DsCommonFunc> getFuncBySystemidAndAccount(Long systemid, String account)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemid", systemid);
		map.put("account", account);
		return executeSelectList("getFuncBySystemidAndAccount", map);
	}
}
