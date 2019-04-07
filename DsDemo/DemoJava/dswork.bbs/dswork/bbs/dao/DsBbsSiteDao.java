/**
 * 站点Dao
 */
package dswork.bbs.dao;

import org.springframework.stereotype.Repository;
import dswork.core.db.BaseDao;
import dswork.bbs.model.DsBbsSite;

@Repository
@SuppressWarnings("all")
public class DsBbsSiteDao extends BaseDao<DsBbsSite, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBbsSiteDao.class;
	}
}