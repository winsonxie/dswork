/**
 * 字典项Dao
 */
package dswork.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.base.model.DsBaseDictData;
import dswork.core.db.BaseDao;
import dswork.core.db.MyBatisDao;
import dswork.core.page.PageRequest;

@Repository
@SuppressWarnings("all")
public class DsBaseDictDataDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseDictDataDao.class;
	}

	/**
	 * 添加
	 * @param po
	 * @return
	 */
	public int save(DsBaseDictData po)
	{
		return executeInsert("insert", po);
	}

	/**
	 * 删除
	 * @param id
	 * @param name
	 * @return
	 */
	public int delete(String id, String name)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		return executeDelete("delete", map);
	}

	/**
	 * 更新
	 * @param po
	 * @return
	 */
	public int update(DsBaseDictData po)
	{
		return executeUpdate("update", po);
	}

	/**
	 * 查询
	 * @param id
	 * @param name
	 * @return
	 */
	public DsBaseDictData get(String id, String name)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		return (DsBaseDictData) executeSelect("select", map);
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
	public void updatePid(String id, String pid, Map<String, Object> map)
	{
		map.clear();
		map.put("id", id);
		map.put("pid", pid);
		executeUpdate("updatePid", map);
	}

	/**
	 * 更新排序
	 * @param id 字典项主键
	 * @param name 字典引用名
	 * @param seq 排序位置
	 * @param map 临时map对象，传递进来后会被clear，并放入id和seq
	 * @return int
	 */
	public int updateSeq(String id, String name, Integer seq, Map<String, Object> map)
	{
		map.clear();
		map.put("id", id);
		map.put("name", name);
		map.put("seq", seq);
		return executeUpdate("updateSeq", map);
	}

	/**
	 * 获取父节点下直接子节点个数
	 * @param pid 父节点ID
	 * @param name 字典分类名，为null时需保证pid大于0，否则返回全部
	 * @return int
	 */
	public int getCount(String pid, String name)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("name", name);
		return queryCount("queryCount", new PageRequest(map));
	}

	/**
	 * 获取指定节点的列表数据，当pid为null时，获取全部数据，当pid小于等于0时，获取根节点数据
	 * @param pid 父节点ID
	 * @param name 字典分类名
	 * @param map 查询条件
	 * @return List&lt;DsBaseDictData&gt;
	 */
	public List<DsBaseDictData> queryList(String pid, String name, Map<String, Object> map)
	{
		map.put("pid", pid);
		map.put("name", name);
		return executeSelectList("query", map);
	}

	/**
	 * 更新节点状态(1树叉,0树叶)
	 * @param id 节点编码
	 * @param name 引用名
	 * @param status 修改后的状态
	 * @return int
	 */
	public int updateStatus(String id, String name, int status)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		map.put("status", status);
		return executeUpdate("updateStatus", map);
	}
}
