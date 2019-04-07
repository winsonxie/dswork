/**
 * 版块Dao
 */
package dswork.bbs.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.bbs.model.DsBbsForum;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsBbsForumDao extends BaseDao<DsBbsForum, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBbsForumDao.class;
	}

	/**
	 * 更新节点
	 * @param id 主键
	 * @param name 名称
	 * @param seq 排序位置
	 * @param siteid 站点ID
	 */
	public void updateBatch(long id, String name, int seq, long siteid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		map.put("seq", seq);
		map.put("siteid", siteid);
		executeUpdate("updateBatch", map);
	}
}
