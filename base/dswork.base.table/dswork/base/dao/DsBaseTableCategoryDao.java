/**
 * 数据表分类Dao
 */
package dswork.base.dao;

import org.springframework.stereotype.Repository;

import dswork.base.model.DsBaseTableCategory;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsBaseTableCategoryDao extends BaseDao<DsBaseTableCategory, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseTableCategoryDao.class;
	}
}