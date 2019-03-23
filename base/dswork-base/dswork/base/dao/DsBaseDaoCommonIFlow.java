/**
 * 公共Dao
 */
package dswork.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import dswork.base.model.IFlow;
import dswork.base.model.IFlowPi;
import dswork.base.model.IFlowPiData;
import dswork.base.model.IFlowTask;
import dswork.base.model.IFlowWaiting;
import dswork.core.db.MyBatisDao;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;

@Repository
@SuppressWarnings("all")
public class DsBaseDaoCommonIFlow extends MyBatisDao
{
	private SqlSessionTemplate sqlSessionTemplateCommon;
	private static boolean hasCommon = false;

	@Override
	protected SqlSessionTemplate getSqlSessionTemplate()
	{
		if(hasCommon)
		{
			return sqlSessionTemplateCommon;
		}
		return super.getSqlSessionTemplate();
	}
	
	public void setSqlSessionTemplateCommon(SqlSessionTemplate sqlSessionTemplate)
	{
		System.out.println("======== DsBaseDaoCommonIFlow call setSqlSessionTemplateCommon ========");
		hasCommon = true;
		this.sqlSessionTemplateCommon = sqlSessionTemplate;
	}
	
	@Override
	protected Class getEntityClass()
	{
		return DsBaseDaoCommonIFlow.class;
	}
	protected IFlow getFlow(String alias)
	{
		IFlow flow = (IFlow) executeSelect("selectFlow", alias);
		return flow;
	}
	protected IFlow getFlowById(long id)
	{
		IFlow flow = (IFlow) executeSelect("selectFlowById", id);
		return flow;
	}
	protected IFlowTask getFlowTask(Long flowid, String talias)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flowid", flowid);
		map.put("talias", talias);
		return (IFlowTask) executeSelect("selectFlowTask", map);
	}
	protected List<IFlowTask> queryFlowTask(Long flowid)
	{
		return executeSelectList("queryFlowTask", flowid);
	}
}
