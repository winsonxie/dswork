/**
 * 字典分类Dao
 */
package dswork.base.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.base.model.DsBaseDict;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsBaseDictDao extends BaseDao<DsBaseDict, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseDictDao.class;
	}
	
	/**
	 * 获取同名的字典分类个数(当前分类除外)
	 * @param id 排除的id
	 * @param name 字典类名
	 * @return int
	 */
	public int getCountByName(long id, String name)
	{
		Map map = new HashMap();
		map.put("notid", id);
		map.put("name", name);
		return (Integer) executeSelect("queryCount", map);
	}

	/**
	 * 根据名称获取字典
	 * @param name 字典引用名
	 * @return DsBaseDict
	 */
	public DsBaseDict getByName(String name)
	{
		return (DsBaseDict) executeSelect("selectByName", name);
	}
}