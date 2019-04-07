/**
 * 主题Dao
 */
package dswork.bbs.dao;

import org.springframework.stereotype.Repository;
import dswork.core.db.BaseDao;
import dswork.bbs.model.DsBbsPage;

@Repository
@SuppressWarnings("all")
public class DsBbsPageDao extends BaseDao<DsBbsPage, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBbsPageDao.class;
	}
}