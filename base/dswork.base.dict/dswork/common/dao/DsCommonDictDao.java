/**
 * 字典分类Dao
 */
package dswork.common.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonDict;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsCommonDictDao extends BaseDao<DsCommonDict, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonDictDao.class;
	}
	
	/**
	 * 获取同名的字典分类个数(当前分类除外)
	 * @param id 排除的id
	 * @param name 字典类名
	 * @return int
	 */
	public int getCountByName(long id, String name)
	{
		Map m = new HashMap();
		m.put("notid", id);
		m.put("name", name);
		return (Integer) executeSelect("queryCount", m);
	}
}