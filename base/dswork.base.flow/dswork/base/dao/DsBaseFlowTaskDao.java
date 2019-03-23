/**
 * 流程任务Dao
 */
package dswork.base.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import dswork.base.model.DsBaseFlowTask;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsBaseFlowTaskDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseFlowTaskDao.class;
	}

	public int save(DsBaseFlowTask entity)
	{
		return executeInsert("insert", entity);
	}

	public void deleteByFlowid(Long flowid)
	{
		executeUpdate("deleteByFlowid", flowid);
	}

	public List<DsBaseFlowTask> queryList(Long flowid)
	{
		return executeSelectList("query", flowid);
	}
}
