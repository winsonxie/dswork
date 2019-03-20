/**
 * 流程任务Dao
 */
package dswork.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonFlowTask;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsCommonFlowTaskDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonFlowTaskDao.class;
	}

	public int save(DsCommonFlowTask entity)
	{
		return executeInsert("insert", entity);
	}

	public void deleteByFlowid(Long flowid)
	{
		executeUpdate("deleteByFlowid", flowid);
	}

	public List<DsCommonFlowTask> queryList(Long flowid)
	{
		return executeSelectList("query", flowid);
	}
}
