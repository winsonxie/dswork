/**
 * 字典项Dao
 */
package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonDictData;
import dswork.core.db.BaseDao;
import dswork.core.page.PageRequest;

@Repository
@SuppressWarnings("all")
public class DsCommonDictDataDao extends BaseDao<DsCommonDictData, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonDictDataDao.class;
	}
	
	/**
	 * 更新字典项Name
	 * @param newName 新Name
	 * @param oldName 旧Name
	 */
	public void updateName(String newName, String oldName)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("newName", newName);
		map.put("oldName", oldName);
		executeUpdate("updateName", map);
	}
	
	/**
	 * 移动节点
	 * @param id 字典项主键
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
	 * 更新排序
	 * @param id 字典项主键
	 * @param seq 排序位置
	 * @param map 临时map对象，传递进来后会被clear，并放入id和seq
	 */
	public void updateSeq(Long id, Integer seq, Map<String, Object> map)
	{
		map.clear();
		map.put("id", id);
		map.put("seq", seq);
		executeUpdate("updateSeq", map);
	}
	
	/**
	 * 获取相同标识的字典个数
	 * @param alias 字典标识值
	 * @param name 字典分类名
	 * @param id 排除的id
	 * @return int
	 */
	public int getCountByAlias(String alias, String name, long id)
	{
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("alias", alias);
		m.put("name", name);
		m.put("notid", id);
		return (Integer) executeSelect("queryCount", m);
	}
	
	/**
	 * 获取父节点字典个数
	 * @param pid 父节点ID
	 * @param name 字典分类名，为null时需保证pid大于0，否则返回全部
	 * @return int
	 */
	public int getCount(Long pid, String name)
	{
		Map<String, Object> m = new HashMap<String, Object>();
		if(pid != null)
		{
			m.put("pid", pid);
		}
		if(name != null)
		{
			m.put("name", name);
		}
		return queryCount(new PageRequest(m));
	}
	
	/**
	 * 获取指定节点的列表数据，当pid为null时，获取全部数据，当pid小于等于0时，获取根节点数据
	 * @param pid 父节点ID
	 * @param name 字典分类名
	 * @param map 查询条件
	 * @return List&lt;DsCommonDictData&gt;
	 */
	public List<DsCommonDictData> queryList(Long pid, String name, Map<String, Object> map)
	{
		if(map == null)
		{
			map = new HashMap<String, Object>();
		}
		if(pid != null)
		{
			map.put("pid", pid);
		}
		map.put("name", name);
		return executeSelectList("query", map);
	}
}