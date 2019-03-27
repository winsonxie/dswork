/**
 * 流程任务Dao
 */
package dswork.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import dswork.flow.model.DsFlowTask;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsFlowTaskDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsFlowTaskDao.class;
	}

	public int save(DsFlowTask entity)
	{
		return executeInsert("insert", entity);
	}

	public void deleteByFlowid(Long flowid)
	{
		executeUpdate("deleteByFlowid", flowid);
	}

	public List<DsFlowTask> queryList(Long flowid)
	{
		return executeSelectList("query", flowid);
	}
}
