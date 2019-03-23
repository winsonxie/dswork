/**
 * 数据表定义Dao
 */
package dswork.base.dao;

import org.springframework.stereotype.Repository;

import dswork.base.model.DsBaseTable;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsBaseTableDao extends BaseDao<DsBaseTable, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseTableDao.class;
	}
}